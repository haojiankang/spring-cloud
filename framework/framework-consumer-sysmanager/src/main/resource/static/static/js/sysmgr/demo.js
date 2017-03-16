var depends = [
               {name:"jquery"},
               {name: "resource"},
               {name: "workflow"},               
               {name:"bootstrap",type:"css"},
               {name:"hjkbase",type:"css"},
               {name:"hjkplug",type:"css"},
               {name:"hjkplug"},
               {name:"timePicker"},   
               {name:"floatWindow"},
			   {name: "font-awesome", type: "css"},
			   {name: "iconfont", type: "css"},
			   {name:"uploader"},
			   {name:"printer"},
			   {name:"hjkIcon",type:"css"},
			   {name:"tabs"}

];
modular.define({name: "demo"}, depends, function () {

    var resrouce=this.resource;
    var workflow=this.workflow;
    resrouce.assess();
    workflow.assess();
	//转圈加载动画
	var CircleLoader=this.hjkplug.CircleLoader;
	//创建一个特小号加载动画
    var smLoader=new CircleLoader();
    smLoader.init({size:"xsm",target:"#loadding-test-0"});
    smLoader.show();

    //创建一个小号加载动画
    var smLoader=new CircleLoader();
    smLoader.init({size:"sm",target:"#loadding-test-1"});
    smLoader.show();

    //创建一个中号加载动画
    var smLoader=new CircleLoader();
    smLoader.init({size:"md",target:"#loadding-test-2"});
    smLoader.show();

    //创建一个大号加载动画
    var smLoader=new CircleLoader();
    smLoader.init({size:"big",target:"#loadding-test-3"});
    smLoader.show();

    //创建一个有遮罩的加载动画
    var hasMaskLoader=new CircleLoader();
    hasMaskLoader.init({size:"md",isModal:true,target:"#loadding-test-4"});
    hasMaskLoader.show();

    //创建一个有颜色的加载动画
    var hasMaskLoader=new CircleLoader();
    hasMaskLoader.init({size:"md",isModal:true,target:"#loadding-test-5",barColor:"#e86d44",maskBgColor:"#f7ebdc"});
    hasMaskLoader.show();
    
    //滑块控制
    var slideController=this.hjkplug.SlideController;
    var slider=new slideController();
    slider.init();

    
    
    

    
    
    
    //文档自动填充代码
    $(function(){
        var htmlCode=$(".htmlCode");
        for(var i=0;i<htmlCode.length;i++){
        	var nodeid=$(htmlCode[i]).data("targetid");
        	var node=$(nodeid)[0]
        	if(node){
        		var html=node.outerHTML
                $(htmlCode[i]).text(html);
        	}        	
        }

        var jsCode=$(".jsCode");
        for(var i=0;i<jsCode.length;i++){
            var html=$( $(jsCode[i]).data("targetid")).html();
            $(jsCode[i]).text(html);
        }

    })
    
    
    
    /*flash 控件 开始*/  
    //上传控件
    var limits;
    var uploadUrl;
    limits=[{
        name:"*",//文件名 默认为 匹配所有文件 *
        maxSize:0,//文件最大长度 单位byte
        minSize:0,//文件最小长度 单位byte
        type:[],//文件类型[String]
        check:false //是否需要按该规则校验该文件 boolean
    }];
    uploadUrl="http://192.168.1.123:8080/EHealthData/upload/postfile";
    uploaderExample= new Uploader();

    uploaderExample.createFlashObject("selectContainer","../../static/framework/plug/swf/uploader.swf","uploaderExample")
    uploaderExample.flashLoaded=flashLoaded;


    //创建上传控件并绑定事件
    function flashLoaded(flashid){
        uploaderExample.init("uploaderExample","none", limits);
        console.log(flashid);
        //绑定重置事件
        $(".resetSelector").click(function(){
            uploaderExample.resetSelector();
        });
        //绑定加载所有文件事件
        $(".loadeAllFile").click(function(){
            uploaderExample.loadAllFile()
        });
        //绑定单个压缩单个上传事件
        $(".compressOneFileSeparateUpload").click(function(){
            uploaderExample.compressOneFileSeparateUpload(uploadUrl)
        });
        //绑定打包压缩打包上传
        $(".compressAllFileUpload").click(function(){
            var zipName="files.zip";
            uploaderExample.compressAllFileUpload(zipName,uploadUrl);
        });
        //绑定删除事件
        $(".uploadController").on("click",".file-delete",function(){
            var id=$(this).parents(".selectedFile:eq(0)").attr("id");
            uploaderExample.cancelUpload(id);
            uploaderExample.deleteOneFile(id);

        })
        //绑定上传某个文件事件
        $(".uploadController").on("click",".file-upload",function(){
            var id=$(this).parents(".selectedFile:eq(0)").attr("id");
            uploaderExample.uploadOneFile(uploadUrl,id);
        });
        $(".uploadController").on("click",".cancelAllUpload",function(){
            uploaderExample.cancelUpload("*");//.cancelUpload("*");
        });
        //文件选择后回调
        uploaderExample.selectedFile=selectedFile;
        //重置
        uploaderExample.selectorIsReset=resetSelector;
        //开始加载
        uploaderExample.startLoadingFile=loadFile;
        //加载进度
        uploaderExample.loadingFileProgress=loadFileProgress;
        //文件加载完成
        uploaderExample.fileLoaded=loadFileEnd;
        //文件被删除
        uploaderExample.oneFileIsDelete=oneFileIsDelete;
        //开始上传文件
        uploaderExample.uploadFile=uploadFile;
        //上传进度
        uploaderExample.uploadFileProgress=uploadFileProgress;
        //上传结束
        uploaderExample.uploadFileEnd=uploadFileEnd;
        //上传文件出错
        uploaderExample.uploadFileError=uploadFileError;
        //文件已经存在
        uploaderExample.file_isExist=file_isExist;
        //文件操作出错
        uploaderExample.file_error=file_error;
        //控制器操作出错
        uploaderExample.file_error=sys_error;
    }
    //提交表单
    function submitFormInFlash(formData,url,method,successCallback,errorCallBack){
        uploaderExample.submitFormInFlash(formData,url,method,successCallback,errorCallBack);
    }
    //提交表单成功回调
    function submitFormSuc(reponse){
        alert(reponse);
    }
    //提交表单错误回调
    function submitFormErr(error){
        alert(error);
    }
    //选择文件回调
    function selectedFile(id,fileName,fileSize){
        console.log(id+":"+fileName+":"+fileSize);


        var file="<dd class='selectedFile' id='"+id+"'><dl>" +
            "<dd class='name'>"+fileName+"</dd>" +

            "<dd class='progressBar'>" +
            "<div class='bytesTotal'>" +
            "<div class='bytesLoaded green-bg'></div>" +
            "<div class='uploaded blue-bg'></div>"+
            "</div>" +
            "</dd>" +

            "<dd>" +
            "<button class='file-delete'  title='删除'><i class='fa fa-minus-circle'></i>删除</button>" +
            "</dd>" +

            "<dd>" +
            "<button class='file-upload'  title='上传/重传'><i class='fa fa-cloud-upload'></i>上传/重传</button>" +
            "</dd>" +

            "<dd>" +
            "<button class='file-cancel hide'  title='取消'><i class='fa fa-cloud-upload'></i>取消</button>" +
            "</dd>"+

            "<dd class='error'>" +
            "<span></span>" +
            "</dd>"+

            "</dl></dd>";

        $(".file-selected").append(file);
    }
    //重置选择器回调
    function resetSelector(){
        console.log("重选");
        $(".file-selected").find("dd").remove();
    }
    //加载文件
    function loadFile(id,fileName){
        $(".error").hide();
        $($("#"+id).find(".bytesLoaded")).show();
        console.log(id+" "+"开始加载文件："+fileName);
    }
    //加载进度
    function loadFileProgress(id,fileName,progress){
        console.log(id+" "+fileName+" -加载进度:"+progress);
        var progressbar=$($("#"+id).find(".bytesLoaded"));
        progressbar.css({"width":(progress+"%")});
        progressbar.html((progress+"%"))
    }
    //加载结束
    function loadFileEnd(id,fileName){
        console.log(id+" "+fileName+"加载结束");
        $($("#"+id).find(".bytesLoaded")).text("加载完成");
    }
    //上传文件
    function uploadFile(id,fileName){
        console.log(id+" "+"开始上传文件"+fileName);
        $(".error").hide();
        $($("#"+id).find(".uploaded")).show();
    }
    //上传进度
    function uploadFileProgress(id,fileName,progress){
        console.log(id+" "+fileName+" 上传进度:"+progress);
        var progressbar=$($("#"+id).find(".uploaded"));
        progressbar.css({"width":(progress+"%")});
        progressbar.html((progress+"%"));
    }
    //上传出错
    function uploadFileError(id,name,error){
        $("#"+id).find(".error").show();
        $("#"+id).find(".error").find("span").text(error);
    }
    //上传结束
    function uploadFileEnd(id,fileName){
        console.log(id+" "+fileName+"上传结束");
        $($("#"+id).find(".uploaded")).text("上传完成");
    }
    //某文件被删除
    function oneFileIsDelete(id){
        $("#"+id).slideUp(500,function(){
            $(this).remove();
        })
    }
    //文件重复选择提示
    function file_isExist(id){
        alert(id);
        attractive($("#"+id));
    }
    //文件操作出错
    function file_error(id,error){console.log(id+" "+error)}
    //控制器系统错误
    function sys_error(id,error){console.log(id+" "+error)}
    //闪三下
    function attractive(obj){
        obj.animate({"opacity":"0.1"},300,function(){
            $(this).animate({"opacity":"1"},300,function(){
                $(this).animate({"opacity":"0.1"},300,function(){
                    $(this).animate({"opacity":"1"},300);
                });
            });
        })
    }



    //转圈加载动画
    var CircleLoader=this.hjkplug.CircleLoader;

    //创建一个特小号加载动画
    var smLoader=new CircleLoader();
    smLoader.init("xsm",false,"loadding-test-0");
    smLoader.show();

    //创建一个小号加载动画
    var smLoader=new CircleLoader();
    smLoader.init("sm",false,"loadding-test-1");
    smLoader.show();

    //创建一个中号加载动画
    var smLoader=new CircleLoader();
    smLoader.init("md",false,"loadding-test-2");
    smLoader.show();

    //创建一个大号加载动画
    var smLoader=new CircleLoader();
    smLoader.init("big",false,"loadding-test-3");
    smLoader.show();

    //创建一个有遮罩的加载动画
    var hasMaskLoader=new CircleLoader();
    hasMaskLoader.init("md",true,"loadding-test-4");
    hasMaskLoader.show();

    //创建一个有颜色的加载动画
    var hasMaskLoader=new CircleLoader();
    hasMaskLoader.init("md",true,"loadding-test-5","#e86d44","#e86d44");
    hasMaskLoader.show();
    /*flash控件 结束*/
    
    
    //-------html 上传控件
    var hjkplug=this.hjkplug;
    $("body").delegate("#htmlLoaderSubmitEvent","click",function(){
    	var buttonHtml;
    	var p={
	            url:"../../upload/files",
	            method:"post",
	            isAsync:true,
	            formid:"t009",
	            //enctype:"multipart/form-data",
	            start:function(){
	            	$("#htmlLoaderSubmitEvent").attr({"disabled":"disabled"});
	            	$(".uploadNotify").text("正在上传...")
	            	$(".uploadNotify").show()
	            	console.log("start（开始）")
	            	},
	            send:function(){	            		
	            		console.log("send（发送）")
	            	},
	            receive:function(){console.log("receive（接收）")},
	            complete:function(){
	            	console.log(buttonHtml);
	            		$("#htmlLoaderSubmitEvent").removeAttr("disabled");	            		
	            		console.log("完成")
	            	},
	            successfulCallback:function(msg){
	            		$(".uploadNotify").text("上传成功")
	            		console.log("successfulCallback（成功）："+msg)
	            	},
	            failedCallback:function(msg){
	            		$(".uploadNotify").text("上传失败"+msg)
	            		console.log("failedCallback（失败）："+msg)
	            	},
	            uninitialized:function(){
	            		$(".uploadNotify").text("未初始化")
	            		console.log("uninitialized（未初始化）");
	            	}
	           }
	  


	  	var HtmlLoader=hjkplug.HtmlLoader;
        var loader=new HtmlLoader();
        loader.init(p);
        loader.submitForm();
    });
    
    
    
    //打印创建参考

    var printer;
    var printerFactory=new (this.printer.PrinterFactory);
    printerFactory.createPrinter({ip:"192.168.1.123",name:"test"},function(_printer){    	
        printer=_printer;
    });
    $("body").delegate("#printPerview","click",function(){
    	if(printer){
    		printer.PREVIEW();
    	}else{
    		console.log("没有找到插件");
    	}
		
	});
   
    


    //实例化对象
  	var HorizontalTab=this.tabs.horizontalTab;
      var horizontalTab=new HorizontalTab();
      //横向标签列表容器
      horizontalTab.tab.containerid="#menu";
      //标签html
      horizontalTab.tab.tabHtml='<li><div class="c-input-group"><em class="fullparent title"></em><div class="addon"><button class="close"><span class="icon-cha"></span></button></div></div></li>';
      //激活状态样式 class
      horizontalTab.tab.tabActiveClass="tab-active";
      //额外的辅助容器 （more 按钮浮动容器）
      horizontalTab.tab.extraContainer="#tab-more-container";//定义额外的辅助容器
      //配置iframe样式
      horizontalTab.iframe.containerid="#iframeContainer";
      //more 按钮id
      horizontalTab.more.moreSelector="#tab-more-btn";
      //调用方式以及参数数据格式
      horizontalTab.open({name:"baidu",url:"http://www.baidu.com",id:"qq"},true);
      horizontalTab.open({name:"126",url:"http://www.126.com",id:"126"},true);
      
});
