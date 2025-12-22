import request from '@/utils/request'

// 供应商
export function getSupplierList() {
  return request({
    url: '/supplier/list',
    method: 'get'
  })
}

export function saveSupplier(data) {
  return request({
    url: '/supplier/save',
    method: 'post',
    data
  })
}

export function deleteSupplier(id) {
  return request({
    url: `/supplier/${id}`,
    method: 'delete'
  })
}

// 采购订单
export function getPurchaseOrderList() {
  return request({
    url: '/purchase-order/list',
    method: 'get'
  })
}

export function savePurchaseOrder(data) {
  return request({
    url: '/purchase-order/save',
    method: 'post',
    data
  })
}

export function deletePurchaseOrder(id) {
  return request({
    url: `/purchase-order/${id}`,
    method: 'delete'
  })
}

// 客户
export function getCustomerList() {
  return request({
    url: '/customer/list',
    method: 'get'
  })
}

export function saveCustomer(data) {
  return request({
    url: '/customer/save',
    method: 'post',
    data
  })
}

export function deleteCustomer(id) {
  return request({
    url: `/customer/${id}`,
    method: 'delete'
  })
}

// 销售单
export function getSalesInvoiceList() {
  return request({
    url: '/sales-invoice/list',
    method: 'get'
  })
}

export function saveSalesInvoice(data) {
  return request({
    url: '/sales-invoice/save',
    method: 'post',
    data
  })
}

export function deleteSalesInvoice(id) {
  return request({
    url: `/sales-invoice/${id}`,
    method: 'delete'
  })
}

// 员工
export function getEmployeeList() {
  return request({
    url: '/employee/list',
    method: 'get'
  })
}

export function saveEmployee(data) {
  return request({
    url: '/employee/save',
    method: 'post',
    data
  })
}

export function deleteEmployee(id) {
  return request({
    url: `/employee/${id}`,
    method: 'delete'
  })
}

// 员工费用
export function getEmployeeExpenseList() {
  return request({
    url: '/employee-expense/list',
    method: 'get'
  })
}

export function saveEmployeeExpense(data) {
  return request({
    url: '/employee-expense/save',
    method: 'post',
    data
  })
}

export function deleteEmployeeExpense(id) {
  return request({
    url: `/employee-expense/${id}`,
    method: 'delete'
  })
}







