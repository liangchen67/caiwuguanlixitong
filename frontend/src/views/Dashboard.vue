<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-content">
            <el-icon class="icon" style="color: #409eff"><Document /></el-icon>
            <div class="info">
              <div class="label">会计凭证</div>
              <div class="value">{{ statistics.journalEntryCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-content">
            <el-icon class="icon" style="color: #67c23a"><ShoppingCart /></el-icon>
            <div class="info">
              <div class="label">采购订单</div>
              <div class="value">{{ statistics.purchaseOrderCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-content">
            <el-icon class="icon" style="color: #e6a23c"><Sell /></el-icon>
            <div class="info">
              <div class="label">销售单</div>
              <div class="value">{{ statistics.salesInvoiceCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <div class="card-content">
            <el-icon class="icon" style="color: #f56c6c"><User /></el-icon>
            <div class="info">
              <div class="label">员工费用</div>
              <div class="value">{{ statistics.employeeExpenseCount || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px">
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>快捷入口</span>
            </div>
          </template>
          <el-space wrap>
            <el-button type="primary" @click="$router.push('/journal-entry')">新建凭证</el-button>
            <el-button type="success" @click="$router.push('/purchase-order')">新建采购单</el-button>
            <el-button type="warning" @click="$router.push('/sales-invoice')">新建销售单</el-button>
            <el-button type="info" @click="$router.push('/employee-expense')">新建费用单</el-button>
          </el-space>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>
            <div class="card-header">
              <span>系统通知</span>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item 
              v-for="notification in notifications" 
              :key="notification.id"
              :timestamp="formatDateTime(notification.timestamp)" 
              placement="top">
              {{ notification.message }}
            </el-timeline-item>
            <el-empty v-if="notifications.length === 0" description="暂无通知" :image-size="80" />
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getDashboardStatistics, getDashboardNotifications } from '@/api/dashboard'

const statistics = ref({
  journalEntryCount: 0,
  purchaseOrderCount: 0,
  salesInvoiceCount: 0,
  employeeExpenseCount: 0
})

const notifications = ref([])

const loadStatistics = async () => {
  try {
    const res = await getDashboardStatistics()
    if (res.data) {
      statistics.value = res.data
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const loadNotifications = async () => {
  try {
    const res = await getDashboardNotifications()
    if (res.data) {
      const allNotifications = []
      
      // 处理会计凭证
      if (res.data.journalEntries) {
        res.data.journalEntries.forEach(entry => {
          allNotifications.push({
            id: `journal-${entry.id}`,
            timestamp: entry.createdAt,
            message: `新增会计凭证 ${entry.voucherNo}`
          })
        })
      }
      
      // 处理采购订单
      if (res.data.purchaseOrders) {
        res.data.purchaseOrders.forEach(order => {
          allNotifications.push({
            id: `purchase-${order.id}`,
            timestamp: order.createdAt,
            message: `新增采购订单 ${order.orderNo}`
          })
        })
      }
      
      // 处理销售单
      if (res.data.salesInvoices) {
        res.data.salesInvoices.forEach(invoice => {
          allNotifications.push({
            id: `sales-${invoice.id}`,
            timestamp: invoice.createdAt,
            message: `新增销售单 ${invoice.invoiceNo}`
          })
        })
      }
      
      // 按时间倒序排序
      allNotifications.sort((a, b) => new Date(b.timestamp) - new Date(a.timestamp))
      
      // 只显示最新的5条
      notifications.value = allNotifications.slice(0, 5)
    }
  } catch (error) {
    console.error('加载通知数据失败:', error)
  }
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit'
  })
}

onMounted(() => {
  loadStatistics()
  loadNotifications()
  
  // 每30秒自动刷新一次数据
  setInterval(() => {
    loadStatistics()
    loadNotifications()
  }, 30000)
})
</script>

<style scoped>
.dashboard {
  padding: 20px;
}

.card-content {
  display: flex;
  align-items: center;
}

.icon {
  font-size: 48px;
  margin-right: 20px;
}

.info .label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.info .value {
  font-size: 24px;
  font-weight: bold;
}

.card-header {
  font-weight: bold;
}
</style>

