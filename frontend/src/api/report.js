import request from '@/utils/request'

// 获取资产负债表
export function getBalanceSheet(endDate) {
  return request({
    url: '/report/balance-sheet',
    method: 'get',
    params: { endDate }
  })
}

// 获取利润表
export function getIncomeStatement(startDate, endDate) {
  return request({
    url: '/report/income-statement',
    method: 'get',
    params: { startDate, endDate }
  })
}

// 获取现金流量表
export function getCashFlow(startDate, endDate) {
  return request({
    url: '/report/cash-flow',
    method: 'get',
    params: { startDate, endDate }
  })
}










