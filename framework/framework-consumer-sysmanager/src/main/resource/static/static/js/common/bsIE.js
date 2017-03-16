if (!console) {
    console = {
        log: function () {
        }
    }
}
//IE检测工具
bsIE = function (version,op) {
    var b = document.createElement('b', op)
    b.innerHTML = '<!--[if '+op+' IE ' + version + ']><i></i><![endif]-->'
    return b.getElementsByTagName('i').length === 1;
};
bsltIE = function (version) {
    return bsIE(version, "lt");
};
var depend = [];
if (bsltIE(9)) {
    console.log("bs ie current ie version lt "+9+" !");
    depend = [
        {name: "html5shiv"},
        {name: "respond"}
    ];
}
modular.define({name: "bsIE"}, depend, function () {
    var self={};
    self.bsIE=bsIE;
    self.bsltIE=bsltIE;
    return self;
});