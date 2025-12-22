<template>
  <div class="sales-invoice-page">
    <el-card>
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="handleAdd">新建销售单</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="invoiceNo" label="销售单号" width="150" />
        <el-table-column prop="customer.name" label="客户" width="200" />
        <el-table-column prop="invoiceDate" label="销售日期" width="120" />
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="{ row }">
            {{ row.totalAmount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="900px">
      <el-form :model="form" label-width="100px" ref="formRef">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户">
              <el-select v-model="form.customer.id" placeholder="请选择客户" filterable style="width: 100%">
                <el-option 
                  v-for="customer in customers" 
                  :key="customer.id" 
                  :label="`${customer.code} - ${customer.name}`" 
                  :value="customer.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销售日期">
              <el-date-picker v-model="form.invoiceDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="到期日">
              <el-date-picker v-model="form.dueDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="收款方式">
              <el-input v-model="form.paymentMethod" placeholder="收款方式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remark" placeholder="备注" />
        </el-form-item>

        <el-divider>销售明细</el-divider>
        <el-button type="primary" size="small" @click="handleAddLine" style="margin-bottom: 10px">添加明细</el-button>
        
        <el-table :data="form.invoiceLines" border>
          <el-table-column label="商品名称" width="150">
            <template #default="{ row }">
              <el-input v-model="row.itemName" placeholder="商品名称" />
            </template>
          </el-table-column>
          <el-table-column label="规格型号" width="120">
            <template #default="{ row }">
              <el-input v-model="row.specification" placeholder="规格" />
            </template>
          </el-table-column>
          <el-table-column label="单位" width="80">
            <template #default="{ row }">
              <el-input v-model="row.unit" placeholder="单位" />
            </template>
          </el-table-column>
          <el-table-column label="数量" width="100">
            <template #default="{ row }">
              <el-input v-model.number="row.quantity" type="number" />
            </template>
          </el-table-column>
          <el-table-column label="单价" width="100">
            <template #default="{ row }">
              <el-input v-model.number="row.unitPrice" type="number" />
            </template>
          </el-table-column>
          <el-table-column label="金额" width="100">
            <template #default="{ row }">
              {{ (row.quantity * row.unitPrice).toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column label="税率%" width="80">
            <template #default="{ row }">
              <el-input v-model.number="row.taxRate" type="number" />
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80">
            <template #default="{ $index }">
              <el-button type="danger" size="small" @click="handleDeleteLine($index)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
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
import { getSalesInvoiceList, saveSalesInvoice, deleteSalesInvoice, getCustomerList } from '@/api/business'

const formRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('新建销售单')
const tableData = ref([])
const customers = ref([]) // 客户列表

const form = ref({
  customer: { id: null },
  invoiceDate: '',
  dueDate: '',
  paymentMethod: '',
  status: '草稿',
  remark: '',
  invoiceLines: []
})

const getStatusType = (status) => {
  const map = {
    '草稿': 'info',
    '已提交': 'warning',
    '已审核': 'primary',
    '已完成': 'success',
    '已取消': 'danger'
  }
  return map[status] || 'info'
}

const loadData = async () => {
  try {
    const res = await getSalesInvoiceList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const loadCustomers = async () => {
  try {
    const res = await getCustomerList()
    customers.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新建销售单'
  form.value = {
    customer: { id: null },
    invoiceDate: new Date().toISOString().split('T')[0],
    dueDate: '',
    paymentMethod: '',
    status: '草稿',
    remark: '',
    invoiceLines: []
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑销售单'
  form.value = { ...row }
  if (!form.value.customer) {
    form.value.customer = { id: null }
  }
  dialogVisible.value = true
}

const handleAddLine = () => {
  form.value.invoiceLines.push({
    itemName: '',
    itemCode: '',
    specification: '',
    unit: '个',
    quantity: 1,
    unitPrice: 0,
    amount: 0,
    taxRate: 13
  })
}

const handleDeleteLine = (index) => {
  form.value.invoiceLines.splice(index, 1)
}

const handleSave = async () => {
  try {
    // 计算每行的金额
    form.value.invoiceLines.forEach(line => {
      line.amount = line.quantity * line.unitPrice
    })
    
    await saveSalesInvoice(form.value)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该销售单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteSalesInvoice(row.id)
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
  loadCustomers()
})
</script>

<style scoped>
.sales-invoice-page {
  padding: 20px;
}
</style>

