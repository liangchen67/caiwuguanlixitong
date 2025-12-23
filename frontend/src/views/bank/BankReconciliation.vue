<template>
  <div class="bank-reconciliation-page">
    <el-card style="margin-bottom: 20px">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="自动对账" name="auto">
          <el-form :inline="true" style="margin-top: 20px">
            <el-form-item label="银行账号">
              <el-input v-model="autoForm.bankAccount" placeholder="请输入银行账号" style="width: 200px" />
            </el-form-item>
            <el-form-item label="对账期间">
              <el-date-picker
                v-model="autoForm.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleAutoMatch">开始自动对账</el-button>
            </el-form-item>
          </el-form>

          <el-alert
            v-if="matchResult"
            :title="`对账完成：共${matchResult.totalStatements}笔流水，成功匹配${matchResult.matchedCount}笔`"
            type="success"
            style="margin-top: 20px"
            show-icon
          />
        </el-tab-pane>

        <el-tab-pane label="生成余额调节表" name="report">
          <el-form :inline="true" style="margin-top: 20px">
            <el-form-item label="银行账号">
              <el-input v-model="reportForm.bankAccount" placeholder="请输入银行账号" style="width: 200px" />
            </el-form-item>
            <el-form-item label="对账日期">
              <el-date-picker
                v-model="reportForm.reconciliationDate"
                type="date"
                placeholder="选择对账日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            <el-form-item label="银行余额">
              <el-input-number v-model="reportForm.bankBalance" :precision="2" placeholder="输入银行对账单余额" />
            </el-form-item>
            <el-form-item label="对账人">
              <el-input v-model="reportForm.createdBy" placeholder="输入对账人" style="width: 150px" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleGenerateReport">生成调节表</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>

        <el-tab-pane label="对账记录" name="history">
          <el-table :data="reconciliationList" border style="margin-top: 20px">
            <el-table-column prop="bankAccount" label="银行账号" width="180" />
            <el-table-column prop="reconciliationDate" label="对账日期" width="120" />
            <el-table-column prop="bookBalance" label="账面余额" width="120" align="right">
              <template #default="{ row }">
                {{ row.bookBalance?.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="bankBalance" label="银行余额" width="120" align="right">
              <template #default="{ row }">
                {{ row.bankBalance?.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="adjustedBookBalance" label="调整后账面余额" width="150" align="right">
              <template #default="{ row }">
                {{ row.adjustedBookBalance?.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="adjustedBankBalance" label="调整后银行余额" width="150" align="right">
              <template #default="{ row }">
                {{ row.adjustedBankBalance?.toLocaleString() }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="100">
              <template #default="{ row }">
                <el-tag :type="row.status === '平衡' ? 'success' : 'danger'">
                  {{ row.status }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createdBy" label="对账人" width="100" />
            <el-table-column label="操作" width="150" fixed="right">
              <template #default="{ row }">
                <el-button type="primary" size="small" @click="handleViewDetail(row)">查看详情</el-button>
                <el-button type="danger" size="small" @click="handleDeleteRecord(row)">删除</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 余额调节表详情对话框 -->
    <el-dialog v-model="detailVisible" title="银行存款余额调节表" width="900px">
      <div v-if="reportDetail">
        <div style="text-align: center; margin-bottom: 20px">
          <h3>银行存款余额调节表</h3>
          <p>账号：{{ reportDetail.reconciliation.bankAccount }} | 日期：{{ reportDetail.reconciliation.reconciliationDate }}</p>
        </div>

        <el-table :data="adjustmentData" border>
          <el-table-column prop="item" label="项目" width="300" />
          <el-table-column prop="amount" label="金额" align="right">
            <template #default="{ row }">
              {{ row.amount ? row.amount.toLocaleString() : '' }}
            </template>
          </el-table-column>
        </el-table>

        <el-divider />

        <h4>企业已记银行未记（未达账项）</h4>
        <el-table :data="reportDetail.bookOnlyEntries" border style="margin-bottom: 20px">
          <el-table-column prop="voucherNo" label="凭证号" width="150" />
          <el-table-column prop="entryDate" label="日期" width="120" />
          <el-table-column prop="description" label="摘要" />
          <el-table-column prop="totalAmount" label="金额" width="120" align="right">
            <template #default="{ row }">
              {{ row.totalAmount?.toLocaleString() }}
            </template>
          </el-table-column>
        </el-table>

        <h4>银行已记企业未记（未达账项）</h4>
        <el-table :data="reportDetail.bankOnlyStatements" border>
          <el-table-column prop="transactionNo" label="流水号" width="150" />
          <el-table-column prop="transactionDate" label="日期" width="120" />
          <el-table-column prop="purpose" label="用途" />
          <el-table-column prop="transactionType" label="类型" width="80" />
          <el-table-column prop="amount" label="金额" width="120" align="right">
            <template #default="{ row }">
              {{ row.amount?.toLocaleString() }}
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  autoMatchStatements,
  generateReconciliationReport,
  getBankReconciliationList,
  getReconciliationDetail,
  deleteBankReconciliation
} from '@/api/bank'

const activeTab = ref('auto')
const matchResult = ref(null)
const reconciliationList = ref([])
const detailVisible = ref(false)
const reportDetail = ref(null)

const autoForm = ref({
  bankAccount: '',
  dateRange: []
})

const reportForm = ref({
  bankAccount: '',
  reconciliationDate: new Date().toISOString().split('T')[0],
  bankBalance: 0,
  createdBy: ''
})

const adjustmentData = computed(() => {
  if (!reportDetail.value) return []
  
  const r = reportDetail.value
  return [
    { item: '账面余额', amount: r.bookBalance },
    { item: '加：银行已收企业未收', amount: r.bankOnlyIncome },
    { item: '减：银行已付企业未付', amount: r.bankOnlyExpense },
    { item: '调整后账面余额', amount: r.adjustedBookBalance },
    { item: '', amount: null },
    { item: '银行对账单余额', amount: r.bankBalance },
    { item: '加：企业已收银行未收', amount: r.bookOnlyIncome },
    { item: '减：企业已付银行未付', amount: r.bookOnlyExpense },
    { item: '调整后银行余额', amount: r.adjustedBankBalance }
  ]
})

const handleAutoMatch = async () => {
  if (!autoForm.value.bankAccount) {
    ElMessage.warning('请输入银行账号')
    return
  }
  if (!autoForm.value.dateRange || autoForm.value.dateRange.length !== 2) {
    ElMessage.warning('请选择对账期间')
    return
  }

  try {
    const res = await autoMatchStatements(
      autoForm.value.bankAccount,
      autoForm.value.dateRange[0],
      autoForm.value.dateRange[1]
    )
    matchResult.value = res.data
    ElMessage.success('自动对账完成')
  } catch (error) {
    console.error(error)
    ElMessage.error('自动对账失败')
  }
}

const handleGenerateReport = async () => {
  if (!reportForm.value.bankAccount) {
    ElMessage.warning('请输入银行账号')
    return
  }
  if (!reportForm.value.reconciliationDate) {
    ElMessage.warning('请选择对账日期')
    return
  }
  if (!reportForm.value.bankBalance) {
    ElMessage.warning('请输入银行余额')
    return
  }

  try {
    const res = await generateReconciliationReport(
      reportForm.value.bankAccount,
      reportForm.value.reconciliationDate,
      reportForm.value.bankBalance,
      reportForm.value.createdBy
    )
    reportDetail.value = res.data
    detailVisible.value = true
    ElMessage.success('生成余额调节表成功')
    loadReconciliationList()
  } catch (error) {
    console.error(error)
    ElMessage.error('生成失败：' + (error.message || '未知错误'))
  }
}

const loadReconciliationList = async () => {
  try {
    const res = await getBankReconciliationList()
    reconciliationList.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const handleViewDetail = async (row) => {
  try {
    const res = await getReconciliationDetail(row.id)
    reportDetail.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error(error)
    ElMessage.error('加载详情失败')
  }
}

const handleDeleteRecord = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除此对账记录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteBankReconciliation(row.id)
    ElMessage.success('删除成功')
    loadReconciliationList()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadReconciliationList()
})
</script>

<style scoped>
.bank-reconciliation-page {
  padding: 20px;
}
</style>










