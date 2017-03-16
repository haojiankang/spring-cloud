var depends = [
    {name: "utils"},
    {name: "basic"},
    {name: "bootstrap-validate"},
    {name: "ztree"},
    {name:"floatWindow"},
    {name: "font-awesome", type: "css"},
    {name: "iconfont", type: "css"},
    //{name: "framework-public", type: "css"},
    {name: "sysmgr.resource", type: "css"},
    {name:"divscroll"}
];
modular.define({name: "sysmgr.resource"}, depends, function () {

// 打开添加界面
    function add() {
        $.hform.val($("#dataform [name]"),
            {
                resourceName: "",
                resourceType: "",
                id: "",
                parentNO: $("#infoform [name=no]").val(),
                no: "",
                juris_code: "",
                properties: ""
            });
        $("#code").removeAttr("readonly");
        
        //$("#datamodal").modal("show");
        showWindow("#datamodal")
    }

// 打开编辑界面
    function edit(id) {
        $.secAjax(
            {
                type: 'POST',
                url: "resource/info",
                data: "id=" + id,
                dataType: 'json',
                success: function (data) {
                    // 初始化编辑界面，转换后台传递数据为满足loadData要求的格式化对象
                    $.hform.val($("#dataform [name]"), data);
                    //$("#datamodal").modal("show");
                    showWindow("#datamodal");
                }
            });
    }

// 执行删除操作
    function del(id) {
        $.hjk.messageBox.show('操作提示', "确认删除记录?", {
            '确认': {
                'primary': true,
                'callback': function () {
                    $.secAjax(
                        {
                            type: 'POST',
                            url: "resource/del",
                            data: JSON.stringify([{"id": id}]),
                            contentType: 'application/json;charset=utf-8',
                            dataType: 'json',
                            success: function (data) {
                                $.hjk.messageBox.alert(
                                    {
                                        body: data.msg
                                    });
                                loadData();
                            }
                        });
                }
            },
            '取消': {
                'primary': true,
                'callback': function () {
                    $.hjk.messageBox.hide();
                }
            }
        });

    }

// 提交方法（添加和修改）
    function submit() {
        var dataObj = $.hform.data($("#datamodal [name]"));
        $.secAjax(
            {
                type: 'POST',
                url: "resource/save",
                data: dataObj,
                dataType: 'json',
                success: function (data) {
                    // 隐藏用户信息窗体
                    //$("#datamodal").modal("hide");
                	hideWindow("#datamodal")
                    $.hjk.messageBox.alert(
                        {
                            body: data.msg
                        });
                    loadData();
                }
            });
        return false;
    }

// 记录选中的节点id
    var selectNodeid;
// 加载数据
    function loadData() {
        $.secAjax(
            {
                type: 'POST',
                url: "resource/list",
                data: {},
                dataType: 'json',
                success: function (data) {
                    var nodeClickFun = function (event, treeId, treeNode) {
                        selectNodeid = treeNode.id;
                        // 修改列表的标题
                        $("#delnode").removeClass("disabled")
                        if (treeNode.children && treeNode.children.length > 0) {
                            $("#delnode").addClass("disabled").unbind("click");
                        } else {
                            $("#delnode").click(function () {
                                del($('#infoform [name=id]').val());
                            });
                        }
                        $("#info_title").html(treeNode.name + "的");
                        $("#list_title").html(treeNode.name + "的下级");
                        var pnode = treeNode.getParentNode();
                        var prop = eval(treeNode.extend);
                        $("form [name=resourceName]").val(treeNode.name);
                        $("form [name=id]").val(treeNode.id);
                        $("form [name=resourceType]").val(treeNode.resourceType);
                        $("form [name=resourceTypeName]").val(treeNode.typename);
                        $("form [name='juris_code']").val(treeNode.juris_code);
                        $("form [name='properties']").val(treeNode.properties);
                        $("form [name='no']").val(treeNode.no);
                        if (pnode) {
                            $("form [name='parentNO']").val(pnode.no);
                        }
                        var nodes = _.filter(treeNode.children, function (node) {
                            return node.resourceType >= 4;
                        })
                        var dataListTemplate = Handlebars.compile($("#template-tbody").html());
                        $('#dataList').html(dataListTemplate(nodes));
                    };
                    var setting =
                    {
                        callback: {
                            onClick: nodeClickFun
                        }
                    };
                    var saxprop = function () {
                        _this = this;
                        if (_this.extend) {
                            var o = eval("(" + _this.extend + ")")
                            _.map(o, function (val, key) {
                                _this[key] = val;
                            })
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
                            })
                        }

                    };
                    saxprop.apply(data);
                    // 初始化树
                    var zTree = $.fn.zTree.init($("#tree"), setting, data);
                    var rootNode = zTree.getNodeByParam("level", 0);
                    if (selectNodeid) {
                        var node = zTree.getNodeByParam("id", selectNodeid)
                        if (node != null) {
                            zTree.selectNode(node);
                            nodeClickFun(null, null, node);
                        }
                    } else {
                        nodeClickFun(null, null, rootNode);
                    }
                }
            });
    }

// 初始化参数及用户数据、机构数据加载，模版函数定义等。
    loadData();

    //注册事件
    $("#btn-add").click(add);
    $("#btn-edit").click(function(){
        edit($('#infoform [name=id]').val());
    });
    $("#btn-save").click(submit);
    $("body").delegate(".biz-btn-edit","click",function(){
        edit($(this).attr("data-id"));
    });
    $("body").delegate(".biz-btn-del","click",function(){
        del($(this).attr("data-id"));
    });


});