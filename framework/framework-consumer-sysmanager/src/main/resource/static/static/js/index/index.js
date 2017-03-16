var depends = [
    {name: "utils"},
    {name: "common.rsa"},
    {name: "bootstrap-validate"},
    {name: "AdminLTE"},
    {name: "index", type: "css"},
    {name: "ionicons", type: "css"},
    {name: "font-awesome", type: "css"},
    {name: "tools", type: "css"},
    {name: "skin-blue", type: "css"},
    {name:"bsIE"},
    {name:"hjkIcon",type:"css"},
    {name:"tabs"},
    {name: "news"},
    {name:"hjkbase",type:"css"},
    {name:"hjkplug",type:"css"}
];
modular.define({name: "index.index"}, depends, function () {
    var rsa=this['common.rsa'];
    var utils=this.utils;
    var horizontalTabsHeight=0;
    
    
    
    var tabsCtrl=this.tabs;
    var news=this.news;
    news.subscribe({channel:"test",recvMsg:function(data){
	console.log(data);
    }});
    news.register({name:"indextest",control:function(data){
	alert(JSON.stringify(data));
    }});
    //配置固定在右上教的标签控制器
    var verticalTabsCtrl=tabsCtrl.createBaseTabsCtroller();
    	verticalTabsCtrl.animate.toWhere="#floatBtn";
	    verticalTabsCtrl.tab.containerid="#verticalMenu";
	    verticalTabsCtrl.tab.tabHtml='<li class="user-body  tabClass"><a href="javascript:void(0);"  class="iframeTag"><i class="fa icon"></i><span class="title"></span></a><span class="glyphicon glyphicon-remove remove"></span></li>'
    
    //配置横向的标签控制器
    var horizontalTabsCtrl=tabsCtrl.createHorizontalCtroller();
	    horizontalTabsCtrl.tab.tabActiveClass="tab-active";
	    horizontalTabsCtrl.tab.containerid="#horizontalMenu";
	    horizontalTabsCtrl.tab.tabHtml='<li><div class="c-input-group"><em class="fullparent title"></em><div class="addon"><button class="remove"><span class="icon-cha"></span></button></div></div></li>';
    
    //设置默认控制器
    tabsCtrl.setDefaultTabsCtrl(verticalTabsCtrl);

    var switchTabsView=function(){    	
    
    	if(tabsCtrl.getTabsCtrl()==verticalTabsCtrl){    		
			
			$(".horizontalTab").fadeIn(); //切换没有数据过来
			tabsCtrl.transferTabs(verticalTabsCtrl,horizontalTabsCtrl);
			$("#verticalTabsBtn").fadeOut();
    	}else if(tabsCtrl.getTabsCtrl()==horizontalTabsCtrl){    		
			
			$("#verticalTabsBtn").fadeIn();  
			tabsCtrl.transferTabs(horizontalTabsCtrl,verticalTabsCtrl);
			$(".horizontalTab").fadeOut();
    	}
		
    }

    $("body").delegate(".btn-modifyTabsStyle","click",function(){ 
    	switchTabsView();
    });
    
    
    
    $(function () {
        $.secAjax(
            {
                type: 'POST',
                url: "index/loadResource",
                data: "resourceName=访问系统/",
                dataType: 'json',
                success: function (ssto) {
                	var data=ssto.data;
                    // 预处理节点数据的properties属性，将properties的属性复制到this上。
                    ProcessingData(data.resource);
                    // 初始化菜单
                    initMenu(data.resource);
                    // 初始化左侧菜单样式
                    initStyle();
                    // 初始化用户信息
                    initUserInfo(data.user);
                    // 初始化修改密码的校验规则
                    initValidate(data.pwdValidate);
                    // 打开首页
                    tabsCtrl.open(
                	    {
                		id: 'index',
                		name: "首页",
                		url: "html/index/welcome.html",
                		close: false
                	    });
                    delete data.user;
                    delete data.resource;
                    $("body").attr(data)
                }
            });
        // 注册窗口大小变化事件
        windowResize();
        $(function(){
        	windowResize();
        });
        

        // 初始化标签悬浮按钮拖拽效果
        /*
        var draggie = new Draggabilly('#floatMenuBox', {
            handle: '.floatBtn',
            containment: '.content-wrapper'
        });
        */
        /*
        var drage=utils.drag();
       // var drage=new Drage();
        var oBox = document.getElementById("floatMenuBox");
    	var oBar = document.getElementById("floatBtn");
    	drage.startDrag(oBar, oBox);
    	*/

    });
// 初始化修改密码的校验规则
    function initValidate(pwdValidate) {
        regexpValue =
        {
            regexp: /.*/,
            message: ''
        }
        if (pwdValidate) {
            pwdValidate = eval("({" + pwdValidate + "})")
            if (pwdValidate.regexp) {
                regexpValue.regexp = new RegExp(pwdValidate.regexp);
                regexpValue.message = pwdValidate.message;
            }
        }
        $('#dataform').bootstrapValidator(
            {
                message: '此值无效',
                group: ".form-group-input",
                excluded: [
                    ':disabled'
                ],
                submitButtons: '#submit',
                feedbackIcons: {
                    valid: 'glyphicon glyphicon-ok',
                    invalid: 'glyphicon glyphicon-remove',
                    validating: 'glyphicon glyphicon-refresh'
                },
                fields: {
                    oldpwd: {
                        oldpwd: '原始密码无效',
                        validators: {
                            notEmpty: {
                                message: '原密码不能为空'
                            }
                        }
                    },
                    newpwd: {
                        validators: {
                            notEmpty: {
                                message: '新密码不能为空'
                            },
                            regexp: regexpValue
                        }
                    },
                    newpwd2: {
                        validators: {
                            notEmpty: {
                                message: '确认密码不能为空'
                            },
                            identical: {
                                field: 'newpwd',
                                message: '两次输入的密码不一致，请确认'
                            }
                        }
                    }

                }
            });
    }

// 初始化用户信息
    function initUserInfo(user, pwdValidate) {
        $("#userName").text(user.userName);
    }

// 注册窗口大小变化事件,并触发事件
    function windowResize() {
    	
        $(window).resize(
            function () {
                var contentHeight = $(self).height() - 50 - 30 - 51;// 30间距，为了减少运算就不再获取 51为 底部栏的高度   $(".content-wrapper").offset().top=67
                $("#mainFrame").height(contentHeight);
                $(".content-wrapper").css({"min-height":(contentHeight+30+"px")});
                //$(".main-sidebar").css({"height":($(self).height() - 50)});
                //$(".main-sidebar").css({"overflow":"hidden"});
                
            }).resize();
            
    }
 

// 处理数据
    function ProcessingData(data) {
        var saxprop = function () {
            var _this = this;
            //只处理 根0  应用1  导航条2   菜单3
            if (_this.resourceType !='Menu'&&_this.resourceType !='Application'&&_this.resourceType !='Navigation'&&_this.resourceType !='Root'  ) {
                return;
            }
            if (_this.properties) {
                var o = eval("({" + _this.properties + "})")
                _.map(o, function (val, key) {
                    _this[key] = val;
                })
            }
            if (_this.sons && _this.sons.length > 0) {
                _.map(_this.sons, function (val) {
                    saxprop.apply(val);
                })
            }
            if (!_this["id"]) _this["id"] = _this["tabid"];
            delete _this.tabid;
            _this.json = JSON.stringify(
                {
                    id: _this.id,
                    name: _this.name,
                    icon: _this.icon,
                    url: _this.url
                });
        };
        saxprop.apply(data);
    }

// 初始化菜单
    function initMenu(data) {
        // 根据模版生成html内容
        var source = $("#leftTree").html();
        var template = Handlebars.compile(source);
        var html = template(data.sons);
        // 将html内容插入指定位置
        $("#leftTreeContainer").append(html);
        // 为二级菜单注册事件
        $(".leftMenu").click(function (event) {
            var options = eval("(" + $(this).attr("data-options") + ")");
            options.event=event;
            tabsCtrl.open(options);
        });
    }

// 初始化样式
    function initStyle() {// 左边一级列表被选中样式变化
        $(".sidebar-menu > li a").click(function () {
            $(".sidebar-menu > li a").removeClass("left-sidebar-level1-active");
            $(this).addClass("left-sidebar-level1-active");
            // 清除所有二级活动样式
            $(".sidebar .sidebar-menu li ul li a").removeClass("left-sidebar-level2-active");
            // 收起除去自己父级以外的所有二级目录
            $(this).parent().prevAll().find("ul").slideUp();
            $(this).parent().nextAll().find("ul").slideUp();

        });
        // 左边二级列表被选中样式变化
        $(".sidebar .sidebar-menu li ul li a").click(function () {
            $(".sidebar .sidebar-menu li ul li a").removeClass("left-sidebar-level2-active");
            $(this).addClass("left-sidebar-level2-active");
        });
    }

// 显示修改密码窗口
    function modifyPwd() {
        $("#datamodal").modal("show");
        $("#datamodal input[type=password]").val();
        $("#submit").bind("click.submit", submitPwd);
        $("#dataform").bootstrapValidator("resetForm", true)
        $("#submit").attr("disabled", "disabled");

    }

// 提交密码信息，执行修改密码操作
    function submitPwd() {
        if (!$("#dataform").data('bootstrapValidator').isValid()) {
            $("#dataform").data('bootstrapValidator').validate();
            return;
        }
        $.secAjax(
            {
                type: 'POST',
                url: "index/modifyPassword",
                data: {
                    oldpwd: rsa.encrypt($('#oldpwd').val()),
                    newpwd: rsa.encrypt($('#newpwd').val())
                },
                dataType: 'json',
                success: function (data) {
                    if (data.state) {
                        utils.alert(
                            {
                                body: "修改密码成功，下次登录请使用新密码！"
                            })
                    } else {
                        $("#dataform").bootstrapValidator('disableSubmitButtons', false);
                        $("#submit").unbind("click.submit");
                        $("#submit").bind("click.submit", submitPwd);
                    }
                }
            });
        return true;
    }

    // 用户注销
    function loginout() {
	location.href = $.urlReWrite("index/loginOut");
	location.href = "../../";
    }
    $("#btn-modifypwd").click(modifyPwd);
    $("#btn-loginout").click(loginout);

});


