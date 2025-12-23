import request from '@/utils/request'

// 获取首页统计数据
export function getDashboardStatistics() {
  return request({
    url: '/dashboard/statistics',
    method: 'get'
  })
}

// 获取最新通知
export function getDashboardNotifications() {
  return request({
    url: '/dashboard/notifications',
    method: 'get'
  })
}










