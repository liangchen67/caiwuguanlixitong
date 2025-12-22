<template>
  <div class="bank-statement-page">
    <el-card style="margin-bottom: 20px">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-button type="primary" @click="handleAdd">录入银行流水</el-button>
          <el-button type="success" @click="handleBatchImport">批量导入</el-button>
        </el-col>
        <el-col :span="18" style="text-align: right">
          <el-input v-model="searchAccount" placeholder="银行账号" style="width: 200px; margin-right: 10px" />
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 300px; margin-right: 10px"
          />
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-col>
      </el-row>
    </el-card>

    <el-card>
      <div style="margin-bottom: 20px">
        <el-statistic title="总流水数" :value="statistics.total" style="display: inline-block; margin-right: 30px" />
        <el-statistic title="已对账" :value="statistics.reconciled" style="display: inline-block; margin-right: 30px" />
        <el-statistic title="未对账" :value="statistics.unreconciled" style="display: inline-block" />
      </div>

      <el-table :data="tableData" border style="width: 100%; margin-top: 20px">
        <el-table-column prop="transactionDate" label="交易日期" width="120" />
        <el-table-column prop="transactionNo" label="流水号" width="180" />
        <el-table-column prop="bankName" label="银行" width="150" />
        <el-table-column prop="bankAccount" label="账号" width="180" />
        <el-table-column prop="transactionType" label="类型" width="80">
          <template #default="{ row }">
            <el-tag :type="row.transactionType === '收入' ? 'success' : 'danger'">
              {{ row.transactionType }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="amount" label="金额" width="120" align="right">
          <template #default="{ row }">
            {{ row.amount?.toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="counterparty" label="对方户名" width="150" />
        <el-table-column prop="purpose" label="用途" />
        <el-table-column prop="reconciliationStatus" label="对账状态" width="100">
          <template #default="{ row }">
            <el-tag v-if="row.reconciliationStatus === '已对账'" type="success">已对账</el-tag>
            <el-tag v-else-if="row.reconciliationStatus === '未对账'" type="warning">未对账</el-tag>
            <el-tag v-else type="info">{{ row.reconciliationStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.reconciliationStatus === '未对账'" type="success" size="small" @click="handleMatch(row)">匹配</el-button>
            <el-button v-else type="warning" size="small" @click="handleUnmatch(row)">取消</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="form" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交易日期">
              <el-date-picker v-model="form.transactionDate" type="date" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="交易类型">
              <el-select v-model="form.transactionType" style="width: 100%">
                <el-option label="收入" value="收入" />
                <el-option label="支出" value="支出" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="银行名称">
          <el-input v-model="form.bankName" placeholder="请输入银行名称" />
        </el-form-item>
        <el-form-item label="银行账号">
          <el-input v-model="form.bankAccount" placeholder="请输入银行账号" />
        </el-form-item>
        <el-form-item label="交易流水号">
          <el-input v-model="form.transactionNo" placeholder="请输入交易流水号" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="交易金额">
              <el-input-number v-model="form.amount" :min="0" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="余额">
              <el-input-number v-model="form.balance" :precision="2" style="width: 100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="对方户名">
          <el-input v-model="form.counterparty" placeholder="请输入对方户名" />
        </el-form-item>
        <el-form-item label="对方账号">
          <el-input v-model="form.counterpartyAccount" placeholder="请输入对方账号" />
        </el-form-item>
        <el-form-item label="用途/摘要">
          <el-input v-model="form.purpose" type="textarea" :rows="3" placeholder="请输入用途或摘要" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getBankStatementList,
  saveBankStatement,
  deleteBankStatement,
  unmatchStatement,
  getBankStatementStatistics,
  getBankStatementByDateRange
} from '@/api/bank'

const dialogVisible = ref(false)
const dialogTitle = ref('录入银行流水')
const tableData = ref([])
const searchAccount = ref('')
const dateRange = ref([])
const statistics = ref({
  total: 0,
  reconciled: 0,
  unreconciled: 0
})

const form = ref({
  transactionDate: new Date().toISOString().split('T')[0],
  transactionNo: '',
  bankName: '',
  bankAccount: '',
  transactionType: '收入',
  amount: 0,
  balance: 0,
  counterparty: '',
  counterpartyAccount: '',
  purpose: '',
  remark: ''
})

const loadData = async () => {
  try {
    const res = await getBankStatementList()
    tableData.value = res.data || []
    loadStatistics()
  } catch (error) {
    console.error(error)
    ElMessage.error('加载数据失败')
  }
}

const loadStatistics = async () => {
  try {
    const res = await getBankStatementStatistics(searchAccount.value)
    statistics.value = res.data || { total: 0, reconciled: 0, unreconciled: 0 }
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '录入银行流水'
  form.value = {
    transactionDate: new Date().toISOString().split('T')[0],
    transactionNo: '',
    bankName: '',
    bankAccount: '',
    transactionType: '收入',
    amount: 0,
    balance: 0,
    counterparty: '',
    counterpartyAccount: '',
    purpose: '',
    remark: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑银行流水'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    await saveBankStatement(form.value)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
    ElMessage.error('保存失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除此流水吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteBankStatement(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
      ElMessage.error('删除失败')
    }
  }
}

const handleMatch = (row) => {
  ElMessage.info('请前往"银行对账"页面进行匹配')
}

const handleUnmatch = async (row) => {
  try {
    await ElMessageBox.confirm('确定取消匹配吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await unmatchStatement(row.id)
    ElMessage.success('取消匹配成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
      ElMessage.error('取消匹配失败')
    }
  }
}

const handleSearch = async () => {
  if (dateRange.value && dateRange.value.length === 2) {
    try {
      const res = await getBankStatementByDateRange(dateRange.value[0], dateRange.value[1])
      tableData.value = res.data || []
    } catch (error) {
      console.error(error)
      ElMessage.error('查询失败')
    }
  } else {
    loadData()
  }
}

const handleReset = () => {
  searchAccount.value = ''
  dateRange.value = []
  loadData()
}

const handleBatchImport = () => {
  ElMessage.info('批量导入功能开发中...')
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.bank-statement-page {
  padding: 20px;
}
</style>

