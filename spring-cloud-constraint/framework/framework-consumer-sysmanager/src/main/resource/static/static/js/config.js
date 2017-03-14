//设置项目部署路径
loader.staticPath="/ghit-basic-consumer/";
loader.dynamicPath="/ghit-basic-consumer/";
loader.context.baseUrl=loader.staticPath+"static/";
loader.urlRewriteSession=false;
loader.htmlurltimestamp=true;
//定义第三方库
var config = {
    para: "version=v1.0_2016.08.01",
    js: {
        jquery: "framework/Jquery/jquery.min.js",
        underscore: "framework/underscore.js",
        handlebars: "framework/handlebars.js",
        bootstrap: "framework/Bootstrap/js/bootstrap.min.js",
        "bootstrap-select": "framework/Bootstrap-select/js/bootstrap-select.min.js",
        "bootstrap-select-cn": "framework/Bootstrap-select/js/i18n/defaults-zh_CN.min.js",
        "bootstrap-validate": "framework/Bootstrap-validate/js/bootstrapValidator.min.js",
        ztree: "framework/zTree/js/jquery.ztree.all.js",
        AdminLTE: "framework/AdminLTE/js/app.min.js",
        draggabilly: "framework/draggabilly/draggabilly.js",
        jwerty: "framework/jwerty.js",
        html5shiv: "framework/AdminLTE/js/html5shiv.min.js",
        respond: "framework/AdminLTE/js/respond.min.js",
        echarts:"framework/echarts/echarts.js",
        "mootools-core":"framework/mootools/mootools-core-1.3.1.js",
        "mootools-more":"framework/mootools/mootools-more-1.3.1.1.js",
            "sockjs":"framework/sockjs.min.js"
    },
    css: {
        "bootstrap-select": "framework/Bootstrap-select/css/bootstrap-select.min.css",
        bootstrap: "framework/Bootstrap/css/bootstrap.min.css",
        ztree: "framework/zTree/css/zTreeStyle/zTreeStyle.css",
        "bootstrap-validate": "framework/Bootstrap-validate/css/bootstrapValidator.min.css",
        "font-awesome": "framework/web-font-awesome/css/font-awesome.min.css",
        "ionicons": "framework/AdminLTE/css/ionicons.css",
        "iconfont": "framework/FontIcon/iconfont.css",
        "AdminLTE": "framework/AdminLTE/css/AdminLTE.min.css"
    },
    depend: {
        bootstrap: [{name: "jquery"}, {name:"bsIE"},{name: "bootstrap", type: "css"}],
        "bootstrap-select": [{name: "bootstrap"}, {name: "bootstrap-select", type: "css"}],
        "bootstrap-select-cn": [{name: "bootstrap-select"}],
        "bootstrap-validate": [{name: "bootstrap"}, {name: "bootstrap-validate", type: "css"}],
        ztree: [{name: "jquery"}, {name: "ztree", type: "css"}],
        AdminLTE: [{name: "AdminLTE", type: "css"}, {name: "bootstrap"}],
    	"draggabilly":[{name:"jquery"}],
        "floatWindow":[{name:"jquery"},{name:"draggabilly"}],
        "mootools-more":[{name:"mootools-core"}],
        "timePicker":[{name:"timePickerStyle",type:"css"},{name:"timePickerSkin_base",type:"css"}],
        "uploader":[{name:"jquery"}],
	    "divscroll":[{name:"jquery"}],
	    "fixedheadertable":[{name:"jquery"}]
    },
    noModular: ["jquery", "underscore", "handlebars", "bootstrap", "bootstrap-select","bootstrap-select-cn", "bootstrap-validate", "ztree", "AdminLTE", "draggabilly"
    ,"html5shiv","respond","jwerty","echarts","floatWindow","timePicker","spin","mootools-core","mootools-more","uploader","divscroll","fixedheadertable","sockjs"]

};
// 定义自定义的通用库
loader.context.put(config);
config = {
    js: {
        utils: "js/common/utils.js",
        basic: "js/common/basic.js",
        rsa: "js/common/rsa.js",
        bsIE: "js/common/bsIE.js",
        "combox": "js/common/combox.js",
        TabController: "js/common/TabController.js",
        discreteness:"framework/discreteness-resources/js/discreteness.js",
        floatWindow:"framework/plug/js/floatWindow_v_2_0.js",
        hjkplug:"framework/plug/js/hjkplug.js",
        timePicker:"framework/timePicker/js/laydate.js",
        pagination:"js/common/pagination.js",
        basicx: "js/common/basicx.js",
        spin:"framework/spin/spin.min.js",
        resource:"js/common/resource.js",
        workflow:"js/common/workflow.js",
        uploader:"framework/plug/js/uploader.js",
        printer:"framework/plug/js/printer.js",
        divscroll:"framework/divscroll/divscroll.js",
        "tabs":"framework/plug/js/tabs.js",
        "fixedheadertable":"framework/table/jquery.fixedheadertable.min.js",
        news:"js/common/news.js",
        "tabs":"framework/plug/js/tabs.js"
    },
    css: {
        "tools": "css/common/tools.css",
        "skin-blue": "css/common/skins/skin-blue.css",
        "framework-public": "css/framework-public.css",
        "discreteness":"framework/discreteness-resources/css/discreteness.css",
        "hjkbase":"framework/plug/css/hjkbase.css",
        //"hjkplug":"framework/plug/css/hjkplug.css",
        "hjkplug":"framework/plug/css/hjkplug_v_2_0.css",
        "timePickerStyle":"framework/timePicker/css/laydate.css",
        "timePickerSkin_base":"framework/timePicker/css/skins/molv/laydate.css",
        "tabs":"framework/plug/css/tabs.css",
        "hjkIcon":"framework/hjkIcon/hjkIcon.css"
    }
};
loader.context.put(config);
// 定义业务专用模块
config = {
    js: {
        login: "js/index/login.js",
        index: "js/index/index.js",
        "welcome":"js/index/welcome.js",
        "demo":"js/sysmgr/demo.js",
        "sysmgr.user": "js/sysmgr/user.js",
        "sysmgr.role": "js/sysmgr/role.js",
        "sysmgr.resource": "js/sysmgr/resource.js",
        "sysmgr.jurisdiction": "js/sysmgr/jurisdiction.js",
        "sysmgr.dataDictionary": "js/sysmgr/dataDictionary.js",
        "sysmgr.configuration": "js/sysmgr/configuration.js",
        "sysmgr.organization":"js/sysmgr/organization.js",
        "statistical.report":"js/statistical/report.js"
    },
    css: {
        login: "css/index/login.css",
        index: "css/index/index.css",
        "sysmgr.user":"css/sysmgr/user.css",
        "sysmgr.resource":"css/sysmgr/resource.css",
        "sysmgr.dataDictionary":"css/sysmgr/dataDictionary.css",
        "sysmgr.configuration":"css/sysmgr/configuration.css",
        "sysmgr.organization":"css/sysmgr/organization.css"
    }
};
loader.context.put(config);
loader.configSuccess();
