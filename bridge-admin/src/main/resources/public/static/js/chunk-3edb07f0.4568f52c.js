(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-3edb07f0"],{"0fac":function(e,t,n){"use strict";n.d(t,"d",(function(){return r})),n.d(t,"c",(function(){return l})),n.d(t,"e",(function(){return o})),n.d(t,"a",(function(){return i})),n.d(t,"f",(function(){return s})),n.d(t,"b",(function(){return c}));var a=n("b775");function r(e){return Object(a["a"])({url:"/topic",method:"get",params:e})}function l(e){return Object(a["a"])({url:"/topic/task/"+e,method:"get"})}function o(e){return Object(a["a"])({url:"/topic/env",method:"get",params:{env:e}})}function i(e){return Object(a["a"])({url:"/topic",method:"post",data:e})}function s(e){return Object(a["a"])({url:"/topic",method:"put",data:e})}function c(e){return Object(a["a"])({url:"/topic/"+e,method:"delete"})}},1276:function(e,t,n){"use strict";var a=n("d784"),r=n("44e7"),l=n("825a"),o=n("1d80"),i=n("4840"),s=n("8aa5"),c=n("50c4"),u=n("14c3"),d=n("9263"),p=n("d039"),f=[].push,g=Math.min,h=4294967295,m=!p((function(){return!RegExp(h,"y")}));a("split",2,(function(e,t,n){var a;return a="c"=="abbc".split(/(b)*/)[1]||4!="test".split(/(?:)/,-1).length||2!="ab".split(/(?:ab)*/).length||4!=".".split(/(.?)(.?)/).length||".".split(/()()/).length>1||"".split(/.?/).length?function(e,n){var a=String(o(this)),l=void 0===n?h:n>>>0;if(0===l)return[];if(void 0===e)return[a];if(!r(e))return t.call(a,e,l);var i,s,c,u=[],p=(e.ignoreCase?"i":"")+(e.multiline?"m":"")+(e.unicode?"u":"")+(e.sticky?"y":""),g=0,m=new RegExp(e.source,p+"g");while(i=d.call(m,a)){if(s=m.lastIndex,s>g&&(u.push(a.slice(g,i.index)),i.length>1&&i.index<a.length&&f.apply(u,i.slice(1)),c=i[0].length,g=s,u.length>=l))break;m.lastIndex===i.index&&m.lastIndex++}return g===a.length?!c&&m.test("")||u.push(""):u.push(a.slice(g)),u.length>l?u.slice(0,l):u}:"0".split(void 0,0).length?function(e,n){return void 0===e&&0===n?[]:t.call(this,e,n)}:t,[function(t,n){var r=o(this),l=void 0==t?void 0:t[e];return void 0!==l?l.call(t,r,n):a.call(String(r),t,n)},function(e,r){var o=n(a,e,this,r,a!==t);if(o.done)return o.value;var d=l(e),p=String(this),f=i(d,RegExp),v=d.unicode,b=(d.ignoreCase?"i":"")+(d.multiline?"m":"")+(d.unicode?"u":"")+(m?"y":"g"),y=new f(m?d:"^(?:"+d.source+")",b),_=void 0===r?h:r>>>0;if(0===_)return[];if(0===p.length)return null===u(y,p)?[p]:[];var w=0,k=0,x=[];while(k<p.length){y.lastIndex=m?k:0;var S,E=u(y,m?p:p.slice(k));if(null===E||(S=g(c(y.lastIndex+(m?0:k)),p.length))===w)k=s(p,k,v);else{if(x.push(p.slice(w,k)),x.length===_)return x;for(var I=1;I<=E.length-1;I++)if(x.push(E[I]),x.length===_)return x;k=w=S}}return x.push(p.slice(w)),x}]}),!m)},"14c3":function(e,t,n){var a=n("c6b6"),r=n("9263");e.exports=function(e,t){var n=e.exec;if("function"===typeof n){var l=n.call(e,t);if("object"!==typeof l)throw TypeError("RegExp exec method returned something other than an Object or null");return l}if("RegExp"!==a(e))throw TypeError("RegExp#exec called on incompatible receiver");return r.call(e,t)}},"333d":function(e,t,n){"use strict";var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"pagination-container",class:{hidden:e.hidden}},[n("el-pagination",e._b({attrs:{background:e.background,"current-page":e.currentPage,"page-size":e.pageSize,layout:e.layout,"page-sizes":e.pageSizes,total:e.total},on:{"update:currentPage":function(t){e.currentPage=t},"update:current-page":function(t){e.currentPage=t},"update:pageSize":function(t){e.pageSize=t},"update:page-size":function(t){e.pageSize=t},"size-change":e.handleSizeChange,"current-change":e.handleCurrentChange}},"el-pagination",e.$attrs,!1))],1)},r=[];n("a9e3");Math.easeInOutQuad=function(e,t,n,a){return e/=a/2,e<1?n/2*e*e+t:(e--,-n/2*(e*(e-2)-1)+t)};var l=function(){return window.requestAnimationFrame||window.webkitRequestAnimationFrame||window.mozRequestAnimationFrame||function(e){window.setTimeout(e,1e3/60)}}();function o(e){document.documentElement.scrollTop=e,document.body.parentNode.scrollTop=e,document.body.scrollTop=e}function i(){return document.documentElement.scrollTop||document.body.parentNode.scrollTop||document.body.scrollTop}function s(e,t,n){var a=i(),r=e-a,s=20,c=0;t="undefined"===typeof t?500:t;var u=function e(){c+=s;var i=Math.easeInOutQuad(c,a,r,t);o(i),c<t?l(e):n&&"function"===typeof n&&n()};u()}var c={name:"Pagination",props:{total:{required:!0,type:Number},page:{type:Number,default:1},limit:{type:Number,default:20},pageSizes:{type:Array,default:function(){return[10,20,30,50]}},layout:{type:String,default:"total, sizes, prev, pager, next, jumper"},background:{type:Boolean,default:!0},autoScroll:{type:Boolean,default:!0},hidden:{type:Boolean,default:!1}},computed:{currentPage:{get:function(){return this.page},set:function(e){this.$emit("update:page",e)}},pageSize:{get:function(){return this.limit},set:function(e){this.$emit("update:limit",e)}}},methods:{handleSizeChange:function(e){this.$emit("pagination",{page:this.currentPage,limit:e}),this.autoScroll&&s(0,800)},handleCurrentChange:function(e){this.$emit("pagination",{page:e,limit:this.pageSize}),this.autoScroll&&s(0,800)}}},u=c,d=(n("dfcf"),n("2877")),p=Object(d["a"])(u,a,r,!1,null,"38ef71f0",null);t["a"]=p.exports},"3f43":function(e,t,n){},7156:function(e,t,n){var a=n("861d"),r=n("d2bb");e.exports=function(e,t,n){var l,o;return r&&"function"==typeof(l=t.constructor)&&l!==n&&a(o=l.prototype)&&o!==n.prototype&&r(e,o),e}},"8aa5":function(e,t,n){"use strict";var a=n("6547").charAt;e.exports=function(e,t,n){return t+(n?a(e,t).length:1)}},9263:function(e,t,n){"use strict";var a=n("ad6d"),r=n("9f7f"),l=RegExp.prototype.exec,o=String.prototype.replace,i=l,s=function(){var e=/a/,t=/b*/g;return l.call(e,"a"),l.call(t,"a"),0!==e.lastIndex||0!==t.lastIndex}(),c=r.UNSUPPORTED_Y||r.BROKEN_CARET,u=void 0!==/()??/.exec("")[1],d=s||u||c;d&&(i=function(e){var t,n,r,i,d=this,p=c&&d.sticky,f=a.call(d),g=d.source,h=0,m=e;return p&&(f=f.replace("y",""),-1===f.indexOf("g")&&(f+="g"),m=String(e).slice(d.lastIndex),d.lastIndex>0&&(!d.multiline||d.multiline&&"\n"!==e[d.lastIndex-1])&&(g="(?: "+g+")",m=" "+m,h++),n=new RegExp("^(?:"+g+")",f)),u&&(n=new RegExp("^"+g+"$(?!\\s)",f)),s&&(t=d.lastIndex),r=l.call(p?n:d,m),p?r?(r.input=r.input.slice(h),r[0]=r[0].slice(h),r.index=d.lastIndex,d.lastIndex+=r[0].length):d.lastIndex=0:s&&r&&(d.lastIndex=d.global?r.index+r[0].length:t),u&&r&&r.length>1&&o.call(r[0],n,(function(){for(i=1;i<arguments.length-2;i++)void 0===arguments[i]&&(r[i]=void 0)})),r}),e.exports=i},"9f7f":function(e,t,n){"use strict";var a=n("d039");function r(e,t){return RegExp(e,t)}t.UNSUPPORTED_Y=a((function(){var e=r("a","y");return e.lastIndex=2,null!=e.exec("abcd")})),t.BROKEN_CARET=a((function(){var e=r("^r","gy");return e.lastIndex=2,null!=e.exec("str")}))},a361:function(e,t,n){"use strict";n("3f43")},a9e3:function(e,t,n){"use strict";var a=n("83ab"),r=n("da84"),l=n("94ca"),o=n("6eeb"),i=n("5135"),s=n("c6b6"),c=n("7156"),u=n("c04e"),d=n("d039"),p=n("7c73"),f=n("241c").f,g=n("06cf").f,h=n("9bf2").f,m=n("58a8").trim,v="Number",b=r[v],y=b.prototype,_=s(p(y))==v,w=function(e){var t,n,a,r,l,o,i,s,c=u(e,!1);if("string"==typeof c&&c.length>2)if(c=m(c),t=c.charCodeAt(0),43===t||45===t){if(n=c.charCodeAt(2),88===n||120===n)return NaN}else if(48===t){switch(c.charCodeAt(1)){case 66:case 98:a=2,r=49;break;case 79:case 111:a=8,r=55;break;default:return+c}for(l=c.slice(2),o=l.length,i=0;i<o;i++)if(s=l.charCodeAt(i),s<48||s>r)return NaN;return parseInt(l,a)}return+c};if(l(v,!b(" 0o1")||!b("0b1")||b("+0x1"))){for(var k,x=function(e){var t=arguments.length<1?0:e,n=this;return n instanceof x&&(_?d((function(){y.valueOf.call(n)})):s(n)!=v)?c(new b(w(t)),n,x):w(t)},S=a?f(b):"MAX_VALUE,MIN_VALUE,NaN,NEGATIVE_INFINITY,POSITIVE_INFINITY,EPSILON,isFinite,isInteger,isNaN,isSafeInteger,MAX_SAFE_INTEGER,MIN_SAFE_INTEGER,parseFloat,parseInt,isInteger".split(","),E=0;S.length>E;E++)i(b,k=S[E])&&!i(x,k)&&h(x,k,g(b,k));x.prototype=y,y.constructor=x,o(r,v,x)}},ac1f:function(e,t,n){"use strict";var a=n("23e7"),r=n("9263");a({target:"RegExp",proto:!0,forced:/./.exec!==r},{exec:r})},ad6d:function(e,t,n){"use strict";var a=n("825a");e.exports=function(){var e=a(this),t="";return e.global&&(t+="g"),e.ignoreCase&&(t+="i"),e.multiline&&(t+="m"),e.dotAll&&(t+="s"),e.unicode&&(t+="u"),e.sticky&&(t+="y"),t}},d784:function(e,t,n){"use strict";n("ac1f");var a=n("6eeb"),r=n("d039"),l=n("b622"),o=n("9263"),i=n("9112"),s=l("species"),c=!r((function(){var e=/./;return e.exec=function(){var e=[];return e.groups={a:"7"},e},"7"!=="".replace(e,"$<a>")})),u=function(){return"$0"==="a".replace(/./,"$0")}(),d=l("replace"),p=function(){return!!/./[d]&&""===/./[d]("a","$0")}(),f=!r((function(){var e=/(?:)/,t=e.exec;e.exec=function(){return t.apply(this,arguments)};var n="ab".split(e);return 2!==n.length||"a"!==n[0]||"b"!==n[1]}));e.exports=function(e,t,n,d){var g=l(e),h=!r((function(){var t={};return t[g]=function(){return 7},7!=""[e](t)})),m=h&&!r((function(){var t=!1,n=/a/;return"split"===e&&(n={},n.constructor={},n.constructor[s]=function(){return n},n.flags="",n[g]=/./[g]),n.exec=function(){return t=!0,null},n[g](""),!t}));if(!h||!m||"replace"===e&&(!c||!u||p)||"split"===e&&!f){var v=/./[g],b=n(g,""[e],(function(e,t,n,a,r){return t.exec===o?h&&!r?{done:!0,value:v.call(t,n,a)}:{done:!0,value:e.call(n,t,a)}:{done:!1}}),{REPLACE_KEEPS_$0:u,REGEXP_REPLACE_SUBSTITUTES_UNDEFINED_CAPTURE:p}),y=b[0],_=b[1];a(String.prototype,e,y),a(RegExp.prototype,g,2==t?function(e,t){return _.call(e,this,t)}:function(e){return _.call(e,this)})}d&&i(RegExp.prototype[g],"sham",!0)}},da82:function(e,t,n){},dfcf:function(e,t,n){"use strict";n("da82")},e84f:function(e,t,n){"use strict";n.r(t);var a=function(){var e=this,t=e.$createElement,n=e._self._c||t;return n("div",{staticClass:"app-container"},[n("div",{staticClass:"filter-container"},[n("el-select",{staticClass:"filter-item",attrs:{placeholder:"任务状态"},model:{value:e.listQuery.status,callback:function(t){e.$set(e.listQuery,"status",t)},expression:"listQuery.status"}},[n("el-option",{key:"-1",attrs:{label:"所有任务状态",value:"-1"}}),n("el-option",{key:"0",attrs:{label:"未发布",value:"0"}}),n("el-option",{key:"1",attrs:{label:"已发布",value:"1"}}),n("el-option",{key:"2",attrs:{label:"已停用",value:"2"}})],1),n("el-input",{staticClass:"filter-item",staticStyle:{width:"200px"},attrs:{placeholder:"源表"},model:{value:e.listQuery.sourceTable,callback:function(t){e.$set(e.listQuery,"sourceTable",t)},expression:"listQuery.sourceTable"}}),n("el-select",{staticClass:"filter-item",attrs:{placeholder:"请选择目标redis"},model:{value:e.listQuery.targetId,callback:function(t){e.$set(e.listQuery,"targetId",t)},expression:"listQuery.targetId"}},[n("el-option",{key:"",attrs:{label:"全部目标redis源",value:""}}),e._l(e.targetList,(function(e){return n("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})}))],2),n("el-button",{staticClass:"filter-item",attrs:{type:"primary",icon:"el-icon-search",plain:""},on:{click:function(t){return e.queryData()}}},[e._v("查询")]),n("el-button",{staticClass:"filter-item",attrs:{type:"primary"},on:{click:function(t){return e.handleCreate()}}},[e._v("新建redis同步规则")])],1),n("el-table",{directives:[{name:"loading",rawName:"v-loading",value:e.listLoading,expression:"listLoading"}],attrs:{data:e.list,"element-loading-text":"Loading",border:"",fit:"","highlight-current-row":""}},[n("el-table-column",{attrs:{align:"center","show-overflow-tooltip":"",label:"ID",width:"50"},scopedSlots:e._u([{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.id)+" ")]}}])}),n("el-table-column",{attrs:{label:"源库",width:"100",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("源库")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"300",trigger:"hover",content:"哪个数据库产生的binlog事件变化"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[n("span",[e._v(e._s(t.row.sourceDb))])]}}])}),n("el-table-column",{attrs:{label:"源表",width:"100",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("源表")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"300",trigger:"hover",content:"哪个数据表产生的binlog事件变化"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.sourceTable)+" ")]}}])}),n("el-table-column",{attrs:{label:"目标redis",width:"100",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("目标redis")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"200",trigger:"hover",content:"同步到的目标redis"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[e._l(e.targetList,(function(a){return[a.id===t.row.targetId?[n("span",[e._v(e._s(a.name))])]:e._e()]}))]}}])}),n("el-table-column",{attrs:{label:"执行命令",width:"100",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("执行命令")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"200",trigger:"hover",content:"执行redis哪个命令"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.command)+" ")]}}])}),n("el-table-column",{attrs:{label:"key格式","show-overflow-tooltip":"",width:"150",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("key格式")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"key的模板，支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.keyFormat)+" ")]}}])}),n("el-table-column",{attrs:{label:"field格式",width:"100","show-overflow-tooltip":"",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("field格式")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"field的模板【field是用来支持 hset、delhKeys指令的】；支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】，如果多个field用(逗号,)隔开"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.fieldFormat)+" ")]}}])}),n("el-table-column",{attrs:{label:"value格式",width:"150","show-overflow-tooltip":"",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("value格式")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"value的模板,支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】，如果hmset命令需要，map对象；可对此value设置json格式字符串，系统会自动转为map"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.valueFormat)+" ")]}}])}),n("el-table-column",{attrs:{label:"过期时间(秒)",width:"120","show-overflow-tooltip":"",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("过期时间(秒)")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"设置key的过期时间，即过多少秒后 过期；可以为空，代表不过期"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.expireTime)+" ")]}}])}),n("el-table-column",{attrs:{label:"固定过期时间","show-overflow-tooltip":"",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("固定过期时间")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"设置key的过期时间，即在固定的时间点过期，即每天的哪个时间点过期，一旦有更新 即代表第二天固定时间点；可以为空，代表不过期"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[e._v(" "+e._s(t.row.fixedTime)+" ")]}}])}),n("el-table-column",{attrs:{label:"新增事件",width:"100",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("新增事件")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"是否开启 源表发生【新增数据事件】的处理同步"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[0==t.row.insertEnable?n("span",{staticStyle:{color:"red"}},[e._v("关闭")]):e._e(),1==t.row.insertEnable?n("span",{staticStyle:{color:"green"}},[e._v("启动")]):e._e()]}}])}),n("el-table-column",{attrs:{label:"修改事件",width:"100",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("修改事件")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"200",trigger:"hover",content:"是否开启 源表发生【修改数据事件】的处理同步"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[0==t.row.updateEnable?n("span",{staticStyle:{color:"red"}},[e._v("关闭")]):e._e(),1==t.row.updateEnable?n("span",{staticStyle:{color:"green"}},[e._v("启动")]):e._e()]}}])}),n("el-table-column",{attrs:{label:"删除事件",width:"100",align:"center"},scopedSlots:e._u([{key:"header",fn:function(t){return[n("span",[e._v("删除事件")]),e._v(" "),n("el-popover",{attrs:{placement:"top-start",width:"200",trigger:"hover",content:"是否开启 源表发生【删除数据事件】的处理同步"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])]}},{key:"default",fn:function(t){return[0==t.row.deleteEnable?n("span",{staticStyle:{color:"red"}},[e._v("关闭")]):e._e(),1==t.row.deleteEnable?n("span",{staticStyle:{color:"green"}},[e._v("启动")]):e._e()]}}])}),n("el-table-column",{attrs:{label:"状态",width:"100",align:"center"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-tag",{attrs:{type:e._f("statusFilter")(t.row.status)}},[e._v(e._s(e._f("statusLabel")(t.row.status)))])]}}])}),n("el-table-column",{attrs:{align:"center",fixed:"right",label:"操作","min-width":"90",width:"100"},scopedSlots:e._u([{key:"default",fn:function(t){return[n("el-dropdown",{attrs:{trigger:"click"}},[n("el-button",{attrs:{type:"primary",size:"mini"}},[e._v(" 操作"),n("i",{staticClass:"el-icon-arrow-down el-icon--right"})]),n("el-dropdown-menu",{attrs:{slot:"dropdown"},slot:"dropdown"},[n("el-dropdown-item",{nativeOn:{click:function(n){return e.handleUpdate(t.row)}}},[e._v("修改")]),n("el-dropdown-item",{nativeOn:{click:function(n){return e.handleDelete(t.row)}}},[e._v("删除")]),n("el-dropdown-item",{nativeOn:{click:function(n){return e.handleStatus(t.row.id,1)}}},[e._v("发布")]),n("el-dropdown-item",{nativeOn:{click:function(n){return e.handleStatus(t.row.id,2)}}},[e._v("停用")])],1)],1)]}}])})],1),n("pagination",{directives:[{name:"show",rawName:"v-show",value:e.count>0,expression:"count>0"}],attrs:{total:e.count,page:e.listQuery.currentPage,limit:e.listQuery.pageSize},on:{"update:page":function(t){return e.$set(e.listQuery,"currentPage",t)},"update:limit":function(t){return e.$set(e.listQuery,"pageSize",t)},pagination:function(t){return e.fetchData()}}}),n("el-dialog",{attrs:{visible:e.dialogFormVisible,title:e.textMap[e.dialogStatus],width:"600px"},on:{"update:visible":function(t){e.dialogFormVisible=t}}},[n("el-form",{ref:"dataForm",staticStyle:{width:"400px","margin-left":"30px"},attrs:{rules:e.rules,model:e.nodeModel,"label-position":"right","label-width":"150px"}},[n("el-form-item",{attrs:{label:"源库",prop:"sourceDb"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"300",trigger:"hover",content:"根据订阅的topic，得到关联的源数据库"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("源库")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),e._v(" "+e._s(e.nodeModel.sourceDb)+" ")],2),n("el-form-item",{attrs:{label:"源表",prop:"sourceTable"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"300",trigger:"hover",content:"同步的源表，订阅此表发生binlog的变化"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("源表")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-select",{attrs:{placeholder:"选择源表"},model:{value:e.nodeModel.sourceTable,callback:function(t){e.$set(e.nodeModel,"sourceTable",t)},expression:"nodeModel.sourceTable"}},e._l(e.sourceTableList,(function(e){return n("el-option",{key:e,attrs:{label:e,value:e}})})),1)],2),n("el-form-item",{attrs:{label:"Redis目标源",prop:"targetId"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"200",trigger:"hover",content:"同步到目标redis源"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("Redis目标源")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-select",{attrs:{placeholder:"选择redis目标源"},model:{value:e.nodeModel.targetId,callback:function(t){e.$set(e.nodeModel,"targetId",t)},expression:"nodeModel.targetId"}},e._l(e.targetList,(function(e){return n("el-option",{key:e.id,attrs:{label:e.name,value:e.id}})})),1)],2),n("el-form-item",{attrs:{label:"执行命令",prop:"command"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"300",trigger:"hover",content:"redis命令，支持 set,hset,hmset,delete,delhKeys,incr 命令"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("执行命令")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-select",{attrs:{placeholder:"执行命令"},model:{value:e.nodeModel.command,callback:function(t){e.$set(e.nodeModel,"command",t)},expression:"nodeModel.command"}},[n("el-option",{key:"0",attrs:{label:"set",value:"set"}}),n("el-option",{key:"1",attrs:{label:"delete",value:"delete"}}),n("el-option",{key:"2",attrs:{label:"hset",value:"hset"}}),n("el-option",{key:"3",attrs:{label:"hmset",value:"hmset"}}),n("el-option",{key:"4",attrs:{label:"incr",value:"incr"}}),n("el-option",{key:"5",attrs:{label:"delhKeys",value:"delhKeys"}})],1)],2),n("el-form-item",{attrs:{label:"key格式",prop:"keyFormat"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"key的模板，支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("key格式")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-input",{staticStyle:{width:"300px"},model:{value:e.nodeModel.keyFormat,callback:function(t){e.$set(e.nodeModel,"keyFormat",t)},expression:"nodeModel.keyFormat"}})],2),n("el-form-item",{attrs:{label:"field格式",prop:"fieldFormat"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"field的模板【field是用来支持 hset、delhKeys指令的】；支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】，如果多个field用(逗号,)隔开"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("field格式")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-input",{staticStyle:{width:"300px"},attrs:{type:"textarea"},model:{value:e.nodeModel.fieldFormat,callback:function(t){e.$set(e.nodeModel,"fieldFormat",t)},expression:"nodeModel.fieldFormat"}})],2),n("el-form-item",{attrs:{label:"value格式",prop:"valueFormat"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"value的模板,支持freemarker解析引擎，利用源表中的数据，对模板进行解析；最终生成值。【如：user_${name}_act】，如果hmset命令需要，map对象；可对此value设置json格式字符串，系统会自动转为map"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("value格式")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-input",{staticStyle:{width:"300px"},attrs:{type:"textarea"},model:{value:e.nodeModel.valueFormat,callback:function(t){e.$set(e.nodeModel,"valueFormat",t)},expression:"nodeModel.valueFormat"}})],2),n("el-form-item",{attrs:{label:"过期时间(秒)",prop:"expireTime"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"设置key的过期时间，即过多少秒后 过期；可以为空，代表不过期。比固定过期时间优先"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("过期时间(秒)")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-input",{staticStyle:{width:"300px"},model:{value:e.nodeModel.expireTime,callback:function(t){e.$set(e.nodeModel,"expireTime",t)},expression:"nodeModel.expireTime"}})],2),n("el-form-item",{attrs:{label:"固定过期时间",prop:"fixedTime"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"500",trigger:"hover",content:"设置key的过期时间，即在固定的时间点过期，即每天的哪个时间点过期，一旦有更新 即代表第二天固定时间点；可以为空，代表不过期"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("固定过期时间")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-input",{staticStyle:{width:"300px"},attrs:{placeholder:"00:01:30"},model:{value:e.nodeModel.fixedTime,callback:function(t){e.$set(e.nodeModel,"fixedTime",t)},expression:"nodeModel.fixedTime"}})],2),n("el-form-item",{attrs:{label:"新增事件",prop:"insertEnable"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"300",trigger:"hover",content:"是否开启 源表发生【新增数据事件】的处理同步"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("新增事件")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-select",{attrs:{placeholder:"选择关闭/启动"},model:{value:e.nodeModel.insertEnable,callback:function(t){e.$set(e.nodeModel,"insertEnable",t)},expression:"nodeModel.insertEnable"}},[n("el-option",{attrs:{label:"关闭",value:0}}),n("el-option",{attrs:{label:"启动",value:1}})],1)],2),n("el-form-item",{attrs:{label:"修改事件",prop:"updateEnable"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"300",trigger:"hover",content:"是否开启 源表发生【修改数据事件】的处理同步"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("修改事件")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-select",{attrs:{placeholder:"修改关闭/启动"},model:{value:e.nodeModel.updateEnable,callback:function(t){e.$set(e.nodeModel,"updateEnable",t)},expression:"nodeModel.updateEnable"}},[n("el-option",{attrs:{label:"关闭",value:0}}),n("el-option",{attrs:{label:"启动",value:1}})],1)],2),n("el-form-item",{attrs:{label:"删除事件",prop:"deleteEnable"}},[n("template",{slot:"label"},[n("el-popover",{attrs:{placement:"top-start",width:"300",trigger:"hover",content:"是否开启 源表发生【删除数据事件】的处理同步"}},[n("span",{attrs:{slot:"reference"},slot:"reference"},[n("span",[e._v("删除事件")]),e._v(" "),n("i",{staticClass:"el-icon-warning-outline",staticStyle:{color:"#1989fa"}})])])],1),n("el-select",{attrs:{placeholder:"删除关闭/启动"},model:{value:e.nodeModel.deleteEnable,callback:function(t){e.$set(e.nodeModel,"deleteEnable",t)},expression:"nodeModel.deleteEnable"}},[n("el-option",{attrs:{label:"关闭",value:0}}),n("el-option",{attrs:{label:"启动",value:1}})],1)],2)],1),n("div",{staticClass:"dialog-footer",attrs:{slot:"footer"},slot:"footer"},[n("el-button",{on:{click:function(t){e.dialogFormVisible=!1}}},[e._v("取消")]),n("el-button",{attrs:{type:"primary"},on:{click:function(t){return e.dataOperation()}}},[e._v("确定")])],1)],1)],1)},r=[],l=(n("ac1f"),n("1276"),n("b775"));function o(e){return Object(l["a"])({url:"/task/rule/redis",method:"get",params:e})}function i(e){return Object(l["a"])({url:"/task/rule/redis",method:"post",data:e})}function s(e){return Object(l["a"])({url:"/task/rule/redis",method:"put",data:e})}function c(e,t){return Object(l["a"])({url:"/task/rule/redis/"+e,method:"post",params:{status:t}})}function u(e){return Object(l["a"])({url:"/task/rule/redis/"+e,method:"delete"})}var d=n("f6d4"),p=n("0fac"),f=n("333d"),g={components:{Pagination:f["a"]},filters:{statusFilter:function(e){var t={1:"success",0:"gray",2:"danger"};return t[e]},statusLabel:function(e){var t={1:"已发布",0:"未发布",2:"已停用"};return t[e]}},data:function(){return{list:null,listLoading:!0,dialogFormVisible:!1,dialogInsertFormVisible:!1,dialogUpdateFormVisible:!1,dialogDeleteFormVisible:!1,dialogStatus:"create",targetList:[],taskId:"",env:"",sourceDb:"",sourceTable:"",sourceTableList:[],nodeModel:{id:void 0,taskId:null,sourceDb:null,sourceTable:null,targetId:null,command:null,keyFormat:null,fieldFormat:null,valueFormat:null,insertEnable:null,expireTime:null,fixedTime:null,insertSourceConditionFilter:null,updateEnable:null,updateSourceConditionFilter:null,updateNewConditionFilter:null,deleteEnable:null,deleteSourceConditionFilter:null,status:0},count:0,listQuery:{sourceTable:"",targetId:"",status:"",taskId:"",currentPage:1,pageSize:20},rules:{sourceDb:[{required:!0,message:"源库不能为空",trigger:"change"}],sourceTable:[{required:!0,message:"源表不能为空",trigger:"change"}],targetId:[{required:!0,message:"目标redis源不能为空",trigger:"change"}],insertEnable:[{required:!0,message:"新增事件订阅不能为空",trigger:"change"}],updateEnable:[{required:!0,message:"修改事件订阅不能为空",trigger:"change"}],deleteEnable:[{required:!0,message:"删除事件订阅不能为空",trigger:"change"}],command:[{required:!0,message:"执行命令不能为空",trigger:"change"}],keyFormat:[{required:!0,message:"key格式不能为空",trigger:"change"}]},textMap:{create:"新建redis任务规则",update:"修改redis任务规则"}}},created:function(){var e=this;this.taskId=this.$route.query.taskId,this.nodeModel.taskId=this.taskId,this.listQuery.taskId=this.taskId,Object(p["c"])(this.taskId).then((function(t){e.env=t.data.env,e.sourceDb=t.data.syncDb,e.nodeModel.sourceDb=e.sourceDb,e.sourceTableList=t.data.syncTable.split(","),Object(d["a"])("redis",e.env).then((function(t){e.targetList=t.data}))})),this.fetchData()},methods:{fetchData:function(){var e=this;this.listLoading=!0,o(this.listQuery).then((function(t){e.list=t.data.records,e.count=t.data.total,e.listLoading=!1}))},queryData:function(){this.listQuery.currentPage=1,this.fetchData()},resetModel:function(){this.nodeModel={id:void 0,taskId:null,targetId:null,command:null,keyFormat:null,fieldFormat:null,valueFormat:null,insertEnable:null,expireTime:null,fixedTime:null,insertSourceConditionFilter:null,updateEnable:null,updateSourceConditionFilter:null,updateNewConditionFilter:null,deleteEnable:null,deleteSourceConditionFilter:null,status:0},this.nodeModel.sourceDb=this.sourceDb,this.nodeModel.taskId=this.taskId},handleCreate:function(){var e=this;this.resetModel(),this.dialogStatus="create",this.dialogFormVisible=!0,console.info("sourceDb1="+this.nodeModel.sourceDb),this.$nextTick((function(){e.$refs["dataForm"].clearValidate()}))},handleStatus:function(e,t){var n=this;c(e,t).then((function(e){n.operationStatusRes(e)}))},dataOperation:function(){var e=this;this.$refs["dataForm"].validate((function(t){t&&("create"===e.dialogStatus&&i(e.nodeModel).then((function(t){e.operationRes(t)})),"update"===e.dialogStatus&&s(e.nodeModel).then((function(t){e.operationRes(t)})))}))},operationStatusRes:function(e){!0===e.success?(this.fetchData(),this.dialogFormVisible=!1,this.$message({message:"任务规则状态 操作成功",type:"success"})):this.$message({message:"任务规则状态 操作失败",type:"error"})},operationRes:function(e){!0===e.success?(this.fetchData(),this.dialogFormVisible=!1,this.$message({message:this.textMap[this.dialogStatus]+"成功",type:"success"})):this.$message({message:this.textMap[this.dialogStatus]+"失败",type:"error"})},handleUpdate:function(e){var t=this;this.resetModel(),this.nodeModel=Object.assign({},e),this.dialogStatus="update",this.dialogFormVisible=!0,this.$nextTick((function(){t.$refs["dataForm"].clearValidate()}))},handleDelete:function(e){var t=this;this.$confirm("删除Redis任务规则会导致很多关联数据失效","确定删除redis任务规则",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}).then((function(){u(e.id).then((function(e){!0===e.success?(t.fetchData(),t.$message({message:"删除redis任务规则成功",type:"success"})):t.$message({message:"删除redis任务规则失败",type:"error"})}))}))}}},h=g,m=(n("a361"),n("2877")),v=Object(m["a"])(h,a,r,!1,null,"0672aeca",null);t["default"]=v.exports},f6d4:function(e,t,n){"use strict";n.d(t,"a",(function(){return r}));var a=n("b775");function r(e,t){return Object(a["a"])({url:"/target/"+e+"/"+t,method:"get"})}}}]);