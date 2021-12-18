import request from '@/utils/request'

export function getEsTargetList(params) {
  return request({
    url: '/target/es',
    method: 'get',
    params: params
  })
}

export function addEsTarget(data) {
  return request({
    url: '/target/es',
    method: 'post',
    data
  })
}

export function updateEsTarget(data) {
  return request({
    url: '/target/es',
    method: 'put',
    data
  })
}

export function deleteEsTarget(id) {
  return request({
    url: '/target/es/' + id,
    method: 'delete'
  })
}
