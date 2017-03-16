/**
 * Created by hjk on 2016/8/1.

 * hjk功能js库
 */

var depends = [    
    {name: "jquery",}
];
modular.define({name: "discreteness"}, depends, function () {
	
	


//颜色处理
var Color = function () {
    //将hex颜色值str转化成rgb数组
    this.HexToRgb = function (str) {
        var r = /^\#?[0-9a-f]{6}$/;
        //test方法检查在字符串中是否存在一个模式，如果存在则返回true，否则返回false
        if (!r.test(str)) return window.alert("输入错误的hex颜色值");
        //replace替换查找的到的字符串
        str = str.replace("#", "");
        //match得到查询数组
        var hxs = str.match(/../g);
        for (var i = 0; i < 3; i++) hxs[i] = parseInt(hxs[i], 16);
        return hxs;
    }
    //将rgb颜色值为a,b,c转化成hex颜色值
    this.RgbToHex = function (a, b, c) {
        var r = /^\d{1,3}$/;
        if (!r.test(a) || !r.test(b) || !r.test(c)) return window.alert("输入错误的rgb颜色值");
        var hexs = [a.toString(16), b.toString(16), c.toString(16)];
        for (var i = 0; i < 3; i++) if (hexs[i].length == 1) hexs[i] = "0" + hexs[i];
        return "#" + hexs.join("");
    }
    //得到hex颜色值为color的加深颜色值，level为加深的程度，限0-1之间
    this.getDarkColor = function (color, level) {
        var r = /^\#?[0-9a-f]{6}$/;
        if (!r.test(color)) return window.alert("输入错误的hex颜色值");
        var rgbc = this.HexToRgb(color);
        //floor 向下取整
        for (var i = 0; i < 3; i++) rgbc[i] = Math.floor(rgbc[i] * (1 - level));
        return this.RgbToHex(rgbc[0], rgbc[1], rgbc[2]);
    }
    //得到hex颜色值为color的减淡颜色值，level为减淡的程度，限0-1之间
    this.getLightColor = function (color, level) {
        var r = /^\#?[0-9a-f]{6}$/;
        if (!r.test(color)) return window.alert("输入错误的hex颜色值");
        var rgbc = this.HexToRgb(color);
        for (var i = 0; i < 3; i++) rgbc[i] = Math.floor((255 - rgbc[i]) * level + rgbc[i]);
        return this.RgbToHex(rgbc[0], rgbc[1], rgbc[2]);
    }
    //得到hex颜色值为color的web安全色
    this.getWebSafeColor = function (color) {
        var r = /^\#?[0-9a-f]{6}$/;
        if (!r.test(color)) return window.alert("输入错误的hex颜色值");
        var rgbc = this.HexToRgb(color);
        for (var i = 0; i < 3; i++) {
            var q1 = Math.floor(rgbc[i] / 51) * 51;
            //ceil向上取整
            var q2 = Math.ceil(rgbc[i] / 51) * 51;
            //abs绝对值
            if (Math.abs(q1 - rgbc[i]) <= Math.abs(q2 - rgbc[i])) rgbc[i] = q1;
            else rgbc[i] = q2;
        }
        return this.RgbToHex(rgbc[0], rgbc[1], rgbc[2]);
    },
    this.getHexBackgroundColor = function(obj) {
        var color = $(obj).css('background-color');
        if(color.indexOf("rgb")>=0){
            color=color.replace("rgba","").replace("rgb","").replace("(","").replace(")","").split(",")
            if(color==null){
                return;
            }
            return this.RgbToHex(parseInt(color[0]),parseInt(color[1]),parseInt(color[2]));
        }else{
            return color;
        }
    }

}

$("body").delegate(".btn-base","mouseover",function(){
    //modifyBtnColor(this,1.2);
});
$("body").delegate(".btn-base","click",function(){
    //modifyBtnColor(this,1.4);
});


$("body").delegate(".btn-base","mouseout",function(){
    //modifyBtnColor(this,1);
});

function modifyBtnColor(obj,level){
    var color=new Color();
    var objColor=color.getHexBackgroundColor(obj);
    var darkColor=color.getDarkColor(objColor,level);
    $(obj).css({"background":darkColor});
}

$(function(){
    /*弹出框 js
     *         //模态是不可以拖动的，有出场动画， 普通弹出框没有背景和出场动画，但可以拖动,
     * */

    var bg=null;//半透明背景
    $("button[data-target]").on("click",function(){
        var targetid=$(this).attr("data-target");
        var target= $(targetid);

        //初始化目标位置
        var mLeft=($(window).width()-target.width())/2;
        var mTop=($(window).height()-target.height())/2;
        target.css({left:mLeft,top:mTop});
        target.toggle();
        //获取是否可以拖动
        var targetDraggable=target.attr("target-draggable");

        //重置层次
        $(".floatbox").removeClass("active");
        target.addClass("active");
        //普通弹出框
        if(targetDraggable=="true"){
            //初始化拖动
            var $draggable = $("div[target-draggable='true']").draggabilly({
                // options...
            })

            //初始化因为有拖动附带的效果
            $(target.find(".crumb")).css({"border-radius":"10px"});
            $(target.find(".crumb")).css({width: "100%",height: "58px",top: "0px","border-radius":"0px",opacity: "0.8"});
            $(target.find(".title")).css({color:"#fff",cursor: "move"});
            $(target.find(".close")).css({color:"#fff"});
            $(target.find(".close")).css({top:"12.5px"});
        }
        //模态弹出框
        else{
            //弹出框出场动画

            //初始化一个半透明背景
            bg=document.createElement("div");
            $(bg).addClass("module-bg");
            $("body").append(bg);

            //给半透明背景绑定事件：1.隐藏当前背景和当前模态
            $(bg).click(function(){
                $(target).hide();
                $(this).hide();
            });
        }
    });

    //窗口点击浮动到最上层
    $(".floatbox").click(function(){
        $(".floatbox").removeClass("active");
        $(this).addClass("active")
    });

    $(".floatbox .close,.floatbox .cancel").on("click",function(){
        $(this).parents(".floatbox").hide();
        if(bg!=null){
            $(bg).hide();
        }
    });

    //选择菜单事件
    $(".dropdown-menu li").on("click",function(){
        var val=$(this).find("a").text();
        $($(this).parents(".btn-group")).find(".selectedValue").text(val);
    });

});

});
