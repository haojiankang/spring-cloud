//多选支持需要引入的依赖
var depends = [
    {name: "jquery"},
    {name:"bootstrap"},
    {name:"hjkplug",type:"css"},
    {name:"bootstrap",type:"css"},
    {name:"font-awesome",type:"css"},
    {name:"spin"}
];
modular.define({name: "hjkplug"}, depends, function () {
    /**
     * Created by hjk on 2016/8/16.
     */
        //跳动的加载等待
    var Loading_Beat = function () {
        var bar;
        var timer;
        var sp = 0;
        var bg;
        var loadingBox,progressbar,progressWord;
        var loading = {
            showGraphical:true,
            showProgress:false,

            create:function(){
                loadingBox=document.createElement("div");
                progressbar=document.createElement("ul");
                progressWord=document.createElement("div");

                loadingBox.appendChild(progressbar);
                loadingBox.appendChild(progressWord);

                loadingBox.setAttribute("id","loading-horizontal-container");
                progressbar.setAttribute("id","progressbar");
                progressWord.setAttribute("id","progressWord");

                if (this.showGraphical) {
                    progressbar.style.display = "block";
                }
                if (this.showProgress) {
                    progressWord.style.display = "block";
                }

                for(var i=0;i<7;i++){
                    var li=document.createElement("li");
                    progressbar.appendChild(li);
                }

                var windowHeight = document.documentElement.clientHeight;
                var _top = (windowHeight - 70) / 2;

                loadingBox.style.top = _top + "px";

                //创建背景
                bg = document.createElement("div");
                bg.setAttribute("class","module-bg");

                bar =progressbar.childNodes;

            },
            init: function (showGraphical, showProgress) {
                //创建元素
                this.showGraphical=showGraphical;
                this.showProgress=showProgress;
            },
            show: function () {

                if(loadingBox==null){
                    this.create();
                }

                timer = setInterval(this.update, 100);

                document.body.appendChild(bg);
                document.body.appendChild(loadingBox);

            },
            hide: function () {
                clearInterval(timer)
                if (bg != null) {
                    bg.remove();
                }
                loadingBox.remove();


            },
            progress: function (n) {
                progressWord.innerHTML = n + "%";
            },

            update:function() {
                var setHeight=function(obj, h, t) {
                    obj.style.height = h + "px";
                    obj.style.top = t + "px";
                }
                for (var i = 0; i < bar.length; i++) {
                    var h = ((sp + i) % 4 + 1) * 5
                    var t = (25 - h) / 2;
                    setHeight(bar[i], h, t);
                }
                sp++;

            }



        }
        return loading;
    }
    // 进度条加载等待
    var Loading_Default=function(){

        var loading_box;
        var progress_word;
        var progress_box;
        var progress_bar;
        var full_bg;
        var timer;
        var step=0;
        var loading={
            create:function(){
                //创建元素
                loading_box=document.createElement("dl");
                progress_word=document.createElement("dt");
                progress_box=document.createElement("dd");
                progress_bar=document.createElement("div");
                full_bg=document.createElement("div");
                //创建元素关系
                loading_box.appendChild(progress_word);
                loading_box.appendChild(progress_box);
                progress_box.appendChild(progress_bar);
                //添加提示文字
                progress_word.innerHTML="加载中...";
                /*定义元素样式*/
                //背景样式
                full_bg.style.position="fixed";
                full_bg.style.left="0px";
                full_bg.style.top="0px";
                full_bg.style.bottom="0px";
                full_bg.style.right="0px";
                full_bg.style.background="#fefefe";
                full_bg.style.zIndex="999999";
                //加载容器样式
                loading_box.style.width="200px";
                loading_box.style.height="32px";
                loading_box.style.position="fixed";
                loading_box.style.left="50%";
                loading_box.style.marginLeft="-100px";
                loading_box.style.top="200px";
                loading_box.style.zIndex="1000000";
                loading_box.style.marginTop="0px";
                loading_box.style.marginBottom="0px";
                //进度容器样式
                progress_box.style.width="200px";
                //progress_box.style.height="10px";
                progress_box.style.border="#bcbcbc 1px solid";
                progress_box.style.background="#e6e6e6";
                progress_box.style.padding="1px";
                progress_box.style.borderRadius="3px";
                progress_box.style.margin="0px";
                //进度条样式
                progress_bar.style.width="0%";
                progress_bar.style.height="10px";
                progress_bar.style.background="#26a0da";
                progress_bar.style.borderRadius="3px";
                //提示文字样式
                progress_word.style.marginBottom="5px";
                progress_word.style.fontSize="12px";
                progress_word.style.color="#939393";
                //设置加载条默认样式
                var windowHeight = document.documentElement.clientHeight;
                var _top = (windowHeight - 32-20) / 2;//20是文字高度
                loading_box.style.top = _top + "px";

            },
            show:function(){
                if(!loading_box){
                    this.create();
                }
                //添加元素到网页
                document.getElementsByTagName("html")[0].appendChild(loading_box);
                document.getElementsByTagName("html")[0].appendChild(full_bg)
            },
            hide:function(){
                if(loading_box){
                    loading_box.remove();
                    full_bg.remove();
                }
                if(timer){
                    clearInterval(timer);
                }
            },
            autoOnceLoad:function(time){
                var onceTime=time/100;
                var setProgress=this.setProgress;
                //清除已经存在的timer
                if(timer){
                    clearInterval(timer);
                }
                timer=setInterval(function(){
                    if(step<100){
                        setProgress(step++);
                    }else{
                        clearInterval(timer);
                    }
                },onceTime);
            },
            autoCycleLoad:function(time){
                var onceTime=time/100;
                var setProgress=this.setProgress;
                //清除已经存在的timer
                if(timer){
                    clearInterval(timer);
                }
                timer=setInterval(function(){
                    if(step<100){
                        setProgress(step++);
                    }else{
                        step=0;
                    }
                },onceTime);

            },
            setProgress:function(n){
                if(n>100)n=100;
                if(n<0)n=0;
                progress_bar.style.width=n+"%";
            }
        }
        return loading;
    }
    //拖动控件
    var Drag=function(){
        var params = {
            left:0,
            right: 0,
            top: 0,
            button:0,
            currentX: 0,
            currentY: 0,
            flag: false
        };
        var isIE=(!!window.ActiveXObject || "ActiveXObject" in window);
        var mask;
        //获取相关CSS属性
        var getCss = function(o,key){
            return o.currentStyle? o.currentStyle[key] : document.defaultView.getComputedStyle(o,false)[key];
        };
        //创建一个遮罩div
        var createMaskDiv=function(){
            if(!mask){
                mask=document.createElement("div");
            }
            mask.style.position="absolute";
            mask.style.width="100%";
            mask.style.height="100%";
            mask.style.zIndex="100000";
            mask.style.right="0px";
            mask.style.top="0px";
            mask.style.right="0px";
            mask.style.button="0px";
            mask.style.background="#fff";
            mask.style.filter="alpha(opacity=0)";
            mask.style.opacity="0";
            document.body.appendChild(mask)
        }
        var deleteMaskDiv=function(){
            if (isIE)  {
                if(mask){
                    mask.removeNode(true);
                }

            }else{
                if(mask){
                    mask.remove();
                }

            }
        }
        //拖拽的实现
        var init = function(bar, target, callback){
            /*
            if(getCss(target, "right") !== "auto"){
                params.right = getCss(target, "right");
            }
            if(getCss(target, "top") !== "auto"){
                params.top = getCss(target, "top");
            }
            */

            target.style.right="auto";
            target.style.bottom="auto";

            params.left=target.offsetLeft;
            params.top=target.offsetTop;




            //o是移动对象
            bar.onmousedown = function(event){
                if(isIE){
                    createMaskDiv();//点击创建遮罩
                }
                params.flag = true;
                if(!event){
                    event = top.event;
                    //防止IE文字选中
                    bar.onselectstart = function(){
                        return false;
                    }
                }
                var e = event;
                params.currentX = e.clientX;
                params.currentY = e.clientY;


                document.onmousemove=onMousemove;



            };
            bar.onmouseup = function(){
                /*
                params.flag = false;
                if(getCss(target, "right") !== "auto"){
                    params.right = getCss(target, "right");
                }
                if(getCss(target, "top") !== "auto"){
                    params.top = getCss(target, "top");
                }
                */

                if(isIE){
                    deleteMaskDiv();//松开鼠标删除遮罩
                }
                document.onmousemove=null;
            };
            var onMousemove = function(event){
                var e = event ? event: top.event;

                if(params.flag){
                    var nowX = e.clientX, nowY = e.clientY;
                    var disX = nowX - params.currentX, disY = nowY - params.currentY;
                    target.style.left = parseInt(params.right) + disX + "px";
                    target.style.top = parseInt(params.top) + disY + "px";
                }

                if (typeof callback == "function") {
                    callback(parseInt(params.right) + disX, parseInt(params.top) + disY);
                }
            }
        };

        var self={
            startDrag:startDrag
        }

        return self;
    }
    /*拖拽结束*/
    
  //转圈加载动画
    var CircleLoader=function(){
        var mask;
        var maskBgColor="#000";
        var isIE=(!!window.ActiveXObject || "ActiveXObject" in window);

        //定义中号加载动画(默认)
        var defaultOpts = {
            lines: 8,            // The number of lines to draw
            length: 7,            // The length of each line
            width: 5,             // The line thickness
            radius: 10,           // The radius of the inner circle
            scale: 1.0,           // Scales overall size of the spinner
            corners: 1,           // Roundness (0..1)
            color: '#000',        // #rgb or #rrggbb
            opacity: 1/4,         // Opacity of the lines
            rotate: 0,            // Rotation offset
            direction: 1,         // 1: clockwise, -1: counterclockwise
            speed: 1,             // Rounds per second
            trail: 100,           // Afterglow percentage
            fps: 20,              // Frames per second when using setTimeout()
            zIndex: 2e9,          // Use a high z-index by default
            className: 'spinner', // CSS class to assign to the element
            top: '50%',           // center vertically
            left: '50%',          // center horizontally
            shadow: false,        // Whether to render a shadow
            hwaccel: false,       // Whether to use hardware acceleration (might be buggy)
            position: 'absolute'  // Element positioning
        };

        //定义特小号加载动画
        function xsmallLoaddingBarConfigure(opts){
            var smOpts=opts;
            smOpts. lines=7;// The number of lines to draw
            smOpts.length=1; // The length of each line
            smOpts.width=3;// The line thickness
            smOpts.radius=6;//
            return smOpts;
        }

        //定义小号加载动画
        function smallLoaddingBarConfigure(opts){
            var smOpts=opts;
            smOpts. lines=7, // The number of lines to draw
                smOpts.length=4, // The length of each line
                smOpts.width=4// The line thickness
            return smOpts;
        }

        //定义大号加载动画
        function bigLoaddingBarConfigure(opts){
            var smOpts=opts;
            smOpts. lines=11, // The number of lines to draw
                smOpts.length=12, // The length of each line
                smOpts.width=6// The line thickness
            smOpts.radius=22;
            return smOpts;
        }

        //创建一个遮罩div
        function createMaskDiv(){
            if(!mask){
                mask=document.createElement("div");
                mask.style.position="absolute";
                mask.style.width="100%";
                mask.style.height="100%";
                mask.style.zIndex="1000000000";
                mask.style.right="0px";
                mask.style.top="0px";
                mask.style.right="0px";
                mask.style.button="0px";
                mask.style.background=maskBgColor;
                mask.style.filter="alpha(opacity=0)";
                mask.style.opacity="0.3";
            }
            return mask;
        }
        //删除遮罩
        function deleteMaskDiv(){
            if (isIE)  {
                if(mask){
                    mask.removeNode(true);
                }
            }else{
                if(mask){
                    mask.remove();
                }

            }
        }

        var circleLoader={
            opts:defaultOpts,
            isModal:false,//true,false
            spinner:null,
            target:null,
            init:function(obj){

                if(obj.size=='xsm'){
                    this.opts=xsmallLoaddingBarConfigure(this.opts);
                }else if(obj.size=='sm'){
                    this.opts=smallLoaddingBarConfigure(this.opts);
                }else if(obj.size=='big'){
                    this.opts=bigLoaddingBarConfigure(this.opts);
                }

                this.isModal=obj.isModal;

                this.target = obj.target;

                if(obj.barColor){
                    this.opts.color=obj.barColor;
                }
                if(obj.maskBgColor){
                    maskBgColor=obj.maskBgColor;
                }


            },
            show:function(){
                if(!this.spinner){
                    this.spinner = new Spinner(this.opts);
                }


                var _targetDocmentObj;

                _targetDocmentObj=$(this.target);

                this.spinner.spin(_targetDocmentObj[0]);
                if(this.isModal){
                	console.log(_targetDocmentObj[0])
                    _targetDocmentObj[0].appendChild(createMaskDiv())
                }
            },
            close:function(){
                this.spinner.stop();
                deleteMaskDiv();
            }

        }



        return circleLoader;
    }
    
    //收起和展开控件
  //收起和展开控件
    var SlideController=function(){
        var slideController={
        		isInitialized:false,
        		configure:{
                    area:"body",
                    toggleBtn:".slider-btn",
                    toggleBtnUpIcon:"fa-angle-right",
                    toggleBtnUpText:"展开",
                    toggleBtnDownIcon:"fa-angle-down",
                    toggleBtnDownText:"收起",
                    speed:200
                },
                slideUpCallBack:function(obj){
                    var icon;
                    icon=$(obj).find("i");
                    $(icon).removeClass(slideController.configure.toggleBtnDownIcon)
                    $(icon).addClass(slideController.configure.toggleBtnUpIcon)

                   
                    var uptext=$(obj).data("uptext");
                    
                    if(uptext){
                    	$(obj).find("span").text(uptext);
                    }else{
                    	$(obj).find("span").text(slideController.configure.toggleBtnUpText);
                    	
                    }
                    
                },
                slideDownCallBack:function(obj){
                    var icon;
                    icon=$(obj).find("i");
                    $(icon).removeClass(slideController.configure.toggleBtnUpIcon)
                    $(icon).addClass(slideController.configure.toggleBtnDownIcon)

                    var downtext=$(obj).data("downtext");
                    
                    if(downtext){
                    	$(obj).find("span").text(downtext);
                    }else{
                    	$(obj).find("span").text(slideController.configure.toggleBtnDownText);
                    	
                    }
                    
                },
            init:function(){
            	if(slideController.isInitialized){
        		return;
        	}
        	slideController.isInitialized=true;            	
            //榛樿鏄叧闂姸鎬�
            $(slideController.configure.area).delegate(slideController.configure.toggleBtn ,"click",function(){
            	var _toggleBtn=$(this);
                if($(this).hasClass("on")){
                    $($(this).data("targetid")).slideUp(slideController.configure.speed,function(){
                        slideController.slideUpCallBack(_toggleBtn)
                    });
                    $(this).removeClass("on")
                    $(this).addClass("off")

                }else{
                    $($(this).data("targetid")).slideDown(slideController.configure.speed,function(){
                        slideController.slideDownCallBack(_toggleBtn);
                    });
                    $(this).removeClass("off")
                    $(this).addClass("on")
                }

            });
        }
        }
        return slideController;
    }
    
    var HtmlLoader=function(){
    	
	        //初始化参数
	        var uploaderParameters={
	            url:undefined,
	            method:"post",
	            isAsync:true,
	            enctype:"multipart/form-data",
	            formid:undefined,
	            uninitialized:function(){},
	            start:function(){},
	            send:function(){},
	            receive:function(){},
	            complete:function(){},
	            successfulCallback:function(){},
	            failedCallback:function(){}
	        }
	        var createXHR=function(){
	            if(typeof XMLHttpRequest != "undefined"){
	                return new XMLHttpRequest();
	            }else{
	                console.log("不支持 ie8以下的浏览器");
	            }
	        }
	        var xhr=createXHR();

	        var loader={
	            init:function(parameters){
	                uploaderParameters=parameters
	                xhr.onreadystatechange=function(){
	                    switch (xhr.readyState){

	                        case 0:(function(){
	                            uploaderParameters.uninitialized();
	                            console.log("未初始化");
	                        })();break;

	                        case 1:(function(){
	                            uploaderParameters.start();
	                            console.log("启动");
	                        })();break;

	                        case 2:(function(){
	                            uploaderParameters.send();
	                            console.log("发送");
	                        })();break;

	                        case 3:(function(){
	                            uploaderParameters.receive();
	                            console.log("接收");
	                        })();break;

	                        case 4:(function(){
	                            if((xhr.status>=200 && xhr.status<300)|| xhr.status==304){
	                                uploaderParameters.successfulCallback(xhr.responseText);
	                            }else{
	                                uploaderParameters.failedCallback(xhr.responseText);
	                            }
	                            uploaderParameters.complete();
	                        })();break;
	                    }

	                }
	            },
	            submitForm:function(){
	                var data=new FormData(document.getElementById(uploaderParameters.formid));
	                xhr.open(uploaderParameters.method,uploaderParameters.url,uploaderParameters.isAsync);
	                //xhr.setRequestHeader("Content-type",uploaderParameters.enctype)
	                xhr.send(data);
	            },
	            abort:function(){
	                xhr.abort()
	            }
	        }

	        return loader;   
    	
    	
    	
    }



    var self={};
    self.Loading_Beat=Loading_Beat;
    self.Loading_Default=Loading_Default;
    self.Drag=Drag;
    self.CircleLoader=CircleLoader;
    self.SlideController=SlideController;
    self.HtmlLoader=HtmlLoader;
    return self;

});