import { createRouter, createWebHistory } from 'vue-router'
import Layout from '@/views/Layout.vue'

const routes = [
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      // 总账模块
      {
        path: '/company',
        name: 'Company',
        component: () => import('@/views/general-ledger/Company.vue'),
        meta: { title: '企业初始化' }
      },
      {
        path: '/account-subject',
        name: 'AccountSubject',
        component: () => import('@/views/general-ledger/AccountSubject.vue'),
        meta: { title: '会计科目管理' }
      },
      {
        path: '/journal-entry',
        name: 'JournalEntry',
        component: () => import('@/views/general-ledger/JournalEntry.vue'),
        meta: { title: '会计分录' }
      },
      {
        path: '/reports',
        name: 'Reports',
        component: () => import('@/views/general-ledger/Reports.vue'),
        meta: { title: '财务报表' }
      },
      // 采购模块
      {
        path: '/supplier',
        name: 'Supplier',
        component: () => import('@/views/business/Supplier.vue'),
        meta: { title: '供应商管理' }
      },
      {
        path: '/purchase-order',
        name: 'PurchaseOrder',
        component: () => import('@/views/business/PurchaseOrder.vue'),
        meta: { title: '采购订单' }
      },
      // 销售模块
      {
        path: '/customer',
        name: 'Customer',
        component: () => import('@/views/business/Customer.vue'),
        meta: { title: '客户管理' }
      },
      {
        path: '/sales-invoice',
        name: 'SalesInvoice',
        component: () => import('@/views/business/SalesInvoice.vue'),
        meta: { title: '销售单管理' }
      },
      // 员工费用模块
      {
        path: '/employee',
        name: 'Employee',
        component: () => import('@/views/business/Employee.vue'),
        meta: { title: '员工管理' }
      },
      {
        path: '/employee-expense',
        name: 'EmployeeExpense',
        component: () => import('@/views/business/EmployeeExpense.vue'),
        meta: { title: '员工费用' }
      },
      // 银行对账模块
      {
        path: '/bank-statement',
        name: 'BankStatement',
        component: () => import('@/views/bank/BankStatement.vue'),
        meta: { title: '银行流水管理' }
      },
      {
        path: '/bank-reconciliation',
        name: 'BankReconciliation',
        component: () => import('@/views/bank/BankReconciliation.vue'),
        meta: { title: '银行对账' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router

