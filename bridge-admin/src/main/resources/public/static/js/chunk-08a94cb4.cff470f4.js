(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-08a94cb4"],{"63f9":function(e,t,n){"use strict";n.d(t,"c",(function(){return l})),n.d(t,"d",(function(){return r})),n.d(t,"a",(function(){return o})),n.d(t,"e",(function(){return i})),n.d(t,"b",(function(){return s}));var a=n("b775");function l(){return Object(a["a"])({url:"/mq",method:"get"})}function r(e){return Object(a["a"])({url:"/mq/query/env",method:"get",params:{env:e}})}function o(e){return Object(a["a"])({url:"/mq",method:"post",data:e})}function i(e){return Object(a["a"])({url:"/mq",method:"put",data:e})}function s(e){return Object(a["a"])({url:"/mq/"+e,method:"delete"})}},aad4:function(e,t,n){"use strict";n.d(t,"c",(function(){return l})),n.d(t,"a",(function(){return r})),n.d(t,"d",(function(){return o})),n.d(t,"b",(function(){return i}));var a=n("b775");function l(){return Object(a["a"])({url:"/ns",method:"get"})}function r(e){return Object(a["a"])({url:"/ns",method:"post",data:e})}function o(e){return Object(a["a"])({url:"/ns",method:"put",data:e})}function i(e){return Object(a["a"])({url:"/ns/"+e,method:"delete"})}},c443:function(e,t,n){"use strict";n.r(t);var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-button",{staticClass:"filter-item",attrs:{type:"primary"},on:{click:function(t){return e.handleCreate()}}},[e._v("新建mq")])],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],attrs:{data:e.list,"element-loading-text":"Loading",border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{align:"center",label:"序号",width:"60"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.id)+" ")]}}])}),n("el-table-column",{attrs:{label:"名称",width:"150",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("span",[e._v(e._s(t.row.name))])]}}])}),n("el-table-column",{attrs:{label:"server地址",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.servers)+" ")]}}])}),n("el-table-column",{attrs:{label:"mq类型",width:"100",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.mqType)+" ")]}}])}),n("el-table-column",{attrs:{label:"备注",width:"200",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.remark)+" ")]}}])}),n("el-table-column",{attrs:{label:"环境代码",width:"100",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.env)+" ")]}}])}),n("el-table-column",{attrs:{align:"center",label:"操作","min-width":"150",width:"200"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-dropdown",{attrs:{trigger:"click"}},[n("el-button",{attrs:{type:"primary",size:"mini"}},[e._v(" 操作"),n("i",{staticClass:"el-icon-arrow-down el-icon--right"})]),n("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[n("el-dropdown-item",{nativeOn:{click:function(n){return e.handleUpdate(t.row)}}},[e._v("修改")]),n("el-dropdown-item",{nativeOn:{click:function(n){return e.handleDelete(t.row)}}},[e._v("删除")])],1)],1)]}}])})],1),n("el-dialog",{attrs:{visible:e.dialogFormVisible,title:e.textMap[e.dialogStatus],width:"600px"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[n("el-form",{ref:"dataForm",staticStyle:{width:"400px","margin-left":"30px"},attrs:{rules:e.rules,model:e.nodeModel,"label-position":"right","label-width":"120px"}},[n("el-form-item",{attrs:{label:"名称",prop:"name"}},[n("el-input",{model:{value:e.nodeModel.name,callback:function(t){e.$set(e.nodeModel,"name",t)},expression:"nodeModel.name"}})],1),n("el-form-item",{attrs:{label:"server地址",prop:"servers"}},[n("el-input",{model:{value:e.nodeModel.servers,callback:function(t){e.$set(e.nodeModel,"servers",t)},expression:"nodeModel.servers"}})],1),n("el-form-item",{attrs:{label:"mq类型",prop:"mqType"}},[n("el-select",{attrs:{placeholder:"选择mq类型"},model:{value:e.nodeModel.mqType,callback:function(t){e.$set(e.nodeModel,"mqType",t)},expression:"nodeModel.mqType"}},[n("el-option",{attrs:{label:"kafka",value:"kafka"}}),n("el-option",{attrs:{label:"rocketmq",value:"rocketmq"}})],1)],1),n("el-form-item",{attrs:{label:"所属命名空间",prop:"env"}},["create"===e.dialogStatus?n("el-select",{attrs:{placeholder:"选择所属命名空间"},model:{value:e.nodeModel.env,callback:function(t){e.$set(e.nodeModel,"env",t)},expression:"nodeModel.env"}},e._l(e.nameSpaces,(function(e){return n("el-option",{key:e.id,attrs:{label:e.name,value:e.env}})})),1):n("el-select",{attrs:{placeholder:"选择所属命名空间",disabled:"disabled"},model:{value:e.nodeModel.env,callback:function(t){e.$set(e.nodeModel,"env",t)},expression:"nodeModel.env"}},e._l(e.nameSpaces,(function(e){return n("el-option",{key:e.id,attrs:{label:e.name,value:e.env}})})),1)],1),n("el-form-item",{attrs:{label:"备注",prop:"remark"}},[n("el-input",{attrs:{type:"textarea"},model:{value:e.nodeModel.remark,callback:function(t){e.$set(e.nodeModel,"remark",t)},expression:"nodeModel.remark"}})],1)],1),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取消")]),n("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.dataOperation()}}},[e._v("确定")])],1)],1)],1)},l=[],r=n("63f9"),o=n("aad4"),i={filters:{statusFilter:function(e){var t={published:"success",draft:"gray",deleted:"danger"};return t[e]}},data:function(){return{list:null,listLoading:!0,dialogFormVisible:!1,dialogStatus:"create",nameSpaces:[],nodeModel:{id:void 0,name:null,env:null,servers:null,mqType:null,remark:null},rules:{name:[{required:!0,message:"名称不能为空",trigger:"change"}],servers:[{required:!0,message:"servers地址不能为空",trigger:"change"}],mqType:[{required:!0,message:"mq类型不能为空",trigger:"change"}],env:[{required:!0,message:"命名空间不能为空",trigger:"change"}]},textMap:{create:"新建mq",update:"修改mq"}}},created:function(){var e=this;Object(o["c"])().then((function(t){e.nameSpaces=t.data})),this.fetchData()},methods:{fetchData:function(){var e=this;this.listLoading=!0,Object(r["c"])().then((function(t){e.list=t.data,e.listLoading=!1}))},resetModel:function(){this.nodeModel={id:void 0,name:null,env:null,mqType:null,servers:null,remark:null}},handleCreate:function(){var e=this;this.resetModel(),this.dialogStatus="create",this.dialogFormVisible=!0,this.$nextTick((function(){e.$refs["dataForm"].clearValidate()}))},dataOperation:function(){var e=this;this.$refs["dataForm"].validate((function(t){t&&("create"===e.dialogStatus&&Object(r["a"])(e.nodeModel).then((function(t){e.operationRes(t)})),"update"===e.dialogStatus&&Object(r["e"])(e.nodeModel).then((function(t){e.operationRes(t)})))}))},operationRes:function(e){!0===e.success?(this.fetchData(),this.dialogFormVisible=!1,this.$message({message:this.textMap[this.dialogStatus]+"成功",type:"success"})):this.$message({message:this.textMap[this.dialogStatus]+"失败",type:"error"})},handleUpdate:function(e){var t=this;this.resetModel(),this.nodeModel=Object.assign({},e),this.dialogStatus="update",this.dialogFormVisible=!0,this.$nextTick((function(){t.$refs["dataForm"].clearValidate()}))},handleDelete:function(e){var t=this;this.$confirm("删除MQ会导致很多关联数据失效","确定删除mq",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){Object(r["b"])(e.id).then((function(e){!0===e.success?(t.fetchData(),t.$message({message:"删除mq成功",type:"success"})):t.$message({message:"删除mq失败",type:"error"})}))}))}}},s=i,d=n("2877"),u=Object(d["a"])(s,a,l,!1,null,null,null);t["default"]=u.exports}}]);