var depend = [
    {name: "utils"}
];
modular.define({name: "workflow"}, depend, function () {
    var self={};
    self.create=function(){
	var workflow={};
	workflow.options={url:"index/loadNodeActionMapping",data:{},selector:"[data-action]",action:"data-action",bpn:"data-bpn"};
	workflow.data={};
	workflow.state=false;
	workflow.call=[];
	workflow.load=function(){
	    $.secAjax(
	            {
	                type: 'POST',
	                url: this.options.url,
	                data: this.options.data,
	                dataType: 'json',
	                success: function (data) {
	                    workflow.data=data;
	                    workflow.state=true;
	                    for(var i=0;i<workflow.call.length;i++){
	                	workflow.call[i].call(workflow);
	                    }
	                }
	            });
	};
	workflow.assess=function(){
	    if(workflow.state){
		$(workflow.options.selector).each(function(){
		    var _this= $(this)
		    var action=workflow.data[_this.attr(workflow.options.action)];
		    var bpn=_this.attr(workflow.options.bpn);
		    if(bpn!==void 0){
			if(action&&action.indexOf(bpn)!=-1){
			    
			}else{
			    $(this).remove();
			}
		    }
		});
	    }else{
		workflow.call.push(workflow.assess);
		workflow.load();
	    }
	};
	
	return workflow;
    }
    self.assess=function(){
	self.create().assess();
    }
    return self;
});
