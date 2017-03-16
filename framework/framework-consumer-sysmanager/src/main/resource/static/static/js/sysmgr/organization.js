var depends = [

    {name: "utils"},
    {name: "basic"},
    {name: "bootstrap-select-cn"},
    {name: "bootstrap-validate"},
    {name: "ztree"},
    {name: "font-awesome", type: "css"},
    {name: "iconfont", type: "css"},
    {name: "framework-public",type:"css"},
    {name:"discreteness",type:"css"},
    {name:"sysmgr.organization",type:"css"},
    {name:"discreteness"}
];
modular.define({name: "sysmgr.organization"}, depends, function () {
    var utils=this.utils;
    var basic = this.basic;
    //设置模块路径
    basic.options.domain = "organization";
    // 设置添加功能参数信息
    basic.add.defaultValue =
    {
        name: "",
        code: "",
        id: "",
        slevel:"1",
        address:"",
        contactPerson:"",
        phone:"",
        orderNum:"",
        isUse:0
    };

    basic.add.dependent.isValidate = basic.edit.dependent.isValidate = function () {
        $("#dataform").data('bootstrapValidator').validate();
        if (!$("#dataform").data('bootstrapValidator').isValid()) {
            return false;
        }
        return true;
    };

    basic.del.sendProp = {"data-id": "id", "data-code": "code"};

    basic.del.dependent.submitBefore = function(data){
        if(data&&data.length>0){
            var nodelIds={};
            $("[data-disable=true]").each(function(){
                nodelIds[($(this).attr("data-id"))]=true;
            });
            for(var i=0;i<data.length;i++){
                if(nodelIds[data[i].id]){
                    utils.messageBox.alert(
                        {
                            body: "不能删除存在下级机构的机构信息！"
                        });
                    return false;
                }
            }
        }
       return true;
    };

    basic.edit.dependent.setVal=function(data){
        $.hform.val($("#dataform [name]"), $.extend(data.organization,
            {
                pname: data.parentName
            }));
        return this;
    };

    basic.list.flushing = function () {
        loadData();
    };

    basic.check.callback.changeAfter=function(values){
        if(!values["data-id"]){
            $(".block-square").removeClass("block-square-active");
        };
        _.map(values,function(v,k){
           if(k=="data-id"){
               v=','+v+',';
               $(".block-square").each(function() {
                   if(v.indexOf(","+$(this).attr("data-id")+",")!=-1){
                       $(this).removeClass("block-square-active");
                       $(this).addClass("block-square-active");
                   }else{
                       $(this).addClass("block-square-active");
                       $(this).removeClass("block-square-active");
                   }
               });
           }
        });
    };
    
    basic.add.dependent.showAfter = basic.edit.dependent.showAfter = function (options) {
        $("#dataform").data('bootstrapValidator').resetForm();
    };

    //加载基本组件-增删改功能
    basic.init.load();

    $('#dataform').bootstrapValidator({
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
        	name: {
                message: '机构名称无效',
                validators: {
                    notEmpty: {
                        message: '机构名称为必填项,不能为空'
                    }
                }
            }
        }
    });

    var selectNodeid = "";

    function loadData() {
        $.secAjax(
            {
                type: 'POST',
                url: "organization/list",
                data: {},
                dataType: 'json',

                success: function (data) {
                    var nodeClickFun = function (event, treeId, treeNode) {
                        selectNodeid = treeNode.id;
                        // 修改列表的标题
                        if (treeNode.code == "R") {
                            $("#list_title").html("");
                        } else {
                            $("#list_title").html(treeNode.name + "的下级");
                        }
                        var dataListTemplate = Handlebars.compile($("#template-tbody").html());
                        $("input[name=pname]").val(treeNode.name);
                        $("input[name=pcode]").val(treeNode.code);
                        $('#dataList').html(dataListTemplate(treeNode.children));

                        //初始化顯示
                        showList();
                    };
                    var setting =
                    {
                        callback: {
                            onClick: nodeClickFun
                        }
                    };
                    // 初始化树
                    var zTree = $.fn.zTree.init($("#organization"), setting, data);
                    var rootNode = zTree.getNodeByParam("level", 0);
                    if (selectNodeid) {
                        var node = zTree.getNodeByParam("id", selectNodeid);
                        zTree.selectNode(node);
                        nodeClickFun(null, null, node);
                    } else {
                        nodeClickFun(null, null, rootNode);
                    }

                }





            });
    }

// 初始化参数及用户数据、机构数据加载，模版函数定义等。
    loadData();
    //方块选中与取消

    // 初始化标签悬浮按钮拖拽效果
    var drage=utils.drag();
    console.log(drage);
   // var drage=new Drage();
    var oBox = document.getElementById("open-close");
	var oBar = document.getElementById("open-close");
	drage.startDrag(oBar, oBox);

    $(".table-view").on("click",function(){
        showTable();
    });
    $(".list-view").on("click",function(){
        showList();
    });

    function showTable(){
        $(".table-view").addClass("selected-view-active");
        $(".list-view").removeClass("selected-view-active");
        $("#data-view-table").show();
        $("#data-view-block").hide();
    }

    function showList(){
        $(".list-view").addClass("selected-view-active");
        $(".table-view").removeClass("selected-view-active");
        $("#data-view-table").hide();
        $("#data-view-block").show();
    }

    $(".open-close").on("click",function(){
        if( $(".leftBarBox").width()>2){
            $(".leftBarBox").css({"width":"0px"});
        }else{
            $(".leftBarBox").css({"width":"250px"});
        }

    });
    
//    $("#btn-search").click(function(){
//    	var data={};
//            if ($("#searchtext").val())
//                data= $.hform.cvMap("conditions",
//                    {
//                        'nameLIKE or codeLIKE': $("#searchtext").val()
//                    });
//            loadData(data);
//    });    

});

