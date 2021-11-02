<template>
  <div class="app-container">

    <div class="filter-container">
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建集群</el-button>
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
      <el-table-column label="集群名称" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="集群代码" width="150" align="center">
        <template slot-scope="scope">
          {{ scope.row.code }}
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
      <el-table-column align="center" prop="created_at" label="创建时间" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.createdTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" prop="updated_at" label="更新时间" width="160">
        <template slot-scope="scope">
          <span>{{ scope.row.updatedTime }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" min-width="100" width="150">
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
        <el-form-item label="集群名称" prop="name">
          <el-input v-model="nodeModel.name" />
        </el-form-item>
        <el-form-item label="集群代码" prop="code">
          <el-input v-model="nodeModel.code" />
        </el-form-item>
        <el-form-item label="所属命名空间" prop="env">
          <el-select v-if="dialogStatus === 'create'" v-model="nodeModel.env" placeholder="选择所属命名空间">
            <el-option v-for="item in nameSpaces" :key="item.id" :label="item.name" :value="item.env" />
          </el-select>
          <el-select v-else v-model="nodeModel.env" placeholder="选择所属命名空间" disabled="disabled">
            <el-option v-for="item in nameSpaces" :key="item.id" :label="item.name" :value="item.env" />
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
import { getClusterList, addCluster, updateCluster, deleteCluster } from '@/api/cluster'
import { getNameSpaceList } from '@/api/namespace'

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
      nameSpaces: [],
      nodeModel: {
        id: undefined,
        name: null,
        env: null,
        code: null,
        remark: null
      },
      rules: {
        name: [{ required: true, message: '集群名称不能为空', trigger: 'change' }],
        code: [{ required: true, message: '集群代码不能为空', trigger: 'change' }],
        env: [{ required: true, message: '命名空间不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建集群',
        update: '修改集群'
      }
    }
  },
  created() {
    getNameSpaceList().then((res) => {
      this.nameSpaces = res.data
    })
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getClusterList().then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    resetModel() {
      this.nodeModel = {
        id: undefined,
        name: null,
        code: null,
        env: null,
        remark: null
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
            addCluster(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateCluster(this.nodeModel).then(res => {
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
      this.$confirm('删除集群会导致很多关联数据失效', '确定删除集群', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCluster(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除集群成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除集群失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
