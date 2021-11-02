<template>
  <div class="app-container">

    <div class="filter-container">
      <el-select v-model="listQuery.status" placeholder="任务状态" class="filter-item">
        <el-option key="-1" label="所有任务状态" value="-1" />
        <el-option key="0" label="未发布" value="0" />
        <el-option key="1" label="已发布" value="1" />
        <el-option key="2" label="已停用" value="2" />
      </el-select>
      <el-input v-model="listQuery.sourceTable" placeholder="源表" style="width: 200px;" class="filter-item" />
      <el-select v-model="listQuery.targetId" placeholder="请选择目标redis" class="filter-item">
        <el-option key="" label="全部目标redis源" value="" />
        <el-option v-for="item in targetList" :key="item.id" :label="item.name" :value="item.id" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建redis同步规则</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" show-overflow-tooltip label="ID" width="50">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="源库" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>源库</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="哪个数据库产生的binlog事件变化">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <span>{{ scope.row.sourceDb }}</span>
        </template>
      </el-table-column>
      <el-table-column label="源表" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>源表</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="哪个数据表产生的binlog事件变化">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.sourceTable }}
        </template>
      </el-table-column>
      <el-table-column label="目标redis" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>目标redis</span>&nbsp;
          <el-popover placement="top-start" width="200" trigger="hover"
            content="同步到的目标redis">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <template v-for="item in targetList">
              <template v-if="item.id === scope.row.targetId">
                <span>{{item.name}}</span>
              </template>
          </template>
        </template>
      </el-table-column>
      <el-table-column label="执行命令" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>执行命令</span>&nbsp;
          <el-popover placement="top-start" width="200" trigger="hover"
            content="执行redis哪个命令">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.command }}
        </template>
      </el-table-column>
      <el-table-column label="key格式" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>key格式</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="key的模板，支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.keyFormat }}
        </template>
      </el-table-column>
      <el-table-column label="field格式" width="100" show-overflow-tooltip align="center">
        <template slot="header" slot-scope="scope">
          <span>field格式</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="field的模板【field是用来支持 hset、delhKeys指令的】；支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】，如果多个field用(逗号,)隔开">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.fieldFormat }}
        </template>
      </el-table-column>
      <el-table-column label="value格式" width="150" show-overflow-tooltip align="center">
        <template slot="header" slot-scope="scope">
          <span>value格式</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="value的模板,支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】，如果hmset命令需要，map对象；可对此value设置json格式字符串，系统会自动转为map">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.valueFormat }}
        </template>
      </el-table-column>
      <el-table-column label="过期时间(秒)" width="120" show-overflow-tooltip align="center">
        <template slot="header" slot-scope="scope">
          <span>过期时间(秒)</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="设置key的过期时间，即过多少秒后 过期；可以为空，代表不过期">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.expireTime }}
        </template>
      </el-table-column>
      <el-table-column label="固定过期时间(秒)" show-overflow-tooltip align="center">
        <template slot="header" slot-scope="scope">
          <span>固定过期时间(秒)</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="设置key的过期时间，即在固定的时间点过期，即每天的哪个时间点过期，一旦有更新 即代表第二天固定时间点；可以为空，代表不过期">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.fixedTime }}
        </template>
      </el-table-column>
      <el-table-column label="新增事件" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>新增事件</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="是否开启 源表发生【新增数据事件】的处理同步">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <span v-if="scope.row.insertEnable==0" style="color:red;">关闭</span>
          <span v-if="scope.row.insertEnable==1" style="color:green;">启动</span>
          &nbsp;
          <el-link type="primary" @click.native="handleInsertUpdate(scope.row)"><i class="el-icon-edit"></i></el-link>
        </template>
      </el-table-column>
      <el-table-column label="修改事件" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>修改事件</span>&nbsp;
          <el-popover placement="top-start" width="200" trigger="hover"
            content="是否开启 源表发生【修改数据事件】的处理同步">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <span v-if="scope.row.updateEnable==0" style="color:red;">关闭</span>
          <span v-if="scope.row.updateEnable==1" style="color:green;">启动</span>
          &nbsp;
          <el-link type="primary" @click.native="handleUpdateUpdate(scope.row)"><i class="el-icon-edit"></i></el-link>
        </template>
      </el-table-column>
      <el-table-column label="删除事件" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>删除事件</span>&nbsp;
          <el-popover placement="top-start" width="200" trigger="hover"
            content="是否开启 源表发生【删除数据事件】的处理同步">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <span v-if="scope.row.deleteEnable==0" style="color:red;">关闭</span>
          <span v-if="scope.row.deleteEnable==1" style="color:green;">启动</span>
          &nbsp;
          <el-link type="primary" @click.native="handleDeleteUpdate(scope.row)"><i class="el-icon-edit"></i></el-link>
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
        <el-form-item label="源库" prop="sourceDb">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="根据订阅的topic，得到关联的源数据库">
              <span slot="reference">
                <span>源库</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          {{nodeModel.sourceDb}}
        </el-form-item>
        <el-form-item label="源表" prop="sourceTable">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="同步的源表，订阅此表发生binlog的变化">
              <span slot="reference">
                <span>源表</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.sourceTable" placeholder="选择源表">
            <el-option v-for="item in sourceTableList" :key="item" :label="item" :value="item" />
          </el-select>
        </el-form-item>
        <el-form-item label="Redis目标源" prop="targetId">
          <template slot="label">
            <el-popover placement="top-start" width="200" trigger="hover"
              content="同步到目标redis源">
              <span slot="reference">
                <span>Redis目标源</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.targetId" placeholder="选择redis目标源">
            <el-option v-for="item in targetList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行命令" prop="command">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="redis命令，支持 set,hset,hmset,delete,delhKeys,incr 命令">
              <span slot="reference">
                <span>执行命令</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.command" placeholder="执行命令">
            <el-option key="0" label="set" value="set" />
            <el-option key="1" label="delete" value="delete" />
            <el-option key="2" label="hset" value="hset" />
            <el-option key="3" label="hmset" value="hmset" />
            <el-option key="4" label="incr" value="incr" />
            <el-option key="5" label="delhKeys" value="delhKeys" />
          </el-select>
        </el-form-item>
        <el-form-item label="key格式" prop="keyFormat">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="key的模板，支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】">
              <span slot="reference">
                <span>key格式</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.keyFormat" style="width:300px"/>
        </el-form-item>
        <el-form-item label="field格式" prop="fieldFormat">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="field的模板【field是用来支持 hset、delhKeys指令的】；支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】，如果多个field用(逗号,)隔开">
              <span slot="reference">
                <span>field格式</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.fieldFormat" style="width:300px" type="textarea"/>
        </el-form-item>
        <el-form-item label="value格式" prop="valueFormat">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="value的模板,支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】，如果hmset命令需要，map对象；可对此value设置json格式字符串，系统会自动转为map">
              <span slot="reference">
                <span>value格式</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.valueFormat" style="width:300px" type="textarea"/>
        </el-form-item>
        <el-form-item label="过期时间(秒)" prop="expireTime">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="设置key的过期时间，即过多少秒后 过期；可以为空，代表不过期。比固定过期时间优先">
              <span slot="reference">
                <span>过期时间(秒)</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.expireTime" style="width:300px"/>
        </el-form-item>
        <el-form-item label="固定过期时间" prop="fixedTime">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="设置key的过期时间，即在固定的时间点过期，即每天的哪个时间点过期，一旦有更新 即代表第二天固定时间点；可以为空，代表不过期">
              <span slot="reference">
                <span>固定过期时间</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.fixedTime" style="width:300px"/>
        </el-form-item>
        <el-form-item label="新增事件" prop="insertEnable">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="是否开启 源表发生【新增数据事件】的处理同步">
              <span slot="reference">
                <span>新增事件</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.insertEnable" placeholder="选择关闭/启动">
            <el-option label="关闭" :value="0" />
            <el-option label="启动" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="修改事件" prop="updateEnable">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="是否开启 源表发生【修改数据事件】的处理同步">
              <span slot="reference">
                <span>修改事件</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.updateEnable" placeholder="修改关闭/启动">
            <el-option label="关闭" :value="0" />
            <el-option label="启动" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="删除事件" prop="deleteEnable">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="是否开启 源表发生【删除数据事件】的处理同步">
              <span slot="reference">
                <span>删除事件</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.deleteEnable" placeholder="删除关闭/启动">
            <el-option label="关闭" :value="0" />
            <el-option label="启动" :value="1" />
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
import { getTaskRedisRuleList, addTaskRedisRule, updateTaskRedisRule, deleteTaskRedisRule, updateTaskRedisRuleStatus } from '@/api/taskRedisRule'
import { getTargetList } from '@/api/target'
import { getTopic } from '@/api/topic'
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
      dialogInsertFormVisible: false,
      dialogUpdateFormVisible: false,
      dialogDeleteFormVisible: false,
      dialogStatus: 'create',
      targetList: [],
      taskId: '',
      env: '',
      sourceDb: '',
      sourceTable: '',
      sourceTableList: [],
      nodeModel: {
        id: undefined,
        taskId: null,
        sourceDb: null,
        sourceTable: null,
        targetId: null,
        command: null,
        keyFormat: null,
        fieldFormat: null,
        valueFormat: null,
        insertEnable: null,
        expireTime: null,
        fixedTime: null,
        insertSourceConditionFilter: null,
        updateEnable: null,
        updateSourceConditionFilter: null,
        updateNewConditionFilter: null,
        deleteEnable: null,
        deleteSourceConditionFilter: null,
        status: 0
      },
      count: 0,
      listQuery: {
        sourceTable: '',
        targetId: '',
        status: '',
        taskId: '',
        currentPage: 1,
        pageSize: 20
      },
      rules: {
        sourceDb: [{ required: true, message: '源库不能为空', trigger: 'change' }],
        sourceTable: [{ required: true, message: '源表不能为空', trigger: 'change' }],
        targetId: [{ required: true, message: '目标redis源不能为空', trigger: 'change' }],
        insertEnable: [{ required: true, message: '新增事件订阅不能为空', trigger: 'change' }],
        updateEnable: [{ required: true, message: '修改事件订阅不能为空', trigger: 'change' }],
        deleteEnable: [{ required: true, message: '删除事件订阅不能为空', trigger: 'change' }],
        command: [{ required: true, message: '执行命令不能为空', trigger: 'change' }],
        keyFormat: [{ required: true, message: 'key格式不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建redis任务规则',
        update: '修改redis任务规则'
      }
    }
  },
  created() {
    this.taskId = this.$route.query.taskId
    this.nodeModel.taskId = this.taskId
    this.listQuery.taskId = this.taskId
    getTopic(this.taskId).then((res) => {
      this.env = res.data.env
      this.sourceDb = res.data.syncDb
      this.nodeModel.sourceDb = this.sourceDb
      this.sourceTableList = res.data.syncTable.split(',')
      getTargetList('redis',this.env).then((res1) => {
        this.targetList = res1.data
      })
    })
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getTaskRedisRuleList(this.listQuery).then(response => {
        this.list = response.data.records
        this.count = response.data.total
        this.listLoading = false
      })
    },
    queryData() {
      this.listQuery.currentPage = 1
      this.fetchData()
    },
    resetModel() {
      this.nodeModel = {
        id: undefined,
        taskId: null,
        targetId: null,
        command: null,
        keyFormat: null,
        fieldFormat: null,
        valueFormat: null,
        insertEnable: null,
        expireTime: null,
        fixedTime: null,
        insertSourceConditionFilter: null,
        updateEnable: null,
        updateSourceConditionFilter: null,
        updateNewConditionFilter: null,
        deleteEnable: null,
        deleteSourceConditionFilter: null,
        status: 0
      }
      this.nodeModel.sourceDb = this.sourceDb
      this.nodeModel.taskId = this.taskId
    },
    handleCreate() {
      this.resetModel()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      console.info('sourceDb1=' + this.nodeModel.sourceDb)
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleStatus(id,status) {
      updateTaskRedisRuleStatus(id,status).then(res => {
        this.operationStatusRes(res)
      })
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addTaskRedisRule(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateTaskRedisRule(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
        }
      })
    },
    operationStatusRes(res) {
      if (res.success === true) {
        this.fetchData()
        this.dialogFormVisible = false
        this.$message({
          message: '任务规则状态 操作成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: '任务规则状态 操作失败',
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
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除Redis任务规则会导致很多关联数据失效', '确定删除redis任务规则', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTaskRedisRule(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除redis任务规则成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除redis任务规则失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>


<style scoped>
::v-deep .el-form-item {
    margin-bottom: 8px !important;
}

::v-deep .el-dialog {
  margin-top: 8vh !important;
}
</style>