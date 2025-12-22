<template>
  <div class="customer-page">
    <el-card>
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="handleAdd">新增客户</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="code" label="客户编码" width="120" />
        <el-table-column prop="name" label="客户名称" width="200" />
        <el-table-column prop="contactPerson" label="联系人" width="100" />
        <el-table-column prop="contactPhone" label="联系电话" width="120" />
        <el-table-column prop="email" label="邮箱" width="150" />
        <el-table-column prop="address" label="地址" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === '启用' ? 'success' : 'danger'">
              {{ row.status }}
            </el-tag>
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

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="700px">
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="客户编码" prop="code">
              <el-input v-model="form.code" placeholder="请输入编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="客户名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="联系人">
              <el-input v-model="form.contactPerson" placeholder="请输入联系人" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话">
              <el-input v-model="form.contactPhone" placeholder="请输入电话" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税号">
              <el-input v-model="form.taxNumber" placeholder="请输入税号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="地址">
          <el-input v-model="form.address" placeholder="请输入地址" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开户行">
              <el-input v-model="form.bankName" placeholder="请输入开户行" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="银行账号">
              <el-input v-model="form.bankAccount" placeholder="请输入银行账号" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio label="启用">启用</el-radio>
            <el-radio label="禁用">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" :rows="3" />
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
import { getCustomerList, saveCustomer, deleteCustomer } from '@/api/business'

const formRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('新增客户')
const tableData = ref([])

const form = ref({
  code: '',
  name: '',
  contactPerson: '',
  contactPhone: '',
  email: '',
  address: '',
  taxNumber: '',
  bankName: '',
  bankAccount: '',
  status: '启用',
  remark: ''
})

const rules = {
  code: [{ required: true, message: '请输入客户编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入客户名称', trigger: 'blur' }]
}

const loadData = async () => {
  try {
    const res = await getCustomerList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增客户'
  form.value = {
    code: '',
    name: '',
    contactPerson: '',
    contactPhone: '',
    email: '',
    address: '',
    taxNumber: '',
    bankName: '',
    bankAccount: '',
    status: '启用',
    remark: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑客户'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    await saveCustomer(form.value)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该客户吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteCustomer(row.id)
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
})
</script>

<style scoped>
.customer-page {
  padding: 20px;
}
</style>







