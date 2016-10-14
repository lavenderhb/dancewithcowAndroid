window.swiper_nav=null;
apiready=function(){
   $(function(){
   		require(['scripts/mods/main/slider_img','scripts/mods/main/swiper_nav',"scripts/mods/main/newsList",'scripts/lib/template'],function(slider,swiper_nav,newsList,tpl){
	         
	      	var slider_img=new slider.slider_img();
	      	window.swiper_nav=new swiper_nav.swiper_nav();
	      	var newsListObj=new newsList.newsList();
	      	getRecomAJaxData();
	      	getColartAJaxData();
	      	setRefreshHeader();
	      	bindEvt();
	      	
	      	//推荐数据
	      	function  getRecomAJaxData(){
		      	var id="index_recom";
				var folder = "cartype";
				var url=td.domain+"/index/recom?token="+td.token;
				var method="get";
				var params="";
				var async="true";
				doCache(folder,id,url,method,params,async,function(data){
					if(data.code==200){
	                    renderRecom(data.data);
	                }
	                else{
	                	toast(data.data);
	                }
				});
	      	}
	      	function renderRecom(finalData){
	      		$("#rec-list").html(tpl("tpl-rec", {data:finalData}));
	      	}
	      	//栏目
	      	function getColartAJaxData(){
				var id2="index_colart";
				var folder = "cartype";
				var url2=td.domain+"/index/colart?token="+td.token;
				var method="get";
				var params="";
				var async="true";
				doCache(folder,id2,url2,method,params,async,function(data){
					if(data.code==200){
	                    renderColart(data.data);
	                }
	                else{
	                	toast(data.data);
	                }
				});
	      	}
	      	function renderColart(finalData){
	      	    $("#colart-list").html(tpl("tpl-colart", {data:finalData}));
	      	}
	      	
	      	function bindEvt(){
	      	    //推荐
	      		$("#rec-list").on("click","li",function(){
	      			var aid=$(this).data("aid");
				    var cid=$(this).data("cid");
					api.openWin({
		              name: 'newsDetail_win',
		              url: '../common/common-header-level2.html',
		              slidBackEnabled:false,
		              pageParam:{page:"newsDetail",pageurl:"../news/newsDetail_frm.html",title:"资讯正文",isshow_btnsave:true,btnsave:"保存",param:{page:"newsDetail",aid:aid,cid:cid}}
		            });
	      		});
	      		
	      	    //栏目
	      	    $("#colart-list").on("click","li",function(){
	      			var aid=$(this).data("aid");
				    var cid=$(this).data("cid");
					api.openWin({
		              name: 'newsDetail_win',
		              url: '../common/common-header-level2.html',
		              slidBackEnabled:false,
		              pageParam:{page:"newsDetail",pageurl:"../news/newsDetail_frm.html",title:"资讯正文",isshow_btnsave:true,btnsave:"保存",param:{page:"newsDetail",aid:aid,cid:cid}}
		            });
	      		});
	      	
	      	    //牛股情报站 “更多” 事件
	      		$("#wealthHotsDesc").on("click",".more",function(){
	      			var onecid=487;
	      			var cid=481;
                    api.execScript({
		                script: 'window.nav_b.openTab("news","'+onecid+'","'+cid+'");'
	                });
	      		});
	      	}
	      	function render(){
	      		slider_img.renderTpl();
	      		newsListObj.render();
	      		window.swiper_nav.ajaxData();
	      		getRecomAJaxData();
	      		getColartAJaxData();
	      	}
	      	function setRefreshHeader() {
				api.setRefreshHeaderInfo({
					visible : true,
					bgColor : '#ccc',
					textColor : '#fff',
					textDown : '下拉刷新...',
					textUp : '松开刷新...',
					showTime : true
				}, function(ret, err) {
					render();
					api.refreshHeaderLoadDone();
				});
			}
			
			function toast(msg) { 
			    api.toast({
			        msg: msg,
			        duration:1000,
			        location: 'middle'
			    });
			}
			
	    });
   });
	
};
function clearUnreadStatus(){
	if(window.swiper_nav){
		window.swiper_nav.clearUnreadStatus("interact");
	}
}