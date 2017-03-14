var depend = [
    {name: "utils"}
];
modular.define({name: "resource"}, depend, function () {
    var self={};
    self.create=function(){
	var resource={};
	resource.options={url:"index/loadResource",data:{resourceName:"访问系统/"},selector:"[data-resource]",prop:"data-resource"};
	resource.data={};
	resource.state=false;
	resource.call=[];
	resource.load=function(){
	    $.secAjax(
	            {
	                type: 'POST',
	                url: this.options.url,
	                data: this.options.data,
	                dataType: 'json',
	                success: function (data) {
	                    var saxdata = function () {
	                        _this = this;
	                        resource.data[_this.code]=true;
	                        if (_this.sons && _this.sons.length > 0) {
	                            _.map(_this.sons, function (val) {
	                        	saxdata.apply(val);
	                            })
	                        }
	                    };
	                    saxdata.apply(data.resource);
	                    resource.state=true;
	                    for(var i=0;i<resource.call.length;i++){
	                	resource.call[i].call(resource);
	                    }
	                }
	            });
	};
	resource.assess=function(){
	    if(resource.state){
		$(resource.options.selector).each(function(){
		    if(!resource.data[$(this).attr(resource.options.prop)]){
			$(this).remove();
		    }
		});
	    }else{
		resource.call.push(resource.assess);
		resource.load();
	    }
	};
	
	return resource;
    }
    self.assess=function(){
	self.create().assess();
    }
    return self;
});
