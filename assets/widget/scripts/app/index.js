window.nav_b=null;
window.header=null;

apiready=function(){
	$(function(){
		require(['scripts/mods/header_level1','scripts/mods/nav-bottom'],function(header,nav_b){
			window.header=new header.header({type:'main'});
			window.nav_b = new nav_b.Nav({headObj:window.header});

			if(api.systemType!="ios"){
				getLastestVer();
			}
			initAjPush();
			api.addEventListener({
				name:'test'
			},function(ret,err){
				clearNotification();
				var extraStr=ret["value"]["cn.jpush.android.EXTRA"];
				var extra=JSON.parse(extraStr);
				if(extra.type=="pm"){
					$api.setStorage("csid",extra.targetid);
					$api.setStorage("cname",extra.csname);
					toInteract();
				}
				else{
					goMsgPage();
				}
			});


		});
	});

};

function openCusSerTab(type){
	window.nav_b.openTab(type);
}
function openTab(type,cid){
	window.nav_b.openTab(type,cid);
}
function updateUserName(){
	window.header.updateUserName();
}
