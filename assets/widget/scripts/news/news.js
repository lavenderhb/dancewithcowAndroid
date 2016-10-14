apiready=function(){
   $(function(){
		require(["scripts/mods/news/news"],function(news){
			{
				var newsObj=new news.news();
			}
		});
   });
};