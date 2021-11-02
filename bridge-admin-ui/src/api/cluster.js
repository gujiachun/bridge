import request from '@/utils/request'

export function getClusterList() {
  return request({
    url: '/cluster',
    method: 'get'
  })
}

export function getClusterListByEnv(env) {
  return request({
    url: '/cluster/env',
    method: 'get',
    params: {
      env: env
    }
  })
}

export function addCluster(data) {
  return request({
    url: '/cluster',
    method: 'post',
    data
  })
}

export function updateCluster(data) {
  return request({
    url: '/cluster',
    method: 'put',
    data
  })
}

export function deleteCluster(id) {
  return request({
    url: '/cluster/' + id,
    method: 'delete'
  })
}
