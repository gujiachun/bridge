<template>
  <div class="app-container">

    <div class="filter-container">
        <el-select v-model="listQuery.env" placeholder="所属命名空间" class="filter-item">
        <el-option key="" label="所属命名空间" value="" />
        <el-option v-for="item in nameSpaces" :key="item.id" :label="item.name" :value="item.env" />
      </el-select>
      <el-input v-model="listQuery.topic" placeholder="topic" style="width: 200px;" class="filter-item" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建订阅的topic</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="topic" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.topic }}</span>
        </template>
      </el-table-column>
      <el-table-column label="归属MQ" width="100" align="center">
        <template slot-scope="scope">
          <template v-for="item in mqList">
              <template v-if="item.id===scope.row.mqId">
                  {{ item.name }}
              </template>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="触发的库" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>触发的库</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="哪个库中的表变化，会把消息发送到此topic，只能配置一个数据库">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.syncDb }}
        </template>
      </el-table-column>
      <el-table-column label="触发的表" align="center">
        <template slot="header" slot-scope="scope">
          <span>触发的表</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="哪些表变化，会把消息发送到此topic，可以多个表以逗号隔开，如【table1,table2】">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.syncTable }}
        </template>
      </el-table-column>
      <el-table-column label="备注" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.remark }}
        </template>
      </el-table-column>
      <el-table-column label="环境代码" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.env }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" min-width="150" width="200">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleUpdate(scope.row)">修改</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(scope.row)">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="count>0" :total="count" :page.sync="listQuery.currentPage" :limit.sync="listQuery.pageSize" @pagination="fetchData()" />
    <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="nodeModel" label-position="right" label-width="120px" style="width: 400px; margin-left:30px;">
        <el-form-item label="topic" prop="topic">
          <el-input v-model="nodeModel.topic" />
        </el-form-item>
        <el-form-item label="所属命名空间" prop="env">
          <el-select v-if="dialogStatus === 'create'" v-model="nodeModel.env" v-on:change="envSelect($event)" placeholder="选择所属命名空间">
            <el-option v-for="item in nameSpaces" :key="item.id" :label="item.name" :value="item.env" />
          </el-select>
          <el-select v-else v-model="nodeModel.env" placeholder="选择所属命名空间" disabled="disabled">
            <el-option v-for="item in nameSpaces" :key="item.id" :label="item.name" :value="item.env" />
          </el-select>
        </el-form-item>
        <el-form-item label="归属MQ" prop="mqId">
          <el-select v-model="nodeModel.mqId" placeholder="选择mq">
            <el-option v-for="item in mqList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="对应源名" prop="sourceId">
          <el-select v-model="nodeModel.sourceId" placeholder="选择对应源名" v-on:change="sourceSelect($event)">
            <el-option v-for="item in sourceList" :key="item.id" :label="item.sourceName" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="触发的库" prop="syncDb">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="哪个库中的表变化，会把消息发送到此topic，只能配置一个数据库">
              <span slot="reference">
                <span>触发的库</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.syncDb" disabled />
        </el-form-item>
        <el-form-item label="触发的表" prop="syncTable">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="哪些表变化，会把消息发送到此topic，可以多个表以逗号隔开，如【table1,table2】">
              <span slot="reference">
                <span>触发的表</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <!-- <el-input v-model="nodeModel.syncTable" type="textarea"/> -->
          <el-select v-model="nodeModel.syncTableList" placeholder="选择对应表" multiple>
            <el-option v-for="item in sourceTableList" :key="item.tableName" :label="item.tableName" :value="item.tableName" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="nodeModel.remark" type="textarea" />
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
import { getTopicList, addTopic, updateTopic, deleteTopic } from '@/api/topic'
import { getNameSpaceList } from '@/api/namespace'
import { getSourceListByEnv,getSource } from '@/api/source'
import { getSourceTableListBySourceId } from '@/api/sourceTable'
import { getMqList,getMqListByEnv } from '@/api/mq'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
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
      mqList: [],
      sourceList: [],
      sourceTableList: [],
      nodeModel: {
        id: undefined,
        topic: null,
        env: null,
        syncDb: null,
        syncTable: null,
        syncTableList: null,
        sourceId: null,
        mqId: null,
        remark: null
      },
      count: 0,
      listQuery: {
        env: '',
        topic: '',
        currentPage: 1,
        pageSize: 20
      },
      rules: {
        topic: [{ required: true, message: 'topic不能为空', trigger: 'change' }],
        syncDb: [{ required: true, message: '触发数据库不能为空', trigger: 'change' }],
        sourceId: [{ required: true, message: '请选择源名，源名不能为空', trigger: 'change' }],
        mqId: [{ required: true, message: 'mq不能为空', trigger: 'change' }],
        env: [{ required: true, message: '命名空间不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建订阅的topic',
        update: '修改订阅topic'
      }
    }
  },
  created() {
    getNameSpaceList().then((res) => {
      this.nameSpaces = res.data
    })
    // getMqList().then((res) => {
    //   this.mqList = res.data
    // })
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getTopicList(this.listQuery).then(response => {
        this.list = response.data.records
        this.count = response.data.total
        this.listLoading = false
      })
    },
    envSelect(event){
      this.nodeModel.sourceId = null
      this.nodeModel.syncDb = null
      this.nodeModel.mqId = null
      this.sourceList = null
      this.mqList = null
      this.nodeModel.syncTable = null
      this.nodeModel.syncTableList = null
      this.sourceTableList = null
      var env = event;
      getMqListByEnv(env).then((res) => {
        this.mqList = res.data
      })
      getSourceListByEnv(env).then((res) => {
        this.sourceList = res.data
      })
    },
    sourceSelect(event){
      var sourceId = event;
      this.nodeModel.sourceId = sourceId
      getSource(sourceId).then((res) => {
        this.nodeModel.syncDb = res.data.dbName
      })
      getSourceTableListBySourceId(sourceId).then((res) => {
        this.sourceTableList = res.data
      })
    },
    queryData() {
      this.listQuery.currentPage = 1
      this.fetchData()
    },
    resetModel() {
      this.nodeModel = {
        id: undefined,
        topic: null,
        env: null,
        syncDb: null,
        sourceId: null,
        syncTable: null,
        syncTableList: null,
        mqId: null,
        remark: null
      };
      this.sourceTableList = null;
    },
    handleCreate() {
      this.resetModel()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {

          if(this.nodeModel.syncTableList != null){
            this.nodeModel.syncTable = this.nodeModel.syncTableList.join(',')
          }

          if (this.dialogStatus === 'create') {
            addTopic(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateTopic(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
        }
      })
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
      this.nodeModel = Object.assign(this.nodeModel, row)

      var env = this.nodeModel.env
      getMqListByEnv(env).then((res) => {
        this.mqList = res.data
      })
      getSourceListByEnv(env).then((res) => {
        this.sourceList = res.data
      })

      getSourceTableListBySourceId(this.nodeModel.sourceId).then((res) => {
        this.sourceTableList = res.data
      })

      if(this.nodeModel.syncTable != null && this.nodeModel.syncTable != ''){
        this.nodeModel.syncTableList = this.nodeModel.syncTable.split(',')
      }

      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除Topic会导致很多关联数据失效', '确定删除topic', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTopic(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除topic成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除topic失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
