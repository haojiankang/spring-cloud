/**
 * Created by hjk on 2016/10/12.
 */






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
    //判断是否为2.0新窗口类型
    if(target.hasClass('modal-frame')){
    	mf.showWindow(targetid)
        return;
    }
    
    //获取是否可以拖动
    var targetDraggable=target.attr("target-draggable");

    //是否可以拖动
    if(targetDraggable=="true"){
    	
    	 //初始化拖动
        var $draggable = $("div[target-draggable='true']").draggabilly({
            // options...
        })
        target.addClass("floatboxDraggable");
        
        /*
         * 新拖动
    	 //初始化拖动
        var dragElement = $(target)[0];
        var dargContainer = $("body");
        var dragHandle = $(target).find(".header")[0];

        new Drag.Move(dragElement, {handle:dragHandle,"container":dargContainer });
         //target.addClass("floatboxDraggable");
        */
       
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
  //判断是否为2.0新窗口类型
    if(target.hasClass('modal-frame')){
    	mf.hideWindow(targetid)
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


//以下是2.0新弹出窗口对象

var mf=new ModalFrame();

function ModalFrame(){
    var maxZIndex=1000;
    var animateSpeed=500;
    var hasActive=false;
    var CENTER= 0,LEFT= 1,TOP= 2,RIGHT= 3,BOTTOM=4;

    //默认初始化绑定事件
    (function(){
        $("body").delegate(".modal-wrap .modal-frame","click",function(){
        	if(!hasActive){
        		modalFrame.active("#"+$(this).attr("id"));
        	}
            
        });

        $("body").delegate(".modal-wrap .modal-frame .close,.modal-wrap .modal-frame .cancel","click",function(){
            var targetid="#"+$(this).parents(".modal-frame").attr("id");
            modalFrame.hideWindow(targetid);

            return false;
        });

        
       
    })()

    var modalFrame={
        showWindow:function(modalid){//id格式
            var atFrame;
            //显示当前框架
            $(modalid).parents(".modal-wrap").fadeIn();
            //判断当前模态所在框架位置
            if($(modalid).parents(".right-wrap").length>0){
                atFrame=RIGHT;
            }else if($(modalid).parents(".center-wrap").length>0){
                atFrame=CENTER;
            }

            switch (atFrame){

                case RIGHT:{
                	hasActive=true;
                    if($(".right-wrap").is(":visible")){
                        modalFrame.hideRightWindow(function(){
                            modalFrame.initRightWindowModal(modalid);
                            modalFrame.showRightWindow(function(){
                            	hasActive=false;
                            });
                        });
                    }else{
                        modalFrame.initRightWindowModal(modalid);
                        modalFrame.showRightWindow(function(){
                        	hasActive=false;
                        });
                    }
                }break;

                case CENTER:{
                    modalFrame.initCenterWindow(modalid);
                    modalFrame.hideAllSidebar();
                    modalFrame.showCenterWindow();
                }
            }
            modalFrame.active(modalid);
        },
        hideWindow:function(modalid){
            var atFrame;

            if($(modalid).parents(".right-wrap").length>0){
                atFrame=RIGHT;
            }else if($(modalid).parents(".center-wrap").length>0){
                atFrame=CENTER;
            }

            switch (atFrame){

                case RIGHT:{
                    modalFrame.hideRightWindow();
                }break;

                case CENTER:{
                    modalFrame.hideCenterWindow();
                }
            }
            //关闭自己时交出自己的活动状态
            modalFrame.surrenderActive(modalid);

            //判断除去当前关闭对象是否还有其他可视对象 如果没有关闭当前框架
            if(!modalFrame.hasViews(modalid)){
                $(modalid).parents(".modal-wrap").fadeOut();
            }
        },

        //showRightWindow流程--》隐藏--》初始化--》显示 （收起左边）
        initRightWindowModal:function(modalid){
            $(".right-wrap .modal-frame").hide();
            $(modalid).fadeIn(function(){
                modalFrame.vertical(modalid);
            });

        },
        showRightWindow:function(callback){
            var maxWidth=100;
            $(".right-wrap .modal-frame").each(function(){
                var _width=$(this).width();
                if(_width>maxWidth){
                    maxWidth=_width;
                }
            });

            $(".right-wrap").fadeIn(function(){
                $(".right-wrap").animate({width:(maxWidth+2)},animateSpeed);
                $(".center-wrap").animate({right:(maxWidth+2)},animateSpeed,function(){
                    if(callback)callback();
                });
            });
        },
        hideRightWindow:function(callback){
            $(".right-wrap").animate({width:0},animateSpeed,function(){$(this).hide()});
            $(".center-wrap").animate({right:0},animateSpeed,function(){
                if(callback)callback();
            });
        },
        //showCenterWindow流程 初始化--》显示 （收起其他边）
        initCenterWindow:function(modalid){
            $(".center-wrap .modal-frame").hide();
            $(modalid).fadeIn();
            modalFrame.vertical(modalid);
        },
        showCenterWindow:function(callback){
            $(".center-wrap").animate({right:"0px",left:"0px"},animateSpeed,function(){
                if(callback)callback();
            });
        },
        hideCenterWindow:function(callback){
            $(".center-wrap").find(".modal-frame").hide()
        },
        //收起所有边栏
        hideAllSidebar:function(callback){
            modalFrame.hideRightWindow();

            if(callback){
                setTimeout(function(){
                    callback();
                },animateSpeed);
            }

        },
        //是其他变淡
        bleachOther:function(modalid){
            $(modalid).parents(".modal-wrap").find(".modal-frame").addClass("thin");
            $(modalid).removeClass("thin")
        },
        recoverOther:function(modalid){
            $(modalid).parents(".modal-wrap").find(".modal-frame").removeClass("thin")
        },
        //居中方式
        vertical:function(targetIDOrClass){
            var windowHeight=$(window).height();
            var targetHeight=$(targetIDOrClass).height();
            if(targetHeight<windowHeight){
                var top=((windowHeight-targetHeight)/2)+"px";
                $(targetIDOrClass).css({"top":top});
            }
        },
        //判断除去某元素以外是否还有其他可视元素存在 如果
        hasViews:function(targetIDOrClass){
            var hasViews=false;
            var target;
            if(targetIDOrClass){
                target=$(targetIDOrClass);//写到这里了 明天记得加一个双击 为show
            }
            $(targetIDOrClass).parents(".modal-wrap").find(".modal-frame").each(function(){
                if($(this).is(":visible")){
                    //判断可视对象不是当前活动对象 避免当前对象还在关闭过程中被判断为还有可视对象
                    try{
                        if(target[0]!=$(this)[0]){
                            hasViews=true;
                        }
                    }catch (e){
                        hasViews=true;
                    }

                }

            });

            return hasViews;
        },
        active:function(modalid){
            //设置层次
            maxZIndex++;
            $(modalid).parent().css({"z-index":maxZIndex});
            //改变选中状态
            modalFrame.recoverOther(modalid);
            modalFrame.bleachOther(modalid);
        },
        //交出自己的状态 默认给予中间元素
        surrenderActive:function(modalid){
            if(!$(modalid).hasClass("thin")){
                $(modalid).parents(".modal-wrap").find(".center-wrap .modal-frame").each(function(){
                    if($(this).is(":visible")){
                        $(this).removeClass("thin")
                    }

                });
            }
        }

    }

    return modalFrame;
}