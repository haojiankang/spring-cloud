var depend = [
    {name: "utils"},
    {name:"tools",type:"css"}
];
var m;
modular.define({name: "TabController"}, depend, function () {
    var utils=this.utils;
    var self = modular.topModular().find("TabController");
    if(self==void 0)
	self={};
    else
	return self;
    var addTagAnimate=function(e,icon){
    	if(!e)return;
        var div=document.createElement("div");
        div.style.zIndex="100000";
        div.style.padding="6px";
        var i=document.createElement("i");
        i.style.fontSize="18px"
        div.appendChild(i);
        $(i).addClass("fa fa-file-text-o");

        $(div).addClass("moveTag");
        $(div).css({"left": (e.pageX-15),"top": (e.pageY-15)});
        $("body").append(div);

        var floatBtnX=$("#floatBtn").offset().left;
        var floatBtnY=$("#floatBtn").offset().top;
        $(div).animate({"left":(floatBtnX+8+3),"top":(floatBtnY+8)},1000,"swing",function(){
            $(this).fadeOut(200,function(){
                $(this).remove()
            });

        });
    }
    
    self.tabControl =
    {
        options :
        {
            tab :
            {
                activeClass : "active"
            },
            template :
            {
            	tab:'<li data-id="{{id}}" class="user-body  {{className}}">'
					+'<a href="javascript:void(0);" data-title="资源管理" class="iframeTag">'
					+'<i class="fa {{icon}}"></i>'
					+'<span>{{name}}</span>'					
					+'</a>'
					+'<span class="{{close}}"></span>'
					+'</li>',
			
            	tab1:'<li data-id="{{id}}" class="user-body {{className}}">'
            			+'<a href="javascript:void(0);" data-title="资源管理" class="iframeTag">'
            			+'<i class="fa {{icon}}"></i>'
            			+'<span class="title">{{name}}</span>'
            			+'</a>'
            			+'<span class="{{close}}"></span>'
            			+'</li>',
            		
                tab_old : '<li data-id="{{id}}" class="active {{className}}">'
		                + '<a href="javascript:void(0)"  data-title="资源管理" class="iframeTag">'
		                + '<i class="fa {{icon}}"></i>' + '<span class="title">{{name}}</span>'
		                + '</a><span class="{{close}}">' + '</span>' + '</li> '
            },
            selector :
            {
                tab : "#rightFloatMenu",
                frame : "#mainFrame"
            },
            frame :
            {
                style :
                {
                    width : "100%",
                    height : "100%",
                    position : "absolute",
                    left : "0px",
                    right : "0px",
                    top : "0px",
                    bottom : "0px"
                }
            },           
            callback:{
                open:function(options){
                	if(options.isOpen){
                		addTagAnimate(options.event,"")
                	}                	
                },
                close:function(options){},
            }
        },
        
        // 初始化方法，传入标签页的容器选择器，iframe的容器选择器
        init : function(options) {
            if (options) this.options = $.extend(this.options, options);
        },
        // 打开一个标签页, id,name,url,backfunction
        open : function(options) {
            var _this = this;
            utils.common.url.reHtml(options);
            // 没有找到标签，新建一个标签
            if (!_this.tab.isExists(options)) {
            	options.isOpen=true;//如果执行了打开就 为true否则为flase
                _this.tab.init(options);
            }else{
            	options.isOpen=false;//如果执行了打开就 为true否则为flase
            }
            
            _this.tab.show(options);
            _this.options.callback.open(options);
        },
        // 关闭一个标签页
        close : function(options) {
            var _this = this;
            _this.tab.remove(options);
            _this.options.callback.close(options);
        },
        // 标签对象
        tab :
        {
            selector : "[data-id='{id}']",
            className : ".tabClass",
            init : function(options) {
                var _this = this;
                var template = Handlebars.compile(self.tabControl.options.template.tab);
                if (!options.close) options.close = "glyphicon glyphicon-remove remove";
                else {
                    options.close = "";
                }
                options = $.extend(options,
                    {
                        className : this.className.replace(".", "")
                    });
                var html = template(options);
                $(self.tabControl.options.selector.tab).append(html);
                _this.select(options).click(function() {
                    _this.show(options);
                }).delegate("span.remove", "click", function(event) {
                    _this.remove(options);
                    event.stopPropagation();
                });
                self.tabControl.frame.init(options);
            },
            show : function(options) {
                var activeClass = self.tabControl.options.tab.activeClass;
                $(self.tabControl.options.selector.tab + " ." + activeClass).removeClass(activeClass);
                var tab = this.select(options).show().addClass(activeClass);
                self.tabControl.frame.show(options);
            },
            hide : function(options) {
                this.select(options).hide();
                self.tabControl.frame.hide(options);
            },
            remove : function(options) {
                this.destory(options);
                self.tabControl.frame.destroy(options);
                $(self.tabControl.options.selector.tab + " " + this.className + ":first").click();
            },
            destory : function(options) {
                this.select(options).remove();
            },
            // 判断标签页是否存在
            isExists : function(options) {
                return this.select(options).length != 0;
            },
            select : function(options) {
                return $(this.className + this.selector.replace("{id}", options.id));
            }
        },
        // iframe窗口对象，用于创建iframe
        frame :
        {
            selector : "[data-id='{id}']",
            className : ".iframeClass",
            init : function(options) {
                var iframe = $("<iframe data-id=\"" + options.id + "\"  frameBorder='0' scrolling='0' marginwidth='0' marginheight='0'></iframe>");
                iframe.css(self.tabControl.options.frame.style);
                iframe.attr("src", options.url);
                iframe.addClass(this.className.replace(".", ""));
                $(self.tabControl.options.selector.frame).append(iframe);
            },
            show : function(options) {
                $(this.className).hide();
                this.select(options).show();
            },
            hide : function(options) {
                this.select(options).hide();
            },
            destroy : function(options) {
                this.select(options).remove();
            },
            select : function(options) {
                return $(this.className + this.selector.replace("{id}", options.id));
            }
        },
        uiAction:function (e,icon) {
            var div = document.createElement("div");
            	div.style.zIndex="100000"
            var i = document.createElement("i");
            div.appendChild(i);
            $(i).addClass("fa fa-file-text-o");

            $(div).addClass("moveTag");
            $(div).css({"left": (e.pageX - 15), "top": (e.pageY - 15)});
            $("body").append(div);

            var floatBtnX = $("#floatBtn").offset().left;
            var floatBtnY = $("#floatBtn").offset().top;
            $(div).animate({"left": (floatBtnX + 8), "top": (floatBtnY + 8)}, 500, function () {
                $(this).fadeOut(200, function () {
                    $(this).remove()
                });

            });
        }
        

    }
    self.openTab=function(options){
        this.tabControl.open(options);
    }
    return self;


});

