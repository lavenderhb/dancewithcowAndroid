apiready=function(){
$(function() {
	require(["scripts/mods/nav-iscroll2","scripts/mods/customeService/customeService"],function(avatarIscroll,customeService) {
		var avatarIscroll = new avatarIscroll.NavIscroll();
		var customeService= new customeService.customeService();
		
		setRefreshHeader();
      	function render(){
      		avatarIscroll.render();
      		customeService.render();
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
		
	});
});
};
function nofind(){
    var img=event.srcElement;
    img.src="../../images/logo.png";
    img.onerror=null; //控制不要一直跳动
}