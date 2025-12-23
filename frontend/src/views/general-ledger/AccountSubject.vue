<template>
  <div class="account-subject-page">
    <el-card>
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="handleAdd">新增科目</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="code" label="科目编码" width="120" />
        <el-table-column prop="name" label="科目名称" width="200" />
        <el-table-column prop="type" label="科目类型" width="120" />
        <el-table-column prop="category" label="科目类别" width="150" />
        <el-table-column prop="direction" label="借贷方向" width="100" />
        <el-table-column prop="level" label="级别" width="80" />
        <el-table-column prop="enabled" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.enabled ? 'success' : 'danger'">
              {{ row.enabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" />
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
        <el-form-item label="科目编码" prop="code">
          <el-input v-model="form.code" placeholder="请输入科目编码" />
        </el-form-item>
        <el-form-item label="科目名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入科目名称" />
        </el-form-item>
        <el-form-item label="科目类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择科目类型" style="width: 100%">
            <el-option label="资产" value="资产" />
            <el-option label="负债" value="负债" />
            <el-option label="所有者权益" value="所有者权益" />
            <el-option label="成本" value="成本" />
            <el-option label="损益" value="损益" />
          </el-select>
        </el-form-item>
        <el-form-item label="科目类别" prop="category">
          <el-input v-model="form.category" placeholder="请输入科目类别" />
        </el-form-item>
        <el-form-item label="借贷方向" prop="direction">
          <el-radio-group v-model="form.direction">
            <el-radio label="借方">借方</el-radio>
            <el-radio label="贷方">贷方</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="科目级别" prop="level">
          <el-input-number v-model="form.level" :min="1" :max="5" />
        </el-form-item>
        <el-form-item label="父级科目" prop="parentId">
          <el-input-number v-model="form.parentId" :min="0" />
        </el-form-item>
        <el-form-item label="是否启用" prop="enabled">
          <el-switch v-model="form.enabled" />
        </el-form-item>
        <el-form-item label="说明">
          <el-input v-model="form.description" type="textarea" :rows="3" />
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
import { getAccountSubjectList, saveAccountSubject, deleteAccountSubject } from '@/api/ledger'

const formRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('新增科目')
const tableData = ref([])

const form = ref({
  code: '',
  name: '',
  type: '',
  category: '',
  parentId: 0,
  level: 1,
  direction: '借方',
  enabled: true,
  description: ''
})

const rules = {
  code: [{ required: true, message: '请输入科目编码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入科目名称', trigger: 'blur' }],
  type: [{ required: true, message: '请选择科目类型', trigger: 'change' }]
}

const loadData = async () => {
  try {
    const res = await getAccountSubjectList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新增科目'
  form.value = {
    code: '',
    name: '',
    type: '',
    category: '',
    parentId: 0,
    level: 1,
    direction: '借方',
    enabled: true,
    description: ''
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑科目'
  form.value = { ...row }
  dialogVisible.value = true
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    await saveAccountSubject(form.value)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error(error)
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该科目吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteAccountSubject(row.id)
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
.account-subject-page {
  padding: 20px;
}
</style>










