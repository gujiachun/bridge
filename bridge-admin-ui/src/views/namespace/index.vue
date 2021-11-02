<template>
  <div class="app-container">

    <div class="filter-container">
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建命名空间</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="序号" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="空间名称" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="环境代码" width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.env }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="创建时间" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.createdTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="updated_at" label="更新时间" width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.updatedTime }}</span>
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

    <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="nodeModel" label-position="right" label-width="120px" style="width: 400px; margin-left:30px;">
        <el-form-item label="空间名称" prop="name">
          <el-input v-model="nodeModel.name" />
        </el-form-item>
        <el-form-item label="环境代码" prop="env">
          <el-input v-model="nodeModel.env" />
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
import { getNameSpaceList, addNs, updateNs, deleteNs } from '@/api/namespace'

export default {
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
      nodeModel: {
        id: undefined,
        name: null,
        env: null
      },
      rules: {
        name: [{ required: true, message: '命名空间不能为空', trigger: 'change' }],
        env: [{ required: true, message: '环境代码不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建命名空间',
        update: '修改命名空间'
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getNameSpaceList().then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    resetModel() {
      this.nodeModel = {
        id: undefined,
        name: null,
        env: null
      }
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
            addNs(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateNs(this.nodeModel).then(res => {
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
      this.$confirm('删除命名空间会导致很多关联数据失效', '确定删除命名空间', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteNs(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除命名空间成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除命名空间失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
