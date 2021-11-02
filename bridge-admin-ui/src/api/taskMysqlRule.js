import request from '@/utils/request'

export function getTaskMysqlRuleList(params) {
  return request({
    url: '/task/rule/mysql',
    method: 'get',
    params: params
  })
}

export function addTaskMysqlRule(data) {
  return request({
    url: '/task/rule/mysql',
    method: 'post',
    data
  })
}

export function updateTaskMysqlRule(data) {
  return request({
    url: '/task/rule/mysql',
    method: 'put',
    data
  })
}

export function updateTaskMysqlRuleStatus(id,status) {
  return request({
    url: '/task/rule/mysql/' + id,
    method: 'post',
    params: {
      status: status
    }
  })
}

export function deleteTaskMysqlRule(id) {
  return request({
    url: '/task/rule/mysql/' + id,
    method: 'delete'
  })
}

export function updateTaskMysqlRuleInsert(data) {
  return request({
    url: '/task/rule/mysql/insert',
    method: 'put',
    data
  })
}

export function updateTaskMysqlRuleUpdate(data) {
  return request({
    url: '/task/rule/mysql/update',
    method: 'put',
    data
  })
}

export function updateTaskMysqlRuleDelete(data) {
  return request({
    url: '/task/rule/mysql/delete',
    method: 'put',
    data
  })
}
