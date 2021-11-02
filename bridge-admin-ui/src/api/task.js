import request from '@/utils/request'

export function getTaskList(params) {
  return request({
    url: '/task',
    method: 'get',
    params: params
  })
}

export function getTask(id) {
  return request({
    url: '/task/' + id,
    method: 'get'
  })
}

export function addTask(data) {
  return request({
    url: '/task',
    method: 'post',
    data
  })
}

export function updateTask(data) {
  return request({
    url: '/task',
    method: 'put',
    data
  })
}

export function updateTaskStatus(id,status) {
  return request({
    url: '/task/' + id,
    method: 'post',
    params: {
      status: status
    }
  })
}

export function deleteTask(id) {
  return request({
    url: '/task/' + id,
    method: 'delete'
  })
}

export function refreshTask(id) {
  return request({
    url: '/task/refresh/' + id,
    method: 'get'
  })
}
