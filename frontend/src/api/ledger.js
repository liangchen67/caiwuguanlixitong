import request from '@/utils/request'

// 会计科目
export function getAccountSubjectList() {
  return request({
    url: '/account-subject/list',
    method: 'get'
  })
}

export function getEnabledAccountSubjects() {
  return request({
    url: '/account-subject/enabled',
    method: 'get'
  })
}

export function getAccountSubject(id) {
  return request({
    url: `/account-subject/${id}`,
    method: 'get'
  })
}

export function saveAccountSubject(data) {
  return request({
    url: '/account-subject/save',
    method: 'post',
    data
  })
}

export function deleteAccountSubject(id) {
  return request({
    url: `/account-subject/${id}`,
    method: 'delete'
  })
}

// 会计分录
export function getJournalEntryList() {
  return request({
    url: '/journal-entry/list',
    method: 'get'
  })
}

export function getJournalEntry(id) {
  return request({
    url: `/journal-entry/${id}`,
    method: 'get'
  })
}

export function saveJournalEntry(data) {
  return request({
    url: '/journal-entry/save',
    method: 'post',
    data
  })
}

export function postJournalEntry(id) {
  return request({
    url: `/journal-entry/post/${id}`,
    method: 'post'
  })
}

export function deleteJournalEntry(id) {
  return request({
    url: `/journal-entry/${id}`,
    method: 'delete'
  })
}

