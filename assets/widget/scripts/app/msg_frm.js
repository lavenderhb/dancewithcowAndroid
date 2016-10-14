apiready=function(){
   $(function(){
   		 require(["scripts/mods/my/msg"],function(msg){
   		       var msgObj=new msg.msg();
   		 
   		 });
//			var currIndex=0;
//			var prevIndex=0;
//			var $msgList=$("#msgList")
//			$(window).on("scroll",function(){
//				setTimeout(function(){
//					doScroll();
//				},300);
//			});
//			
//
//			var fontSize = window.getComputedStyle(document.documentElement).fontSize;
//				fontSize = fontSize.substring(0, fontSize.length - 2);
//			var diff=fontSize*1.275;
//			function doScroll(){
//			   
//				$msgList.find(".msgList").each(function(){
//				        var scrollTop=document.body.scrollTop;
//						if(scrollTop<$(this)[0].offsetHeight+$(this)[0].offsetTop-diff){
//						    currIndex=$(this).index();
//						    if(prevIndex!=currIndex){ 
//						        $msgList.find(".timeStamp").eq(0).html("<span>"+$msgList.find(".timeStamp").eq(currIndex).data("src")+"</span>");
//								prevIndex=currIndex;
//						    }
//						    return false; 
//						}
//
//				});
//			}
//			setRefreshHeader();
//			setInfiniteScroll();
//			function setRefreshHeader() {
//				api.setRefreshHeaderInfo({
//					visible : true,
//					bgColor : '#ccc',
//					textColor : '#fff',
//					textDown : '下拉刷新...',
//					textUp : '松开刷新...',
//					showTime : true
//				}, function(ret, err) {
//					//render();
//					api.refreshHeaderLoadDone();
//				});
//			}
//			var page=26;
//			function setInfiniteScroll(){
//				api.addEventListener({
//					name : 'scrolltobottom'
//				}, function(ret, err) {
//					page--;
//				var html='<div class="msgList">'
//				+'<div class="timeStamp" data-src="2015/12/'+page+'">'
//					+'<span>2015/12/'+page+'</span>'
//				+'</div>'
//				+'<ul >'
//					+'<li>'
//						+'<a href="#" class="item-link item-content">'
//						+'<div class="item-inner">'
//							+'<div class="item-text">'
//								+'自2015年7月6号天鼎1号证券基金成立后至2015年9月30日期低位灵活补仓    三招巧防踏空（上）'
//							+'</div>'
//							+'<div class="item-date">'
//								+'17:14'
//							+'</div>'
//						+'</div> </a>'
//					+'</li>'
//					+'<li>'
//						+'<a href="#" class="item-link item-content">'
//						+'<div class="item-inner">'
//							+'<div class="item-text">'
//								+'自2015年7月6号天鼎1号证券基金成立后至2015年9月30日期低位灵活补仓    三招巧防踏空（上）'
//							+'</div>'
//							+'<div class="item-date">'
//								+'17:14'
//							+'</div>'
//						+'</div> </a>'
//					+'</li>'
//					+'<li>'
//						+'<a href="#" class="item-link item-content">'
//						+'<div class="item-inner">'
//							+'<div class="item-text">'
//								+'自2015年7月6号天鼎1号证券基金成立后至2015年9月30日期低位灵活补仓    三招巧防踏空（上）'
//							+'</div>'
//							+'<div class="item-date">'
//								+'17:14'
//							+'</div>'
//						+'</div> </a>'
//					+'</li>'
//				+'</ul>'
//			+'</div>';
//					$("#msgList").append(html);
//					
//				});
//			}
	  
   });
	
};