var depends = [
    {name: "utils"},
    {name: "basic"},
    {name: "bootstrap-validate"},
    {name: "ztree"},
    {name: "font-awesome", type: "css"},
    {name: "iconfont", type: "css"},
    {name: "framework-public", type: "css"},
    {name: "sysmgr.configuration", type: "css"},
    {name:"timePicker"}
];
modular.define({name: "sysmgr.configuration"}, depends, function () {

//搜索数据
    function searchData() {
        $('#dataList tbody tr').each(function () {
            if ($(this).text().indexOf($("#searchtext").val()) != -1) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    }

    function loadData() {
        $.secAjax(
            {
                type: 'POST',
                url: "configuration/list",
                dataType: 'json',
                success: function (data) {
                    var bodyTemplate = Handlebars.compile($("#template-tbody").html());
                    var list = _.flatten(_.map(data, function (v) {
                        return v;
                    }));
                    $('#dataList tbody').html(bodyTemplate(list));
                }
            });
    }

// 初始化参数及用户数据、机构数据加载，模版函数定义等。

    var basic = this.basic;
    //设置模块路径
    basic.options.domain = "configuration";
    // 设置添加功能参数信息
    basic.add.defaultValue =
    {
        configurationGroup: "",
        configurationName: "",
        configurationValue: "",
        callback: "",
        flag: 1,
        id: ""
    };
    basic.list.flushing = function () {
        loadData();
    };
    //加载基本组件-增删改功能
    basic.init.load();


    // 定义性别格式化显示函数
    Handlebars.registerHelper('_IF', function (v, k1, v1, k2, v2, options) {
        return v == k1 ? v1 : v == k2 ? v2 : "";
    });
    loadData();
    $("#btn-search").click(searchData);
});