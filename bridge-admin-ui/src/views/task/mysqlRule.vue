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
      <el-input v-model="listQuery.targetTable" placeholder="目标表" style="width: 200px;" class="filter-item" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建mysql同步规则</el-button>
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
      <el-table-column label="目标库" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>目标库</span>&nbsp;
          <el-popover placement="top-start" width="200" trigger="hover"
            content="同步到的目标库">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.targetDb }}
        </template>
      </el-table-column>
      <el-table-column label="目标表" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>目标表</span>&nbsp;
          <el-popover placement="top-start" width="200" trigger="hover"
            content="同步到的目标表">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.targetTable }}
        </template>
      </el-table-column>
      <el-table-column label="源与目标关联列" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>源与目标关联列</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="源表 和 目标表之间通过 哪些列进行关联的，【格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2)】；【新增时：无效】；【修改和删除时：当目标表更新哪些数据的过滤条件】">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.syncPks }}
        </template>
      </el-table-column>
      <el-table-column label="同步列" show-overflow-tooltip align="center">
        <template slot="header" slot-scope="scope">
          <span>同步列</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="源表 和 目标表 同步的列名映射关系，【格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2)】">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.syncColumns }}
        </template>
      </el-table-column>
      <el-table-column label="新增事件" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>新增事件</span>&nbsp;
          <el-popover placement="top-start" width="200" trigger="hover"
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
      <el-form ref="dataForm" :rules="rules" :model="nodeModel" label-position="right" 
            label-width="150px" style="width: 600px; margin-left:30px;">
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
        <el-form-item label="Mysql目标源" prop="targetId">
          <template slot="label">
            <el-popover placement="top-start" width="200" trigger="hover"
              content="同步到目标mysql源">
              <span slot="reference">
                <span>Mysql目标源</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.targetId" placeholder="选择mysql目标源">
            <el-option v-for="item in targetList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标库" prop="targetDb">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="同步到目标库，必须和选择的目标源配置的数据库名保持一致">
              <span slot="reference">
                <span>目标库</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.targetDb" style="width:300px"/>
        </el-form-item>
        <el-form-item label="目标表" prop="targetTable">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="同步到目标表，这里只能写一张表">
              <span slot="reference">
                <span>目标表</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.targetTable" style="width:300px"/>
        </el-form-item>
        <el-form-item label="源与目标关联列" prop="syncPks">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="源表 和 目标表之间通过 哪些列进行关联的，【格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2)】；【新增时：无效】；【修改和删除时：当目标表更新哪些数据的过滤条件】">
              <span slot="reference">
                <span>源与目标关联列</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.syncPks" style="width:300px" type="textarea"/>
        </el-form-item>
        <el-form-item label="同步列" prop="syncColumns">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="源表 和 目标表 同步的列名映射关系，【格式（源列1=目标列1;源列2=目标列2）;如果列名一样可简写(列名1;列名2)】">
              <span slot="reference">
                <span>同步列</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.syncColumns" style="width:300px" type="textarea"/>
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
    <el-dialog :visible.sync="dialogInsertFormVisible" title="新增事件同步规则" width="750px">
      <el-form ref="dataInsertForm" :model="nodeModel" label-position="right" label-width="180px" style="width: 500px; margin-left:30px;">
        <el-form-item label="目标表的主键列名" prop="insertTargetPks">
          <template slot="label">
            <el-popover placement="top-start" width="400" trigger="hover"
              content="新增同步时，目标表的主键Id列名，以及生成规则，暂只支持uuid（列名=uuid）; 如果此值为空 表示新增时忽略">
              <span slot="reference">
                <span>目标表的主键列名</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.insertTargetPks" style="width:400px"/>
        </el-form-item>
        <el-form-item label="目标表的同步标识列名" prop="insertTargetOriginCol">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="新增同步时，目标表区分数据来源列名，【格式(目标列=指定来源值)，如: sourceType=sync】，这样就可以区分哪些数据是同步过来的；可以为空，表示不需要区分来源">
              <span slot="reference">
                <span>目标表的同步标识列名</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.insertTargetOriginCol" style="width:400px"/>
        </el-form-item>
        <el-form-item label="新增数据过滤-同步条件" prop="insertSourceConditionFilter">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="新增过滤条件，针对源数据的过滤条件，表达式成立才会同步此数据【goods_name=='abc' 或 price < 30 && is_del==0】">
              <span slot="reference">
                <span>新增数据过滤-同步条件</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.insertSourceConditionFilter" style="width:400px" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogInsertFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dataInsertOperation()">确定</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogUpdateFormVisible" title="修改事件同步规则" width="750px">
      <el-form ref="dataUpdateForm" :model="nodeModel" label-position="right" label-width="220px" style="width: 700px; margin-left:30px;">
        <el-form-item label="源表的旧数据过滤-同步条件" prop="updateSourceConditionFilter">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="修改事件的过滤条件，针对修改前的源数据过滤条件，表达式成立才同步；如【goods_name=='abc' 或 price < 30 && is_del==0】">
              <span slot="reference">
                <span>源表的旧数据过滤-同步条件</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.updateSourceConditionFilter" style="width:400px" type="textarea"/>
        </el-form-item>
        <el-form-item label="源表的新数据过滤-同步条件" prop="updateNewConditionFilter">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="修改事件的过滤条件，针对修改后的源数据过滤条件，表达式成立才同步；如【goods_name=='abc' 或 price < 30 && is_del==0】">
              <span slot="reference">
                <span>源表的新数据过滤-同步条件</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.updateNewConditionFilter" style="width:400px" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogUpdateFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dataUpdateOperation()">确定</el-button>
      </div>
    </el-dialog>
    <el-dialog :visible.sync="dialogDeleteFormVisible" title="删除事件同步规则" width="750px">
      <el-form ref="dataDeleteForm" :model="nodeModel" label-position="right" label-width="220px" style="width: 400px; margin-left:30px;">
        <el-form-item label="删除策略" prop="deleteStrategy">
          <el-select v-model="nodeModel.deleteStrategy" style="width:400px" placeholder="选择删除策略">
            <el-option label="删除对应的行" :value="0" />
            <el-option label="只更新对应的值为空（源和目标值不一致，就忽略更新）" :value="1" />
            <el-option label="只更新对应的值为空（源和目标值不一致，也强制更新为NULL）" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="源表的删除数据过滤-同步条件" prop="deleteSourceConditionFilter">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="删除事件的过滤条件，针对删除的源数据过滤条件，表达式成立才同步；如【goods_name=='abc' 或 price < 30 && is_del==0】">
              <span slot="reference">
                <span>源表的删除数据过滤-同步条件</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.deleteSourceConditionFilter" style="width:400px" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDeleteFormVisible = false">取消</el-button>
        <el-button type="primary" @click="dataDeleteOperation()">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getTaskMysqlRuleList, addTaskMysqlRule, updateTaskMysqlRule, deleteTaskMysqlRule, updateTaskMysqlRuleStatus, updateTaskMysqlRuleInsert, updateTaskMysqlRuleUpdate, updateTaskMysqlRuleDelete } from '@/api/taskMysqlRule'
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
        targetDb: null,
        targetTable: null,
        syncPks: null,
        syncColumns: null,
        insertEnable: null,
        insertTargetPks: null,
        insertTargetOriginCol: null,
        insertSourceConditionFilter: null,
        updateEnable: null,
        updateSourceConditionFilter: null,
        updateNewConditionFilter: null,
        deleteEnable: null,
        deleteStrategy: null,
        deleteSourceConditionFilter: null,
        status: 0
      },
      count: 0,
      listQuery: {
        sourceTable: '',
        targetTable: '',
        status: '',
        taskId: '',
        currentPage: 1,
        pageSize: 20
      },
      rules: {
        sourceDb: [{ required: true, message: '源库不能为空', trigger: 'change' }],
        sourceTable: [{ required: true, message: '源表不能为空', trigger: 'change' }],
        targetId: [{ required: true, message: '目标源不能为空', trigger: 'change' }],
        targetDb: [{ required: true, message: '目标库不能为空', trigger: 'change' }],
        targetTable: [{ required: true, message: '目标表不能为空', trigger: 'change' }],
        insertEnable: [{ required: true, message: '新增事件订阅不能为空', trigger: 'change' }],
        updateEnable: [{ required: true, message: '修改事件订阅不能为空', trigger: 'change' }],
        deleteEnable: [{ required: true, message: '删除事件订阅不能为空', trigger: 'change' }],
        syncPks: [{ required: true, message: '源与目标关联列不能为空', trigger: 'change' }],
        syncColumns: [{ required: true, message: '同步列不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建mysql任务规则',
        update: '修改mysql任务规则'
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
      getTargetList('mysql',this.env).then((res1) => {
        this.targetList = res1.data
      })
    })
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getTaskMysqlRuleList(this.listQuery).then(response => {
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
        targetDb: null,
        targetTable: null,
        sourceDb: null,
        sourceTable: null,
        syncPks: null,
        syncColumns: null,
        insertEnable: null,
        insertTargetPks: null,
        insertTargetOriginCol: null,
        insertSourceConditionFilter: null,
        updateEnable: null,
        updateSourceConditionFilter: null,
        updateNewConditionFilter: null,
        deleteEnable: null,
        deleteStrategy: null,
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
      updateTaskMysqlRuleStatus(id,status).then(res => {
        this.operationStatusRes(res)
      })
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addTaskMysqlRule(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateTaskMysqlRule(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
        }
      })
    },
    dataInsertOperation() {
      this.$refs['dataInsertForm'].validate((valid) => {
        if (valid) {
          updateTaskMysqlRuleInsert(this.nodeModel).then(res => {
              this.operationInsertRes(res)
          })
        }
      })
    },
    dataUpdateOperation() {
      this.$refs['dataUpdateForm'].validate((valid) => {
        if (valid) {
          updateTaskMysqlRuleUpdate(this.nodeModel).then(res => {
              this.operationUpdateRes(res)
          })
        }
      })
    },
    dataDeleteOperation() {
      this.$refs['dataDeleteForm'].validate((valid) => {
        if (valid) {
          updateTaskMysqlRuleDelete(this.nodeModel).then(res => {
              this.operationDeleteRes(res)
          })
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
    operationInsertRes(res) {
      if (res.success === true) {
        this.fetchData()
        this.dialogInsertFormVisible = false
        this.$message({
          message: '新增事件订阅内容 维护成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: '新增事件订阅内容 维护失败',
          type: 'error'
        })
      }
    },
    operationUpdateRes(res) {
      if (res.success === true) {
        this.fetchData()
        this.dialogUpdateFormVisible = false
        this.$message({
          message: '修改事件订阅内容 维护成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: '修改事件订阅内容 维护失败',
          type: 'error'
        })
      }
    },
    operationDeleteRes(res) {
      if (res.success === true) {
        this.fetchData()
        this.dialogDeleteFormVisible = false
        this.$message({
          message: '删除事件订阅内容 维护成功',
          type: 'success'
        })
      } else {
        this.$message({
          message: '删除事件订阅内容 维护失败',
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
    handleInsertUpdate(row){
      this.resetModel()
      this.nodeModel = Object.assign({}, row)
      this.dialogInsertFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataInsertForm'].clearValidate()
      })
    },
    handleUpdateUpdate(row){
      this.resetModel()
      this.nodeModel = Object.assign({}, row)
      this.dialogUpdateFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataUpdateForm'].clearValidate()
      })
    },
    handleDeleteUpdate(row){
      this.resetModel()
      this.nodeModel = Object.assign({}, row)
      this.dialogDeleteFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataDeleteForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除Mysql任务规则会导致很多关联数据失效', '确定删除mysql任务规则', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTaskMysqlRule(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除mysql任务规则成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除mysql任务规则失败',
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


