/**
 * Created by hjk on 2016/8/18.
 */
Uploader = function () {
    var flashid;
    var uploader = {
        callbackObj: null,
        url: null,
        limitType: "none",
        limit: null,
        createFlashObject:function(targetid,src,_flashid){
            flashid=_flashid;

            var flashObject="<object id='"+_flashid+"' type='application/x-shockwave-flash' data='"+src+"' class='flash-select-button' style='width: 70px; height: 30px;'>" +
                "<a href='http://www.adobe.com/go/getflash'></a>" +
                "<param name='movie' value='"+src+"'>" +
                "<param name='quality' value='high'>" +
                "<param name='allowscriptaccess' value='always'><param name='wmode' value='transparent'>" +
                "</object>";
            /*
             var FlashObject=document.createElement("object");
             var param1=document.createElement("param");
             var param2=document.createElement("param");
             var param3=document.createElement("param");
             var param4=document.createElement("param");
             var a=document.createElement("a");
             var img=document.createElement("img");

             FlashObject.setAttribute("id",flashid);
             //FlashObject.setAttribute("name","xx");
             FlashObject.setAttribute("type","application/x-shockwave-flash");
             FlashObject.setAttribute("data",src);
             FlashObject.setAttribute("class","flash-select-button");

             FlashObject.style.width="70px";
             FlashObject.style.height="30px";

             param1.setAttribute("name","movie");
             param1.setAttribute("value",src);

             param2.setAttribute("name","quality");
             param2.setAttribute("value","high");

             param3.setAttribute("name","allowscriptaccess");
             param3.setAttribute("value","always");

             param4.setAttribute("name","wmode");
             param4.setAttribute("value","transparent");

             a.setAttribute("href","http://www.adobe.com/go/getflash");
             //a.appendChild(img);

             img.setAttribute("src","http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif")
             img.setAttribute("alt","Get Adobe Flash player");

             FlashObject.appendChild(a);
             FlashObject.appendChild(param1);
             FlashObject.appendChild(param2);
             FlashObject.appendChild(param3);
             FlashObject.appendChild(param4);
             */
            $("#"+targetid).append(flashObject);
        },
        flashLoaded:function(){
            console.log("flash加载完成");
        },
        //初始化flash
        init: function (callbackObj, limitType, limit) {
            this.callbackObj = callbackObj;
            this, limitType = limitType;
            this.limit = limit;
            document.getElementById(flashid).init(callbackObj, limitType, limit);
        },
        //文件选择后被回调函数
        selectedFile: function (flashid, fileName, fileSize) {
            alert("选择了：" + flashid);
        },
        //清空/重选/重置控制器
        resetSelector: function () {
            document.getElementById(flashid).resetSelector();
        },
        //清空/重选/重置控制器 成功回调
        selectorIsReset: function () {
            alert("重置成功");
        },

        //加载文件
        loadAllFile: function () {
            document.getElementById(flashid).loadAllFile();
        },
        //开始加载某个文件时被回调函数
        startLoadingFile: function (fileid, fileName) {
            console.log("weblog_开始加载:" + fileid + ":" + fileName);
        },
        //某个文件加载进度
        loadingFileProgress: function (fileid, fileName, progress) {
            console.log("weblog_加载进度:" + fileid + ":" + fileName + ":" + progress);
        },
        //某个文件加载结束
        fileLoaded: function (fileid, fileName) {
            console.log("weblog_加载结束:" + fileid + ":" + fileName);
        },
        //文件重复选择
        file_isExist: function (fileid) {
            console.log("weblog_文件存在:" + fileid);
        },
        //单个压缩单个上传
        compressOneFileSeparateUpload: function (uploadUrl) {//startCallback,progressCallback,completeCallback,errorCallback
            document.getElementById(flashid).compressOneFileSeparateUpload(uploadUrl);
        },
        //打包压缩打包上传
        compressAllFileUpload: function (uploadUrl, zipName) {
            document.getElementById(flashid).compressAllFileUpload(uploadUrl, zipName);
        },
        //取消上传
        cancelUpload: function (fileid) {
            console.log("开始取消" + fileid);
            document.getElementById(flashid).cancelUpload(fileid);
        },
        //取消上传成功
        uploadIsCancelled: function (fileid) {
            console.log("上传取消成功:" + fileid)
        },

        //删除某个文件
        deleteOneFile: function (fileid) {
            document.getElementById(flashid).deleteOneFile(fileid);
        },
        //上传单个文件/重新上传
        uploadOneFile: function (uploadUrl,fileid) {
            document.getElementById(flashid).uploadOneFile(uploadUrl,fileid);
        },

        //上传文件
        uploadFile: function (id, fileName) {
        },
        //上传进度
        uploadFileProgress: function (id, fileName, progress) {
        },
        uploadFileError: function (id, fileName, error) {
            console.log(id + " " + fileName + " " + error);
        },
        //表单提交
        submitFormInFlash: function (formData, url, method, successCallback, errorCallBack) {
            document.getElementById(flashid).submitForm(formData, url, method, successCallback, errorCallBack);
        },
        //某个文件被删除回调函数
        oneFileIsDelete: function (fileid, fileName) {

        },
        //文件操作出错
        file_error: function (fileid, error) {
            console.log(fileid + ":" + error);
        },
        //控制器系统错误
        sys_error: function (error) {
            console.log(error);
        }
    }

    return uploader;
}