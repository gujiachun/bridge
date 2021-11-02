<template>
  <div class="app-container">

    <div class="filter-container">
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建zk</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" label="序号" width="60">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="名称" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="server地址" align="center">
        <template slot-scope="scope">
          {{ scope.row.servers }}
        </template>
      </el-table-column>
      <el-table-column label="根节点路径" width="100" align="center">
        <template slot-scope="scope">
          {{ scope.row.rootPath }}
        </template>
      </el-table-column>
      <el-table-column label="备注" width="100" align="center">
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
        <el-form-item label="名称" prop="name">
          <el-input v-model="nodeModel.name" />
        </el-form-item>
        <el-form-item label="server地址" prop="servers">
          <el-input v-model="nodeModel.servers" />
        </el-form-item>
        <el-form-item label="根节点路径" prop="rootPath">
          <el-input v-model="nodeModel.rootPath" />
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
import { getZkList, addZk, updateZk, deleteZk } from '@/api/zk'
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
        servers: null,
        rootPath: null,
        remark: null
      },
      rules: {
        name: [{ required: true, message: '名称不能为空', trigger: 'change' }],
        servers: [{ required: true, message: 'servers地址不能为空', trigger: 'change' }],
        rootPath: [{ required: true, message: '根节点路径不能为空', trigger: 'change' }],
        env: [{ required: true, message: '命名空间不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建zk',
        update: '修改zk'
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
      getZkList().then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    resetModel() {
      this.nodeModel = {
        id: undefined,
        name: null,
        env: null,
        rootPath: null,
        servers: null,
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
            addZk(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateZk(this.nodeModel).then(res => {
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
      this.$confirm('删除Zk会导致很多关联数据失效', '确定删除zk', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteZk(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除zk成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除zk失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
