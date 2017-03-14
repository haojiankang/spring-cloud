var depends = [
    {name: "utils"},
    {name: "resource"},
    {name: "bootstrap-select-cn"},
    {name: "ztree"},
    {name: "combox"},
    {name:"divscroll"}
];


modular.define({name: "statistical.report"}, depends, function () {
    var SCINAME="USER-SQL";
    $.secAjax(
            {
                type: 'POST',
                url: "statistical/load/"+SCINAME,
                dataType: 'json',
                success: function (data) {
                    //渲染条件区域
                    //渲染列表区域-表头
                }
            });
    
});