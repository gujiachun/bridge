<template>
  <div class="app-container">

    <div class="filter-container">
      <el-input v-model="listQuery.tableName" placeholder="表名" style="width: 200px;" class="filter-item" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建源表</el-button>
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
      <el-table-column label="表名" width="300" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.tableName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="源名" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.sourceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="库名" width="200" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.dbName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center">
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
        <el-form-item label="表名" prop="tableName">
          <el-input v-model="nodeModel.tableName" />
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
import { getSourceTableListByEnv, addSourceTable, updateSourceTable, deleteSourceTable } from '@/api/sourceTable'
import { getNameSpaceList } from '@/api/namespace'
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
      nodeModel: {
        id: undefined,
        sourceId: null,
        tableName: null,
        remark: null
      },
      count: 0,
      listQuery: {
        tableName: '',
        sourceId: null,
        currentPage: 1,
        pageSize: 20
      },
      rules: {
        tableName: [{ required: true, message: '表名不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建源表',
        update: '修改源表'
      }
    }
  },
  created() {
    this.listQuery.sourceId = this.$route.query.sourceId
    this.nodeModel.sourceId = this.$route.query.sourceId
    getNameSpaceList().then((res) => {
      this.nameSpaces = res.data
    })
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getSourceTableListByEnv(this.listQuery).then(response => {
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
        tableName: null,
        sourceId: null,
        remark: null
      },
      this.nodeModel.sourceId = this.$route.query.sourceId
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
          if (this.dialogStatus === 'create') {
            addSourceTable(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateSourceTable(this.nodeModel).then(res => {
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
      this.nodeModel = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleDelete(row) {
      this.$confirm('删除源表会导致很多关联数据失效', '确定删除源表', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteSourceTable(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除源表成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除源表失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
