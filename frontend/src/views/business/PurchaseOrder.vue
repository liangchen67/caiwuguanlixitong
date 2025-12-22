<template>
  <div class="purchase-order-page">
    <el-card>
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="handleAdd">新建采购单</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="orderNo" label="订单号" width="150" />
        <el-table-column prop="supplier.name" label="供应商" width="200" />
        <el-table-column prop="orderDate" label="订单日期" width="120" />
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
            <el-form-item label="供应商">
              <el-select v-model="form.supplier.id" placeholder="请选择供应商" filterable style="width: 100%">
                <el-option 
                  v-for="supplier in suppliers" 
                  :key="supplier.id" 
                  :label="`${supplier.code} - ${supplier.name}`" 
                  :value="supplier.id" 
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="订单日期">
              <el-date-picker v-model="form.orderDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="预计到货日期">
              <el-date-picker v-model="form.expectedDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="付款方式">
              <el-input v-model="form.paymentMethod" placeholder="付款方式" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="备注">
          <el-input v-model="form.remark" placeholder="备注" />
        </el-form-item>

        <el-divider>订单明细</el-divider>
        <el-button type="primary" size="small" @click="handleAddLine" style="margin-bottom: 10px">添加明细</el-button>
        
        <el-table :data="form.orderLines" border>
          <el-table-column label="物料名称" width="150">
            <template #default="{ row }">
              <el-input v-model="row.itemName" placeholder="物料名称" />
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
import { getPurchaseOrderList, savePurchaseOrder, deletePurchaseOrder, getSupplierList } from '@/api/business'

const formRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('新建采购单')
const tableData = ref([])
const suppliers = ref([]) // 供应商列表

const form = ref({
  supplier: { id: null },
  orderDate: '',
  expectedDate: '',
  paymentMethod: '',
  status: '草稿',
  remark: '',
  orderLines: []
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
    const res = await getPurchaseOrderList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const loadSuppliers = async () => {
  try {
    const res = await getSupplierList()
    suppliers.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新建采购单'
  form.value = {
    supplier: { id: null },
    orderDate: new Date().toISOString().split('T')[0],
    expectedDate: '',
    paymentMethod: '',
    status: '草稿',
    remark: '',
    orderLines: []
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑采购单'
  form.value = { ...row }
  if (!form.value.supplier) {
    form.value.supplier = { id: null }
  }
  dialogVisible.value = true
}

const handleAddLine = () => {
  form.value.orderLines.push({
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
  form.value.orderLines.splice(index, 1)
}

const handleSave = async () => {
  try {
    // 计算每行的金额
    form.value.orderLines.forEach(line => {
      line.amount = line.quantity * line.unitPrice
    })
    
    await savePurchaseOrder(form.value)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该采购单吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deletePurchaseOrder(row.id)
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
  loadSuppliers()
})
</script>

<style scoped>
.purchase-order-page {
  padding: 20px;
}
</style>

