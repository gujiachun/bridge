(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-e7556572"],{3520:function(e,t,n){"use strict";n.r(t);var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-button",{staticClass:"filter-item",attrs:{type:"primary"},on:{click:function(t){return e.handleCreate()}}},[e._v("新建集群")])],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],attrs:{data:e.list,"element-loading-text":"Loading",border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{align:"center",label:"序号",width:"95"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.id)+" ")]}}])}),n("el-table-column",{attrs:{label:"集群名称",width:"150",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(t.row.name))])]}}])}),n("el-table-column",{attrs:{label:"集群代码",width:"150",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.code)+" ")]}}])}),n("el-table-column",{attrs:{label:"备注",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.remark)+" ")]}}])}),n("el-table-column",{attrs:{label:"环境代码",width:"100",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.env)+" ")]}}])}),n("el-table-column",{attrs:{label:"在线任务数",width:"150",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("在线任务数")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"200",trigger:"hover",content:"集群中处理了多少个任务"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[n("span",{staticStyle:{color:"green","font-weight":"bold"}},[e._v(e._s(t.row.activeTaskCount))]),e._v(" "),t.row.activeTaskCount>0?n("el-link",{attrs:{type:"success"},nativeOn:{click:function(n){return e.handleShowActiveTask(t.row)}}},[n("i",{staticClass:"el-icon-view el-icon--right"})]):e._e()]}}])}),n("el-table-column",{attrs:{label:"在线实例数",width:"150",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("在线实例数")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"200",trigger:"hover",content:"集群中有多少在线server实例在处理任务"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[n("span",{staticStyle:{color:"green","font-weight":"bold"}},[e._v(e._s(t.row.activeServerCount))]),e._v(" "),t.row.activeServerCount>0?n("el-link",{attrs:{type:"success"},nativeOn:{click:function(n){return e.handleShowActiveServer(t.row)}}},[n("i",{staticClass:"el-icon-view el-icon--right"})]):e._e()]}}])}),n("el-table-column",{attrs:{align:"center",prop:"created_at",label:"创建时间",width:"160"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(t.row.createdTime))])]}}])}),n("el-table-column",{attrs:{align:"center",prop:"updated_at",label:"更新时间",width:"160"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(t.row.updatedTime))])]}}])}),n("el-table-column",{attrs:{align:"center",label:"操作","min-width":"100",width:"150"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-dropdown",{attrs:{trigger:"click"}},[n("el-button",{attrs:{type:"primary",size:"mini"}},[e._v(" 操作"),n("i",{staticClass:"el-icon-arrow-down el-icon--right"})]),n("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[n("el-dropdown-item",{nativeOn:{click:function(n){return e.handleUpdate(t.row)}}},[e._v("修改")]),n("el-dropdown-item",{nativeOn:{click:function(n){return e.handleDelete(t.row)}}},[e._v("删除")])],1)],1)]}}])})],1),n("el-dialog",{attrs:{visible:e.dialogFormVisible,title:e.textMap[e.dialogStatus],width:"600px"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[n("el-form",{ref:"dataForm",staticStyle:{width:"400px","margin-left":"30px"},attrs:{rules:e.rules,model:e.nodeModel,"label-position":"right","label-width":"120px"}},[n("el-form-item",{attrs:{label:"集群名称",prop:"name"}},[n("el-input",{model:{value:e.nodeModel.name,callback:function(t){e.$set(e.nodeModel,"name",t)},expression:"nodeModel.name"}})],1),n("el-form-item",{attrs:{label:"集群代码",prop:"code"}},[n("el-input",{model:{value:e.nodeModel.code,callback:function(t){e.$set(e.nodeModel,"code",t)},expression:"nodeModel.code"}})],1),n("el-form-item",{attrs:{label:"所属命名空间",prop:"env"}},["create"===e.dialogStatus?n("el-select",{attrs:{placeholder:"选择所属命名空间"},model:{value:e.nodeModel.env,callback:function(t){e.$set(e.nodeModel,"env",t)},expression:"nodeModel.env"}},e._l(e.nameSpaces,(function(e){return n("el-option",{key:e.id,attrs:{label:e.name,value:e.env}})})),1):n("el-select",{attrs:{placeholder:"选择所属命名空间",disabled:"disabled"},model:{value:e.nodeModel.env,callback:function(t){e.$set(e.nodeModel,"env",t)},expression:"nodeModel.env"}},e._l(e.nameSpaces,(function(e){return n("el-option",{key:e.id,attrs:{label:e.name,value:e.env}})})),1)],1),n("el-form-item",{attrs:{label:"备注",prop:"remark"}},[n("el-input",{attrs:{type:"textarea"},model:{value:e.nodeModel.remark,callback:function(t){e.$set(e.nodeModel,"remark",t)},expression:"nodeModel.remark"}})],1)],1),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取消")]),n("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.dataOperation()}}},[e._v("确定")])],1)],1)],1)},r=[],o=(n("159b"),n("7ea2")),l=n("aad4"),i={filters:{statusFilter:function(e){var t={published:"success",draft:"gray",deleted:"danger"};return t[e]}},data:function(){return{list:null,listLoading:!0,dialogFormVisible:!1,dialogStatus:"create",nameSpaces:[],nodeModel:{id:void 0,name:null,env:null,code:null,remark:null},rules:{name:[{required:!0,message:"集群名称不能为空",trigger:"change"}],code:[{required:!0,message:"集群代码不能为空",trigger:"change"}],env:[{required:!0,message:"命名空间不能为空",trigger:"change"}]},textMap:{create:"新建集群",update:"修改集群"}}},created:function(){var e=this;Object(l["c"])().then((function(t){e.nameSpaces=t.data})),this.fetchData()},methods:{fetchData:function(){var e=this;this.listLoading=!0,Object(o["c"])().then((function(t){e.list=t.data,e.listLoading=!1}))},resetModel:function(){this.nodeModel={id:void 0,name:null,code:null,env:null,remark:null}},handleShowActiveTask:function(e){var t="";t+="格式: 任务Id + 序列号（如：taskID000000001） <br/>",e.tasks.forEach((function(e){t+="任务流水: "+e+"<br/>"})),this.$alert(t,"任务信息",{confirmButtonText:"确定",dangerouslyUseHTMLString:!0})},handleShowActiveServer:function(e){var t="";t+="格式: ip:port <br/>",e.servers.forEach((function(e){t+="实例: "+e+"<br/>"})),this.$alert(t,"实例信息",{confirmButtonText:"确定",dangerouslyUseHTMLString:!0})},handleCreate:function(){var e=this;this.resetModel(),this.dialogStatus="create",this.dialogFormVisible=!0,this.$nextTick((function(){e.$refs["dataForm"].clearValidate()}))},dataOperation:function(){var e=this;this.$refs["dataForm"].validate((function(t){t&&("create"===e.dialogStatus&&Object(o["a"])(e.nodeModel).then((function(t){e.operationRes(t)})),"update"===e.dialogStatus&&Object(o["e"])(e.nodeModel).then((function(t){e.operationRes(t)})))}))},operationRes:function(e){!0===e.success?(this.fetchData(),this.dialogFormVisible=!1,this.$message({message:this.textMap[this.dialogStatus]+"成功",type:"success"})):this.$message({message:this.textMap[this.dialogStatus]+"失败",type:"error"})},handleUpdate:function(e){var t=this;this.resetModel(),this.nodeModel=Object.assign({},e),this.dialogStatus="update",this.dialogFormVisible=!0,this.$nextTick((function(){t.$refs["dataForm"].clearValidate()}))},handleDelete:function(e){var t=this;this.$confirm("删除集群会导致很多关联数据失效","确定删除集群",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){Object(o["b"])(e.id).then((function(e){!0===e.success?(t.fetchData(),t.$message({message:"删除集群成功",type:"success"})):t.$message({message:"删除集群失败",type:"error"})}))}))}}},s=i,c=n("2877"),d=Object(c["a"])(s,a,r,!1,null,null,null);t["default"]=d.exports},"7ea2":function(e,t,n){"use strict";n.d(t,"c",(function(){return r})),n.d(t,"d",(function(){return o})),n.d(t,"a",(function(){return l})),n.d(t,"e",(function(){return i})),n.d(t,"b",(function(){return s}));var a=n("b775");function r(){return Object(a["a"])({url:"/cluster",method:"get"})}function o(e){return Object(a["a"])({url:"/cluster/env",method:"get",params:{env:e}})}function l(e){return Object(a["a"])({url:"/cluster",method:"post",data:e})}function i(e){return Object(a["a"])({url:"/cluster",method:"put",data:e})}function s(e){return Object(a["a"])({url:"/cluster/"+e,method:"delete"})}},aad4:function(e,t,n){"use strict";n.d(t,"c",(function(){return r})),n.d(t,"a",(function(){return o})),n.d(t,"d",(function(){return l})),n.d(t,"b",(function(){return i}));var a=n("b775");function r(){return Object(a["a"])({url:"/ns",method:"get"})}function o(e){return Object(a["a"])({url:"/ns",method:"post",data:e})}function l(e){return Object(a["a"])({url:"/ns",method:"put",data:e})}function i(e){return Object(a["a"])({url:"/ns/"+e,method:"delete"})}}}]);