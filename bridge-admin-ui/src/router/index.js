import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '首页', icon: 'dashboard' }
    }],
    hidden: true
  },

  {
    path: '/setting',
    component: Layout,
    redirect: '/setting/ns',
    name: 'setting',
    meta: { title: '基础设置', icon: 'el-icon-setting' },
    children: [
      {
        path: 'ns',
        name: 'NameSpace',
        component: () => import('@/views/namespace/index'),
        meta: { title: '命名空间', icon: 'el-icon-crop' }
      },
      {
        path: 'cluster',
        name: 'cluster',
        component: () => import('@/views/cluster/index'),
        meta: { title: '集群管理', icon: 'el-icon-house' }
      },
      {
        path: 'zk',
        name: 'zk',
        component: () => import('@/views/zk/index'),
        meta: { title: 'ZK管理', icon: 'el-icon-collection' }
      },
      {
        path: 'mq',
        name: 'mq',
        component: () => import('@/views/mq/index'),
        meta: { title: 'MQ管理', icon: 'el-icon-money' }
      },
      {
        path: 'source',
        name: 'source',
        component: () => import('@/views/source/index'),
        meta: { title: 'Source源', icon: 'el-icon-collection-tag' }
      },
      {
        path: 'source/table',
        name: 'source源表',
        component: () => import('@/views/source/sourceTable'),
        meta: { title: 'source源表' },
        hidden: true
      },
      {
        path: 'topic',
        name: 'topic',
        component: () => import('@/views/topic/index'),
        meta: { title: 'Topic管理', icon: 'el-icon-collection-tag' }
      }
    ]
  },

  {
    path: '/target',
    component: Layout,
    redirect: '/target/mysql',
    name: 'target',
    meta: { title: '目标源设置', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'mysql',
        name: 'mysqlTarget',
        component: () => import('@/views/target/mysql'),
        meta: { title: 'Mysql目标源', icon: 'el-icon-coin' }
      },
      {
        path: 'redis',
        name: 'redislTarget',
        component: () => import('@/views/target/redis'),
        meta: { title: 'Redis目标源', icon: 'el-icon-money' }
      },
      {
        path: 'es',
        name: 'eslTarget',
        component: () => import('@/views/target/es'),
        meta: { title: 'Es目标源', icon: 'el-icon-connection' }
      }
    ]
  },

  {
    path: '/task',
    component: Layout,
    redirect: '/task/setting',
    name: 'target',
    meta: { title: '任务管理', icon: 'el-icon-s-help' },
    children: [
      {
        path: 'setting',
        name: 'setting',
        component: () => import('@/views/task/index'),
        meta: { title: '任务管理', icon: 'el-icon-notebook-2' }
      },
      {
        path: 'rule/mysql',
        name: 'mysql任务规则',
        component: () => import('@/views/task/mysqlRule'),
        meta: { title: 'mysql任务规则' },
        hidden: true
      },
      {
        path: 'rule/redis',
        name: 'redis任务规则',
        component: () => import('@/views/task/redisRule'),
        meta: { title: 'redis任务规则' },
        hidden: true
      },
      {
        path: 'rule/es',
        name: 'es任务规则',
        component: () => import('@/views/task/esRule'),
        meta: { title: 'es任务规则' },
        hidden: true
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
