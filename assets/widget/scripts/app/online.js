apiready=function(){
   $(function(){
   		require(['scripts/mods/header_level2'],function(header){
           var header=new header.header_level2({type:'online'});
           
           api.openFrame({
	             name: 'online_frm',
	             url: 'online_frm.html',
	             rect: {
	                 x: 0,
	                 y: $("#header").height(),
	                 w: 'auto',
	                 h: 'auto'
	             },
	             showProgress: 'true',
				 allowEdit:true,
				 softInputMode:'resize',
				 scaleEnabled:false
	         });
           
        });
   });
};