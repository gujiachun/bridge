import request from '@/utils/request'

export function getTopicList(params) {
  return request({
    url: '/topic',
    method: 'get',
    params: params
  })
}

export function getTopic(taskId) {
  return request({
    url: '/topic/task/' + taskId,
    method: 'get'
  })
}

export function getTopicListByEnv(env) {
  return request({
    url: '/topic/env',
    method: 'get',
    params: {
      env: env
    }
  })
}

export function addTopic(data) {
  return request({
    url: '/topic',
    method: 'post',
    data
  })
}

export function updateTopic(data) {
  return request({
    url: '/topic',
    method: 'put',
    data
  })
}

export function deleteTopic(id) {
  return request({
    url: '/topic/' + id,
    method: 'delete'
  })
}
