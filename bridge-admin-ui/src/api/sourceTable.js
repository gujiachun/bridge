import request from '@/utils/request'

export function getSourceTableList(params) {
  return request({
    url: '/source-table',
    method: 'get',
    params: params
  })
}

export function getSourceTableListByEnv(params) {
  return request({
    url: '/source-table/search',
    method: 'get',
    params: params
  })
}

export function getSourceTableListBySourceId(sourceId) {
  return request({
    url: '/source-table/sourceId',
    method: 'get',
    params: {
      sourceId
    }
  })
}

export function addSourceTable(data) {
  return request({
    url: '/source-table',
    method: 'post',
    data
  })
}

export function updateSourceTable(data) {
  return request({
    url: '/source-table',
    method: 'put',
    data
  })
}

export function deleteSourceTable(id) {
  return request({
    url: '/source-table/' + id,
    method: 'delete'
  })
}
