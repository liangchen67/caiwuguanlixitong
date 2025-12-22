import request from '@/utils/request'

// 企业管理
export function getCompanyList() {
  return request({
    url: '/company/list',
    method: 'get'
  })
}

export function getCompany(id) {
  return request({
    url: `/company/${id}`,
    method: 'get'
  })
}

export function saveCompany(data) {
  return request({
    url: '/company/save',
    method: 'post',
    data
  })
}

export function deleteCompany(id) {
  return request({
    url: `/company/${id}`,
    method: 'delete'
  })
}







