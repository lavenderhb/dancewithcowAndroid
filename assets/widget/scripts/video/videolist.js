apiready=function(){
   $(function(){
		require(["scripts/mods/video/videolist"],function(videolist){
			{
				var videolistObj=new videolist.videolist();
			}
		});
   });
};