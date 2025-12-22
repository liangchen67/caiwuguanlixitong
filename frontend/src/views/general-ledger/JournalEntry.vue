<template>
  <div class="journal-entry-page">
    <el-card>
      <div style="margin-bottom: 20px">
        <el-button type="primary" @click="handleAdd">新建凭证</el-button>
      </div>

      <el-table :data="tableData" border style="width: 100%">
        <el-table-column prop="voucherNo" label="凭证号" width="150" />
        <el-table-column prop="entryDate" label="记账日期" width="120" />
        <el-table-column prop="description" label="摘要" />
        <el-table-column prop="totalAmount" label="总金额" width="120">
          <template #default="{ row }">
            {{ row.totalAmount?.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '已过账' ? 'success' : 'warning'">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="制单人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button v-if="row.status !== '已过账'" type="success" size="small" @click="handlePost(row)">过账</el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="900px">
      <el-form :model="form" label-width="100px" ref="formRef">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="记账日期">
              <el-date-picker v-model="form.entryDate" type="date" placeholder="选择日期" style="width: 100%" value-format="YYYY-MM-DD" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="制单人">
              <el-input v-model="form.createdBy" placeholder="请输入制单人" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="摘要">
          <el-input v-model="form.description" placeholder="请输入摘要" />
        </el-form-item>

        <el-divider>分录明细</el-divider>
        <el-button type="primary" size="small" @click="handleAddLine" style="margin-bottom: 10px">添加分录</el-button>
        
        <el-table :data="form.entryLines" border>
          <el-table-column label="会计科目" width="250">
            <template #default="{ row }">
              <el-select v-model="row.accountSubject.id" placeholder="请选择会计科目" filterable style="width: 100%">
                <el-option 
                  v-for="subject in accountSubjects" 
                  :key="subject.id" 
                  :label="`${subject.code} - ${subject.name}`" 
                  :value="subject.id" 
                />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="借贷方向" width="120">
            <template #default="{ row }">
              <el-select v-model="row.direction" placeholder="选择">
                <el-option label="借" value="借" />
                <el-option label="贷" value="贷" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="金额" width="150">
            <template #default="{ row }">
              <el-input-number v-model="row.amount" :min="0" :precision="2" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="备注">
            <template #default="{ row }">
              <el-input v-model="row.remark" placeholder="备注" />
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
import { getJournalEntryList, saveJournalEntry, postJournalEntry, deleteJournalEntry, getAccountSubjectList } from '@/api/ledger'

const formRef = ref(null)
const dialogVisible = ref(false)
const dialogTitle = ref('新建凭证')
const tableData = ref([])
const accountSubjects = ref([]) // 会计科目列表

const form = ref({
  entryDate: '',
  description: '',
  createdBy: '',
  status: '草稿',
  entryLines: []
})

const loadData = async () => {
  try {
    const res = await getJournalEntryList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const loadAccountSubjects = async () => {
  try {
    const res = await getAccountSubjectList()
    accountSubjects.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  dialogTitle.value = '新建凭证'
  form.value = {
    entryDate: new Date().toISOString().split('T')[0],
    description: '',
    createdBy: '',
    status: '草稿',
    entryLines: []
  }
  dialogVisible.value = true
}

const handleEdit = (row) => {
  dialogTitle.value = '编辑凭证'
  // 深拷贝，保留ID和凭证号
  form.value = {
    id: row.id,
    voucherNo: row.voucherNo,
    entryDate: row.entryDate,
    description: row.description,
    createdBy: row.createdBy,
    status: row.status,
    entryLines: row.entryLines ? row.entryLines.map(line => ({
      id: line.id,
      accountSubject: { id: line.accountSubject?.id || null },
      direction: line.direction,
      amount: line.amount,
      remark: line.remark,
      currency: line.currency || 'CNY',
      exchangeRate: line.exchangeRate || 1
    })) : []
  }
  dialogVisible.value = true
}

const handleAddLine = () => {
  form.value.entryLines.push({
    accountSubject: { id: null },
    direction: '借',
    amount: 0,
    remark: '',
    currency: 'CNY',
    exchangeRate: 1
  })
}

const handleDeleteLine = (index) => {
  form.value.entryLines.splice(index, 1)
}

const handleSave = async () => {
  try {
    // 验证数据
    if (!form.value.entryDate) {
      ElMessage.error('请选择记账日期')
      return
    }
    if (!form.value.description) {
      ElMessage.error('请输入摘要')
      return
    }
    if (!form.value.createdBy) {
      ElMessage.error('请输入制单人')
      return
    }
    if (!form.value.entryLines || form.value.entryLines.length === 0) {
      ElMessage.error('请至少添加一条分录')
      return
    }
    
    // 验证每条分录
    for (let i = 0; i < form.value.entryLines.length; i++) {
      const line = form.value.entryLines[i]
      if (!line.accountSubject || !line.accountSubject.id) {
        ElMessage.error(`第${i + 1}行：请选择会计科目`)
        return
      }
      if (!line.direction) {
        ElMessage.error(`第${i + 1}行：请选择借贷方向`)
        return
      }
      if (!line.amount || line.amount <= 0) {
        ElMessage.error(`第${i + 1}行：金额必须大于0`)
        return
      }
    }
    
    // 验证借贷平衡
    let debitTotal = 0
    let creditTotal = 0
    form.value.entryLines.forEach(line => {
      if (line.direction === '借') {
        debitTotal += parseFloat(line.amount || 0)
      } else if (line.direction === '贷') {
        creditTotal += parseFloat(line.amount || 0)
      }
    })
    
    if (Math.abs(debitTotal - creditTotal) > 0.01) {
      ElMessage.warning(`借贷不平衡！借方：${debitTotal.toFixed(2)}，贷方：${creditTotal.toFixed(2)}`)
      // 不阻止保存，只是警告
    }
    
    console.log('保存凭证数据：', JSON.stringify(form.value, null, 2))
    
    await saveJournalEntry(form.value)
    ElMessage.success('保存成功')
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('保存失败详情：', error)
    ElMessage.error('保存失败：' + (error.message || '未知错误'))
  }
}

const handlePost = async (row) => {
  try {
    await ElMessageBox.confirm('确定过账吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await postJournalEntry(row.id)
    ElMessage.success('过账成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      console.error(error)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定删除该凭证吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    await deleteJournalEntry(row.id)
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
  loadAccountSubjects()
})
</script>

<style scoped>
.journal-entry-page {
  padding: 20px;
}
</style>

