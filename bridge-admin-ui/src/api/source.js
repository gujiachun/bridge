import request from '@/utils/request'

export function getSourceList(params) {
  return request({
    url: '/source',
    method: 'get',
    params: params
  })
}

export function getSource(sourceId) {
  return request({
    url: '/source/' + sourceId,
    method: 'get'
  })
}

export function getSourceListByEnv(env) {
  return request({
    url: '/source/query/env',
    method: 'get',
    params: {
      env: env
    }
  })
}

export function addSource(data) {
  return request({
    url: '/source',
    method: 'post',
    data
  })
}

export function updateSource(data) {
  return request({
    url: '/source',
    method: 'put',
    data
  })
}

export function deleteSource(id) {
  return request({
    url: '/source/' + id,
    method: 'delete'
  })
}
