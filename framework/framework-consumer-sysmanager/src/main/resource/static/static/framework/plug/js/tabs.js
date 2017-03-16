/**
 * Created by hjk on 2016/11/8.
 */
//多选支持需要引入的依赖
var depends = [
    {name: "jquery"},
    {name: "tabs",type:"css"},
    {name: "utils"}
];
modular.define({name: "tabs"}, depends, function () {
	var utils=this.utils;
	var self = modular.topModular().find("tabs");
    if(self==void 0)
	self={};
    else
	return self;
	//基础类
    var TabController=function(){
        var maxZIndex=1000;
        var tabController={
        		animate:{
                	toWhere:"body",
                	play:function(e,icon){
                    	if(!e)return;
                        var div=document.createElement("div");
                        div.style.zIndex="100000";
                        div.style.padding="6px";
                        var i=document.createElement("i");
                        i.style.fontSize="18px"
                        div.appendChild(i);
                        $(i).addClass("fa fa-file-text-o");

                        $(div).addClass("moveTag");
                        $(div).css({"left": (e.clientX-15),"top": (e.clientY-15)});
                        $("body").append(div);

                        var X=$(this.toWhere).offset().left+8+3;
                        var Y=$(this.toWhere).offset().top+8;
                        
                        //e.clientX=X;
                		//e.clientY=Y;
                		
                        $(div).animate({"left":X,"top":Y},1000,"swing",function(){
                            $(this).fadeOut(200,function(){
                                $(this).remove()
                            });

                        }); 
                        

                    }
                },	
            configure:{
                animateFunction:function(){},
                //state == created 表示新创建并且成功的，==isExists 表示已经存在
                openCallback:function(options){
//                	if(state=="created"){
//                		tabController.animate.play(options.event,options.icon);
//                	}
                	
                },
                closeCallback:function(id){
                	
                }

            },
            init:function(){
            	//在这里可以处理一些控制器初始化操作
            },
            getTabsData:function(){
            	return this.tab.tabsData;
            },
            setTabsData:function(arr){
            	this.tab.tabsData=arr;
            },
            getIframeZIndex:function(){
            	return maxZIndex;
            },
            setIframeZIndex:function(zIndex){
            	maxZIndex=zIndex;
            },
            open:function(options,refresh){
                var tab;
                var iframe;
                tab=this.tab.create(options);
                if(!tab){
                    return;
                }
                iframe=this.iframe.create(options.id,options.url,refresh);
                if(!iframe){
                    return;
                }
                //绑定关闭点击事件
                tab.find(".remove").click(function(){

                    //状态转移
                    if(tab.hasClass(tabController.tab.tabActiveClass)){
                        var currentIndex=$(tab[0]).index();

                        if(currentIndex>0){
                            currentIndex--;
                        }

                        var _tab=$(tab.siblings()[currentIndex]);

                        tabController.tab.select(_tab);
                    }
                    //移除将被关闭的选项卡
                    tabController.close(options.id)
                    //tabController.tab.remove(tab);
                    //tabController.iframe.remove(iframe);


                });
                //绑定点击事件
                tab.click(function(){
                	tabController.active(tab,iframe);
                    /*if(tab){
                        tabController.tab.select(tab);
                        tabController.iframe.moveToTop(iframe)
                    }*/
                });
                //设置被选中状态
                tabController.active(tab,iframe);
                //tabController.tab.select(tab);

                this.configure.openCallback(options);


            },
            active:function(tab,iframe){
            	if(tab){
                    tabController.tab.select(tab);
                    tabController.iframe.moveToTop(iframe)
                }
            },
            close:function(id){
                this.tab.remove(id);
                this.iframe.remove(id);
                this.configure.closeCallback(id);
            },
            tab:{
                /**
                 *tab html 遵循如下格式
                 * 容器（containerid）、子标签（图标（icon）、标题（title）、 关闭按钮（close））
                 * **/
                containerid:"#verticalMenu",
                extraContainer:"#tab-more-container",//定义额外的辅助容器
                tabHtml:'<li><div class="c-input-group"><em class="fullparent title"></em><div class="addon"><button class="remove"><span class="icon-cha"></span></button></div></div></li>',
                tabActiveClass:"active",
                tabsData:[],
                removed:function(){

                },
                selected:function(objct){

                },
                //选项卡被选中
                select:function(objct){
                    $(objct).siblings().removeClass(this.tabActiveClass);
                    $(objct).addClass(this.tabActiveClass);
                    this.selected(objct)
                },
                //创建
                create:function(options){
                    var _id;
                    if(!options.id){
                        return;
                    }
                    _id=options.id.replace(".","").replace("#","");
                    if(!this.isExists(_id)){
                        //标签不存在
                        var tab=$(this.tabHtml);
                        //添加id
                        tab.attr("id",_id);
                        //关闭按钮附加id数据
                        tab.find(".remove").attr("data-id",_id);
                        //设置标题
                        tab.find(".title").text(options.name);
                        tab.find(".title").attr("title",options.name);
                        //设置图标
                        tab.find(".icon").addClass(options.icon);
                        //绑定点击事件
                        tab.find(".remove").click(function(){
                            //tab.remove();
                        })
                        $(this.containerid).append(tab);
                        
                        if(options.close==false){
                        	tab.find(".remove").remove();
                        }

                        this.tabsData.push(options);
                        
                        tabController.animate.play(options.event,options.icon);
                        
                        return tab;
                    }else{
                        //标签存在时
                        console.log("子标签已经存在"+$("#"+_id));                
                        return $("#"+_id);
                    }

                },
                //移除
                remove:function(target){
                    if(typeof(target)=="object"){
                    	//移除元素
                        target.remove();
                        //移除堆栈数据
                        var id=$(target).attr("id");
                        for(var i=0;i<this.tabsData.length;i++){
                        	if(this.tabsData[i].id==id){
                        		this.tabsData.splice(i,1)
                        	}
                        }
                        this.removed();
                    }else if(typeof(target)=="string"){
                        var _target=target.replace(".","").replace("#","");
                        $("#"+_target).remove();
                      //移除堆栈数据
                        var id=_target;
                        for(var i=0;i<this.tabsData.length;i++){
                        	if(this.tabsData[i].id==id){
                        		this.tabsData.splice(i,1)
                        	}
                        }
                        
                        this.removed(target);
                    }


                },
                //判断是否存在
                isExists:function(id){
                    var _id=id.replace(".","").replace("#","");
                    //判断内部容器是否存在
                    var isExists_1=$(this.containerid).find("#"+_id).length>0?true:false;
                    //判断外部容器是否存在
                    var isExists_2=$(this.extraContainer).find("#"+_id).length>0?true:false;

                    if(isExists_1||isExists_2){
                        return true;
                    }else{
                        return false;
                    }
                }
            },
            iframe:{
                /**
                 *iframe 遵循如下结构
                 * 容器（containerid）、子元素（data-id='id'）
                 * 传入的id中包含的.或者#都会被去掉
                 * **/
                containerid:"#mainFrame",
                iframeHtml:"<iframe></iframe>",
                style:{
                	width : "100%",
                    height : "100%",
                    position : "absolute",
                    left : "0px",
                    right : "0px",
                    top : "0px",
                    bottom : "0px",
                    background:"#f5f5f5"

                },
                //创建
                create:function(id,url,refresh){
                    if(!id)return;
                    var _id=id.replace(".","").replace("#","")
                    if(!this.isExists(_id)){
                        //iframe不存在时
                        var iframe=$(this.iframeHtml);
                        iframe.css(this.style);
                        iframe.attr("data-id",_id);
                        //好健康特殊处理url
                        var _url=utils.common.url.reHtml(url);
                        iframe.attr("src",_url);
                        maxZIndex++;
                        iframe.css({"z-index":maxZIndex});
                        $(this.containerid).append(iframe);

                        return iframe;
                    }else{
                        if(refresh){
                            this.refresh(id)
                        }
                        return $(this.containerid).find("iframe[data-id='"+_id+"']");
                        //iframe存在时
                        console.log("iframe已经存在");
                    }

                },
                //移除
                remove:function(target){
                    if(typeof(target)=="object"){
                        target.remove();
                    }else if(typeof(target)=="string"){
                        var _target=target.replace(".","").replace("#","");
                        $("iframe[data-id='"+_target+"']").remove();
                    }
                },
                //刷新
                refresh:function(id){
                    var _id=id.replace(".","").replace("#","")
                    var iframe=$("iframe[data-id='"+_id+"']");
                    iframe.attr("src",iframe.attr("src"))
                },
                //移到最上层
                moveToTop:function(target){
                    maxZIndex++;
                    if(typeof(target)=="object"){
                        target.css({"z-index":maxZIndex});
                    }else if(typeof(target)=="string"){
                        var _target=target.replace(".","").replace("#","");
                        $("#"+_target).css({"z-index":maxZIndex});
                    }
                },
                //判断是否存在
                isExists:function(id){
                    var _id=id.replace(".","").replace("#","")
                    return $(this.containerid).find("iframe[data-id='"+_id+"']").length>0?true:false;
                }
            }
        }
        return tabController;
    }
    
    
  //扩展类
    var HorizontalTab=function() {


        var tabController = new TabController();
        tabController.configure.openCallback = function (options) {
            tabController.more.initContainer(options.id);
        };
        tabController.animate.play=function(e,icon){};
        tabController.tab.selected = function (obj) {
            tabController.more.out(obj)
            $(obj).siblings().removeClass(tabController.tab.tabActiveClass);
            $(obj).addClass(this.tabActiveClass);
            tabController.more.initContainer();
        }
        tabController.configure.closeCallback = function (id) {
            tabController.more.initContainer();
        }
        
        tabController.init=function(){
            this.more.initContainer();
        }

        tabController.more = {
            moreSelector: "#tab-more-btn",
            toInt: function (s) {
                if (!s)return 0;
                var _s = s.replace("px", "");
                var _i = parseInt(_s);
                if (_i > 0) {
                    return _i;
                } else {
                    return 0;
                }
            },
            onClick: function () {

                var moreBtn = $(this.moreSelector);
                //计算额外容器的位置
                /*
	                var _right = moreBtn.offset().right;
	                var _top = moreBtn.offset().top + moreBtn.height() + 8;
	                $(tabController.tab.extraContainer).css({"right": _right, "top": _top});
                */
                $(tabController.tab.extraContainer).toggle();


            },            
            initContainer: function (target) {
                var _target;

                if (target) {
                    //取得激活状态的对象
                    if (typeof(target) == "object") {
                        _target = target
                    } else if (typeof(target) == "string") {
                        _target = target.replace(".", "").replace("#", "");
                        _target = $("#" + _target);

                    }
                } else {
                    _target = $(tabController.tab.containerid).find("." + tabController.tab.tabActiveClass);
                }


                //取得标签容器宽度
                var tabContainerWidth = $(tabController.tab.containerid).width();
                //获取子元素宽度
                var tabWidth = _target.width();


                //容器实际可用宽度
                var containerPaddingLeft = this.toInt($(tabController.tab.containerid).css("padding-left"));
                var containerPaddingRight = this.toInt($(tabController.tab.containerid).css("padding-right"));

                var containerActualWidth = tabContainerWidth - containerPaddingLeft - containerPaddingRight;

                //标签实际占用宽度
                var tabMarginLeft = this.toInt(_target.css("margin-left"));
                ;
                var tabMarginRight = this.toInt(_target.css("margin-right"));
                var tabPaddingLeft = this.toInt(_target.css("padding-left"));
                var tabPaddingRight = this.toInt(_target.css("padding-right"));

                var tabActualWidth = tabWidth + tabMarginLeft + tabMarginRight + tabPaddingLeft + tabPaddingRight + 1;//1为偏差

                //应该显示多少个元素
                var behooveShowCount = parseInt(containerActualWidth / tabActualWidth);
                //实际有多少个
                var actual = _target.siblings().length + 1;

                //console.log("容器宽度："+tabContainerWidth+"子元素宽度"+“”+);
                if (actual > behooveShowCount) {
                    var children = _target.siblings();
                    for (var i = behooveShowCount - 1; i < children.length; i++) {
                        this.insert(children[i]);
                        //每成功放入一个标签到副标签容器，主标签容器实际个数-1
                        actual--;
                    }
                } else {
                    var children = $(tabController.tab.extraContainer).children();
                    var shouldAddCount = behooveShowCount - actual;
                    for (var i = children.length; i >= children.length - shouldAddCount; i--) {
                        this.out(children[i]);
                    }
                }

                //判断是否显示 more 按钮和更多容器
                var allChildren = $(tabController.tab.containerid).children().length + $(tabController.tab.extraContainer).children().length;
                //如果总个数等于外部容器内标签个数表示标签已经全部显示到外部容器了
                if (allChildren <= $(tabController.tab.containerid).children().length) {
                    $(this.moreSelector).hide()
                } else {
                    $(this.moreSelector).show()
                }

            },
            //移入
            insert: function (obj) {
                $(obj).appendTo(tabController.tab.extraContainer);
            },
            //移出
            out: function (obj) {
                //判断该元素是否在外部标签容器中
                if ($(obj).parents(tabController.tab.containerid).length <= 0) {
                    $(obj).appendTo(tabController.tab.containerid);
                }
            },
            init: function () {
                $(window).resize(function () {
                    tabController.more.initContainer();
                });
                $("body").delegate(this.moreSelector, "click", function () {
                    tabController.more.onClick()
                })
            }

        }

        tabController.more.init();
        return tabController;
    }
    
    
    var defaultTabsCtrl;
 
    
//以上为页面代码
    var self={
            createBaseTabsCtroller:function(){
            	return new TabController();
            },
            createHorizontalCtroller:function(){
            	return new HorizontalTab();
            },
            open:function(data,tabsCtrl,refresh){
            	this.getTabsCtrl(tabsCtrl).open(data,refresh);
            },
            close:function(id,tabsCtrl){
            	this.getTabsCtrl(tabsCtrl).close(id);
            },
            setDefaultTabsCtrl:function(tabsCtrl){
            	defaultTabsCtrl=tabsCtrl;
            },
            getDefaultTabsCtrl:function(){
            	return defaultTabsCtrl;
            },
            //无参数时候回返回当前使用的控制器 有参数时候返回参数代表的控制器
            getTabsCtrl:function(tabsCtrl){
            	//如果没有传递控制器并且默认的控制器为空
            	if(tabsCtrl){
            		
            		if(typeof(tabsCtrl)=="string"){
            			return eval(tabsCtrl)
            		}else if(typeof(tabsCtrl)=="object"){
            			return tabsCtrl
            		}
            		
            	}else{
            		
            		if(!defaultTabsCtrl){
                		defaultTabsCtrl=new TabController();
                	}
            		
            		return defaultTabsCtrl;
            		
            	} 
            },
            //切换容器
            transferTabs:function(sourceTabsCtlr,targetTabsCtlr){
            	
            	//清空原有的标签数据
            	$(sourceTabsCtlr.tab.containerid).empty();
            	//除去原有容器于现在容器重复的标签标签
            	var srcData=sourceTabsCtlr.getTabsData();
            	var tData=targetTabsCtlr.getTabsData();
            	
            	for(var i=0;i<srcData.length;i++){
            		for(var j=0;j<tData.length;j++){
            			if(srcData[i].id==tData[j].id){
            				//删除重复数据
            				srcData.splice(i,1);
            				//因为删除了一个元素 所以索引要后退一个元素
            				i--;
            			}
            		}
            	}
            	//追加到目标控制器标签
            	targetTabsCtlr.setTabsData(tData.concat(srcData));
            	//清空原有标签
            	sourceTabsCtlr.setTabsData([]);
            	//设置iframe最高层次（目前暂时不支持多个控制器控制同一个iframe容器中的iframe，这样会出现iframe的层次不可控问题）
            	targetTabsCtlr.setIframeZIndex(sourceTabsCtlr.getIframeZIndex());
            	//初始化标签
            	for(var i=0;i<targetTabsCtlr.getTabsData().length;i++){
            		targetTabsCtlr.open(targetTabsCtlr.getTabsData()[i]);
        		}
            	//初始化控制器
            	targetTabsCtlr.init();
            	defaultTabsCtrl=targetTabsCtlr;


            }
        }

        return self;
	
});

