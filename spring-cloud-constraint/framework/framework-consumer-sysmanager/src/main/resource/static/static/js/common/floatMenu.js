var obj={
	icon:"",
	title:"",
	url:"",
	target:"",
	data:{}
 }
 var objs=[];

 //control
 StorageBox={
	init:function(containerId,triggerId){
		this.view.initView(containerId,triggerId);
		this.view.container.mouseleave(function(){
			StorageBox.view.hide();
		});

		this.view.trigger.click(function(){
			StorageBox.view.show();
		});	

	},
	set:function(obj){
		this.model.addObj(obj);
		this.view.addView(obj);
	},
	del:function(obj){
		var id=$(obj).parent().data("id");
		this.model.delObj(id);
		this.view.delView(id);
		console.log(id);
	},
	request:function(id){
		//构建一个请求...
	}
 }

 StorageBox.model={
	addObj:function(obj){
		obj.id=(new Date()).valueOf();
		objs.push(obj);
	},
	delObj:function(id){
		for(var i=0;i<objs.length;i++){
			if(objs[i].id==id){
				objs.splice(i,1);
			}
		}
	}
 }

 StorageBox.view={
	container:null,
	trigger:null,
	initView:function(containerId,triggerId){

		this.container=$("#"+containerId);
		this.trigger=$("#"+triggerId);


		//var floatBtn=$(".floatBtn");
		var leftDist;
		var topDist;
		if(this.trigger.offset().left>=176){
			leftDist=-176-8;//this.trigger.offset().left-176-8;	
		}else{
			leftDist=176+8;//this.trigger.offset().left+46+8;	
		}
		topDist=this.trigger.offset().top;
		this.container.css({left:leftDist,top:topDist})

	},
	addView:function(obj){
		var child="<li data-id="+obj.id+"><a href='"+obj.url+"' target="+obj.target+"><span class='glyphicon glyphicon-"+obj.icon+"'></span><span class='title'>"+obj.title+"</span></a><span class='glyphicon glyphicon-remove remove' onclick='StorageBox.del(this)'></span></li>";
		this.container.append(child);
		if(this.container.css("display")=="none"){
			this.show();
		}
		
	},
	delView:function(id){
		var _li=this.container.find("li");
		for(var i=0;i<_li.length;i++){
			if($(_li[i]).data("id")==id){
				$(_li[i]).remove();				
			}
		}
	},
	update:function(arr){
		this.container.show();
		
		var child;

		for(var i=0;i<arr.length;i++){
				child="<li class='hide' data-id="+arr[i].id+" style='display:none'><a href='"+arr[i].url+"' target="+arr[i].target+"><span class='glyphicon glyphicon-"+arr[i].icon+"'></span><span class='title'>"+arr[i].title+"</span></a><span class='glyphicon glyphicon-remove remove' onclick='StorageBox.del(this)'></span></li>";
				this.container.append(child);
		}

	},
	show:function(){
		this.container.removeClass("hide");
		this.container.show();
		var child=this.container.find("li");

		if(child.length<=0){
		/**
			this.addView({
				title:"暂无添加记录",
				url:"#",
				target:""				
			});
			this.container.find("li").delay(2000).fadeOut(function(){
				$(this).remove();
			});
		*/
			return;
			
		}


		for(var i=0;i<child.length;i++){
			$(child[i]).fadeIn(100);			
			$(child[i]).animate({"left":"0px"},(i*100)+500);
			
		}
	},
	hide:function(){
		
		this.container.find("li").finish();		

		var child=this.container.find("li");

		for(var i=0;i<child.length;i++){
			$(child[child.length-i]).fadeIn(100);			
			$(child[child.length-i]).animate({"left":"0px"},(i*100)+500);

		}

		this.container.fadeOut();
		this.container.find("li").fadeOut();
	
	}

 }
