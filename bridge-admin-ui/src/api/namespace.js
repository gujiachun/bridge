import request from '@/utils/request'

export function getNameSpaceList() {
  return request({
    url: '/ns',
    method: 'get'
  })
}

export function addNs(data) {
  return request({
    url: '/ns',
    method: 'post',
    data
  })
}

export function updateNs(data) {
  return request({
    url: '/ns',
    method: 'put',
    data
  })
}

export function deleteNs(id) {
  return request({
    url: '/ns/' + id,
    method: 'delete'
  })
}
