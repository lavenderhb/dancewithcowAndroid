var curVal = "";
var $list = null;
var curPage = 1;
var pageNum = 1;
var finalData;
var Tpl = null;
var Echo = null;
apiready = function() {
    
    $(function() {
        require(['scripts/mods/search_bar', 'scripts/echo', "scripts/lib/template"], function(search_bar, echo, tpl) {
             
            var search_bar = new search_bar.search_bar();
            var searchwrap = $api.byId("search-wrap");
            $api.fixIos7Bar(searchwrap);
            var commonlist = $api.byId("common-list");
            $api.fixIos7Bar(commonlist);
            
            api.setStatusBarStyle({
                style : 'light'
            });
            $list = $("#searchResultList");
            Tpl = tpl;
            Echo = echo;
            bindEvt();
            setInfiniteScroll();
            //api.parseTapmode();

            function bindEvt() {
			    $("#searchBtn").on("click", function() {
			            searchSubmit();
			    });
			    $list.on("click", ".item-link", function() {
			        var aid = $(this).data("aid");
			        var cid = $(this).data("cid");
			        api.openWin({
			          name: 'newsDetail_win',
			          url: 'common-header-level2.html',
			          slidBackEnabled:false,
			          pageParam:{page:"newsDetail",pageurl:"../news/newsDetail_frm.html",title:"资讯正文",isshow_btnsave:true,btnsave:"保存",param:{page:"newsDetail",aid:aid,cid:cid}}
			        });
			        
			    });
			}
			

            

        });
    });

};
function setInfiniteScroll() {
    api.addEventListener({
        name : 'scrolltobottom',
        extra:{
	        threshold:0
	    }
    }, function(ret, err) {
        if (curPage >= pageNum) {
            toast("已到最底部");
            //                          api.removeEventListener({
            //                              name: 'scrolltobottom'
            //                          });
        } else {
            curPage++;
            appendRender(curVal, curPage);
        }
    });
}
function render(val) {
    if (api.connectionType == "none") {
        toast('请检查网络连接');
        return;
    }
    //				var id="search";
    //				var folder = "cartype";
    //				var url=td.domain+"/search/_"+val+"/"+1+"?token="+td.token;
    //				var method="get";
    //				var params="";
    //				var async=true;
    //				doCache(folder,id,url,method,params,async,function(data){
    getAjaxData(val, 1, searchCallBack);

    //				});
}

function searchCallBack(data) {
    var finalData3 = data.data;
    if (typeof(finalData3)!="string") {
        if (finalData3.length > 0) {
            $list.html(Tpl("tpl", {
                data : finalData3
            }));
            
            curPage = 1;
            pageNum = data.pageinfo.pagenum;
            lazyImg();
        }
    }
    else{
    	$list.html("");
    	curPage = 1;
        pageNum = 1;
    }
}

function appendRender(val, page) {
    if (api.connectionType == "none") {
        toast('请检查网络连接');
        return;
    }
    getAjaxData(val, page, searchAppendCallBack);

}

function searchAppendCallBack(data) {
    var finalData2=data.data;
    if (finalData2.length > 0) {
        $list.append(Tpl("tpl", {
            data : finalData2
        }));
        lazyImg();
    }
}

function getAjaxData(kwords, page, callback) {
    //var finalData=[];
    curPage = page;
    //var url=td.domain + "/search/_" + kwords + "/" + page + "?token=" + td.token;
    var url=td.domain + "/search?kwords="+kwords+ "&page="+page+"&token="+td.token;
    //alert("url:"+url);
    try {
       
        api.ajax({
            url : url,
            method : "get"
        }, function(ret, err) {
            if (ret) {
                if (ret.code == 200) {
                    callback(ret);
                } else {
                    searchCallBack(ret);
                    toast(ret.data);
                }
            } else {
                toast("网络异常，请稍后重试");
            }
        });
    } catch(err) {
        toast("系统异常，请稍后重试");
    }
}

function lazyImg() {
    if(Echo){
        Echo.init({
            offset : 100, //可是区域多少像素可以被加载
            throttle : 0 //设置图片延迟加载的时间
        });
    }
}

function toast(msg) {
    api.toast({
        msg : msg,
        duration : 1000,
        location : 'middle'
    });
}

function searchSubmit() {
    curVal = $("#search").val();
    curVal = $.trim(curVal);
    if (curVal != "") {
        render(curVal);
    }
}