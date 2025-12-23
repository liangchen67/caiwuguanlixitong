package com.finance.service;

import com.finance.entity.JournalEntry;
import com.finance.entity.JournalEntryLine;
import com.finance.repository.JournalEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 财务报表服务
 */
@Service
public class ReportService {

    @Autowired
    private JournalEntryRepository journalEntryRepository;

    @Autowired
    private CompanyService companyService;

    /**
     * 生成资产负债表
     */
    public Map<String, Object> generateBalanceSheet(LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // 获取截止日期前的所有已过账凭证
        List<JournalEntry> entries = journalEntryRepository.findByEntryDateBeforeAndStatus(endDate.plusDays(1), "已过账");
        
        // 按科目类型汇总余额
        Map<String, BigDecimal> balances = calculateBalances(entries);
        
        // 资产项
        Map<String, Object> assets = new HashMap<>();
        // 流动资产
        Map<String, BigDecimal> currentAssets = new HashMap<>();
        currentAssets.put("货币资金", getBalance(balances, "1001", "1002")); // 库存现金+银行存款
        currentAssets.put("应收账款", getBalance(balances, "1122"));
        currentAssets.put("存货", getBalance(balances, "1403", "1405")); // 原材料+库存商品
        
        BigDecimal currentAssetsTotal = currentAssets.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 非流动资产
        Map<String, BigDecimal> nonCurrentAssets = new HashMap<>();
        nonCurrentAssets.put("固定资产", getBalance(balances, "1601").subtract(getBalance(balances, "1602"))); // 固定资产-累计折旧
        nonCurrentAssets.put("无形资产", getBalance(balances, "1701"));
        
        BigDecimal nonCurrentAssetsTotal = nonCurrentAssets.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalAssets = currentAssetsTotal.add(nonCurrentAssetsTotal);
        
        assets.put("currentAssets", currentAssets);
        assets.put("currentAssetsTotal", currentAssetsTotal);
        assets.put("nonCurrentAssets", nonCurrentAssets);
        assets.put("nonCurrentAssetsTotal", nonCurrentAssetsTotal);
        assets.put("totalAssets", totalAssets);
        
        // 负债项
        Map<String, Object> liabilities = new HashMap<>();
        // 流动负债
        Map<String, BigDecimal> currentLiabilities = new HashMap<>();
        currentLiabilities.put("短期借款", getBalance(balances, "2001"));
        currentLiabilities.put("应付账款", getBalance(balances, "2201"));
        currentLiabilities.put("应付职工薪酬", getBalance(balances, "2211"));
        
        BigDecimal currentLiabilitiesTotal = currentLiabilities.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // 非流动负债
        Map<String, BigDecimal> nonCurrentLiabilities = new HashMap<>();
        nonCurrentLiabilities.put("长期借款", getBalance(balances, "2501"));
        
        BigDecimal nonCurrentLiabilitiesTotal = nonCurrentLiabilities.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        BigDecimal totalLiabilities = currentLiabilitiesTotal.add(nonCurrentLiabilitiesTotal);
        
        liabilities.put("currentLiabilities", currentLiabilities);
        liabilities.put("currentLiabilitiesTotal", currentLiabilitiesTotal);
        liabilities.put("nonCurrentLiabilities", nonCurrentLiabilities);
        liabilities.put("nonCurrentLiabilitiesTotal", nonCurrentLiabilitiesTotal);
        liabilities.put("totalLiabilities", totalLiabilities);
        
        // 所有者权益
        Map<String, Object> equity = new HashMap<>();
        Map<String, BigDecimal> equityItems = new HashMap<>();
        equityItems.put("实收资本", getBalance(balances, "4001"));
        equityItems.put("未分配利润", getBalance(balances, "4104", "4103")); // 本年利润+利润分配
        
        BigDecimal totalEquity = equityItems.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        equity.put("items", equityItems);
        equity.put("totalEquity", totalEquity);
        
        // 组装报表
        report.put("assets", assets);
        report.put("liabilities", liabilities);
        report.put("equity", equity);
        report.put("endDate", endDate.toString());
        report.put("companyName", getCompanyName());
        
        return report;
    }

    /**
     * 生成利润表
     */
    public Map<String, Object> generateIncomeStatement(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // 获取期间内的已过账凭证
        List<JournalEntry> entries = journalEntryRepository.findByEntryDateBetweenAndStatus(startDate, endDate, "已过账");
        
        // 按科目汇总
        Map<String, BigDecimal> balances = calculateBalances(entries);
        
        // 收入
        BigDecimal revenue = getBalance(balances, "6001"); // 主营业务收入
        BigDecimal otherRevenue = getBalance(balances, "6051"); // 其他业务收入
        BigDecimal totalRevenue = revenue.add(otherRevenue);
        
        // 成本费用
        BigDecimal cost = getBalance(balances, "6401"); // 主营业务成本
        BigDecimal taxes = getBalance(balances, "6403"); // 税金及附加
        BigDecimal salesExpense = getBalance(balances, "6601"); // 销售费用
        BigDecimal adminExpense = getBalance(balances, "6602"); // 管理费用
        BigDecimal financeExpense = getBalance(balances, "6603"); // 财务费用
        
        // 营业利润
        BigDecimal operatingProfit = totalRevenue
                .subtract(cost)
                .subtract(taxes)
                .subtract(salesExpense)
                .subtract(adminExpense)
                .subtract(financeExpense);
        
        // 营业外收支
        BigDecimal nonOperatingIncome = getBalance(balances, "6301");
        BigDecimal nonOperatingExpense = getBalance(balances, "6701");
        
        // 利润总额
        BigDecimal totalProfit = operatingProfit
                .add(nonOperatingIncome)
                .subtract(nonOperatingExpense);
        
        // 所得税
        BigDecimal incomeTax = getBalance(balances, "6801");
        
        // 净利润
        BigDecimal netProfit = totalProfit.subtract(incomeTax);
        
        // 组装报表
        Map<String, BigDecimal> items = new HashMap<>();
        items.put("revenue", revenue);
        items.put("cost", cost);
        items.put("taxes", taxes);
        items.put("salesExpense", salesExpense);
        items.put("adminExpense", adminExpense);
        items.put("financeExpense", financeExpense);
        items.put("operatingProfit", operatingProfit);
        items.put("nonOperatingIncome", nonOperatingIncome);
        items.put("nonOperatingExpense", nonOperatingExpense);
        items.put("totalProfit", totalProfit);
        items.put("incomeTax", incomeTax);
        items.put("netProfit", netProfit);
        
        report.put("items", items);
        report.put("startDate", startDate.toString());
        report.put("endDate", endDate.toString());
        report.put("companyName", getCompanyName());
        
        return report;
    }

    /**
     * 生成现金流量表
     */
    public Map<String, Object> generateCashFlow(LocalDate startDate, LocalDate endDate) {
        Map<String, Object> report = new HashMap<>();
        
        // 获取期间内的已过账凭证
        List<JournalEntry> entries = journalEntryRepository.findByEntryDateBetweenAndStatus(startDate, endDate, "已过账");
        
        // 计算现金流量（简化版，实际应该根据业务类型详细分类）
        BigDecimal operatingCashFlow = BigDecimal.ZERO;
        BigDecimal investingCashFlow = BigDecimal.ZERO;
        BigDecimal financingCashFlow = BigDecimal.ZERO;
        
        for (JournalEntry entry : entries) {
            for (JournalEntryLine line : entry.getEntryLines()) {
                if (line.getAccountSubject() != null) {
                    String code = line.getAccountSubject().getCode();
                    BigDecimal amount = line.getAmount();
                    
                    // 判断是借方还是贷方
                    if ("贷".equals(line.getDirection())) {
                        amount = amount.negate();
                    }
                    
                    // 现金类科目
                    if (code.startsWith("1001") || code.startsWith("1002")) {
                        // 根据业务类型分类
                        String businessType = entry.getBusinessType();
                        if ("采购".equals(businessType) || "销售".equals(businessType) || "员工费用".equals(businessType)) {
                            operatingCashFlow = operatingCashFlow.add(amount);
                        } else if ("投资".equals(businessType)) {
                            investingCashFlow = investingCashFlow.add(amount);
                        } else if ("融资".equals(businessType)) {
                            financingCashFlow = financingCashFlow.add(amount);
                        } else {
                            operatingCashFlow = operatingCashFlow.add(amount);
                        }
                    }
                }
            }
        }
        
        BigDecimal netCashFlow = operatingCashFlow.add(investingCashFlow).add(financingCashFlow);
        
        Map<String, BigDecimal> items = new HashMap<>();
        items.put("operatingCashFlow", operatingCashFlow);
        items.put("investingCashFlow", investingCashFlow);
        items.put("financingCashFlow", financingCashFlow);
        items.put("netCashFlow", netCashFlow);
        
        report.put("items", items);
        report.put("startDate", startDate.toString());
        report.put("endDate", endDate.toString());
        report.put("companyName", getCompanyName());
        
        return report;
    }

    /**
     * 计算各科目余额
     */
    private Map<String, BigDecimal> calculateBalances(List<JournalEntry> entries) {
        Map<String, BigDecimal> balances = new HashMap<>();
        
        for (JournalEntry entry : entries) {
            if (entry.getEntryLines() != null) {
                for (JournalEntryLine line : entry.getEntryLines()) {
                    if (line.getAccountSubject() != null) {
                        String code = line.getAccountSubject().getCode();
                        BigDecimal amount = line.getAmount();
                        String direction = line.getDirection();
                        String subjectDirection = line.getAccountSubject().getDirection();
                        
                        // 根据科目借贷方向和分录借贷方向计算余额
                        BigDecimal change = amount;
                        if (!direction.equals(subjectDirection)) {
                            change = amount.negate();
                        }
                        
                        balances.put(code, balances.getOrDefault(code, BigDecimal.ZERO).add(change));
                    }
                }
            }
        }
        
        return balances;
    }

    /**
     * 获取指定科目的余额
     */
    private BigDecimal getBalance(Map<String, BigDecimal> balances, String... codes) {
        BigDecimal total = BigDecimal.ZERO;
        for (String code : codes) {
            total = total.add(balances.getOrDefault(code, BigDecimal.ZERO));
        }
        return total;
    }

    /**
     * 获取公司名称
     */
    private String getCompanyName() {
        try {
            List companies = companyService.findAll();
            if (!companies.isEmpty()) {
                return ((com.finance.entity.Company) companies.get(0)).getName();
            }
        } catch (Exception e) {
            // 忽略错误
        }
        return "XXX公司";
    }
}










