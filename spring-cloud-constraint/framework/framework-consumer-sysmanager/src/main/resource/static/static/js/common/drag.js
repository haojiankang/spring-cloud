var params = {
	right: 0,
	bottom: 0,
	currentX: 0,
	currentY: 0,
	flag: false
};
//获取相关CSS属性
var getCss = function(o,key){
	return o.currentStyle? o.currentStyle[key] : document.defaultView.getComputedStyle(o,false)[key]; 	
};

//拖拽的实现
var startDrag = function(bar, target, callback){
	if(getCss(target, "right") !== "auto"){
		params.right = getCss(target, "right");
	}
	if(getCss(target, "bottom") !== "auto"){
		params.bottom = getCss(target, "bottom");
	}
	//o是移动对象
	bar.onmousedown = function(event){
		params.flag = true;
		if(!event){
			event = window.event;
			//防止IE文字选中
			bar.onselectstart = function(){
				return false;
			}  
		}
		var e = event;
		params.currentX = e.clientX;
		params.currentY = e.clientY;
	};
	document.onmouseup = function(){
		params.flag = false;	
		if(getCss(target, "right") !== "auto"){
			params.right = getCss(target, "right");
		}
		if(getCss(target, "bottom") !== "auto"){
			params.bottom = getCss(target, "bottom");
		}
	};
	document.onmousemove = function(event){
		var e = event ? event: window.event;
		if(params.flag){
			var nowX = e.clientX, nowY = e.clientY;
			var disX = nowX - params.currentX, disY = nowY - params.currentY;
			var _right=parseInt(params.right) - disX;
			var _bottom=parseInt(params.bottom) - disY;

			if(_right<0){
				target.style.right=0+"px";
			}else if(_right>document.body.offsetWidth){
				target.style.right=document.body.offsetWidth+"px";
			}else{
				target.style.right=_right+"px";
			}

			if(_bottom<0){
				target.style.bottom=0+"px";
			}else if(_right>document.body.offsetHeight){
				target.style.bottom=document.body.offsetHeight+"px";
			}else{
				target.style.bottom=_bottom+"px";
			}

		}
		
		if (typeof callback == "function") {
			callback(parseInt(params.right) + disX, parseInt(params.bottom) + disY);
		}
	}	
};