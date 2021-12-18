import request from '@/utils/request'

export function getTaskEsRuleList(params) {
  return request({
    url: '/task/rule/es',
    method: 'get',
    params: params
  })
}

export function addTaskEsRule(data) {
  return request({
    url: '/task/rule/es',
    method: 'post',
    data
  })
}

export function updateTaskEsRule(data) {
  return request({
    url: '/task/rule/es',
    method: 'put',
    data
  })
}

export function updateTaskEsRuleStatus(id,status) {
  return request({
    url: '/task/rule/es/' + id,
    method: 'post',
    params: {
      status: status
    }
  })
}

export function deleteTaskEsRule(id) {
  return request({
    url: '/task/rule/es/' + id,
    method: 'delete'
  })
}

export function updateTaskEsRuleInsert(data) {
  return request({
    url: '/task/rule/es/insert',
    method: 'put',
    data
  })
}

export function updateTaskEsRuleUpdate(data) {
  return request({
    url: '/task/rule/es/update',
    method: 'put',
    data
  })
}

export function updateTaskEsRuleDelete(data) {
  return request({
    url: '/task/rule/es/delete',
    method: 'put',
    data
  })
}
