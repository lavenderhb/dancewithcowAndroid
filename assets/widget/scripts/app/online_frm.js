apiready=function(){
	$(function(){
	   require(["html/tpl/build/template"],function(tpl){
            var id="online";
            var folder = "cartype";
            var url=td.domain+"/live/?token="+td.token;
            var method="get";
            var params="";
            var async="true";
            doCache(folder,id,url,method,params,async,function(data){
                var finalData=data.data;
                if(data.code==200){
                      if(finalData.length>0){
                        $("#video-list").html(tpl("video/online", {data:finalData}));
                      }
                }
                else{
                    toast(finalData);
                }
              });
        
            function toast(msg){
                api.toast({
                    msg: msg,
                    duration:1000,
                    location: 'middle'
                });
            }
            $("#video-list").on("click",".video-btn",function(){ 
                var aid=$(this).data("aid");
                var cid=$(this).data("cid");
                var type="newsDetail";
                api.openWin({
                    name : type,
                    url : '../news/'+type+'.html',
                    bounces : false,
                    delay : 200,
                    pageParam:{aid:aid,cid:cid}
                });
            
            });
            
	   });
		
	});
};