/**
 * Created by hjk on 2016/10/17.
 */
modular.define({name: "printer"}, depends, function () {
	
	function PrinterFactory(){

	    var extend=function(des, src, override){
	        if(src instanceof Array){
	            for(var i = 0, len = src.length; i < len; i++)
	                extend(des, src[i], override);
	        }
	        for( var i in src){
	            if(override || !(i in des)){
	                des[i] = src[i];
	            }
	        }
	        return des;
	    }

	    //判断浏览器版本
	    var whatBrowser=function(){
	        var browser;
	        var uaStr=navigator.userAgent.toLowerCase();
	        if(uaStr.indexOf("msie")!=-1){
	            //ie浏览器 用本地打印
	            browser="ie";
	        }else if(uaStr.indexOf("chrome")!=-1){
	            //google chrome
	            browser="chrome";

	        }else if(uaStr.indexOf("safari")!=-1){
	            //Safari

	        }else if(uaStr.indexOf("firefox")!=-1){
	            //firefox
	            browser="firefox";

	        }else if(uaStr.indexOf("opera")!=-1){
	            //firefox
	            browser="opera";
	        }else{
	            browser="other";
	        }

	        return browser;

	    }


	    var printerObject={
	        localConfigure:{
	            ip:"localhost",
	            port:"8000",
	            priority:1,
	            name:"CLODOP"
	        },
	        cloudConfigure:{
	            ip:null,
	            port:"8000",
	            priority:1,
	            name:"CLODOP"
	        },
	        createPrinter:function(_localConfigure,callback){
	            //请求地址
	            var _src;
	            //打印对象
	            var _CLODOP;
	            //打印对象自定义名称
	            var printerName;
	            //拼合默认参数和自定义参数，如果重复 自定义参数将覆盖默认参数
	            var __localConfigure=extend({},[_localConfigure,printerObject.localConfigure]);	           

	            //判断浏览器
	            var browser=whatBrowser();

                if(__localConfigure.ip==null){
                    alert("请配置本地打印参数");
                    return;
                }
                printerName=__localConfigure.name;
                _src="http://"+__localConfigure.ip+":"+__localConfigure.port+"/CLodopfuncs.js?priority="+__localConfigure.priority+"&name="+__localConfigure.name;


	            console.log("请求地址："+_src);

	            var script=document.createElement("script");
	            script.src=_src;
	            document.body.appendChild(script);

	            var timer=setInterval(function(){
	                console.log("正在寻找打印驱动插件...");
	                try{
	                	_CLODOP=eval(printerName);
	                }catch(e){
	                	_CLODOP=null;
	                }
	                
	                if(_CLODOP){
	                    if(callback){
	                        callback(_CLODOP);
	                    }
	                    clearInterval(timer);
	                    console.log("已找到插件，版本号："+_CLODOP.VERSION);
	                }
	            },500);
	            setTimeout(function(){
	                clearInterval(timer);
	                if(!_CLODOP){	                	
	                	//添加提示内容
	                	callback(null);
	                	printerObject.prompt();
                        //输出提示日志
	                    //console.log("插件请求超时，请检查ip参数是否配置正确，或者检查网络是否流畅");
	                };

	            },1000);
	        },
            prompt:function(){
                var notifyBox=document.createElement("div");
                notifyBox.style.width="100%";
                notifyBox.style.padding="8px";
                notifyBox.style.position="fixed";
                notifyBox.style.top="0px";
                notifyBox.style.left="0px";
                notifyBox.style.zIndex="10000000";
                notifyBox.style.background="#0b8cd5";
                notifyBox.style.display="none";
                var a=document.createElement("a");
                a.href="http://c-lodop.com/download.html";
                a.innerHTML="您还没有安装打印插件，请点击此处下载安装,并刷新页面。";
                a.target="_blank";
                a.style.color="#fff";
                notifyBox.appendChild(a);
                document.body.appendChild(notifyBox);
                $(notifyBox).fadeIn();
                setTimeout(function(){
                	$(notifyBox).fadeOut();
                },5000)
            }
	    }

	    return printerObject;
	}
	
	 var self={};
	    self.PrinterFactory=PrinterFactory;
	    return self;
});
