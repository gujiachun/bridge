import request from '@/utils/request'

export function getMqList() {
  return request({
    url: '/mq',
    method: 'get'
  })
}

export function getMqListByEnv(env) {
  return request({
    url: '/mq/query/env',
    method: 'get',
    params: {
      env: env
    }
  })
}

export function addMq(data) {
  return request({
    url: '/mq',
    method: 'post',
    data
  })
}

export function updateMq(data) {
  return request({
    url: '/mq',
    method: 'put',
    data
  })
}

export function deleteMq(id) {
  return request({
    url: '/mq/' + id,
    method: 'delete'
  })
}
