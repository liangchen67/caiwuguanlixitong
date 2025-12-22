<template>
  <div class="employee-expense-page">
    <el-card>
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="handleAdd">新建费用单</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="expenseNo" label="费用单号" width="150" />
        <el-table-column prop="employee.name" label="员工" width="100" />
        <el-table-column prop="expenseDate" label="费用日期" width="120" />
        <el-table-column prop="expenseType" label="费用类型" width="120" />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            {{ row.amount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="description" label="费用说明" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-form-item label="员工" prop="employee">
          <el-select v-model="form.employee.id" placeholder="请选择员工" filterable style="width: 100%">
            <el-option 
              v-for="employee in employees" 
              :key="employee.id" 
              :label="`${employee.employeeNo} - ${employee.name} - ${employee.department}`" 
              :value="employee.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="费用日期" prop="expenseDate">
          <el-date-picker v-model="form.expenseDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="费用类型" prop="expenseType">
          <el-select v-model="form.expenseType" placeholder="请选择费用类型" style="width: 100%">
            <el-option label="差旅费" value="差旅费" />
            <el-option label="办公费" value="办公费" />
            <el-option label="通讯费" value="通讯费" />
            <el-option label="交通费" value="交通费" />
            <el-option label="招待费" value="招待费" />
            <el-option label="培训费" value="培训费" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input v-model.number="form.amount" placeholder="请输入金额" type="number" />
        </el-form-item>
        <el-form-item label="费用说明" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入费用说明" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="草稿" value="草稿" />
            <el-option label="已提交" value="已提交" />
            <el-option label="已审核" value="已审核" />
            <el-option label="已支付" value="已支付" />
            <el-option label="已拒绝" value="已拒绝" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="2" />
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
import { getEmployeeExpenseList, saveEmployeeExpense, deleteEmployeeExpense, getEmployeeList } from '@/api/business'

const formRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('新建费用单')
const tableData = ref([])
const employees = ref([]) // 员工列表

const form = ref({
  employee: { id: null },
  expenseDate: '',
  expenseType: '',
  amount: 0,
  description: '',
  status: '草稿',
  remark: ''
})

const rules = {
  employee: [{ required: true, message: '请选择员工', trigger: 'change' }],
  expenseDate: [{ required: true, message: '请选择费用日期', trigger: 'change' }],
  expenseType: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }],
  description: [{ required: true, message: '请输入费用说明', trigger: 'blur' }]
}

const getStatusType = (status) => {
  const map = {
    '草稿': 'info',
    '已提交': 'warning',
    '已审核': 'primary',
    '已支付': 'success',
    '已拒绝': 'danger'
  }
  return map[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await getEmployeeExpenseList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const loadEmployees = async () => {
  try {
    const res = await getEmployeeList()
    employees.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新建费用单'
  form.value = {
    employee: { id: null },
    expenseDate: new Date().toISOString().split('T')[0],
    expenseType: '',
    amount: 0,
    description: '',
    status: '草稿',
    remark: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑费用单'
  form.value = { ...row }
  if (!form.value.employee) {
    form.value.employee = { id: null }
  }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    await saveEmployeeExpense(form.value)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该费用单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteEmployeeExpense(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

onMounted(() => {
  loadData()
  loadEmployees()
})
</script>

<style scoped>
.employee-expense-page {
  padding: 20px;
}
</style>

