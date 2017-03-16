var depends = [
    {name: "utils"},
    {name: "basic"},
    {name: "resource"},
    {name: "bootstrap-select-cn"},
    {name: "bootstrap-validate"},
    {name: "ztree"},
    {name: "combox"},
    {name: "font-awesome", type: "css"},
    {name: "iconfont", type: "css"},
    //{name: "framework-public",type:"css"},
    {name: "sysmgr.user", type: "css"},
    {name:"divscroll"}
];


modular.define({name: "sysmgr.user"}, depends, function () {
    var basic = this.basic;
    var utils = this.utils;
    var combox=this.combox;
    var resource=this.resource.create();
    resource.options.data.resourceName="访问系统/系统管理/";
    resource.assess();
// 用于返回查询条件对象。
    var fun_search = function () {
        return
        {
        }
        ;
    };
// 用于返回树查询条件对象。
    var fun_tree = function () {
        return
        {
        }
        ;
    };
// 点击查询按钮时修改fun_search方法，并重新加载页面数据。
    function searchData() {
        fun_search = function () {
            if ($("#searchtext").val()) 
            	return $.hform.cvMap("conditions",{'userNameLIKE or fullnameLIKE': $("#searchtext").val()});
            return {};
        };
        $.hjk.pageLoad(1, 10);
    }

// 导出Excel数据
    function exportData() {
        fun_search = function () {
            if ($("#fullname").val()) return $.hform.cvMap("conditions",
                {
                    'userNameLIKE': $("#fullname").val()
                });
            return
            {
            }
            ;
        };
        var condition = $.extend(fun_search(), fun_tree());
        $.hjk.common.fn.postData("../../user/export", condition, "post");
    }


// 初始化参数及用户数据、机构数据加载，模版函数定义等。

    // 设置模块路径
    basic.options.domain = "user";
    // 设置添加功能参数信息
    basic.add.defaultValue =
    {
        userName: "",
        flag: 1,
        sex: 1,
        id: "",
        fullname: "",
        tel: "",
        "roles[index].id": []
    };
    basic.add.dependent.isValidate = basic.edit.dependent.isValidate = function () {
        $("#dataform").data('bootstrapValidator').validate();
        if (!$("#dataform").data('bootstrapValidator').isValid()) {
            return false;
        }
        return true;
    };
    basic.edit.dependent.setVal = function (data, options) {
	var other= {
                "roles[index].id": _.map(data.roles, function (element) {
                    return element.id;
                })                
            };
	if(data.organization){
	    other["organization.id"]= data.organization.id;
	    other["organization.code"]=data.organization.code;
            other["organization.name"]= data.organization.name;
	}
        $.hform.val($("#dataform [name]"), $.extend(data.user,
        	other));
        return this;
    };
    basic.add.ajaxOpts = basic.edit.ajaxOpts = {
        failAfter: function (data) {
            return true;
        },
        successBefore: function () {
            return true;
        }
    };
    basic.add.dependent.showAfter = basic.edit.dependent.showAfter = function (options) {
        $("#dataform").data('bootstrapValidator').resetForm();
    };

    // 加载基本组件-增删改功能
    basic.init.load();


    $('#dataform')
        .bootstrapValidator({
            message: '此值无效',
            group: ".form-group-input",
            excluded: [':disabled'],
            submitButtons: '#btn-save',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
                userName: {
                    message: '帐号无效',
                    validators: {
                        notEmpty: {
                            message: '帐号为必填项,不能为空'
                        },
                        stringLength: {
                            min: 1,
                            max: 16,
                            message: '帐号必须超过1,不超过16个字符'
                        }
                    }
                }
                ,
                fullname: {
                    validators: {
                        notEmpty: {
                            message: '姓名为必填项,不能为空'
                        },
                        stringLength: {
                            min: 1,
                            max: 15,
                            message: '姓名应为1-15个字符'
                        }
                    }
                },
                tel: {
                    validators: {
                        stringLength: {
                            min: 6,
                            max: 30,
                            message: '电话应为6-30个字符'
                        }
                    }
                }
            }
        });


  
    // 初始化页面组件参数并加载用户、机构数据
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
                return $.extend(fun_search(), fun_tree());
            },
            url: 'user/list',
            callback:function(){
        	resource.assess();
            }
        }).pageLoad(
        1,
        10,
        function () {
            return $.hform.cvMap("conditions",
                {
                    "firstLoad": 'true'
                });
        }
        // 第一次加载数据，返回含有机构信息的对象。{Organization:,page:{result:,records:,page:,rows:}}
        ,
        function (page) {
            // 设置树的配置信息
            var setting =
            {
                callback: {
                    onClick: function (event, treeId, treeNode, clickFlag) {
                        // 修改列表的标题
                        $("#list_title").html(treeNode.name + "的");
                        fun_tree = function () {
                            if ($("#ck_children").is(":checked")) {
                                return $.hform.cvMap("conditions",
                                    {
                                        'organization.codeindexLIKE or organization.codeindex': treeNode.index + " or " + treeNode.index
                                    });
                            } else {
                                return $.hform.cvMap("conditions",
                                    {
                                        'organization.codeindex': treeNode.index
                                    });
                            }
                        };
                        $.hjk.pageLoad(1, 10);
                        $.hform.val($("#dataform [name]"),
                            {
                                "organization.name": treeNode.name,
                                'organization.code': treeNode.code,
                                'organization.id': treeNode.id
                            });
                    }
                }
            };
            // 初始化树
            $.fn.zTree.init($("#organization"), setting, page.Organization.children);
            
            utils.dataDic.bind("sex",page.sex);
            combox.changeOptions("#sex",page.sex);
            
            utils.dataDic.bind("flag",page.flag);
            combox.changeOptions("#flag",page.flag,true);
            // 初始化角色列表
            combox.changeOptions("#roles",combox.cvData(page.roles,"roleName","id"));
            combox.multiple("#roles");
            return true;
        });
    // 注册页面按钮事件
    $("#btn-search").click(searchData);
    $("#exportbtn").click(exportData);
    $("#dataList").delegate("#btn-reset","click",function(){
	var id=$(this).attr("data-id");
	$.secAjax({
            type: 'POST',
            url: "user/reset",
            data: {id:id},
            dataType: 'json',
            success: function (data) {
        	utils.messageBox.alert({ body: data.msg.replace(new RegExp(/\r\n/g), "<br/>") });
                basic.list.flush();
            }
        });
	
    });

});