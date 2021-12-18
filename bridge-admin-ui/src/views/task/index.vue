<template>
  <div class="app-container">

    <div class="filter-container">
      <el-select v-model="listQuery.env" placeholder="所属命名空间" class="filter-item">
        <el-option key="" label="所属命名空间" value="" />
        <el-option v-for="item in nameSpaces" :key="item.id" :label="item.name" :value="item.env" />
      </el-select>
      <el-select v-model="listQuery.targetType" placeholder="目标源类型" class="filter-item">
        <el-option key="" label="所有目标源类型" value="" />
        <el-option key="mysql" label="mysql" value="mysql" />
        <el-option key="es" label="es" value="es" />
        <el-option key="redis" label="redis" value="redis" />
      </el-select>
      <el-select v-model="listQuery.status" placeholder="任务状态" class="filter-item">
        <el-option key="-1" label="所有任务状态" value="-1" />
        <el-option key="0" label="未发布" value="0" />
        <el-option key="1" label="已发布" value="1" />
        <el-option key="2" label="已停用" value="2" />
      </el-select>
      <el-input v-model="listQuery.name" placeholder="任务名" style="width: 200px;" class="filter-item" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建任务</el-button>
      <el-dropdown class="filter-item" trigger="click">
        <el-button type="primary">
          任务刷新<i class="el-icon-arrow-down el-icon--right" />
        </el-button>
        <el-dropdown-menu slot="dropdown">
          <el-dropdown-item v-for="item in clusterList" @click.native="taskRefresh(item.id)">{{item.name + " - " + item.env}}</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" fixed show-overflow-tooltip label="任务ID" width="180">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="任务名称" fixed align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column width="200" align="center">
        <template slot="header" slot-scope="scope">
          <span>订阅Topic</span>&nbsp;
          <el-popover
            placement="top-start"
            width="500"
            trigger="hover"
            content="订阅canal server产生的哪一个topic，此topic一样要和canal server设置保持一致">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.topic }}
        </template>
      </el-table-column>
      <el-table-column width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>Topic所在MQ</span>&nbsp;
          <el-popover
            placement="top-start"
            width="250"
            trigger="hover"
            content="topic所在的消息中间件MQ">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.mqName }}
        </template>
      </el-table-column>
      <el-table-column width="120" align="center">
        <template slot="header" slot-scope="scope">
          <span>目标源类型</span>&nbsp;
          <el-popover
            placement="top-start"
            width="400"
            trigger="hover"
            content="同步到哪个类型的数据源，支持mysql、redis、es">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.targetType }}
        </template>
      </el-table-column>
      <el-table-column width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>执行任务的集群</span>&nbsp;
          <el-popover
            placement="top-start"
            width="200"
            trigger="hover"
            content="任务会在哪个集群中执行">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.clusterName }}
        </template>
      </el-table-column>
      <el-table-column width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>执行期望实例数</span>&nbsp;
          <el-popover
            placement="top-start"
            width="400"
            trigger="hover"
            content="任务会在集群中执行,为了高可用、负载均衡可设置多个实例执行此任务">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.instanceCount }}
        </template>
      </el-table-column>
      <el-table-column width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>在线实例数</span>&nbsp;
          <el-popover
            placement="top-start"
            width="300"
            trigger="hover"
            content="集群中已经有多少个实例 在线处理此任务">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <span style="color:green;font-weight:bold">{{ scope.row.activeCount }}</span>
          &nbsp;
          <el-link type="success" v-if="scope.row.activeCount > 0" @click.native="handleShowActive(scope.row)"><i class="el-icon-view el-icon--right"></i></el-link>
        </template>
      </el-table-column>
      <el-table-column label="环境代码" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.env }}
        </template>
      </el-table-column>
      <el-table-column width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>执行方式</span>&nbsp;
          <el-popover
            placement="top-start"
            width="500"
            trigger="hover"
            content="执行任务时采用异步或同步；(异步方式能够提升处理性能，但是出现异常时，消息不会重发)；(同步方式 出现异常时，消息会重发)">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <span v-if="scope.row.async==0" style="color:blue;">同步</span>
          <span v-if="scope.row.async==1" style="color:green;">异步</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status | statusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" min-width="90" width="100">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleRule(scope.row)">规则</el-dropdown-item>
              <el-dropdown-item @click.native="handleUpdate(scope.row)">修改</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(scope.row)">删除</el-dropdown-item>
              <el-dropdown-item @click.native="handleStatus(scope.row.id,1)">发布</el-dropdown-item>
              <el-dropdown-item @click.native="handleStatus(scope.row.id,2)">停用</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="count>0" :total="count" :page.sync="listQuery.currentPage" :limit.sync="listQuery.pageSize" @pagination="fetchData()" />
    <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="nodeModel" label-position="right" label-width="150px" style="width: 400px; margin-left:30px;">
        <el-form-item label="任务名" prop="name">
          <el-input v-model="nodeModel.name" style="width:200px"/>
        </el-form-item>
        <el-form-item label="目标源类型" prop="targetType">
          <el-select v-model="nodeModel.targetType" placeholder="选择目标源类型">
            <el-option label="mysql" value="mysql" />
            <el-option label="es" value="es" />
            <el-option label="redis" value="redis" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属命名空间" prop="env">
          <el-select v-model="nodeModel.env" @change="envSelectChange" placeholder="选择所属命名空间">
            <el-option v-for="item in nameSpaces" :key="item.id" :label="item.name" :value="item.env" />
          </el-select>
        </el-form-item>
        <el-form-item label="订阅Topic" prop="basicTopicId">
          <template slot="label">
            <el-popover
              placement="top-start"
              width="200"
              trigger="hover"
              content="订阅canal server产生的哪一个topic，此topic一样要和canal server设置保持一致">
              <span slot="reference">
                <span>订阅Topic</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.basicTopicId" placeholder="选择订阅Topic">
            <el-option v-for="item in topicList" :key="item.id" :label="item.topic" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行集群" prop="publishCluster">
          <template slot="label">
            <el-popover
              placement="top-start"
              width="200"
              trigger="hover"
              content="任务会在哪个集群中执行">
              <span slot="reference">
                <span>执行集群</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.publishCluster" placeholder="选择执行集群">
            <el-option v-for="item in clusterEnvList" :key="item.id" :label="item.name" :value="item.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行期望实例数" prop="instanceCount">
          <template slot="label">
            <el-popover
              placement="top-start"
              width="200"
              trigger="hover"
              content="任务会在集群中执行,为了高可用、负载均衡可设置多个实例执行此任务">
              <span slot="reference">
                <span>执行期望实例数</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input-number :min="1" v-model="nodeModel.instanceCount"/>
        </el-form-item>
        <el-form-item label="执行方式" prop="async">
          <template slot="label">
            <el-popover placement="top-start" width="200" trigger="hover"
              content="执行任务时采用异步或同步；(异步方式能够提升处理性能，但是出现异常时，消息不会重发)；(同步方式 出现异常时，消息会重发)">
              <span slot="reference">
                <span>执行方式</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.async" placeholder="选择执行方式">
            <el-option label="同步" :value="0" />
            <el-option label="异步" :value="1" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dataOperation()">确定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { getTaskList, getTaskActiveList, addTask, updateTask, deleteTask, updateTaskStatus, refreshTask } from '@/api/task'
import { getNameSpaceList } from '@/api/namespace'
import { getTopicListByEnv } from '@/api/topic'
import { getClusterListByEnv, getClusterList } from '@/api/cluster'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        '1': 'success',
        '0': 'gray',
        '2': 'danger'
      }
      return statusMap[status]
    },
    statusLabel(status) {
      const statusMap = {
        '1': '已发布',
        '0': '未发布',
        '2': '已停用'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      dialogFormVisible: false,
      dialogStatus: 'create',
      nameSpaces: [],
      clusterEnvList: [],
      clusterList: [],
      topicList: [],
      nodeModel: {
        id: undefined,
        name: null,
        env: null,
        targetType: null,
        basicTopicId: null,
        publishCluster: null,
        async: null,
        status: 0,
        instanceCount: null
      },
      count: 0,
      listQuery: {
        env: '',
        name: '',
        targetType: '',
        status: '',
        currentPage: 1,
        pageSize: 10
      },
      rules: {
        name: [{ required: true, message: '任务名不能为空', trigger: 'change' }],
        targetType: [{ required: true, message: '目标源不能为空', trigger: 'change' }],
        basicTopicId: [{ required: true, message: '订阅topic不能为空', trigger: 'change' }],
        publishCluster: [{ required: true, message: '执行集群不能为空', trigger: 'change' }],
        instanceCount: [
          { required: true, message: '执行期望实例数不能为空', trigger: 'change' }
        ],
        async: [{ required: true, message: '执行方式不能为空', trigger: 'change' }],
        env: [{ required: true, message: '命名空间不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建任务',
        update: '修改任务'
      }
    }
  },
  created() {
    getNameSpaceList().then((res) => {
      this.nameSpaces = res.data
    })
    getClusterList().then((res) => {
      this.clusterList = res.data
    })
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getTaskList(this.listQuery).then(response => {
        this.list = response.data.records
        this.count = response.data.total
        this.listLoading = false
      })
    },
    envSelectChange() {
      this.nodeModel.publishCluster = null
      this.nodeModel.basicTopicId = null
      this.getClusterTopicListByEnv()
    },
    getClusterTopicListByEnv(){
      getClusterListByEnv(this.nodeModel.env).then((res) => {
        this.clusterEnvList = res.data
      })
      getTopicListByEnv(this.nodeModel.env).then((res) => {
        this.topicList = res.data
      })
    },
    taskRefresh(id) {
      refreshTask(id).then((res)=>{
        this.operationRefreshTaskRes(res)
      })
    },
    queryData() {
      this.listQuery.currentPage = 1
      this.fetchData()
    },
    resetModel() {
      this.nodeModel = {
        id: undefined,
        name: null,
        env: null,
        targetType: null,
        basicTopicId: null,
        publishCluster: null,
        async: null,
        status: 0,
        instanceCount: null
      }
    },
    handleRule(row) {
      if(row.targetType == 'mysql'){
        console.info('taskId=' + row.id)
        this.$router.push('/task/rule/mysql?taskId=' + row.id)
      }else if(row.targetType == 'redis'){
        console.info('taskId=' + row.id)
        this.$router.push('/task/rule/redis?taskId=' + row.id)
      }else if(row.targetType == 'es'){
        console.info('taskId=' + row.id)
        this.$router.push('/task/rule/es?taskId=' + row.id)
      }
    },
    handleShowActive(row){
      getTaskActiveList(row.env,row.clusterCode,row.id).then(res => {
        var str='';
        res.data.forEach(element => {
          var split = element.split(",")
          str += '实例: ' + split[0] + '，开始时间: ' + split[1] + '<br/>';
        });
        this.$alert(str, '实例信息', {
          confirmButtonText: '确定',
          dangerouslyUseHTMLString: true
        });
      })
    },
    handleCreate() {
      this.resetModel()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleStatus(id,status) {
      updateTaskStatus(id,status).then(res => {
        this.operationStatusRes(res)
      })
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addTask(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateTask(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
        }
      })
    },
    operationRefreshTaskRes(res) {
      if (res.success === true) {
        this.$message({
          message: '任务刷新 操作成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: '任务刷新 操作失败',
          type: 'error'
        })
      }
    },
    operationStatusRes(res) {
      if (res.success === true) {
        this.fetchData()
        this.dialogFormVisible = false
        this.$message({
          message: '任务状态 操作成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: '任务状态 操作失败',
          type: 'error'
        })
      }
    },
    operationRes(res) {
      if (res.success === true) {
        this.fetchData()
        this.dialogFormVisible = false
        this.$message({
          message: this.textMap[this.dialogStatus] + '成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: this.textMap[this.dialogStatus] + '失败',
          type: 'error'
        })
      }
    },
    handleUpdate(row) {
      this.resetModel()
      this.nodeModel = Object.assign({}, row)
      this.getClusterTopicListByEnv()
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除Task会导致很多关联数据失效', '确定删除task', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTask(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除task成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除task失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
