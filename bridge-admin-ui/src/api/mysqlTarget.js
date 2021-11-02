import request from '@/utils/request'

export function getMysqlTargetList(params) {
  return request({
    url: '/target/mysql',
    method: 'get',
    params: params
  })
}

export function addMysqlTarget(data) {
  return request({
    url: '/target/mysql',
    method: 'post',
    data
  })
}

export function updateMysqlTarget(data) {
  return request({
    url: '/target/mysql',
    method: 'put',
    data
  })
}

export function deleteMysqlTarget(id) {
  return request({
    url: '/target/mysql/' + id,
    method: 'delete'
  })
}
