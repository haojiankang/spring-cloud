//多选支持需要引入的依赖
var depends = [
    {name: "bootstrap-select-cn"},
    {name: "utils"},
    {name: "underscore"}
];

modular.define({name: "combox"}, depends, function () {
    var keys = function (obj) {
        return _.map(obj, function (v, k) {
            return k;
        });
    };
    function newOption(k,v){
        return "<option value='"+v+"'>"+k+"</option>"
    };
    //改变下拉选项
    function changeSelectOptions(dom, data,change) {
        var options = "";
        if (data instanceof Array) {
            _.map(data, function (v, k) {
                if (typeof v != "object") {
                    options+=(newOption(v,v));
                } else {
                    options+=(newOption(keys(v)[0], v[keys(v)[0]]))
                }
            });
        } else {
            _.map(data, function (v, k) {
                options+=(newOption(k, k));
            });
        }
        var _this = dom;
        _this.options?_this.options.length = 0:0;
        $(_this).html(options);
        if(change)
            $(dom).change();
    }

    //初始化下拉框,并在初始化的时候触发change事件
    var initSelect = function (select, data, vs,change) {
        var dom = select.dom;
        if (data) {
            $(dom).data("options" + vs.toString(), data);
            select.data = data;
            changeSelectOptions(dom, data,change);
        } else {
            data = $(dom).data("options" + vs.toString());
            if (data) {
                changeSelectOptions(dom, data,change);
            } else {
                //设置发起的异步请求数据
                var sendData = {};
                var name = select.parent.asynOpts.name;
                ;
                if (typeof name == "string") {
                    sendData[name] = vs[vs.length - 1];
                } else {
                    _.map(name, function (v, k) {
                        sendData[v] = vs[k];
                    });
                }
                //获取数据并执行回调
                $.secAjax({
                    type: 'POST',
                    url: select.parent.asynOpts.url,
                    data: sendData,
                    dataType: 'json',
                    success: function (data) {
                        select.data=data;
                        changeSelectOptions(dom, data,change);
                    }

                });

            }

        }

    };
    //下拉框封装对象构造函数
    var Select = function (dom, data,change) {
        var _this = this;
        this.asynOpts = void 0;
        _this.dom = dom;
        _this.data = data;
        _this.children = null;
        _this.parent = null;
        _this.init = function () {
            with (_this) {
                if (children)
                    $(dom).change(function () {
                        var vs = [];
                        var p = _this;
                        while (p != null) {
                            vs.push(p.dom.value);
                            p = p.parent;
                        }
                        ;
                        vs.reverse();
                        if (data instanceof Array)
                            initSelect(children, void 0, vs,change);
                        else
                            initSelect(children, data[dom.value], vs,change);
                    });
                if (data) {
                    initSelect(_this, data, [],change);
                }
            }
        };
        return this;
    };
    var self = {};
    self.selects = {};
    /**
     * 级联下拉框
     * @param selectors 需要级联的下拉框的选择器
     * @param data 级联下拉列表数据
     */
    self.cascade = function (selectors, data, asynOpts,change) {
        var selects = [];
        for (var i = 0; i < 3; i++) {
            var select = new Select($(selectors[i]).get(0),void 0,change);
            if (selects[i - 1]) {
                selects[i - 1].children = select;
                select.parent = selects[i - 1];
            }
            if (asynOpts && asynOpts.length > i) {
                select.asynOpts = asynOpts[i];
            }
            selects.push(select);
        }
        selects[0].data = data;
        _.map(selects, function (v, i) {
            v.init();
            self.selects[selectors[i]] = v;
        });
    };
    /**
     * 改变下拉框的选择项
     * @param selector 要改变的下拉框的选择器
     * @param data 选择项数据
     */
    self.changeOptions = function (selector, data,firstOptions,change) {
	if(data instanceof Array){
        	if(firstOptions===true)
        	{
        	   data= [{"请选择":""}].concat(data)
        	} 
        	else if(firstOptions!==void 0){
        	    data=[firstOptions].concat(data)
        	}
	}
        var select = new Select($(selector).get(0), data,change);
        select.init();
    };
    /**
     * 设置选择框为多选下拉框
     * @param selector 要修改的下拉框的选择器
     */
    self.multiple = function (selector) {
        var _this = $(selector);
        $(selector).selectpicker('destroy');
        _this.attr({
            "data-live-search-placeholder": "Search",
            "multiple": "", "data-live-search": "true", "data-actions-box": "true"
        }).addClass("selectpicker");
        $.fn.selectpicker.call(_this, _this.data());
    };
    self.cvData=function(data,text,value){
	return _.map(data,function(v){
    	var obj={};
    	obj[v[text]]=v[value];
    	return obj;
        });
    };
    return self;

});