import request from '@/utils/request'

export function getRedisTargetList(params) {
  return request({
    url: '/target/redis',
    method: 'get',
    params: params
  })
}

export function addRedisTarget(data) {
  return request({
    url: '/target/redis',
    method: 'post',
    data
  })
}

export function updateRedisTarget(data) {
  return request({
    url: '/target/redis',
    method: 'put',
    data
  })
}

export function deleteRedisTarget(id) {
  return request({
    url: '/target/redis/' + id,
    method: 'delete'
  })
}
