var depends = [
    {name: "utils"},
    {name: "basic"},
    {name: "bootstrap-validate"},
    {name: "ztree"},
    {name: "font-awesome", type: "css"},
    {name: "iconfont", type: "css"},
    {name: "framework-public", type: "css"}
];
modular.define({name: "sysmgr.role"}, depends, function () {
	var fun_search=function(){return {};};
    var basic = this.basic;
    //数据提交函数
    var data = function () {
        var dataObj = $.hform.data($("#datamodal [name]"));
        return JSON.stringify($.extend($("#dataform").data("role"), dataObj));
    };
    //设置模块路径
    basic.options.domain = "role";
    // 设置添加功能参数信息
    basic.add.defaultValue =
    {
        roleName: "",
        description: "",
        id: "",
        roleCode: ""
    };
    basic.add.dependent.data = data;
    basic.add.dependent.showAfter = function (options) {
        $("#code").removeAttr("readonly");
        $("#dataform").data("role",
            {});
    };
    basic.add.ajaxOpts = {contentType: "application/json"};
    // 设置编辑功能参数信息
    basic.edit.dependent.data = data;
    basic.edit.dependent.showAfter = function (data, options) {
        $("#code").attr("readonly", "readonly");
        $("#dataform").data("role", data);
    };
    basic.edit.ajaxOpts = {contentType: "application/json"};
    //加载基本组件-增删改功能
    basic.init.load();
    // 初始化参数及用户数据、机构数据加载，模版函数定义等。
    $.hjk.pageOpts(
        {
            "template": {
                "data": $("#template-tbody").html()
            },
            selector: {
                data: "#dataList tbody",
                page: "#pageContainer"
            },
            condition: function () {
                return fun_search();
            },
            url: 'role/list',
            callback: function (page) {
            	page.role_juris={};
            	_.map(page.page.result,function(v,k){
            		page.role_juris[v.id]=v.jurisdictions;
            	});
                $("#dataList button[rolecode]").click(
                    function () {
                        $("#rolemdal").modal("show");
                        var t = $(this);
                        // 修改保存按钮的事件
                        $("#btn-save-resource").unbind("click");
                        $("#btn-save-resource").click(
                            function () {
                                var role =
                                {};
                                role['jurisdictions'] = _.filter(_.map($.fn.zTree.getZTreeObj("tree")
                                    .getCheckedNodes(true), function (ele) {
                                    return {"jurisdictionCode": ele.juris_code};
                                }), function (obj) {
                                    return obj.jurisdictionCode;
                                });
                                role.id = t.attr("id");
                                role.roleName = t.attr("roleName");
                                role.description = t.attr("description");
                                role.roleCode = t.attr("roleCode");
                                $.secAjax(
                                    {
                                        type: 'POST',
                                        url: "role/save",
                                        contentType: "application/json",
                                        data: JSON.stringify(role),
                                        dataType: 'json',
                                        success: function (data) {
                                            // 隐藏用户信息窗体
                                            $("#datamodal").modal("hide");
                                            // 修改提示窗口内容并弹出提示窗口
                                            $.hjk.messageBox.alert(
                                                {
                                                    body: data.msg
                                                });
                                            $.hjk.pageFlush();
                                        }
                                    });

                            });

                        var roleid = t.attr("id");
                        $("#tree_title").html(t.attr("roleName") + "的");
                        if (roleid) {
                            var currentJuris = page.role_juris[roleid];
                            currentJuris = _.pluck(currentJuris, "jurisdictionCode");
                            if (currentJuris) {
                                var ztree = $.fn.zTree.getZTreeObj("tree");
                                var nodeChecked =void 0;
                                nodeChecked= function () {
                                    _this = this;
                                    if (_.contains(currentJuris, _this.juris_code)) {
                                        ztree.checkNode(_this, true, true);
                                    } else {
                                        ztree.checkNode(_this, false, false);
                                    }
                                    _.map(_this.children, function (v) {
                                        nodeChecked.apply(v);
                                    });
                                };
                                nodeChecked.apply(ztree.getNodes()[0]);
                            }
                        }
                    });
            }
        }).pageLoad(1, 10, function () {
        return $.hform.cvMap("conditions",
            {
                "firstLoad": 'true'
            });
    }, function (page) {
        // 设置树的配置信息
        var setting =
        {
            check: {
                chkboxType: {
                    "Y": "ps",
                    "N": "s"
                },
                enable: true
            }
        };
        var saxprop =void 0;
        saxprop= function () {
            _this = this;
            _this["isHidden"] = false;
            if (_this.extend) {
                var o = eval("(" + _this.extend + ")");
                _.map(o, function (val, key) {
                    _this[key] = val;
                });
            }
            switch (_this.resourceType) {
                case "0":
                    _this['typename'] = "根";
                    break;
                case "1":
                    _this['typename'] = "应用";
                    break;
                case "2":
                    _this['typename'] = "导航条";
                    break;
                case "3":
                    _this['typename'] = "菜单";
                    break;
                case "4":
                    _this['typename'] = "按钮";
                    break;
            }
            if (_this.children) {
                _.map(_this.children, function (val) {
                	saxprop.apply(val);
                });
            }
        };
        saxprop.apply(page.resource);

        // 初始化树
        $.fn.zTree.init($("#tree"), setting, page.resource.children);
        return true;
    });
    
 // 点击查询按钮时修改fun_search方法，并重新加载页面数据。
    $("#btn-search").click(function(){
        fun_search = function () {
            if ($("#searchtext").val())
                return $.hform.cvMap("conditions",
                    {
                        'roleNameLIKE or descriptionLIKE or roleCodeLIKE': $("#searchtext").val()
                    });
            return {};
        };
        $.hjk.pageLoad(1, 10);
    });    

});