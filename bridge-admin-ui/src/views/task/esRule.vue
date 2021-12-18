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
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建es同步规则</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column align="center" fixed show-overflow-tooltip label="ID" width="50">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="源库" fixed width="100" align="center">
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
      <el-table-column label="源表" fixed width="100" align="center">
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
      <el-table-column label="索引格式" fixed width="200" align="center">
        <template slot="header" slot-scope="scope">
          <span>索引格式</span>&nbsp;
          <el-popover placement="top-start" width="250" trigger="hover"
            content="索引格式(支持freemarker)">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.indexFormat }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100" fixed align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.status | statusFilter">{{ scope.row.status | statusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="id格式" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>id格式</span>&nbsp;
          <el-popover placement="top-start" width="200" trigger="hover"
            content="id格式(支持freemarker)">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.idFormat }}
        </template>
      </el-table-column>
      <el-table-column label="同步模式" width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>同步模式</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="索引更新类型 0：索引更新sql模式(执行sql语句，获得es属性))，1：索引更新canal模式（直接从binlog属性中获取）">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <span v-if="scope.row.indexType==0" style="color:blue;">sql模式</span>
          <span v-if="scope.row.indexType==1" style="color:green;">canal模式</span>
        </template>
      </el-table-column>
      <el-table-column label="字段映射" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>字段映射</span>&nbsp;
          <el-popover placement="top-start" width="500" trigger="hover"
            content="canal模式时有效，映射es和db的属性字段，格式{k1:v1,	k2:v2} k1=v1(es属性=db列)">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.partFormat }}
        </template>
      </el-table-column>
      <el-table-column label="sql语句" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>sql语句</span>&nbsp;
          <el-popover placement="top-start" width="600" trigger="hover"
            content="sql模式时有效，sql格式，有事件时执行sql，变量用?代替">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.sqlFormat }}
        </template>
      </el-table-column>
      <el-table-column label="sql字段" show-overflow-tooltip width="100" align="center">
        <template slot="header" slot-scope="scope">
          <span>sql字段</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="sql模式时有效，sql格式中?对应的值，（以#@#隔开）">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.sqlFieldFormat }}
        </template>
      </el-table-column>
      <el-table-column label="sqlNull字段" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>sqlNull字段</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="sql模式，如果sql执行结果为空是，需要清理的esFiled（逗号隔开）">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.sqlNullField }}
        </template>
      </el-table-column>
      <el-table-column label="字段类型" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>字段类型</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="字段的类型（k1=v1 以#@#隔开）(表示：es字段名=字段类型)\n类型有 int、date、string、array、json、decimal\n如：\nF1=array;   数组格式 array+值分隔符(1个字符)+值类型（int，decimal，string；默认不写为string）, 配合 group_concat 字符; 代表值以;隔开\nF1=json    json对象">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.fieldType }}
        </template>
      </el-table-column>
      <el-table-column label="忽略字段" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>忽略字段</span>&nbsp;
          <el-popover placement="top-start" width="400" trigger="hover"
            content="跳过忽略此字段（es属性），不需要把此es属性更新进去（F1,F2以逗号隔开）">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.skipsField }}
        </template>
      </el-table-column>
      <el-table-column label="文档类型" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>文档类型</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="0:普通文档，1：父文档，2：子文档">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          <span v-if="scope.row.relationType==0" style="color:orange;">普通文档</span>
          <span v-if="scope.row.relationType==1" style="color:blue;">父文档</span>
          <span v-if="scope.row.relationType==2" style="color:green;">子文档</span>
        </template>
      </el-table-column>
      <el-table-column label="父子关联名" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>父子关联名</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="父子关联健名（父子文档有效），如：join_field">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.relationFieldName }}
        </template>
      </el-table-column>
      <el-table-column label="文档关联名" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>文档关联名</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="文档关联名（父子文档有效），如：父:question，子：answer">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.relationJoinName }}
        </template>
      </el-table-column>
      <el-table-column label="子文档路由" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>子文档路由</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="子文档的route格式（父子文档有效）支持freemarker">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.relationChildRouteFormat }}
        </template>
      </el-table-column>
      <el-table-column label="子文档的父Id" show-overflow-tooltip width="150" align="center">
        <template slot="header" slot-scope="scope">
          <span>子文档的父Id</span>&nbsp;
          <el-popover placement="top-start" width="300" trigger="hover"
            content="子文档的parent格式（父子文档有效）支持freemarker">
            <span slot="reference">
              <i class="el-icon-warning-outline" style="color:#1989fa"></i>
            </span>
          </el-popover>
        </template>
        <template slot-scope="scope">
          {{ scope.row.relationChildParentFormat }}
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
        <el-form-item label="Es目标源" prop="targetId">
          <template slot="label">
            <el-popover placement="top-start" width="200" trigger="hover"
              content="同步到目标es源">
              <span slot="reference">
                <span>Es目标源</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.targetId" placeholder="选择es目标源">
            <el-option v-for="item in targetList" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="索引格式" prop="indexFormat">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="同步到es目标库，index名，可支持freemarker">
              <span slot="reference">
                <span>索引格式</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.indexFormat" style="width:300px"/>
        </el-form-item>
        <el-form-item label="id格式" prop="idFormat">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="同步到es目标库，索引中的id，可支持freemarker">
              <span slot="reference">
                <span>id格式</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.idFormat" style="width:300px"/>
        </el-form-item>
        <el-form-item label="同步模式" prop="indexType">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="索引更新类型 0：索引更新sql模式(执行sql语句，获得es属性))，1：索引更新canal模式（直接从binlog属性中获取）">
              <span slot="reference">
                <span>同步模式</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.indexType" placeholder="选择同步模式">
            <el-option label="sql模式" :value="0" />
            <el-option label="canal模式" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="字段映射" v-if="nodeModel.indexType=='1'" prop="partFormat">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="canal模式时有效，json格式，映射es和db的属性字段，格式{k1:v1,	k2:v2} k1=v1(es属性=db列)">
              <span slot="reference">
                <span>字段映射</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.partFormat" style="width:300px" type="textarea"/>
        </el-form-item>
        <el-form-item label="sql语句" v-if="nodeModel.indexType=='0'" prop="sqlFormat">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="sql模式时有效，sql格式，有事件时执行sql，变量用?代替">
              <span slot="reference">
                <span>sql语句</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.sqlFormat" style="width:300px" type="textarea"/>
        </el-form-item>
        <el-form-item label="sql字段" v-if="nodeModel.indexType=='0'" prop="sqlFieldFormat">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="sql模式时有效，sql格式中?对应的值，（以#@#隔开）">
              <span slot="reference">
                <span>sql字段</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.sqlFieldFormat" style="width:300px" type="textarea"/>
        </el-form-item>
        <el-form-item label="sqlNull字段" v-if="nodeModel.indexType=='0'" prop="sqlNullField">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="sql模式，如果sql执行结果为空是，需要清理的esFiled（逗号隔开）">
              <span slot="reference">
                <span>sqlNull字段</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.sqlNullField" style="width:300px" type="textarea"/>
        </el-form-item>
        <el-form-item label="字段类型" prop="fieldType">
          <template slot="label">
            <el-popover placement="top-start" width="600" trigger="hover"
              content="字段的类型（k1=v1 以#@#隔开）(表示：es字段名=字段类型)\n类型有 int、date、string、array、json、decimal\n如：\nF1=array;   数组格式 array+值分隔符(1个字符)+值类型（int，decimal，string；默认不写为string）, 配合 group_concat 字符; 代表值以;隔开\nF1=json    json对象">
              <span slot="reference">
                <span>字段类型</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.fieldType" style="width:300px" type="textarea"/>
        </el-form-item>
        <el-form-item label="忽略字段" prop="skipsField">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="跳过忽略此字段（es属性），不需要把此es属性更新进去（F1,F2以逗号隔开）">
              <span slot="reference"> 
                <span>忽略字段</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.skipsField" style="width:300px" type="textarea"/>
        </el-form-item>
        <el-form-item label="文档类型" prop="relationType">
          <template slot="label">
            <el-popover placement="top-start" width="300" trigger="hover"
              content="0:普通文档，1：父文档，2：子文档">
              <span slot="reference"> 
                <span>文档类型</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-select v-model="nodeModel.relationType" placeholder="选择文档类型">
            <el-option label="普通文档" :value="0" />
            <el-option label="父文档" :value="1" />
            <el-option label="子文档" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="父子关联名" v-if="nodeModel.relationType != '0'" prop="relationFieldName">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="父子关联健名（父子文档有效），如：join_field">
              <span slot="reference">
                <span>父子关联名</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.relationFieldName"  style="width:300px"/>
        </el-form-item>
        <el-form-item label="文档关联名" v-if="nodeModel.relationType != '0'" prop="relationJoinName">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="文档关联名（父子文档有效），如：父:question，子：answer">
              <span slot="reference">
                <span>文档关联名</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.relationJoinName" style="width:300px"/>
        </el-form-item>
        <el-form-item label="子文档路由" v-if="nodeModel.relationType == '2'" prop="relationChildRouteFormat">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="子文档的route格式（父子文档有效）支持freemarker">
              <span slot="reference">
                <span>子文档路由</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.relationChildRouteFormat" style="width:300px"/>
        </el-form-item>
        <el-form-item label="子文档的父Id" v-if="nodeModel.relationType == '2'" prop="relationChildParentFormat">
          <template slot="label">
            <el-popover placement="top-start" width="500" trigger="hover"
              content="子文档的parent格式（父子文档有效）支持freemarker">
              <span slot="reference">
                <span>子文档的父Id</span>&nbsp;
                <i class="el-icon-warning-outline" style="color:#1989fa"></i>
              </span>
            </el-popover>
          </template>
          <el-input v-model="nodeModel.relationChildParentFormat" style="width:300px"/>
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
            <el-option label="根据index和id模板删除索引" :value="0" />
            <el-option label="执行sql模板，更新索引（sql模式有效）" :value="1" />
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
import { getTaskEsRuleList, addTaskEsRule, updateTaskEsRule, deleteTaskEsRule, updateTaskEsRuleStatus, updateTaskEsRuleInsert, updateTaskEsRuleUpdate, updateTaskEsRuleDelete } from '@/api/taskEsRule'
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
        indexFormat: null,
        idFormat: null,
        indexType: null,
        partFormat: null,
        sqlFormat: null,
        sqlFieldFormat: null,
        sqlNullField: null,
        fieldType: null,
        skipsField: null,
        relationType: 0,
        relationFieldName: null,
        relationChildRouteFormat: null,
        relationChildParentFormat: null,
        insertEnable: null,
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
        status: '',
        taskId: '',
        currentPage: 1,
        pageSize: 20
      },
      rules: {
        sourceDb: [{ required: true, message: '源库不能为空', trigger: 'change' }],
        sourceTable: [{ required: true, message: '源表不能为空', trigger: 'change' }],
        targetId: [{ required: true, message: '目标源不能为空', trigger: 'change' }],
        indexFormat: [{ required: true, message: '索引格式不能为空', trigger: 'change' }],
        idFormat: [{ required: true, message: 'id格式不能为空', trigger: 'change' }],
        insertEnable: [{ required: true, message: '新增事件订阅不能为空', trigger: 'change' }],
        updateEnable: [{ required: true, message: '修改事件订阅不能为空', trigger: 'change' }],
        deleteEnable: [{ required: true, message: '删除事件订阅不能为空', trigger: 'change' }],
        indexType: [{ required: true, message: '同步模式不能为空', trigger: 'change' }],
        relationType: [{ required: true, message: '文档类型不能为空', trigger: 'change' }]
      },
      textMap: {
        create: '新建es任务规则',
        update: '修改es任务规则'
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
      getTargetList('es',this.env).then((res1) => {
        this.targetList = res1.data
      })
    })
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getTaskEsRuleList(this.listQuery).then(response => {
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
        taskId: null,
        sourceDb: null,
        sourceTable: null,
        targetId: null,
        indexFormat: null,
        idFormat: null,
        indexType: null,
        partFormat: null,
        sqlFormat: null,
        sqlFieldFormat: null,
        sqlNullField: null,
        fieldType: null,
        skipsField: null,
        relationType: 0,
        relationFieldName: null,
        relationChildRouteFormat: null,
        relationChildParentFormat: null,
        insertEnable: null,
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
      updateTaskEsRuleStatus(id,status).then(res => {
        this.operationStatusRes(res)
      })
    },
    dataOperation() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          if (this.dialogStatus === 'create') {
            addTaskEsRule(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
          if (this.dialogStatus === 'update') {
            updateTaskEsRule(this.nodeModel).then(res => {
              this.operationRes(res)
            })
          }
        }
      })
    },
    dataInsertOperation() {
      this.$refs['dataInsertForm'].validate((valid) => {
        if (valid) {
          updateTaskEsRuleInsert(this.nodeModel).then(res => {
              this.operationInsertRes(res)
          })
        }
      })
    },
    dataUpdateOperation() {
      this.$refs['dataUpdateForm'].validate((valid) => {
        if (valid) {
          updateTaskEsRuleUpdate(this.nodeModel).then(res => {
              this.operationUpdateRes(res)
          })
        }
      })
    },
    dataDeleteOperation() {
      this.$refs['dataDeleteForm'].validate((valid) => {
        if (valid) {
          updateTaskEsRuleDelete(this.nodeModel).then(res => {
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
      this.$confirm('删除Es任务规则会导致很多关联数据失效', '确定删除es任务规则', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTaskEsRule(row.id).then((res) => {
          if (res.success === true) {
            this.fetchData()
            this.$message({
              message: '删除es任务规则成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除es任务规则失败',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>

<style>

.el-tooltip__popper{
	max-width: 600px;
}

</style>

<style scoped>
::v-deep .el-form-item {
    margin-bottom: 8px !important;
}

::v-deep .el-dialog {
  margin-top: 8vh !important;
}
</style>


