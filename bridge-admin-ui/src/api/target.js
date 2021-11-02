import request from '@/utils/request'

export function getTargetList(type,env) {
  return request({
    url: '/target/' + type + '/' + env,
    method: 'get'
  })
}
