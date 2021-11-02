import request from '@/utils/request'

export function getTaskRedisRuleList(params) {
  return request({
    url: '/task/rule/redis',
    method: 'get',
    params: params
  })
}

export function addTaskRedisRule(data) {
  return request({
    url: '/task/rule/redis',
    method: 'post',
    data
  })
}

export function updateTaskRedisRule(data) {
  return request({
    url: '/task/rule/redis',
    method: 'put',
    data
  })
}

export function updateTaskRedisRuleStatus(id,status) {
  return request({
    url: '/task/rule/redis/' + id,
    method: 'post',
    params: {
      status: status
    }
  })
}

export function deleteTaskRedisRule(id) {
  return request({
    url: '/task/rule/redis/' + id,
    method: 'delete'
  })
}
