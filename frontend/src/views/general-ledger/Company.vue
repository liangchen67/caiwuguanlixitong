<template>
  <div class="company-page">
    <el-card>
      <el-form :model="form" label-width="120px" :rules="rules" ref="formRef">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="企业规模" prop="scale">
              <el-select v-model="form.scale" placeholder="请选择企业规模" style="width: 100%">
                <el-option label="小型" value="小型" />
                <el-option label="中型" value="中型" />
                <el-option label="大型" value="大型" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="注册资金" prop="registeredCapital">
              <el-input v-model.number="form.registeredCapital" placeholder="请输入注册资金" type="number" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属行业" prop="industry">
              <el-input v-model="form.industry" placeholder="请输入所属行业" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="法人代表" prop="legalPerson">
              <el-input v-model="form.legalPerson" placeholder="请输入法人代表" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="联系电话" prop="contactPhone">
              <el-input v-model="form.contactPhone" placeholder="请输入联系电话" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" placeholder="请输入邮箱" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="税号" prop="taxNumber">
              <el-input v-model="form.taxNumber" placeholder="请输入税号" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="公司地址" prop="address">
          <el-input v-model="form.address" type="textarea" placeholder="请输入公司地址" :rows="3" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSave">保存</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getCompanyList, saveCompany } from '@/api/company'

const formRef = ref(null)
const form = ref({
  name: '',
  scale: '',
  registeredCapital: null,
  industry: '',
  address: '',
  legalPerson: '',
  contactPhone: '',
  email: '',
  taxNumber: ''
})

const rules = {
  name: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
  scale: [{ required: true, message: '请选择企业规模', trigger: 'change' }],
  registeredCapital: [{ required: true, message: '请输入注册资金', trigger: 'blur' }]
}

const loadCompany = async () => {
  try {
    const res = await getCompanyList()
    if (res.data && res.data.length > 0) {
      form.value = res.data[0]
    }
  } catch (error) {
    console.error(error)
  }
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    await saveCompany(form.value)
    ElMessage.success('保存成功')
    loadCompany()
  } catch (error) {
    console.error(error)
  }
}

const handleReset = () => {
  formRef.value.resetFields()
}

onMounted(() => {
  loadCompany()
})
</script>

<style scoped>
.company-page {
  padding: 20px;
}
</style>










