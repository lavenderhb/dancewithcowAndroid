apiready=function(){
   $(function(){
		require(["scripts/mods/news/news_kuaixun"],function(news_rec){
			{
				var newsRecObj=new news_rec.news_rec();
			}
		});
   });
};