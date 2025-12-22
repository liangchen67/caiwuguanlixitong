<template>
  <div class="reports-page">
    <el-card style="margin-bottom: 20px">
      <el-form :inline="true">
        <el-form-item label="报表日期">
          <el-date-picker
            v-model="reportDate"
            type="date"
            placeholder="选择日期"
            value-format="YYYY-MM-DD"
            @change="loadReports"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="loadReports" :loading="loading">刷新报表</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-tabs v-model="activeTab" @tab-change="handleTabChange">
      <el-tab-pane label="资产负债表" name="balance-sheet">
        <el-card v-loading="loading">
          <div style="text-align: center; margin-bottom: 20px">
            <h2>资产负债表</h2>
            <p>编制单位：{{ balanceSheet.companyName || 'XXX公司' }}　　　　　{{ balanceSheet.endDate || currentDate }}　　　　　单位：元</p>
          </div>
          <el-table :data="balanceSheetData" border :show-header="false">
            <el-table-column prop="item" label="项目" width="250" />
            <el-table-column prop="amount" label="金额" align="right">
              <template #default="{ row }">
                {{ row.amount ? formatNumber(row.amount) : '' }}
              </template>
            </el-table-column>
            <el-table-column prop="item2" label="项目" width="250" />
            <el-table-column prop="amount2" label="金额" align="right">
              <template #default="{ row }">
                {{ row.amount2 ? formatNumber(row.amount2) : '' }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="利润表" name="income-statement">
        <el-card v-loading="loading">
          <div style="text-align: center; margin-bottom: 20px">
            <h2>利润表</h2>
            <p>编制单位：{{ incomeStatement.companyName || 'XXX公司' }}　　　　　{{ incomeStatement.startDate }} 至 {{ incomeStatement.endDate }}　　　　　单位：元</p>
          </div>
          <el-table :data="incomeStatementData" border>
            <el-table-column prop="item" label="项目" width="300" />
            <el-table-column prop="amount" label="金额" align="right">
              <template #default="{ row }">
                {{ row.amount ? formatNumber(row.amount) : '' }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>

      <el-tab-pane label="现金流量表" name="cash-flow">
        <el-card v-loading="loading">
          <div style="text-align: center; margin-bottom: 20px">
            <h2>现金流量表</h2>
            <p>编制单位：{{ cashFlow.companyName || 'XXX公司' }}　　　　　{{ cashFlow.startDate }} 至 {{ cashFlow.endDate }}　　　　　单位：元</p>
          </div>
          <el-table :data="cashFlowData" border>
            <el-table-column prop="item" label="项目" width="400" />
            <el-table-column prop="amount" label="金额" align="right">
              <template #default="{ row }">
                {{ row.amount ? formatNumber(row.amount) : '' }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { getBalanceSheet, getIncomeStatement, getCashFlow } from '@/api/report'

const activeTab = ref('balance-sheet')
const currentDate = new Date().toLocaleDateString('zh-CN')
const reportDate = ref(new Date().toISOString().split('T')[0])
const loading = ref(false)

const balanceSheet = ref({})
const incomeStatement = ref({})
const cashFlow = ref({})

// 资产负债表数据
const balanceSheetData = computed(() => {
  if (!balanceSheet.value.assets) return []
  
  const assets = balanceSheet.value.assets
  const liabilities = balanceSheet.value.liabilities
  const equity = balanceSheet.value.equity
  
  return [
    { item: '流动资产：', amount: '', item2: '流动负债：', amount2: '' },
    { item: '　货币资金', amount: assets.currentAssets?.货币资金 || 0, item2: '　短期借款', amount2: liabilities.currentLiabilities?.短期借款 || 0 },
    { item: '　应收账款', amount: assets.currentAssets?.应收账款 || 0, item2: '　应付账款', amount2: liabilities.currentLiabilities?.应付账款 || 0 },
    { item: '　存货', amount: assets.currentAssets?.存货 || 0, item2: '　应付职工薪酬', amount2: liabilities.currentLiabilities?.应付职工薪酬 || 0 },
    { item: '流动资产合计', amount: assets.currentAssetsTotal || 0, item2: '流动负债合计', amount2: liabilities.currentLiabilitiesTotal || 0 },
    { item: '非流动资产：', amount: '', item2: '非流动负债：', amount2: '' },
    { item: '　固定资产', amount: assets.nonCurrentAssets?.固定资产 || 0, item2: '　长期借款', amount2: liabilities.nonCurrentLiabilities?.长期借款 || 0 },
    { item: '　无形资产', amount: assets.nonCurrentAssets?.无形资产 || 0, item2: '非流动负债合计', amount2: liabilities.nonCurrentLiabilitiesTotal || 0 },
    { item: '非流动资产合计', amount: assets.nonCurrentAssetsTotal || 0, item2: '负债合计', amount2: liabilities.totalLiabilities || 0 },
    { item: '', amount: '', item2: '所有者权益：', amount2: '' },
    { item: '', amount: '', item2: '　实收资本', amount2: equity.items?.实收资本 || 0 },
    { item: '', amount: '', item2: '　未分配利润', amount2: equity.items?.未分配利润 || 0 },
    { item: '', amount: '', item2: '所有者权益合计', amount2: equity.totalEquity || 0 },
    { item: '资产总计', amount: assets.totalAssets || 0, item2: '负债和所有者权益总计', amount2: (liabilities.totalLiabilities || 0) + (equity.totalEquity || 0) }
  ]
})

// 利润表数据
const incomeStatementData = computed(() => {
  if (!incomeStatement.value.items) return []
  
  const items = incomeStatement.value.items
  return [
    { item: '一、营业收入', amount: items.revenue || 0 },
    { item: '　减：营业成本', amount: items.cost || 0 },
    { item: '　　　税金及附加', amount: items.taxes || 0 },
    { item: '　　　销售费用', amount: items.salesExpense || 0 },
    { item: '　　　管理费用', amount: items.adminExpense || 0 },
    { item: '　　　财务费用', amount: items.financeExpense || 0 },
    { item: '二、营业利润', amount: items.operatingProfit || 0 },
    { item: '　加：营业外收入', amount: items.nonOperatingIncome || 0 },
    { item: '　减：营业外支出', amount: items.nonOperatingExpense || 0 },
    { item: '三、利润总额', amount: items.totalProfit || 0 },
    { item: '　减：所得税费用', amount: items.incomeTax || 0 },
    { item: '四、净利润', amount: items.netProfit || 0 }
  ]
})

// 现金流量表数据
const cashFlowData = computed(() => {
  if (!cashFlow.value.items) return []
  
  const items = cashFlow.value.items
  return [
    { item: '一、经营活动产生的现金流量净额', amount: items.operatingCashFlow || 0 },
    { item: '二、投资活动产生的现金流量净额', amount: items.investingCashFlow || 0 },
    { item: '三、筹资活动产生的现金流量净额', amount: items.financingCashFlow || 0 },
    { item: '四、现金及现金等价物净增加额', amount: items.netCashFlow || 0 }
  ]
})

// 格式化数字
const formatNumber = (num) => {
  if (!num) return '0.00'
  return Number(num).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

// 加载报表数据
const loadReports = async () => {
  loading.value = true
  try {
    // 根据当前tab加载对应报表
    if (activeTab.value === 'balance-sheet') {
      await loadBalanceSheet()
    } else if (activeTab.value === 'income-statement') {
      await loadIncomeStatement()
    } else if (activeTab.value === 'cash-flow') {
      await loadCashFlow()
    }
  } catch (error) {
    console.error('加载报表失败:', error)
    ElMessage.error('加载报表失败')
  } finally {
    loading.value = false
  }
}

// 加载资产负债表
const loadBalanceSheet = async () => {
  try {
    const res = await getBalanceSheet(reportDate.value)
    if (res.data) {
      balanceSheet.value = res.data
    }
  } catch (error) {
    console.error('加载资产负债表失败:', error)
  }
}

// 加载利润表
const loadIncomeStatement = async () => {
  try {
    // 默认查询本年数据
    const year = new Date(reportDate.value).getFullYear()
    const startDate = `${year}-01-01`
    const res = await getIncomeStatement(startDate, reportDate.value)
    if (res.data) {
      incomeStatement.value = res.data
    }
  } catch (error) {
    console.error('加载利润表失败:', error)
  }
}

// 加载现金流量表
const loadCashFlow = async () => {
  try {
    // 默认查询本年数据
    const year = new Date(reportDate.value).getFullYear()
    const startDate = `${year}-01-01`
    const res = await getCashFlow(startDate, reportDate.value)
    if (res.data) {
      cashFlow.value = res.data
    }
  } catch (error) {
    console.error('加载现金流量表失败:', error)
  }
}

// 切换标签页
const handleTabChange = () => {
  loadReports()
}

onMounted(() => {
  loadReports()
})

// 以下是旧的静态数据（保留作为备用）
const oldBalanceSheetData = ref([
  { item: '流动资产：', amount: '', item2: '流动负债：', amount2: '' },
  { item: '　货币资金', amount: '1,000,000.00', item2: '　短期借款', amount2: '500,000.00' },
  { item: '　应收账款', amount: '800,000.00', item2: '　应付账款', amount2: '600,000.00' },
  { item: '　存货', amount: '500,000.00', item2: '　应付职工薪酬', amount2: '100,000.00' },
  { item: '流动资产合计', amount: '2,300,000.00', item2: '流动负债合计', amount2: '1,200,000.00' },
  { item: '非流动资产：', amount: '', item2: '非流动负债：', amount2: '' },
  { item: '　固定资产', amount: '3,000,000.00', item2: '　长期借款', amount2: '1,000,000.00' },
  { item: '　无形资产', amount: '500,000.00', item2: '非流动负债合计', amount2: '1,000,000.00' },
  { item: '非流动资产合计', amount: '3,500,000.00', item2: '负债合计', amount2: '2,200,000.00' },
  { item: '', amount: '', item2: '所有者权益：', amount2: '' },
  { item: '', amount: '', item2: '　实收资本', amount2: '2,000,000.00' },
  { item: '', amount: '', item2: '　未分配利润', amount2: '1,600,000.00' },
  { item: '', amount: '', item2: '所有者权益合计', amount2: '3,600,000.00' },
  { item: '资产总计', amount: '5,800,000.00', item2: '负债和所有者权益总计', amount2: '5,800,000.00' }
])

const oldIncomeStatementData = ref([
  { item: '一、营业收入', amount: '2,000,000.00', yearAmount: '20,000,000.00' },
  { item: '　减：营业成本', amount: '1,200,000.00', yearAmount: '12,000,000.00' },
  { item: '　　　税金及附加', amount: '50,000.00', yearAmount: '500,000.00' },
  { item: '　　　销售费用', amount: '100,000.00', yearAmount: '1,000,000.00' },
  { item: '　　　管理费用', amount: '150,000.00', yearAmount: '1,500,000.00' },
  { item: '　　　财务费用', amount: '20,000.00', yearAmount: '200,000.00' },
  { item: '二、营业利润', amount: '480,000.00', yearAmount: '4,800,000.00' },
  { item: '　加：营业外收入', amount: '20,000.00', yearAmount: '200,000.00' },
  { item: '　减：营业外支出', amount: '10,000.00', yearAmount: '100,000.00' },
  { item: '三、利润总额', amount: '490,000.00', yearAmount: '4,900,000.00' },
  { item: '　减：所得税费用', amount: '122,500.00', yearAmount: '1,225,000.00' },
  { item: '四、净利润', amount: '367,500.00', yearAmount: '3,675,000.00' }
])

const oldCashFlowData = ref([
  { item: '一、经营活动产生的现金流量：', amount: '', yearAmount: '' },
  { item: '　销售商品、提供劳务收到的现金', amount: '2,100,000.00', yearAmount: '21,000,000.00' },
  { item: '　购买商品、接受劳务支付的现金', amount: '1,300,000.00', yearAmount: '13,000,000.00' },
  { item: '　支付给职工以及为职工支付的现金', amount: '200,000.00', yearAmount: '2,000,000.00' },
  { item: '　支付的各项税费', amount: '150,000.00', yearAmount: '1,500,000.00' },
  { item: '经营活动产生的现金流量净额', amount: '450,000.00', yearAmount: '4,500,000.00' },
  { item: '二、投资活动产生的现金流量：', amount: '', yearAmount: '' },
  { item: '　购建固定资产支付的现金', amount: '500,000.00', yearAmount: '3,000,000.00' },
  { item: '投资活动产生的现金流量净额', amount: '-500,000.00', yearAmount: '-3,000,000.00' },
  { item: '三、筹资活动产生的现金流量：', amount: '', yearAmount: '' },
  { item: '　吸收投资收到的现金', amount: '0.00', yearAmount: '2,000,000.00' },
  { item: '　取得借款收到的现金', amount: '200,000.00', yearAmount: '1,500,000.00' },
  { item: '　偿还债务支付的现金', amount: '100,000.00', yearAmount: '500,000.00' },
  { item: '筹资活动产生的现金流量净额', amount: '100,000.00', yearAmount: '3,000,000.00' },
  { item: '四、现金及现金等价物净增加额', amount: '50,000.00', yearAmount: '4,500,000.00' }
])
</script>

<style scoped>
.reports-page {
  padding: 20px;
}

h2 {
  margin: 0;
  font-size: 24px;
}

p {
  margin: 10px 0;
  color: #666;
}
</style>

