import request from '@/utils/request'

export function getZkList() {
  return request({
    url: '/zk',
    method: 'get'
  })
}

export function addZk(data) {
  return request({
    url: '/zk',
    method: 'post',
    data
  })
}

export function updateZk(data) {
  return request({
    url: '/zk',
    method: 'put',
    data
  })
}

export function deleteZk(id) {
  return request({
    url: '/zk/' + id,
    method: 'delete'
  })
}
