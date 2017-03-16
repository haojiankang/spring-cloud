var depends = [
    {name: "utils"},
    {name: "basic"},
    {name: "bootstrap-validate"},
    {name: "ztree"},
    {name: "font-awesome", type: "css"},
    {name: "iconfont", type: "css"},
    {name: "framework-public", type: "css"},

    {name:"hjkbase",type:"css"},
    {name:"hjkplug",type:"css"}
];
modular.define({name: "sysmgr.jurisdiction"}, depends, function () {

// 用于返回查询条件对象。
    var fun_search = function () {
        return
        {
        }
        ;
    };
    //排序函数，用于返回排序条件
    var fun_order=function(){
	return {"orders[1]":"+id","orders[0]":"+jurisdictionCode"};
    }
//    var fun_order=function(){
//	return {"orders":"+id"};
//    }

// 点击查询按钮时修改fun_search方法，并重新加载页面数据。
    function searchData() {
        fun_search = function () {
            if ($("#searchtext").val())
                return $.hform.cvMap("conditions",
                    {
                        'authenticationRuleLIKE or authenticationTypeLIKE or jurisdictionNameLIKE or jurisdictionCodeLIKE': $(
                            "#searchtext").val()
                        + " or " + $("#searchtext").val() + " or " + $("#searchtext").val() + " or " + $("#searchtext").val()
                    });
            return {};
        }
        $.hjk.pageLoad(1, 10);
    }

// 初始化

    var basic = this.basic;

    //设置模块路径
    basic.options.domain = "jurisdiction";
    // 设置添加功能参数信息
    basic.add.defaultValue =
    {
        id:"",
        jurisdictionName: "",
        jurisdictionCode: "",
        authenticationType: "",
        authenticationRule: ""
    };
    //加载基本组件-增删改功能
    basic.init.load();

    // 初始化页面组件参数
    $.hjk.pageOpts(
        {
            "template": {
                data: $("#template-tbody").html()
            },
            selector: {
                data: "#dataList tbody",
                page: "#pageContainer"
            },
            url: 'jurisdiction/list',
            condition: function () {
                return $.extend(fun_search(),fun_order());
            }
        }).pageLoad(1, 10);

    $("#btn-search").click(searchData);
});