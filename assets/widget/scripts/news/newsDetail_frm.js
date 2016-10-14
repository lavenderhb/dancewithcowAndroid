apiready=function(){
   $(function(){
		require(["scripts/mods/news/newsDetail","scripts/lib/template"],function(newsDetail,tpl){
			var newsDetailObj=new newsDetail.newsDetail();
			//相关阅读
			var aid=api.pageParam.aid;
			getRelatedAjaxData(aid,relatedCallback);
			bindRelatedEvt();
			function getRelatedAjaxData(aid,callBack){
			    api.ajax({
	                url: td.domain+"/news/related/"+aid+"?token="+td.token,
	                method: "GET"
	            },function(ret, err){
	                if (ret) {
	                     if(ret.code==200){
	                        var finalData=ret.data;
	                        if(finalData.length>0){
	                        	callBack(finalData);
	                        }
	                     } 
	                } else {
	                    toast("网络异常，请稍后重试");
	                }
	            });
			}
			function relatedCallback(finalData){
				$("#wealthHots").html(tpl("tpl-related", {data:finalData}));
			}
			function bindRelatedEvt(){
				$("#wealthHots").on("click","li",function(){
					var aid=$(this).data("aid");
				    var cid=$(this).data("cid");
					api.openWin({
		              name: 'newsDetail_win'+"_"+aid+"_"+cid,
		              url: '../common/common-header-level2.html',
		              slidBackEnabled:false,
		              pageParam:{page:"newsDetail",pageurl:"../news/newsDetail_frm.html",title:"资讯正文",isshow_btnsave:true,btnsave:"保存",param:{page:"newsDetail",aid:aid,cid:cid}}
		            });
				});
			}
			function toast(msg){
				api.toast({
			        msg: msg,
			        duration:1000,
			        location: 'bottom'
			    });
			}
		});
   });
};
