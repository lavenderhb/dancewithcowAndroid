var ajpush =null;
function initAjPush(){
//  var user=$api.getStorage('uid'); 
//  if(user&&user!=""){}else{return;}
    ajpush = api.require('ajpush'); 
    if (api.systemType == 'ios') {//ios系统不需要进行加载自动就加载了，但是需要 进行点击消息事件的监听
        //getRegistrationId若获取不到说明极光初始化有问题，deviceToken若获取不到说明你证书有问题或者推送未被允许
//		ajpush.getRegistrationId(function(ret) {
//		    var registrationId = ret.id;
//		});
//		alert("deviceToken:"+api.deviceToken);
	} else {
		ajpush.init(function(ret) {  
	        if (ret && ret.status) {   //注意安卓系统必须要这样   
	            //alert('ret.status'+ret.status);
	        }
	    });
	}
    
    if(ajpush){
        bindAlias();
        if(api.systemType != 'ios'){
            //两个监听事件 
        	//监听应用进入后台，通知jpush暂停事件 
        	api.addEventListener({ 
	            name : 'pause' 
	        }, function(ret, err) {
	           // alert('pause'); 
	            ajpush.onPause(); 
	        });
	        
	        //监听应用恢复到前台，通知jpush恢复事件 
	        api.addEventListener({ 
	            name : 'resume' 
	        }, function(ret, err) { 
	            //alert('resume'); 
	            ajpush.onResume(); 
	        });
        }
        
        if(api.systemType != 'ios'){//针对非IOS系统的消息点击
        	api.addEventListener({ 
	            name : 'appintent' 
	        }, function(ret, err) {
	            if (ret && ret.appParam.ajpush) {
	               //以下是点击了消息，然后出来的信息，并打开加载相应的新闻数据

	                var push = ret.appParam.ajpush; 
	                var jsonStr = JSON.parse(push.extra); 
	                var type = jsonStr.type; 
	               // var id = jsonStr.txt; 
	               clearNotification();
	                if(type=="pm"){
	                    $api.setStorage("csid",jsonStr.targetid);
	                    $api.setStorage("cname",jsonStr.csname);
                		toInteract();
	                }
	                else{
	                	goMsgPage();
	                }
	               // alert('appintent'+JSON.stringify(ret)); 
	                
	            } 
	        });
        
        }
        if(api.systemType == 'ios'){//针对IOS系统的消息点击
        	 api.addEventListener({name:'noticeclicked'}, function(ret,err) {
	            if(ret && ret.value){
	                var push = ret.value;
	                var content = push.content;
	                var jsonStr = push.extra;
	                var type=jsonStr.type;
	                // alert('noticeclicked'+JSON.stringify(ret)); 
	                clearNotification();
	                if(type=="pm"){
	                	$api.setStorage("csid",jsonStr.targetid);
	                    $api.setStorage("cname",jsonStr.csname);
                		toInteract();
	                }
	                else{
	                	goMsgPage();
	                }
	               
	                //goMsgPage();
	            }
	        });
//	        ajpush.setListener(
//			    function(ret) {
//			         var id = ret.id;
//			         var title = ret.title;
//			         var content = ret.content;
//			         var jsonStr = ret.extra;
//			         var type=jsonStr.type;
//	                // alert('noticeclicked'+JSON.stringify(ret)); 
//	                clearNotification();
//	                if(type=="pm"){
//	                	$api.setStorage("csid",jsonStr.targetid);
//	                    $api.setStorage("cname",jsonStr.csname);
//              		toInteract();
//	                }
//	                else{
//	                	goMsgPage();
//	                }
//			         
//			    }
//			);
        }
        //消息监听，即应用在前台的时候的监听
//      ajpush.setListener(
//          function(ret) {
//               var id = ret.id;
//               var title = ret.title;
//               var content = ret.content;
//               var extra = ret.extra;
//               alert('setListener'+extra); 
//              //goMsgPage();
//          }
//      );
    }
}

function bindAlias(){
    var user=$api.getStorage('uid'); 
    if(user&&user!=""){
        var isbind=$api.getStorage('bindTag');
        if(isbind&&isbind==user){
            
        }
        else{
            var param = { 
                alias : user 
            }; 
            ajpush.bindAliasAndTags(param, function(ret) { 
                var statusCode = ret.statusCode;
				if (statusCode == '6002') {
				    bindAlias();
				}
				else{
					$api.setStorage('bindTag',user);
				}
            }); 
        } 
    }
}
function clearNotification(){
	var param = {id:-1};
	if(ajpush){
		ajpush.clearNotification(param,function(ret) {
		    if(ret && ret.status){
		        //success
		    }
		});
	}
}
function goMsgPage(){
    var uid=$api.getStorage("uid");
    if(!uid||uid==''){
        goLogin();
        return;
    }
    else{
        api.openWin({
            name : "msg",
            url : 'html/my/msg.html',
            bounces : false,
            delay : 200
        });
    }
}
function goLogin(){
    api.openWin({
        name : 'login',
        url : 'html/common/login.html',
        bounces : false,
        animation : 'push',
        scrollToTop : true,
        rect : {
            x : 0,
            y : 0,
            w : 'auto',
            h : 'auto'
        }
    });
}
function toInteract(){
	var uid=$api.getStorage("uid");
    if(!uid||uid==""){
    	goLogin();
    }
    else{
       if(api.connectionType=="none"){
       	   toast("请检查网络连接");
           return;
       }
        var csid=$api.getStorage("csid");
        if(!csid||csid==""){
             getAjaxData(interactCallBack);
        }
        else{
           interactCallBack();
        }
    }
}    
function toast(msg){
	api.toast({
        msg: msg,
        duration:3000,
        location: 'middle'
    });
}
function interactCallBack(){
    var csid=$api.getStorage("csid");
    var cname=$api.getStorage("cname");
    if(csid&&csid!=""){
        api.openWin({
            name : 'interact',
            url : 'html/public/interact.html',
            bounces : false,
            delay : 200,
            pageParam:{csid:csid,cname:cname}
        });
    }
}		
function getAjaxData(callback,displayToast){
    var uid=$api.getStorage("uid");
    try{
        api.ajax({
            url: td.domain+"/isallowcus/"+uid+"?token="+td.token,
            method: "get"
        },function(ret, err){
            if (ret) {
                 if(ret.code==200){
                    $api.setStorage("csid",ret.data.csid);
                    $api.setStorage("cname",ret.data.name);
                    callback();
                 }
                 else if(ret.code==201){
                   if(displayToast){
                        toast("未分配客服");
                   }
                   
                 }
                 else{
                    if(displayToast){
                        toast("没有会员ID");
                    }
                 } 
                 
            } else {
                toast("网络异常，请稍后重试");
            }
        });   
    }catch(err){
    	toast("系统异常，请稍后重试");
    }
}