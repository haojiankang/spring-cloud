var depend = [
    {name: "handlebars"},
    {name: "underscore"},
    {name: "jquery"},
    {name: "bootstrap"},
    {name: "spin"}
];
modular.define({name: "utils"}, depend, function () {
    // 公共模版存放
    var template =
    {
        PAGINATION_PAGE_DEFAULT: '<div class="col-xs-12">'
        + '<div id="pageinfo" no="{{page}}" size="{{rows}}" >'
        + '<ul class="pagination">'
        + ' <li {{#if first}}class="disabled"{{/if}} page="1" ><span><span aria-hidden="true" >首页</span></span></li>'
        + ' <li {{#if first}}class="disabled"{{/if}} page="{{upper}}" ><span><span aria-hidden="true"  >«</span></span></li>'
        + '{{#each pages}}'
        + ' <li {{#if active}}class="active"{{/if}} page="{{page}}"><span aria-hidden="true">{{page}}</span></span></li>'
        + '{{/each}}'
        + ' <li {{#if last}}class="disabled"{{/if}} page="{{lower}}"><span><span aria-hidden="true">»</span></span></li>'
        + ' <li {{#if last}}class="disabled"{{/if}} page="{{max}}"><span><span aria-hidden="true">尾页</span></span></li>'
        + '</ul>' + '<ul class="pagination">' + ' <li><span><span>共{{records}}行</span></span></li>'
        + '</ul>' + '</div>' + '</div>',
         PAGINATION_PAGE_CUSTOM : '<nav id="pageinfo" no="{{page}}" size="{{rows}}">'
                    + '<ul class="pagination">'
                    + '<li {{#if first}}class="disabled"{{/if}} page="1">'
                    + '    <a href="javascript:void(0);" aria-label="Previous">'
                    + '        <span aria-hidden="true">&laquo;</span>'
                    + '    </a>'
                    + '</li>'
                    + '{{#each pages}}'
                    + '<li {{#if active}}class="active"{{/if}} page="{{page}}"><a href="javascript:void(0);">{{page}}</a></li>'
                    + '{{/each}}' + '<li {{#if last}}class="disabled"{{/if}} page="{{max}}">'
                    + '    <a href="javascript:void(0);" aria-label="Next">'
                    + '        <span aria-hidden="true">&raquo;</span>' + '    </a>' + '</li>'
                    + '<li class="gotoPage"><input type="text" /><button class="goto">跳转</button></li>'
                    + '<li class="prompt"><pan>共{{records}}条，每页{{rows}}条，共{{max}}页</span></li>'

                    + '</ul></nav>'

    };
    var self = {};
    self.namespace = "HJKAPP";
    // top顶层页面组件的引用
    self.top = {};
    // 公共方法
    self.common = {};
    self.data=function(key,value){
	if(top.data===void 0){
	    top.data={};
	}
	if(value===void 0){
	    var obj=top.data[key]
	    delete top.data[key];
	    return obj;
	}
	top.data[key]=value;
    }
    // url组件，获取根目录、获取url上的指定参数、给url增加参数、重写url给url增加hsessionid 和htoken
    self.common.url =
    {
        getRoot: function (url,fasten) {
            if(fasten&&loader.staticPath)
                return loader.staticPath;
            if(loader.dynamicPath)
                return loader.dynamicPath;
            // 项目部署到根目录时请将prefix修改为/
            var prefix = $("html").data("url.prefix");
            if (prefix) return prefix;
            return "/" + url.split("/")[3] + "/";
        },
        getParam: function (url, name) {
            var rtObj = "";
            if (url && url.indexOf("?") != -1) {
                var split = url.split("?")[1].split("&");
                if(name===void 0){
                    rtObj={};
                    for (var i=0;i<split.length;i++) {
                	var para=split[i].split("=")
                	rtObj[para[0]]=para[1];
                    }
                }else{
                    name = name + "=";
                    for (var i=0;i<split.length;i++) {
                	if (split[i].indexOf(name) != -1) {
                	    rtObj = split[i].replace(name, "");
                	    break;
                	}
                    }
                }
            }
            return rtObj;
        },
        addParam: function (url, data) {
            var param = _.map(data, function (v, k) {
                return k + "=" + v;
            }).join("&");
            if(param){
            	if (url.indexOf("?") != -1) {
            		url = url + "&" + param;
            	} else {
            		url = url + "?" + param;
            	}
            }
            return url;
        },
        rewrite: function (options) {
            if (typeof options == "string") {
                var topt =
                {
                    url: options
                };
                options = topt;
            }
            var url = location.href;
            // 给url增加前缀
            if(options.url.indexOf("http")!=0){
            	options.url = this.getRoot(url, options.fasten) + options.url;
            }
            var data =
            {};
            var htoken = this.getParam(url, "htoken");
            if (htoken != "") data.htoken = htoken;
            if(loader.urlRewriteSession){
            	data.hsessionid = $("body").attr("hsessionid");
            	if (!data.hsessionid) {
            		data.hsessionid = this.getParam(url, "hsessionid");
            	}
            }
            if (!options.timestamp) {
                data.timestamp = new Date().getTime();
            }
            options.url = this.addParam(options.url, data);
            return options.url;
        },
        reHtml:function(options){
            if(typeof options =='string'){
                options={url:options};
            }
            options.timestamp=!loader.htmlurltimestamp;
            options.fasten=true;
            this.rewrite(options);
            options.url+=(options.url.indexOf("?")!=-1?"&":"?")+loader.context.para;
            return options.url;
        }

    };
    self.common.form =
    {
        /**
		 * 转换普通对象为map参数对象
		 * 
		 * @param map
		 *            map参数名
		 * @param obj
		 *            待转换的对象
		 */
        cvMap: function (map, obj) {
            var rtObj =
            {};
            _.map(obj, function (val, key) {
                rtObj[map + "['" + key + "']"] = val;
            })
            return rtObj;
        },
        /**
		 * 给target中name和data中name一致的元素设置value值。 支持selectpicker，多选下拉组件
		 * 
		 * @param target
		 *            jquery对象，需要修改值的对象集合
		 * @param data
		 *            普通对象，值来源
		 */
        val: function (target, data) {
            target.each(function () {
                var t = $(this);
                if(data[t.attr("name")]===undefined){

                }else{
                    switch (t.attr("type")) {
                        case "radio":
                        case "checkbox":
                            this.checked = t.val() == data[t.attr("name")];
                            break;
                        default:
                            // 多选组件特殊处理
                            if (t.hasClass("selectpicker")) {
                                t.selectpicker('val', data[t.attr("name")]);// 默认选中
                                t.selectpicker('refresh');
                            } else {
                                t.val(data[t.attr("name")])
                            }
                            break;
                    }
                }


            });
        },
        /**
		 * 转换html格式的target为数据对象 支持entity[index].val的写法 最总会替换为entity[0].val
		 */
        data: function (target) {
            var dataObj =
            {};
            target.each(function () {
                var t = $(this);
                var val = t.val();
                var name = t.attr("name");
                var type = t.attr("type");
                if (val != undefined) {
                    switch (type) {
                        case "radio":
                        case "checkbox":
                            if (!this.checked) break;
                        default:
                            if (name.indexOf("[index]") != -1) {
                                _.each(val, function (element, index) {
                                    dataObj[name.replace("index", index)] = element;
                                })
                            } else {
                                dataObj[name] = val;
                            }
                    }
                }
            });
            return dataObj;
        },
        trim: function (target) {
            var inputs = target.find("input,textarea").not(":button,:submit,:reset,:image,:hidden,:disabled,[readonly]").filter(":visible");
            $(inputs).each(function (index, element) {
                $(element).val($.trim($(element).val()));
            });
        }
    };
    self.common.fn =
    {
        guid: function () {
            function S4() {
                return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
            }

            return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
        },
        /**
		 * ajax安全调用 在接收到返回数据后，会判断是否有提示信息。 如果状态为false且有提示信息，则不执行原success方法
		 */
        sec_ajax: function (options) {
            self.common.url.rewrite(options);
            var fun = options.success;
            options.successBefore = options.successBefore ? options.successBefore : function () {
                return true
            };
            options.failAfter = options.failAfter ? options.failAfter : function () {
                return false
            };
            options.success = function (data, textStatus, jqXHR) {
                if (data && (data.state === false || data.state === "false")) {
                    var msg=data.message?data.message:data.msg;
                    // 弹出提示信息
                    if (msg) {
                	self.messageBox.show('系统提示', msg, {
                            '确认': {
                                'primary': true,
                                'callback': function () {
                                    if(msg=="您的IP地址发生变化，请重新登陆！"||msg=="请先登录再进行操作！"||msg=="会话超时请重新登陆！"){
                                	top.location.href=loader.dynamicPath;
                                    }
                                }
                            }
                        });
                    }
                    options.failAfter(data, textStatus, jqXHR);
                } else {
                    if (options.successBefore(data, textStatus, jqXHR)) {
                        fun(data, textStatus, jqXHR);
                    }
                }
            }
            options.xhrFields =
            {
                withCredentials: true
            };
            $.ajax(options)
        },
        /**
		 * Ajax 文件下载
		 */
        download: function (url, data, method) {
            // 获取url和data
            if (url && data) {
                // data 是 string 或者 array/object
                data = typeof data == 'string' ? data : jQuery.param(data);
                // 把参数组装成 form的 input
                var inputs = '';
                jQuery.each(data.split('&'), function () {
                    var pair = this.split('=');
                    inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '"/>';
                });
                url = self.common.url.rewrite(url);
                // request发送请求
                jQuery('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>')
                    .appendTo('body').submit().remove();
            }
            ;
        },
        /**
		 * jQuery 动态表单提交
		 */
     postData: function (url, data, method) {
         // 获取url和data
         if (url && data) {
             // data 是 string 或者 array/object
             data = typeof data == 'string' ? data : jQuery.param(data);
             // 把参数组装成 form的 input
             var inputs = '';
             jQuery.each(data.split('&'), function () {
                 var pair = this.split('=');
                 inputs += '<input type="hidden" name="' + pair[0] + '" value="' + pair[1] + '"/>';
             });
             url = self.common.url.rewrite(url);
             // request发送请求
             jQuery('<form action="' + url + '" method="' + (method || 'post') + '">' + inputs + '</form>')
                 .appendTo('body').submit().remove();
         };
     }
    };
    /*
	 * self.HJKMessageBox={
	 * 
	 * <div class="floatbox" id="modal" target-draggable="flase"> <div
	 * class="left-block-blue crumb"></div> <button class="close"><i class="fa
	 * fa-times" aria-hidden="true"></i></button> <div class="title"> 标题
	 * </div> <hr class="splitline" /> <form> <div class="form-inline"> 内容...
	 * </div> <hr class="splitline" /> <div class="footbar"> <button
	 * class="btn-base confirm">确认</button> <button class="btn-base cancel">取消</button>
	 * </div> </form> </div> }
	 */
    self.messageBox = {
        modalContainerId: '#modal-container',
        modalBackgroundId: '#modal-background',
        modalMainId: '#confirm-modal',
        customButton: {
            '确定': {
                'primary': true,
                'callback': function () {
                    self.hide();
                }
            }
        },
        customEvent: null,

        init: function (size) {
            var self = this;
            var ElemHtml = '';

            $(self.modalMainId).remove();

            ElemHtml = '<div id="confirm-modal" class="modal fade role="dialog" tabindex="-1">' + '<div class="modal-dialog modal-' + size + '">' + '<div class="modal-content">' + '<div class="modal-header">' + '<button id="modal-upper-close" class="close modal-close" aria-label="Close" type="button">' + '<span aria-hidden="true">×</span>' + '</button>' + '<h4 id="modal-title" class="modal-title">系统提示</h4>' + '</div>' + '<div id="modal-body" class="modal-body"> Modal Message </div>' + '<div id="modal-footer" class="modal-footer">' + '</div>' + '</div>' + '</div>' + '</div>' + '<div id="modal-background" class=""></div>';

            $('body').append(ElemHtml);
        },

        addCustomButtons: function () {
            var self = this;
            var condition = true;

            $('.modal-custom-button').remove();

            closeButton = '';

            if (!self.customButton)
// closeButton = '<button id="modal-close" type="button" class="btn btn-default
// modal-custom-button">Close</button>';
// else
            {
                self.customButton = {
                    '确定': {
                        'primary': true,
                        'callback': function () {
                            self.hide();
                        }
                    }
                };
            }

            $.each(self.customButton, function (key, val) {
                buttonName = key.replace(/ /g, '');

                var ElemHtml = '';
                var ButtonState = 'btn-default';

                if (val['primary'])
                    ButtonState = 'btn-primary';
                if (buttonName.toLowerCase() == 'okay' || buttonName.toLowerCase() == 'ok')
                    closeButton = '';

                if (buttonName.toLowerCase() == 'delete' || buttonName.toLowerCase() == 'remove')
                    ButtonState = 'btn-danger';

                ElemHtml = closeButton + '<button id="button-' + buttonName.toLowerCase() + '" type="button" class="btn modal-custom-button ' + ButtonState + '">' + buttonName + '</button>';

                $('#modal-footer').append(ElemHtml);

                if ($('#modal-close'))
                    closeButton = '';

                self.addCustomButtonEvents(buttonName.toLowerCase(), val['callback']);
            });

            $('#modal-upper-close').unbind();
            $('#modal-upper-close').bind('click', function (e) {
                e.preventDefault();
                self.hide();
            });

            $('#modal-close').unbind();
            $('#modal-close').bind('click', function (e) {
                e.preventDefault();
                self.hide();
            });
        },

        addCustomButtonEvents: function (customButtonId, callback) {
            var self = this;

            $('#button-' + customButtonId).unbind();
            $('#button-' + customButtonId).bind('click', function (e) {
                e.preventDefault();
                if (!callback()) {
                    self.hide();
                }
            });
        },

        show: function (title, message, customEvent) {
            var self = this;

            if (title)
                $('#modal-title').html(title);

            if (message)
                $('#modal-body').html(message);

            self.customButton = customEvent;

            $(self.modalMainId).addClass('in');
            $(self.modalBackgroundId).addClass('modal-backdrop fade in');
            $(self.modalMainId).css({
                'display': 'block',
                'padding-right': '17px'
            });
            self.addCustomButtons();
        },
        alert: function (options) {
            var self = this;
            self.show(options.title, options.body);
        },
        hide: function () {
            var self = this;

            $(self.modalMainId).removeClass('in');
            $(self.modalBackgroundId).removeClass('modal-backdrop fade in');

            $(self.modalMainId).css('display', 'none');
        }
    };

    self.pagination =
    {
        options: {
            template: {
                page: template.PAGINATION_PAGE_CUSTOM,
                data: ''
            },
            selector: {
                data: "#hjk_listData",
                page: "#hjk_listPage"
            },
            condition: function () {
                return


                {
                }
                ;
            },
            url: '',
            pageNumber: 5,
            callback: function (page) {
            },
            callface: function (page) {
        	return true;
            }
        },
        opts: function (options) {
            this.options = $.extend(true, this.options, options);
        },
        // 刷新数据列表
        flushData: function (page) {
            var listInfo = Handlebars.compile(this.options.template.data);
            $(this.options.selector.data).html(listInfo(page));
        },
        // 刷新页码区域
        flushPageInfo: function (page) {
            var pageinfo = Handlebars.compile(this.options.template.page);
            $(this.options.selector.page).html(pageinfo(page));
        },
        // 计算最大页码、上一页、下一页、首尾页状态、数字页码。
        pageInit: function (page) {
            page['max'] = Math.ceil(page.records * 1.0 / page.rows);
            page['first'] = page.page == 1;
            page['upper'] = page.page - 1;
            page['lower'] = page.page + 1;
            page['last'] = page.page == page.max || page.max == 0;
            page['pages'] = [];
            // 设置开始页码为当前页减去页码数量的一半，保证当前也在中间
            var start = page.page - (Math.floor(this.options.pageNumber / 2));
            // 开始页面和最大页码之差小于pageNumber，则修改开始页码为最大页码减pageNumber+1
            start = page.max - start < this.options.pageNumber ? page.max - this.options.pageNumber + 1 : start;
            // 开始页码小于1则重置开始页码为1
            start = start < 1 ? 1 : start;
            for (; start <= page.max && page.pages.length < this.options.pageNumber; start++) {
                page['pages'].push(
                    {
                        "page": start,
                        "active": start == page.page
                    });
            }
        },
        // 绑定翻页事件
        bindEvent: function () {
            var _this = this;
            $(_this.options.selector.page + " [page]").each(function () {
                if (!$(this).hasClass("disabled")) {
                    $(this).click(function () {
                        _this.load($(this).attr("page"), $("#pageinfo").attr("size"));
                    });
                }
            });
        },
        // 查找page对象
        findPage: function (page) {
            if (page) {
                if (page.result) {
                    return page;
                }
                if (page.page && page.page.result!==void 0) {
                    return page.page;
                }
            }
            return page;
        },
        // 刷新页面数据
        flush: function (page, back) {
            if(this.options.callface(page)){
        	var pageObj = self.pagination.findPage(page);
        	this.pageInit(pageObj);
        	this.flushData(pageObj);
        	this.flushPageInfo(pageObj);
        	this.bindEvent();
        	// back为false则不执行回调函数
        	back ? this.options.callback(page) : 0;
            }
        },
        // 加载指定页码和页容量的数据
        load: function (pageNo, pageSize, dataFun, callback) {
            self.loading.show();
            var _this = this;
            dataFun = dataFun ? dataFun : function () {
                return {};
            };
            if (!pageNo && !pageSize) {
                pageNo = $("#pageinfo").attr("no");
                pageSize = $("#pageinfo").attr("size");
            }
            self.common.fn.sec_ajax(
                {
                    type: 'POST',
                    url: _this.options.url,
                    data: $.extend(
                        {
                            "page": pageNo,
                            "rows": pageSize
                        }, _this.options.condition(), dataFun()),
                    dataType: 'json',
                    success: function (page) {
                        callback = callback ? callback : function () {
                            return true;
                        }
                        _this.flush(page, callback(page));
                        self.loading.hide();
                    }
                });
        }

    };
    self.checkbox = {
        controller: function () {
            var switchSelect = function (obj, isSelected) {
                if (isSelected) {
                    obj.removeClass("fa-square-o");
                    obj.addClass("fa-check-square-o");
                    obj.attr("data-checked", "checked");
                } else {
                    obj.removeClass("fa-check-square-o");
                    obj.addClass("fa-square-o");
                    obj.removeAttr("data-checked");
                }
            }
            var controller = {
                init: function (selector, callback) {
                    var _this = this;
                    _this.selector = selector;
                    if(!selector.container)selector.container="body";
                    if (selector.box) {
                        $(selector.container).delegate(selector.box, "click", function (event) {
                            event.stopPropagation();
                            var checked = $(this).attr("data-checked");
                            $(this).each(function () {
                                switchSelect($(this), !checked);
                            });
                            $(selector.container).find(selector.check).each(function () {
                                switchSelect($(this), !checked);
                            });

                            if (callback && callback.change) {
                                callback.change.call(_this,this);
                            }
                        });
                    }
                    $(selector.container).delegate(selector.check, "click", function (event) {
                	event.stopPropagation();
                        var checked = $(this).attr("data-checked");
                        switchSelect($(this), !checked);
                        if (selector.box) {
                            var allchecked = true;
                            $(selector.container).find(selector.check).each(function () {
                                if (!$(this).attr("data-checked")) {
                                    allchecked = false;
                                    return false;
                                }
                            });
                            switchSelect($(selector.container).find(selector.box), allchecked);
                        }
                        if (callback && callback.change) {
                            callback.change.call(_this,this);
                        }
                        return false;
                    });

                }
            }
            return controller;
        },
        init: function (options) {
            this.controller().init(options.selector, options.callback);
        }
    };
    self.TableChoiceController = function () {
        var tableChoiceController = {
            selectAllBtnClass: null,
            checkboxLabelClass: null,
            containerClass: null,
            init: function (containerClass, selectAllBtnClass, checkboxLabelClass) {
                this.selectAllBtnClass = selectAllBtnClass;
                this.checkboxLabelClass = checkboxLabelClass;
                $(containerClass).delegate("." + checkboxLabelClass + " input", "click", function () {
                    tableChoiceController.checkboxChange(this);
                });

                $(containerClass).delegate("." + selectAllBtnClass, "click", function () {
                    tableChoiceController.allCheckboxChange(this);
                });

                this.containerClass = containerClass;

            },
            checkboxChange: function (obj) {
                tableChoiceController.setIconChange(obj);
                tableChoiceController.isSelectedAll();
            },
            setIconChange: function (obj) {
                var i = $(obj).parent().find("i");
                tableChoiceController.switchSelect(i, obj.checked);

            },
            allCheckboxChange: function (obj) {
                var allCheckboxBtn = $(tableChoiceController.containerClass + " ." + tableChoiceController.checkboxLabelClass + " input");
                var isSelected = !$(obj).find("i").hasClass("fa-check-square-o")
                if (isSelected) {
                    tableChoiceController.switchSelect($(obj).find("i"), true);
                } else {
                    tableChoiceController.switchSelect($(obj).find("i"), false);
                }

                for (var i = 0; i < allCheckboxBtn.length; i++) {
                    allCheckboxBtn[i].checked = isSelected;
                    tableChoiceController.setIconChange(allCheckboxBtn[i], allCheckboxBtn[i].checked)
                }

            },
            isSelectedAll: function () {
                var allBtn = $(tableChoiceController.containerClass + " .square-btn input");
                var choiceBtnCount = 0;
                for (var j = 0; j < allBtn.length; j++) {
                    if (allBtn[j].checked) {
                        choiceBtnCount++;
                    }
                }
                var selectAllBtnClass = $(tableChoiceController.containerClass + " .square-btn-all");
                if (choiceBtnCount == allBtn.length) {
                    tableChoiceController.switchSelect($(selectAllBtnClass).find("i"), true);
                } else {
                    tableChoiceController.switchSelect($(selectAllBtnClass).find("i"), false);
                }
            },
            switchSelect: function (obj, isSelected) {
                if (isSelected) {
                    obj.removeClass("fa-square-o");
                    obj.addClass("fa-check-square-o");
                } else {
                    obj.removeClass("fa-check-square-o");
                    obj.addClass("fa-square-o");
                }
            }
        }
        return tableChoiceController;
    };

    self.BaseTree = function () {
        tree = {
            data: null,
            containerid: null,
            liClick: function () {
            },
            init: function (data, containerid) {
                this.data = data;
                this.containerid = containerid;
                // 创建树
                var ul = document.createElement("ul");
                this.create(data, ul);
                // 把树添加到网页
                var container = document.getElementById(containerid);
                container.appendChild(ul);

                $("ul").delegate("li", "click", function (e) {
                    tree.liClick();
                    e.stopPropagation();
                });

                $("ul").delegate("._slideToggle", "click", function () {
                    var _i = $(this);
                    var _div = $(this).parent().find("div").eq(0);

                    $(_div).slideToggle(200, function () {
                        // var _i=$(_li).find("i").eq(0)
                        if ($(this).is(':hidden')) {
                            $(_i).removeClass("fa-caret-down");
                            $(_i).addClass("fa-caret-right");
                        } else {
                            $(_i).removeClass("fa-caret-right");
                            $(_i).addClass("fa-caret-down");
                        }
                    });
                    return false;
                })

            },
            // 注意这里包含_ul和__ul;一个是一条下划线一个是两条下划线
            create: function (_data, _ul) {
                console.log("1")
                var _li = document.createElement("li");
                // $(_li).addClass("_slideToggle")

                if (_data.children.length > 0) {
                    var icon_folder = document.createElement("i");
                    $(icon_folder).addClass("fa fa-folder");
                    _li.innerHTML = "<i class='fa fa-caret-right _slideToggle' aria-hidden='true'></i><i class='fa fa-folder'></i>" + _data.name;
                    +"";
                    var __div = document.createElement("div");
                    $(__div).css({"display": "none"});
                    for (var i = 0; i < _data.children.length; i++) {

                        var __ul = document.createElement("ul");
                        $(__div).append(__ul);
                        _li.appendChild(__div);
                        this.create(_data.children[i], __ul);
                    }
                } else {
                    _li.innerHTML = "<i class='fa fa-file'></i>" + _data.name;
                    +"";
                }
                _ul.appendChild(_li);
            }
        }
        return tree;

    }

    self.pageOpts = function (options) {
        this.pagination.opts(options);
        return this;
    };
    self.pageLoad = function (pageNo, pageSize, dataFun, callback) {
        this.pagination.load(pageNo, pageSize, dataFun, callback);
        return this;
    };
    self.pageFlush = function () {
        this.pagination.load();
        return this;
    };
    self.alert = function (options) {
        this.messageBox.alert(options);
    };

    /* 拖拽开始 */
    self.drag=function(){
    	var params = {
    		right: 0,
    		top: 0,
    		currentX: 0,
    		currentY: 0,
    		flag: false
    	};
    	var isIE=(!!window.ActiveXObject || "ActiveXObject" in window);
    	console.log("---------------------------"+isIE);
    	var mask;
    	// 获取相关CSS属性
    	var getCss = function(o,key){
    		return o.currentStyle? o.currentStyle[key] : document.defaultView.getComputedStyle(o,false)[key];
    	};
    	// 创建一个遮罩div
    	var createMaskDiv=function(){
    		if(!mask){
    			mask=document.createElement("div");
    		}
    		mask.style.position="absolute";
    		mask.style.width="100%";
    		mask.style.height="100%";
    		mask.style.zIndex="100000";
    		mask.style.right="0px";
    		mask.style.top="0px";
    		mask.style.right="0px";
    		mask.style.button="0px";
    		mask.style.background="#fff";
    		mask.style.filter="alpha(opacity=0)";
    		mask.style.opacity="0";
    		document.body.appendChild(mask)
    	}
    	var deleteMaskDiv=function(){
    		if (isIE)  {
    			if(mask){
    				mask.removeNode(true);
    			}

        	}else{
        		if(mask){
        			mask.remove();
        		}

        	}
    	}





    	// 拖拽的实现
    	var startDrag = function(bar, target, callback){
    		if(getCss(target, "right") !== "auto"){
    			params.right = getCss(target, "right");
    		}
    		if(getCss(target, "top") !== "auto"){
    			params.top = getCss(target, "top");
    		}


    		// o是移动对象
    		bar.onmousedown = function(event){
    			if(isIE){
    				createMaskDiv();// 点击创建遮罩
    			}
    			params.flag = true;
    			if(!event){
    				event = top.event;
    				// 防止IE文字选中
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
    			if(getCss(target, "top") !== "auto"){
    				params.top = getCss(target, "top");
    			}

    			if(isIE){
    				deleteMaskDiv();// 松开鼠标删除遮罩
    			}
    		};
    		document.onmousemove = function(event){
    			var e = event ? event: top.event;

    			if(params.flag){
    				var nowX = e.clientX, nowY = e.clientY;
    				var disX = nowX - params.currentX, disY = nowY - params.currentY;


    				target.style.right = parseInt(params.right) - disX + "px";
    				target.style.top = parseInt(params.top) + disY + "px";
    			}

    			if (typeof callback == "function") {
    				callback(parseInt(params.right) + disX, parseInt(params.top) + disY);
    			}
    		}
    	};

    	var self={
    		startDrag:startDrag
    	}

    	return self;
    }

    var isIE=(!!window.ActiveXObject || "ActiveXObject" in window);
    /* 拖拽结束 */
    self.loading={
	    cache:{
		
	    },
	    defArgs:{
		name:"loading",
		target:"body"
	    }
	    ,
	    defOpts:{
		  lines: 17 // The number of lines to draw
			, length: 6 // The length of each line
			, width: 6 // The line thickness
			, radius: 20 // The radius of the inner circle
			, scale: 1 // Scales overall size of the spinner
			, corners: 1 // Corner roundness (0..1)
			, color: '#FFF' // #rgb or #rrggbb or array of colors
			, opacity: 0.25 // Opacity of the lines
			, rotate: 0 // The rotation offset
			, direction: 1 // 1: clockwise, -1: counterclockwise
			, speed: 1 // Rounds per second
			, trail: 60 // Afterglow percentage
			, fps: 20 // Frames per second when using setTimeout() as a
						// fallback for CSS
			, zIndex: 2e9 // The z-index (defaults to 2000000000)
			, className: 'spinner' // The CSS class to assign to the spinner
			, top: '50%' // Top position relative to parent
			, left: '50%' // Left position relative to parent
			, shadow: false // Whether to render a shadow
			, hwaccel: false // Whether to use hardware acceleration
			, position: 'relative' // Element positioning
			},
	    show:function(args){
		var _this=this;
		args=_.extend({},args,_this.defArgs);
		
		if(_this.cache[args.name]&&!_this.cache[args.name].isClose){
		    _this.cache[args.name].close();
		}
		
		var opts={};
		opts=_.extend({},opts,_this.defOpts,args.opts);
		
		var mask=document.createElement("div");
		mask.style.position="absolute";
		mask.style.width="100%";
		mask.style.height="100%";
		mask.style.zIndex=2e9-1;
		mask.style.left="0px";
		mask.style.top="0px";
		mask.style.buttom="0px";
		mask.style.background="#000";
		mask.style.filter="alpha(opacity=50)";
		mask.style.opacity="0.5";
		
		var target = $(args.target)[0];
		target.appendChild(mask);
		var spinner = new Spinner(opts).spin(mask);
		var load={
			isClose:false,
			close:function(){
			    this.isClose=true;
			    setTimeout(function(){
				spinner.stop();
				    if (isIE)  {
			                    mask.removeNode(true);
			            }else{
			                    mask.remove();
			            }
			    },500);
			}
		};
		_this.cache[args.name]=load;
	    },hide:function(args){
		var _this=this;
		args=_.extend({},args,_this.defArgs);
		if(_this.cache[args.name]&&!_this.cache[args.name].isClose){
		    _this.cache[args.name].close();
		}
	    }
    }



    self.messageBox.init();
    $.extend(
        {
            hjk: self,
            secAjax: function (options) {
                self.common.fn.sec_ajax(options);
            },
            urlReWrite: function (options) {
                return self.common.url.rewrite(options);
            },
            htmlUrl:function(options){
                return self.common.url.reHtml(options);
            },
            hform: self.common.form,
            topFun: function () {
                return self.topFun;
            },
            drag:self.drag
        });
    self.dataDic={
	    bind:function(name,value){
		if(name=="bind"||name=="unbind"||name=="keys")
		    throw "datadic not set bind|unbind|keys ！";
		var obj={};
		_.map(value,function(v){
		    _.extend(obj,v);
		});
		this[name]=obj;
	    },
	    unbind:function(name){
		if(name=="bind"||name=="unbind"||name=="keys")
		    throw "datadic not set bind|unbind|keys ！";
		this[name]=void 0;
	    },
	    keys:function(){
		var keys=[];
		for(var i in this){
		    if(i=="bind"||i=="unbind"||i=="keys")
			continue;
		    keys.push(i);
		}
		return keys;
	    }

    };
    // 定义字典格式化显示函数，适合[{key:name}]格式
    Handlebars.registerHelper('_dataVal', function (name, k, options) {
        return self.dataDic[name][k];
    });
    // 定义字典格式化显示函数，适合[{name:key}]格式
    Handlebars.registerHelper('_dataKey', function (name, v, options) {
        return _.invert(self.dataDic[name])[v];
    });
    // 注册数值增加的helper
    Handlebars.registerHelper("_AddNum",function(v, num){
        return v + num;
    });
    self.common.date={
	    	format:function(target,fmt){
	    		 var result = fmt;
	    		 // target 未Date 类型,fmt 未格式话
	    		 // 对Date的扩展，将 Date 转化为指定格式的String
	    		 // 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符，
	    		 // 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字)
	    		 // 例子：
	    		 // (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==>
					// 2006-07-02 08:09:04.423
	    		 // (new Date()).Format("yyyy-M-d h:m:s.S") ==> 2006-7-2
					// 8:9:4.18
	    		 var o = {
			         "M+": target.getMonth() + 1, // 月份
			         "d+": target.getDate(), // 日
			         "h+": target.getHours(), // 小时
			         "m+": target.getMinutes(), // 分
			         "s+": target.getSeconds(), // 秒
			         "q+": Math.floor((target.getMonth() + 3) / 3), // 季度
			         "S": target.getMilliseconds() // 毫秒
	    		 };
	    		 if(/(y+)/.test(fmt)){
	    			 result = result.replace(RegExp.$1, (target.getFullYear() + "").substr(4 - RegExp.$1.length));
	    		 }
	    		 for(var k in o){
	    			 if(new RegExp("(" + k + ")").test(fmt)){
	    				 result = result.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    			 }
	    		 }
	    		 return result;
	    	}
	    };
    return self;

});