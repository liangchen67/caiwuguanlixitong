import request from '@/utils/request'

// 银行流水管理
export const getBankStatementList = () => {
  return request.get('/bank-statement/list')
}

export const saveBankStatement = (data) => {
  return request.post('/bank-statement/save', data)
}

export const deleteBankStatement = (id) => {
  return request.delete(`/bank-statement/${id}`)
}

export const getBankStatementStatistics = (bankAccount = '') => {
  return request.get('/bank-statement/statistics', { params: { bankAccount } })
}

export const getBankStatementByDateRange = (startDate, endDate) => {
  return request.get('/bank-statement/date-range', {
    params: { startDate, endDate }
  })
}

export const unmatchStatement = (id) => {
  return request.post(`/bank-statement/unmatch/${id}`)
}

// 银行对账
export const autoMatchStatements = (bankAccount, startDate, endDate) => {
  return request.post('/bank-reconciliation/auto-match', {
    bankAccount,
    startDate,
    endDate
  })
}

export const generateReconciliationReport = (bankAccount, reconciliationDate, bankBalance, createdBy) => {
  return request.post('/bank-reconciliation/generate-report', {
    bankAccount,
    reconciliationDate,
    bankBalance,
    createdBy
  })
}

export const getBankReconciliationList = () => {
  return request.get('/bank-reconciliation/list')
}

export const getReconciliationDetail = (id) => {
  return request.get(`/bank-reconciliation/detail/${id}`)
}

export const deleteBankReconciliation = (id) => {
  return request.delete(`/bank-reconciliation/${id}`)
}
