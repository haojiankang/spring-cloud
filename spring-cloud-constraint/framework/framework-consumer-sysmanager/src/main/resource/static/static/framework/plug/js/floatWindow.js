/**
 * Created by hjk on 2016/8/25.
 * 所有的id都是：#xx 格式
 */
var currentZIndex=100;

var floatDepend={};
function setFloatDepend(btn){
    var targetid=$(btn).attr("data-target");
    var parentWindowidArray=$(btn).parents("floatbox");

    if(parentWindowidArray.length<=0){
        var parentWindowId=$(parentWindowidArray[0].attr("id"));
    }
}

//给按钮添加显示窗口事件
$(document).delegate("button[data-target]","click",function(){
    var targetid=$(this).attr("data-target");
    showWindow(targetid);
    //event.stopPropagation();
    //event.preventdefault();
});
//显示窗口
function showWindow(targetid){
    var target= $(targetid);

    //判断是否是boostrap模态
    if(target.hasClass('modal')){
        $(targetid).modal('show');
        return;
    }
    //获取是否可以拖动
    var targetDraggable=target.attr("target-draggable");

    //是否可以拖动
    if(targetDraggable=="true"){
    	
    	
    	 //初始化拖动
        var dragElement = $(target)[0];
        var dargContainer = $("body");
        var dragHandle = $(target).find(".header")[0];

        new Drag.Move(dragElement, {handle:dragHandle,"container":dargContainer });
        
        
        //target.addClass("floatboxDraggable");
    }else{
        //弹出框出场动画
        //初始化一个半透明背景
        createBg(targetid);

    }

    initTarget(targetid);
    increaseZIndex(targetid)
}
//初始化浮动框
function initTarget(targetid){
    var target= $(targetid);
    //初始化目标位置
    var mLeft=($(window).width()-target.width())/2;
    var mTop=($(window).height()-target.height())/2;
    $(target).css({left:mLeft,top:mTop});
    $(target).show();
}
//创建背景
function createBg(targetid){

    var bg=document.createElement("div");
    var bgid=targetid.replace("#","")+"-bg"
    $(bg).attr("id",bgid);
    $(bg).addClass("module-bg");

    $("body").append(bg);

    //给半透明背景绑定事件：1.隐藏当前背景和当前模态
    $(bg).click(function(){
        closeFloat(targetid)
    });

}
//移除背景
function removeBg(targetid){
    var bgid=targetid+"-bg";
    var bg=$(bgid);
    if(bg!=null){
        $(bg).remove();
    }
}
//通过事件关闭
function hideWindow(targetid){//这里的id 是：#xx 格式

    var target= $(targetid);

    //判断是否是boostrap模态
    if(target.hasClass('modal')){
        target.modal("hide");
        return;
    }

    closeFloat(targetid)

}
//点击按钮 关闭浮动
$(document).delegate(".floatbox .close,.floatbox .cancel","click",function(){
    var target=$(this).parents(".floatbox");
    var targetid="#"+$(target).attr("id");

    closeFloat(targetid);
});
var object={}
//关闭浮动
function closeFloat(targetid){
    //隐藏子代

    //隐藏浮动框
    $(targetid).hide();
    //移除背景
    removeBg(targetid);
}
//调整层次
function increaseZIndex(targetid){
    var targetZIndex;
    targetZIndex=parseInt($(targetid).css("z-index"));
    //如果浮动框层次不为最高层次就提升层次
    if(targetZIndex<currentZIndex){
        currentZIndex=currentZIndex+2;
    }

    var bgid=targetid+"-bg";
    var bgZIndex=currentZIndex-1;
    $(targetid).css({"z-index":currentZIndex});
    if($(bgid)){
        $(bgid).css({"z-index":bgZIndex});
    }



}

//窗口点击浮动到最上层
$(document).delegate(".floatbox","click",function(){
    var targetid="#"+$(this).attr("id");
    increaseZIndex(targetid);
});
//选择菜单事件
$(document).delegate(".dropdown-menu li","click",function(){
    var val=$(this).find("a").text();
    $($(this).parents(".btn-group")).find(".selectedValue").val(val);
});