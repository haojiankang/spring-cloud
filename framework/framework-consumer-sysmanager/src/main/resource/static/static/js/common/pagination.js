var depend = [
    {name: "handlebars"},
    {name: "underscore"},
    {name: "jquery"},
    {name: "utils"}
];
modular.define({name: "pagination"}, depend, function () {
    var utils=this.utils;
    
    var self = {};
    self.footer = {
        PAGINATION_PAGE_CUSTOM: '<nav data-select="pageinfo" no="{{page}}" size="{{rows}}">'
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
        + '<li class="prompt"><pan>一共有{{records}}条，每页{{rows}}条，一共{{max}}页</span></li>'
        + '</ul></nav>'
    };
    var pages={};
    self.find=function(id){
        return pages[id];
    };
    self.load=function(id,pageNo, pageSize, dataFun, callback){
	self.find(id).load(pageNo, pageSize, dataFun, callback);
    }
    self.create = function (id,options) {
        var pagination = {};
        pages[id]=pagination;
        pagination.options = {
            template: {
                page: self.footer.PAGINATION_PAGE_CUSTOM,
                data: ""
            },
            selector: {
                data: "",
                page: ""
            },
            condition: function () {
                return {};
            },
            url: '',
            page:{
                size:10
            },
            pageNumber: 5,
            callback: function (page) {
            },
            callface:function(page){
        	return true;
            }
        };
        pagination.init = function (options) {
            this.options = $.extend(true, this.options, options);
        };
        // 刷新数据列表
        pagination.flushData = function (page) {
            var listInfo = Handlebars.compile(this.options.template.data);
            $(this.options.selector.data).html(listInfo(page));
        };
        // 刷新页码区域
        pagination.flushPageInfo=function (page) {
            var pageinfo = Handlebars.compile(this.options.template.page);
            $(this.options.selector.page).html(pageinfo(page));
        };
        // 计算最大页码、上一页、下一页、首尾页状态、数字页码。
        pagination.pageInit= function (page) {
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
        };

        // 绑定翻页事件
        pagination.bindEvent= function () {
            var _this = this;
            $(_this.options.selector.page + " [page]").each(function () {
                if (!$(this).hasClass("disabled")) {
                    $(this).click(function () {
                        _this.load($(this).attr("page"), _this.options.page.size);
                    });
                }
            });
        };
        // 查找page对象
        pagination.findPage=function (page) {
            if (page) {
                if (page.result) {
                    return page;
                }
                if (page.page && page.page.result!==void 0) {
                    return page.page;
                }
            }
            return page;
        };
        // 刷新页面数据
        pagination.flush= function (page, back) {
            if(this.options.callface(page)){
                var pageObj = this.findPage(page);
                this.pageInit(pageObj);
                this.flushData(pageObj);
                this.flushPageInfo(pageObj);
                this.bindEvent();
                // back为false则不执行回调函数
                back ? this.options.callback(page) : 0;
            }
        };

        // 加载指定页码和页容量的数据
        pagination.load=function (pageNo, pageSize, dataFun, callback) {
            utils.loading.show();
            var _this = this;
            dataFun = dataFun ? dataFun : function () {
                return {};
            };
            if (!pageNo && !pageSize) {
                pageNo = $(_this.options.selector.page+" [data-select=pageinfo]").attr("no");
                pageSize = $(_this.options.selector.page+" [data-select=pageinfo]").attr("size");
            }
            if (!pageSize) {
        	pageSize=_this.options.page.size;
            }
            utils.common.fn.sec_ajax(
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
                        utils.loading.hide();
                    }
                });
        };
        pagination.init(options);
        return pagination;
    };

    return self;

});