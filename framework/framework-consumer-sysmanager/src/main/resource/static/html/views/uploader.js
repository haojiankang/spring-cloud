/**
 * Created by hjk on 2016/8/12.
 */
var Uploader=function(flashid){
    var flashid=flashid;
    /*
     var callbackObj;
     var url;
     var limitType="none";//String 限制类型 已经开放[完全限制:completeRestriction、限制包容于:contained、不限制:none] 未开放[限制排斥、混合限制] 默认不限制
     var limit=null;
        [{
    		name:"*",//文件名 默认为 匹配所有文件 *
    		maxSize:0,//文件最大长度 单位byte
    		minSize:0,//文件最小长度 单位byte
    		type:[],//文件类型[String]
    		check:false //是否需要按该规则校验该文件 boolean
    	}]
    */
    var uploader={
        callbackObj:null,
        url:null,
        limitType:"none",
        limit:null,
        //初始化flash
        init:function(callbackObj,limitType,limit){
            this.callbackObj=callbackObj;
            this,limitType=limitType;
            this.limit=limit;
            document.getElementById(flashid).init(callbackObj,limitType,limit);
        },
        //文件选择后被回调函数
        selectedFile:function(flashid,fileName,fileSize){
            alert("选择了："+flashid);
        },
        //清空/重选/重置控制器
        resetSelector:function(){
            document.getElementById(flashid).resetSelector();
        },
        //清空/重选/重置控制器 成功回调
        selectorIsReset:function(){
        alert("重置成功");
        },

        //加载文件
        loadAllFile:function(fileid,fileName){
            document.getElementById(flashid).loadAllFile();
        },
        //开始加载某个文件时被回调函数
        startLoadingFile:function(fileid,fileName){
            console.log("weblog_开始加载:"+fileid+":"+fileName);
        },
        //某个文件加载进度
        loadingFileProgress:function(fileid,fileName,progress){
            console.log("weblog_加载进度:"+fileid+":"+fileName+":"+progress);
        },
        //某个文件加载结束
        fileLoaded:function(fileid,fileName){
            console.log("weblog_加载结束:"+fileid+":"+fileName);
        },
        //文件重复选择
        file_isExist:function(fileid){
            console.log("weblog_文件存在:"+fileid);
        },
        //单个压缩单个上传
        compressOneFileSeparateUpload:function(startCallback,progressCallback,completeCallback,errorCallback){
            document.getElementById(flashid).compressOneFileSeparateUpload(startCallback,progressCallback,completeCallback,errorCallback);
        },
        //打包压缩打包上传
        compressAllFileUpload:function(zipName,startCallback,progressCallback,completeCallback,errorCallback){
            document.getElementById(flashid).compressAllFileUpload(zipName);
        },
        //取消上传
        undo:function(fileid){
            document.getElementById(flashid).undo(fileid);
        },
        //删除某个文件
        deleteOneFile:function(fileid){
             document.getElementById("ExternalInterfaceExample").deleteOneFile(fileid);
        },
        //上传单个文件/重新上传
        uploadOneFile:function(fileid){
            document.getElementById(flashid).uploadOneFile(fileid);
        },

        //上传文件
         uploadFile:function(id,fileName){
            console.log(id+" "+"开始上传文件"+fileName);
        },
         //上传进度
        uploadFileProgress:function(id,fileName,progress){
            console.log(id+" "+fileName+" 上传进度:"+progress);
        },
        //上传结束
        uploadFileEnd:function(id,fileName){
            console.log(id+" "+fileName+"上传结束");
        },

        //表单提交
        submitFormInFlash:function(formData,url,method,successCallback,errorCallBack){
            document.getElementById(flashid).submitForm(formData,url,method,successCallback,errorCallBack);
        },
        //某个文件被删除回调函数
        oneFileIsDelete:function(fileid,fileName){

        },
        //文件操作出错
        file_error:function(fileid,error){

        },
        //控制器系统错误
        sys_error:function(fileid,error){

        }
    }

    return uploader;
}