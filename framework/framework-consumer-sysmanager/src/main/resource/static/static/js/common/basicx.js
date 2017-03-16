var depend = [
    {name: "utils"},
    {name: "floatWindow"}, 	
    {name: "pagination"},
    {name: "hjkbase", type: "css"},
    {name: "hjkplug", type: "css"}
];
modular.define({name: "basicx"}, depend, function () {
    var self = {};
    var pagination = this.pagination;
    var utils = this.utils;

    var basics = {};
    self.create = function (id) {
        var obj = new basic(id);
        basics[id] = obj;
        return obj;
    };
    self.find = function (id) {
        return basics[id];
    };

    function basic(componentId) {
        var combasic = this;
        combasic.options = {};
        combasic.init = {};
        // 设置预定义url
        combasic.options.domain = "";
        // 定义插件名称
        // 设置预定义加载模块
        combasic.options.module = [
            "add", "edit", "del", "list", "check"
        ];

        //编写不在库中的局部函数
        //url处理函数
        var domainUrl = function (usedomain, url) {
            if (usedomain) {
                return combasic.options.domain + "/" + url
            }
            return url;
        }
        //新增、编辑 数据提交函数
        var dataSubmit = function () {
            var control = $(document).data("basic-options-" + componentId);
            //校验通过才执行
            if (control.dependent.isValidate()) {
                var ajaxOpts = {
                    type: control.type,
                    url: domainUrl(control.usedomain, control.url),
                    data: control.dependent.data(control),
                    dataType: control.dataType,
                    success: function (data) {
                        // 隐藏信息窗体
                        control.dependent.hideWindow(control).submitAfter(data, control);
                    }
                }
                if (control.ajaxOpts) {
                    ajaxOpts = $.extend(ajaxOpts, control.ajaxOpts)
                }
                $.secAjax(ajaxOpts);
            }
        };


        // 设置add功能相关基本参数
        combasic.add =
        {
            usedomain: true,
            url: "save",
            selector: {
                click: "[data-select=btn-add],#btn-add",
                submit: "#btn-save",
                form: "#datamodal [name]",
                modal: "#datamodal"
            },
            defaultValue: {},
            dataType: "json",
            type: "POST"
        };
        var controlAdd = combasic.add;
        // 设置add功能依赖函数
        combasic.add.dependent =
        {
            isValidate: function (control) {
                return true;
            },
            setDefVal: function (control) {
                if (typeof control.defaultValue == "function") {
                    utils.common.form.val($(control.selector.form), control.defaultValue.call(this));
                } else {
                    utils.common.form.val($(control.selector.form), control.defaultValue);
                }
                return this;
            },
            showWindow: function (control) {
                //$(control.selector.modal).modal("show");
                showWindow(control.selector.modal);
                return this;
            },
            showBefore: function (control) {
                return true;
            },
            showAfter: function (control) {
                return this;
            },
            data: function (control) {
                return utils.common.form.data($(control.selector.form));
            },
            hideWindow: function (control) {
                //$(control.selector.modal).modal("hide");
                hideWindow(control.selector.modal);
                return this;
            },
            submitAfter: function (data, control) {
                utils.messageBox.alert(
                    {
                        body: data.msg
                    });
                combasic.list.flush();
                return this;
            }
        };
        combasic.add.trigger = {};


        //定义添加的保存按钮事件
        combasic.add.trigger.submit = dataSubmit
        // 设置add功能事件触发函数
        combasic.add.trigger.click = function (event) {
            var control = controlAdd;
            if (control.dependent.showBefore(control)) {
                $(control.selector.submit).unbind("click.basic");
                // 设置默认值->打开窗口->执行打开窗口后续处理事件
                control.dependent.setDefVal(control).showWindow(control).showAfter(control);
                // 给保存按钮绑定保存触发事件
                $(control.selector.submit).bind("click.basic", control.trigger.submit);
                $(document).data("basic-options-" + componentId, control);
            }
        };


        // 设置edit功能相关基本参数
        combasic.edit =
        {
            usedomain: true,
            url: "save",
            gurl: "info",
            selector: {
                click: "[data-select=btn-edit],#btn-edit,#btn-edit-check",
                submit: "#btn-save",
                form: "#datamodal [name]",
                modal: "#datamodal"
            },
            dataType: "json",
            type: "POST",
            sendProp: {"data-id": "id"}
        };

        var controlEdit = combasic.edit;
        // 设置edit功能依赖函数
        combasic.edit.dependent =
        {
            isValidate: function (options) {
                return true;
            },
            setVal: function (data, options) {
                utils.common.form.val($(options.selector.form), data);
                return this;
            },
            showWindow: function (control) {
                //$(control.selector.modal).modal("show");
                showWindow(control.selector.modal);
                return this;
            },
            showBefore: function (data, control) {
                return true;
            },
            showAfter: function (data, control) {
                return this;
            },
            data: function (control) {
                return utils.common.form.data($(control.selector.form));
            },
            hideWindow: function (control) {
                //$(control.selector.modal).modal("hide");
                hideWindow(control.selector.modal);
                return this;
            },
            submitAfter: function (data, control) {
                utils.messageBox.alert(
                    {
                        body: data.msg
                    });
                combasic.list.flush();
                return this;
            }
        };
        combasic.edit.trigger = {};
        //定义添加的保存按钮事件
        combasic.edit.trigger.submit = dataSubmit;
        // 设置edit功能事件触发函数
        combasic.edit.trigger.click = function (event) {
            event.stopPropagation();

            var control = controlEdit;
            var id = saxDataId(this, control.sendProp);
            if (!id) {
                utils.messageBox.alert({body: "请先选择要编辑的记录!"});
                return;
            }
            if (id.length > 1) {
                utils.messageBox.alert({body: "不能同时编辑多条记录!"});
                return;
            }
            $.secAjax(
                {
                    type: 'POST',
                    url: domainUrl(control.usedomain, control.gurl),
                    data: id[0],
                    dataType: 'json',
                    success: function (data) {
                        if (control.dependent.showBefore(control)) {
                            $(control.selector.submit).unbind("click.basic");
                            // 设置默认值->打开窗口->执行打开窗口后续处理事件
                            control.dependent.setVal(data, control).showWindow(control).showAfter(data, control);
                            // 给保存按钮绑定保存触发事件
                            $(control.selector.submit).bind("click.basic", control.trigger.submit);
                            $(document).data("basic-options-" + componentId, control);
                        }
                    }
                });
        };


        // 设置del功能相关基本参数
        combasic.del =
        {
            usedomain: true,
            url: "del",
            selector: {
                click: "[data-select=btn-del],#btn-del,#btn-del-check"
            },
            dataType: "json",
            type: "POST",
            sendProp: {
                "data-id": "id"
            }
        };
        var controlDel = combasic.del;
        // 设置add功能依赖函数
        combasic.del.dependent =
        {
            submitBefore: function (datas) {
                return true;
            },
            submitAfter: function (data, options) {
                utils.messageBox.alert(
                    {
                        body: data.msg
                    });
                combasic.list.flush();
                return this;
            }
        };
        combasic.del.trigger = {};

        // 设置del功能事件触发函数
        combasic.del.trigger.click = function (event) {
            event.stopPropagation();
            var control = controlDel;
            var id = saxDataId(this, control.sendProp);
            if (!id) {
                utils.messageBox.alert({body: "请先选择要删除的记录!"});
                return;
            }
            if (!control.dependent.submitBefore(id))
                return false;
            utils.messageBox.show('操作提示', "确认删除记录?", {
                '确认': {
                    'primary': true,
                    'callback': function () {
                        $.secAjax(
                            {
                                type: control.type,
                                url: domainUrl(control.usedomain, control.url),
                                data: JSON.stringify(id),
                                dataType: control.dataType,
                                contentType: 'application/json;charset=utf-8',
                                success: function (data) {
                                    // 隐藏信息窗体
                                    control.dependent.submitAfter(data, control);
                                }
                            });
                    }
                },
                '取消': {
                    'primary': true,
                    'callback': function () {
                        utils.messageBox.hide();
                    }
                }
            });
        };

        combasic.list = {};
        combasic.list.id = componentId;
        combasic.list.usedomain = true;
        combasic.list.url = "list";

        combasic.list.pagination = {};
        combasic.list.flushAfter = function () {
            $("#btn-edit-check,#btn-del-check").each(function () {
                var _this = $(this);
                _.map(this.attributes, function (v) {
                    if (v.nodeName.indexOf("data-") == 0 && v.nodeName != "data-disable" && v.nodeName != "data-checked"&&(v.nodeName!="data-resource"&&v.nodeName!="data-action")) {
                        _this.attr(v.nodeName, "");
                    }
                });
            });
        };

        combasic.list.flushing = function () {
            pagination.find(combasic.list.id).load();
        };
        combasic.list.flushed = function () {
        };
        combasic.list.flush = function () {
            this.flushed();
            this.flushing();
            this.flushAfter();
        };
        combasic.list.page = {};


        combasic.check = {};
        combasic.check.selector={
        	container:"body",
        	btn:"#btn-edit-check,#btn-del-check"
        };
        combasic.check.callback = {
            changeBefore: function () {
                return true;
            },
            change: function () {
                if (!combasic.check.callback.changeBefore()) {
                    return false;
                }
                var values = {};
                $(this.selector.container).find(this.selector.check+":first").each(function(){
                    _.map(this.attributes, function (v) {
                        if (v.nodeName.indexOf("data-") == 0&&(v.nodeName!="data-resource"&&v.nodeName!="data-action")) {
                            if (!values[v.nodeName])
                                values[v.nodeName] = [];
                        }
                    });    
                });
                $(this.selector.container).find(this.selector.check + "[data-checked]").each(function () {
                    _.map(this.attributes, function (v) {
                        if (v.nodeName.indexOf("data-") == 0&&(v.nodeName!="data-resource"&&v.nodeName!="data-action")) {
                            if (!values[v.nodeName])
                                values[v.nodeName] = [];
                            values[v.nodeName].push(v.nodeValue);
                        }
                    });
                });
                _.map(values, function (v, k) {
                    values[k] = v.join(",");
                })
                $(combasic.check.selector.btn).attr(values);
                combasic.check.callback.changeAfter(values);
            },
            changeAfter: function (values) {
            }
        };


        // 初始化添加事件
        combasic.init.add = function (control) {
            // 给添加按钮绑定点击触发添加事件
            $("body").delegate(control.selector.click, "click", control.trigger.click);
        };
        // 初始化编辑事件
        combasic.init.edit = function (control) {
            $("body").delegate(control.selector.click, "click", control.trigger.click);
        };
        // 初始化删除事件
        combasic.init.del = function (control) {
            $("body").delegate(control.selector.click, "click", control.trigger.click);
        };
        combasic.init.list = function (control) {
            if (control.pagination.url === void 0)control.pagination.url = domainUrl(control.usedomain, control.url);
            var page = pagination.create(control.id, control.pagination);
            var tmpPage=control.page
            control.page=page;
            $.extend(control.page,tmpPage);
            control.page.load(1);
        };
        combasic.init.check = function (control) {
            // 初始化check组件
            $(control.selector.container).delegate(".case-checkbox", "click", function (event) {
//        	event.stopPropagation();
        	$(control.selector.container).find(".square-btn i[data-id='" + $(this).attr("data-id") + "']").click();
            });
            // 初始化checkbox组件
            utils.checkbox.init({
                selector: {container:control.selector.container,box:".square-btn-all i", check:".square-btn i"}, callback: {
                    change: combasic.check.callback.change
                }
            });
        };
        combasic.init.load = function () {
            var __this = this;
            _.map(combasic.options.module, function (v) {
                __this[v].call(this, combasic[v]);
            })
        };

        return this;
    };

    var saxDataId = function (dom, field) {
        var $dom = $(dom);
        var vs = _.map(field, function (v, k) {
            return {key: v, value: $dom.attr(k) ? $dom.attr(k).split(",") : []};
        });
        var rt = null;
        if (vs && vs[0] && vs[0].value.length > 0) {
            rt = [];
            for (var i = 0; i < vs[0].value.length; i++) {
                rt[i] = {};
                _.map(vs, function (t) {
                    rt[i][t.key] = t.value[i];
                })
            }
        }
        return rt;
    };


    return self;
});
