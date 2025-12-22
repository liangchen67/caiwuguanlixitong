<template>
  <el-container style="height: 100vh">
    <el-aside width="200px" style="background-color: #545c64">
      <div style="height: 60px; line-height: 60px; text-align: center; color: white; font-size: 20px; font-weight: bold; border-bottom: 1px solid #434a50">
        财务管理系统
      </div>
      <el-menu
        :default-active="activeMenu"
        class="el-menu-vertical"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><House /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <el-sub-menu index="1">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>总账会计</span>
          </template>
          <el-menu-item index="/company">企业初始化</el-menu-item>
          <el-menu-item index="/account-subject">会计科目管理</el-menu-item>
          <el-menu-item index="/journal-entry">会计分录</el-menu-item>
          <el-menu-item index="/reports">财务报表</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="2">
          <template #title>
            <el-icon><ShoppingCart /></el-icon>
            <span>采购管理</span>
          </template>
          <el-menu-item index="/supplier">供应商管理</el-menu-item>
          <el-menu-item index="/purchase-order">采购订单</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="3">
          <template #title>
            <el-icon><Sell /></el-icon>
            <span>销售管理</span>
          </template>
          <el-menu-item index="/customer">客户管理</el-menu-item>
          <el-menu-item index="/sales-invoice">销售单管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="4">
          <template #title>
            <el-icon><User /></el-icon>
            <span>员工费用</span>
          </template>
          <el-menu-item index="/employee">员工管理</el-menu-item>
          <el-menu-item index="/employee-expense">费用管理</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="5">
          <template #title>
            <el-icon><CreditCard /></el-icon>
            <span>银行对账</span>
          </template>
          <el-menu-item index="/bank-statement">银行流水管理</el-menu-item>
          <el-menu-item index="/bank-reconciliation">银行对账</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header style="border-bottom: 1px solid #dcdfe6; display: flex; align-items: center; justify-content: space-between">
        <div style="font-size: 20px; font-weight: bold">{{ currentTitle }}</div>
        <div>
          <el-icon style="font-size: 24px; cursor: pointer"><Setting /></el-icon>
        </div>
      </el-header>

      <el-main style="background-color: #f0f2f5">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '财务管理系统')
</script>

<style scoped>
.el-menu-vertical {
  border-right: none;
}
</style>

