<template>
  <div class="app-container">

    <div class="filter-container">
      <el-select v-model="listQuery.env" placeholder="所属命名空间" class="filter-item">
        <el-option key="" label="所属命名空间" value="" />
        <el-option v-for="item in nameSpaces" :key="item.id" :label="item.name" :value="item.env" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建Source源</el-button>
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
      <el-table-column label="源名" width="150" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.sourceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="库名" width="100" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.dbName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="链接字符串" show-overflow-tooltip align="center">
        <template slot="header" slot-scope="scope">
          <span>链接字符串</span>&nbsp;
          <el-popover placement="top-start" width="600" trigger="hover"
            content="数据源配置格式（k1=v1;k2=v2）">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.props }}
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
              <el-dropdown-item @click.native="handleTableInfo(scope.row)">表信息</el-dropdown-item>
              <el-dropdown-item @click.native="handleUpdate(scope.row)">修改</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(scope.row)">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogFormVisible" :title="textMap[dialogStatus]" width="600px">
      <el-form ref="dataForm" :rules="rules" :model="nodeModel" label-position="right" label-width="120px" style="width: 400px; margin-left:30px;">
        <el-form-item label="源名" prop="sourceName">
          <el-input v-model="nodeModel.sourceName" />
        </el-form-item>
        <el-form-item label="库名" prop="dbName">
          <el-input v-model="nodeModel.dbName" />
        </el-form-item>
        <el-form-item label="链接字符串" prop="props">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="数据源配置格式（k1=v1;k2=v2）">
              <span slot="reference">
                <span>链接字符串</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.props" type="textarea"/>
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
import { getSourceList, getSourceListByEnv,addSource, updateSource, deleteSource } from '@/api/source'
import { getNameSpaceList } from '@/api/namespace'

export default {
  components: { },
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
        sourceName: null,
        env: null,
        dbName: null,
        props: null,
        remark: null
      },
      count: 0,
      listQuery: {
        env: ''
      },
      rules: {
        sourceName: [{ required: true, message: '源名不能为空', trigger: 'change' }],
        dbName: [{ required: true, message: '库名不能为空', trigger: 'change' }],
        props: [{ required: true, message: '链接字符串不能为空', trigger: 'change' }],
        env: [{ required: true, message: '命名空间不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建Source源',
        update: '修改Source源'
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
      getSourceListByEnv(this.listQuery.env).then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    queryData() {
      this.fetchData()
    },
    resetModel() {
      this.nodeModel = {
        id: undefined,
        sourceName: null,
        env: null,
        dbName: null,
        props: null,
        remark: null
      }
    },
    handleTableInfo(row) {
      this.$router.push('/setting/source/table?sourceId=' + row.id)
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
            addSource(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateSource(this.nodeModel).then(res => {
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
      this.$confirm('删除Source源码会导致很多关联数据失效', '确定删除Source源', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteSource(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除Source源成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除Source源失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
