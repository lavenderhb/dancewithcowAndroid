apiready=function(){
$(function() {
	require(["scripts/mods/customeService/personDetail"],function(personDetail) {
		var personDetail = new personDetail.personDetail();
	});
});

};
function nofind(){
    var img=event.srcElement;
    img.src="../../images/logo.png";
    img.onerror=null; //控制不要一直跳动
}