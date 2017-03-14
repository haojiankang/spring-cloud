var loader, modular;
var m;
if (!window.console){
    var names = ["log", "debug", "info", "warn", "error", "assert", "dir", "dirxml", "group", "groupEnd", "time", "timeEnd", "count", "trace", "profile", "profileEnd"];

    window.console = {};
    for (var i = 0; i < names.length; ++i)
        window.console[names[i]] = function() {}
};
(function () {
    //私有工具对象
    var _ = {};
    //私有对象
    var _private = {};
    //模块容器
    var container = {};
    //加载器,提供加载模块功能
    loader = {};
    //模块句柄,记录已经加载的模块
    modular = {};
    window.modular=modular;
    
    //v1.0.0.0-提供基本的异步加载JS、CSS，模块在依赖模块加载完成后再进行加载
    modular.version = "v1.0.1.1";
    //给modular对象增加IE版本识别函数
    modular.IE={
	data:{},
	test:function(version,op){
	    if(!op)op='';
	    if(this.data[version+op]===void 0){
		var b = document.createElement('b', op)
		b.innerHTML = '<!--[if '+op+' IE ' + version + ']><i></i><![endif]-->'
		return b.getElementsByTagName('i').length === 1;
	    }
	    return this.data[version+op];
	},
	lt:function(version){
	    return this.test(version,"lt");
	},
	gt:function(version){
	    return this.test(version,"gt");
	},
	isIE:function(){
	    return "ActiveXObject" in window;
	}
    };
    modular.topModular=function(){
	var win=window;
	try{
		while(win.parent==null||!win.parent.modular||win!==win.parent){
		    win=win.parent;
		}
	}catch(e){
		console.log(e);
	}
	return win.modular;
    };
    modular.find=function(name){
	return container.modularsHandle[name];
    };
    //使用到的工具函数
    _.head = document.getElementsByTagName("head")[0];
    _.script = function () {
        return document.getElementsByTagName('script');
    };
    _.each = function (target, fun) {
        if (!target)return void 0;
        if (target instanceof Array) {
            for (var i = 0; i < target.length; i++)
                if (fun(target[i], i) == true)
                    return i;
        } else {
            for (var i in target)
                if (fun(target[i], i) == true)
                    return i;
        }
        return void 0;
    };
    _.eachReverse = function (ary, func) {
        if (ary) {
            var i;
            for (i = ary.length - 1; i > -1; i -= 1) {
                if (ary[i] && func(ary[i], i, ary)) {
                    break;
                }
            }
        }
    };
    _.find = function (target, fun) {
        var i = this.each(target, fun);
        if (i != void 0)
            return target[i];
        return void 0;
    };
    _.toString = function (obj) {
        if (typeof obj == "object") {
            var array = [];
            _.each(obj, function (v, k) {
                array.push(k + ":" + v);
            });
            return array.join(" , ");
        }
        return obj;
    };
    //克隆对象
    _.clone = function () {
        var options, name, copy, clone,
            target = {},
            length = arguments.length;
        // 开始遍历需要被扩展到target上的参数
        for (var i = 0; i < length; i++) {
            if ((options = arguments[i]) != null) {
                // 遍历第i个对象的所有可遍历的属性
                for (name in options) {
                    // 得到被扩展对象的值
                    clone = target[name];
                    copy = options[name];
                    if (target === copy) {
                        continue;
                    }
                    // 递归合并
                    if (copy && typeof copy == "object") {
                        if (clone == void 0) {
                            if (copy instanceof Array)
                                clone = [];
                            else
                                clone = {};
                        }

                        // 递归调用copy方法，继续进行深度遍历
                        _.copy(clone, copy);
                        target[name] = clone;
                        // 递归调用clone方法，继续进行深度遍历
                        target[name] = _.clone(copy);
                    } else {
                        target[name] = copy;
                    }
                }
            }
        }
        return target;
    };
    //复制对象
    _.copy = function () {
        var options, name, copy, clone,
            target = arguments[0],
            length = arguments.length;
        // 开始遍历需要被扩展到target上的参数
        for (var i = 1; i < length; i++) {
            if ((options = arguments[i]) != null) {
                // 遍历第i个对象的所有可遍历的属性
                for (name in options) {
                    // 得到被扩展对象的值
                    clone = target[name];
                    copy = options[name];
                    if (target === copy) {
                        continue;
                    }
                    // 递归合并
                    if (copy && typeof copy == "object") {
                        if (clone == void 0) {
                            if (copy instanceof Array)
                                clone = [];
                            else
                                clone = {};
                        }

                        // 递归调用copy方法，继续进行深度遍历
                        _.copy(clone, copy);
                        target[name] = clone;
                    } else {
                        target[name] = copy;
                    }
                }
            }
        }
    };
    //创建节点
    _.createNode = function (tagName, options) {
        var node = document.createElement(tagName);
        _.copy(node, options);
        console.log("create node: "+_.toString(options));
        return node;
    };


    //工具函数定义结束

    //模块信息对象
    var Modular = function (options) {
        options = _.clone({
            type: "js",
            package: "",
            name: "",
            loadFile: false,
            load: false,
            lazy: true
        }, options);
        options.allName = options.type + "/" + options.package + "/" + options.name;
        options.simpleName = options.allName.replace(options.type + "/", "").replace(/^\//, "");
        //handle未定义的情况下，已经new的对象不再new创建
        if (container.modulars[options.allName] != void 0)
            return container.modulars[options.allName];
        //没有new过的模块之间初始化，并记录到container中
        var self = this;
        _.copy(self, options);
        self.path = loader.context.getPath(self.type, self.simpleName);
        self.equals = function (obj) {
            if (obj == self)return true;
            if (obj.package == self.package && obj.name == self.name)return true;
        };
        self.getName = function () {
            return self.allName;
        };
        self.getSimpleName = function () {
            return self.simpleName;
        };
        container.modulars[self.allName] = self;
        return self;
    };
    //模块容器定义
    container.modulars = {};
    container.modularsHandle = {};
    //记录模块引用了哪些模块
    container.quote = {};
    //记录模块被哪些模块引用
    container.referenced = {}
    //设置两个模块的依赖关系
    container.bind = function (mod, refMod) {
        var self = this;
        //记录被引用关系
        var ref = self.referenced[refMod.getName()];
        if (ref == void 0) {
            ref = {}
        }
        ref[mod.getName()] = mod;
        self.referenced[refMod.getName()] = ref;
        //记录引用关系
        var quo = self.quote[mod.getName()];
        if (quo == void 0) {
            quo = {};
        }
        quo[refMod.getName()] = refMod;
        self.quote[mod.getName()] = quo;
    };
    //模块加载完成调用方法
    container.loadSuccess = function (mod) {
        var self = this;
        //查找应用了该模块的模块信息
        var refMod = self.referenced[mod.getName()];
        if (refMod != void 0) {
            _.each(refMod, function (v) {
                self.callMod(v);
            });
        }
    };
    //尝试加载指定模块
    container.callMod = function (mod) {
        var self = this;
        //如果模块已经被加载，就不再加载
        if (mod.load) {
            return;
        }
        //查找引用到的模块
        var quo = self.quote[mod.getName()];
        //没有引用到的模块直接加载
        var quo = _.find(quo, function (v) {
            return !v.load;
        });
        if (quo != void 0) {
            self.callMod(quo);
        } else {
            self.load(mod);
        }
    };
    container.load = function (mod) {
        var self = this;
        if (mod.load || mod.handle == void 0)
            return;
        self.modularsHandle[mod.getSimpleName()] = mod.handle == void 0 ? void 0 : mod.handle(self.modularsHandle);
        mod.load = true;
        self.loadSuccess(mod);
    };
    container.nodeOnLoad = function (mod) {
//	console.log("node on load:"+mod.getSimpleName())
        var self = this;
        //非标准模块，在脚本加载完成的时候就算作模块已经加载成功
        var findRes = _.find(loader.context.noModular, function (v) {
            return v == mod.getSimpleName();
        });
        if (findRes != void 0) {
            mod.load = true;
            self.loadSuccess(mod);
        }
    };


    /**
     * 模块定义函数
     * @param config 定义当前模块的属性，包名(package),模块名称(name),类型(type)
     * @param depend 定义引用到的模块
     * @param construct 模块定义函数
     */
    modular.define = function (mod, ref, construct) {
        var refMod;
        //初始化模块
        mod = new Modular(mod);
        //延迟加载模式，有引用模块先加载引用到的模块
        var jsMods = [];
        var cssMods = [];
        if (ref && ref.length > 0) {
            _.each(ref, function (v) {
                refMod = new Modular(v);
                if (refMod.type == "js") {
                    //设置依赖关系
                    container.bind(mod, refMod);
                    jsMods.push(refMod);
                } else if (refMod.type == "css") {
                    cssMods.push(refMod);
                }
            });
        }
        _.each(cssMods, function (v) {
            if (!v.lazy)
                loader.load(v);
        });
        mod.handle = function (handle) {
            //先加载css模块
            _.each(cssMods, function (v) {
                loader.load(v);
            });
            //执行模块构造函数
            var ret = construct.call(handle);
            if(mod.equals(loader.mainModular)){
                loader.endLoad();
            }
            return ret;
        };
        _.each(jsMods, function (v) {
            loader.load(v);
        });
        //启用懒加载模式，先加载引用到的模块
        container.callMod(mod);
    };
    //第三方文件直接加载方法
    modular.loadLib = function (mod) {
        mod = new Modular(mod);
        loader.load(mod);
    };

    //加载器上下文
    loader.context = {
        //记录加载过的模块
        loaded: {
            css: {},
            js: {}
        },
        baseUrl: "",
        js: {},
        css: {},
        para: {},
        noModular: [],
        depend: {},
        put: function (options) {
            var self = this;
            _.copy(self.js, options.js);
            _.copy(self.css, options.css);
            _.copy(self.depend, options.depend);
            if (options.baseUrl)
                self.baseUrl = options.baseUrl;
            if (options.para)
                self.para = options.para;
            if (options.noModular) {
                for (var i = 0; i < options.noModular.length; i++) {
                    self.noModular.push(options.noModular[i]);
                }
            }

        },
        getPath: function (type, name) {
            var self = this;
            return self.baseUrl + self[type][name] + (self.para ? "?" + self.para : "");
        }
    };
    loader.configCount = 0;
    loader.configSuccess = function () {
        loader.configCount--;
        if (loader.configCount == 0)
            loader.loadMain();
    };
    //模块加载完成调用方法
    loader.loadSuccess = function (mod) {
        var self = this;
        mod.loadFile = true;
        //查找应用了该模块的模块信息
        var refMod = container.referenced[mod.getName()];
        if (refMod != void 0) {
            _.each(refMod, function (v) {
                self.load(v);
            });
        }
    };
    //远程加载文件
    loader.load = function (mod) {
        var self = this;
        var loaded = loader.context.loaded[mod.type][mod.path];
        //已经加载过的文件不再加载
        if (loaded != void 0)
            return;
        if (mod.type == "js") {
            var depends = self.context.depend[mod.getSimpleName()];
            if (depends != void 0) {
                _.each(depends, function (v) {
                    container.bind(mod, new Modular(v));
                })
            }
            //查找引用到的模块
            var quo = container.quote[mod.getName()];
            //没有引用到的模块直接加载
            var quo = _.find(quo, function (v) {
                return !v.loadFile;
            });
            if (quo != void 0) {
                self.load(quo);
                return;
            }
        }
        var node, tagName, nodeProp = {};
        if (mod.type == "js") {
            nodeProp.async = true;
            nodeProp.charset = "utf-8";
            nodeProp.type = "text/javascript";
            nodeProp.src = mod.path;
            tagName = "script";
        } else if (mod.type == "css") {
            nodeProp.rel = "stylesheet";
            nodeProp.type = "text/css";
            nodeProp.href = mod.path;
            tagName = "link";
        }
        node = _.createNode(tagName, nodeProp);
        if (node) {
            if (node.attachEvent && !(node.attachEvent.toString && node.attachEvent.toString().indexOf('[native code') < 0))
            {
                node.attachEvent('onreadystatechange', function () {
                    if(node.readyState == "complete"||node.readyState == "loaded"){
                        self.loadSuccess(mod);
                        if (mod.type == "js" && mod instanceof Modular) {
                            container.nodeOnLoad(mod);
                        }
                    }
                });
            } else {
                node.addEventListener('load', function () {
                        self.loadSuccess(mod);
                        if (mod.type == "js" && mod instanceof Modular) {
                            container.nodeOnLoad(mod);
                        }
                }, false);
                node.addEventListener("error", function (error) {
                    console.log("load modular " + mod.name + " error,mod.toString: " + _.toString(mod));
                });
            }
            _.head.appendChild(node);
        }
        loader.context.loaded[mod.type][mod.path] = node;
        return node;
    };
    loader.loadConfig = function (options) {
        loader.configCount++;
        var self = this;
        var loaded = loader.context.loaded[options.type][options.path];
        //已经加载过的文件不再加载
        if (loaded != void 0)
            return;
        var node, tagName, nodeProp = {};
        if (options.type == "js") {
            nodeProp.async = true;
            nodeProp.charset = "utf-8";
            nodeProp.type = "text/javascript";
            nodeProp.src = options.path;
            tagName = "script";
        } else if (options.type == "css") {
            nodeProp.rel = "stylesheet";
            nodeProp.type = "text/css";
            nodeProp.href = options.path;
            tagName = "link";
        }
        node = _.createNode(tagName, nodeProp);
        if (node)
            _.head.appendChild(node);
        if (options.type == "js" && options instanceof Modular) {
            node.addEventListener('load', function () {
                container.nodeOnLoad(mod);
            }, false);
        }
        ;
        loader.context.loaded[options.type][options.path] = node;
        return node;
    };


    //记录程序主入口的文件路径
    var main = "";
    var config = "";
    _.eachReverse(_.script(), function (tag) {
        var dataMain = tag.getAttribute('data-main');
        var dataCfg = tag.getAttribute('data-config');
        if (dataMain)main = dataMain;
        if (dataCfg)config = dataCfg;
        return main && config;
    });
    //加载程序入口，只能有一个入口
    //加载等待提示控件开始
    var loading_box;
    var progress_word;
    var progress_box;
    var progress_bar;
    var full_bg;
    var timer;
    var step=0;
    var loading={
        create:function(){
            //创建元素
            loading_box=document.createElement("dl");
            progress_word=document.createElement("dt");
            progress_box=document.createElement("dd");
            progress_bar=document.createElement("div");
            full_bg=document.createElement("div");
            //创建元素关系
            loading_box.appendChild(progress_word);
            loading_box.appendChild(progress_box);
            progress_box.appendChild(progress_bar);
            //添加提示文字
            progress_word.innerHTML="加载中...";
            /*定义元素样式*/
            //背景样式
            full_bg.style.position="fixed";
            full_bg.style.left="0px";
            full_bg.style.top="0px";
            full_bg.style.bottom="0px";
            full_bg.style.right="0px";
            full_bg.style.background="#fefefe";
            full_bg.style.zIndex="999999";
            //加载容器样式
            loading_box.style.width="200px";
            loading_box.style.height="32px";
            loading_box.style.position="fixed";
            loading_box.style.left="50%";
            loading_box.style.marginLeft="-100px";
            loading_box.style.top="200px";
            loading_box.style.zIndex="1000000";
            loading_box.style.marginTop="0px";
            loading_box.style.marginBottom="0px";
            //进度容器样式
            progress_box.style.width="200px";
            //progress_box.style.height="10px";
            progress_box.style.border="#bcbcbc 1px solid";
            progress_box.style.background="#e6e6e6";
            progress_box.style.padding="1px";
            progress_box.style.borderRadius="3px";
            progress_box.style.margin="0px";
            //进度条样式
            progress_bar.style.width="0%";
            progress_bar.style.height="10px";
            progress_bar.style.background="#26a0da";
            progress_bar.style.borderRadius="3px";
            //提示文字样式
            progress_word.style.marginBottom="5px";
            progress_word.style.fontSize="12px";
            progress_word.style.color="#939393";
            //设置加载条默认样式
            var windowHeight = document.documentElement.clientHeight;
            var _top = (windowHeight - 32-20) / 2;//20是文字高度
            loading_box.style.top = _top + "px";

        },
        show:function(){
            if(!loading_box){
                this.create();
            }
            //添加元素到网页
            document.getElementsByTagName("html")[0].appendChild(loading_box);
            document.getElementsByTagName("html")[0].appendChild(full_bg)
        },
        hide:function(){
        	var navigatorName;
            if(loading_box){
            	if (!!window.ActiveXObject || "ActiveXObject" in window)  {
            		loading_box.removeNode(true);
            		full_bg.removeNode(true);
            	}else{
            		loading_box.remove();
                    full_bg.remove();

            	}
                
            }
            if(timer){
                clearInterval(timer);
            }
        },
        autoOnceLoad:function(time){
            var onceTime=time/100;
            var setProgress=this.setProgress;
            //清除已经存在的timer
            if(timer){
                clearInterval(timer);
            }
            timer=setInterval(function(){
                if(step<100){
                    setProgress(step++);
                }else{
                    clearInterval(timer);
                }
            },onceTime);
        },
        autoCycleLoad:function(time){
            var onceTime=time/100;
            var setProgress=this.setProgress;
            //清除已经存在的timer
            if(timer){
                clearInterval(timer);
            }
            timer=setInterval(function(){
                if(step<100){
                    setProgress(step++);
                }else{
                    step=0;
                }
            },onceTime);

        },
        setProgress:function(n){
            if(n>100)n=100;
            if(n<0)n=0;
            progress_bar.style.width=n+"%";
        }
    }
  //加载等待提示控件结束
    loader.mainModular={};
    loader.startLoad=function(){
        //loading.init(true,true);
        loading.show();
        loading.autoOnceLoad(2000);
        console.log("start load main modular.");
    };
    loader.endLoad=function(){
    	loading.hide();
        console.log("load main modular success.");
    };

    loader.loadMain = function () {
        var mod = new Modular({name: main});
        loader.mainModular=mod;
        loader.load(mod);
    };
    //加载程序配置文件
    var configs = config.split(",");
    loader.startLoad();
    var time=new Date().getTime()
    _.each(configs, function (v) {
        loader.loadConfig({type: "js", path: v+"?datetime="+time});
    });

})()




