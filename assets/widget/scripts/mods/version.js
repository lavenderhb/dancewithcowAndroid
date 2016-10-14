 function getLastestVer(displayToast){
    //ajax 去取最新version
    if(api.connectionType=="none"){
       toast("请检查网络连接");
       return;
   }
   
   api.ajax({
	    url: td.domain+"/appinfo?token="+td.token,
        method: 'get'
   },function(ret,err){
   	    if(ret){
   	        if(ret.code==200){
   	            var appVer=ret.data.ios_ver;
   	            var sysTyp=api.systemType;
   	            if(sysTyp!="ios"){
   	            	appVer=ret.data.version;
   	            }
                $api.setStorage('lastestVersion',appVer);
                if(checkVer()){
                    if(api.pageParam.type=="my"){
                        api.execScript({
                            frameName:"my",
                            script: 'hideVersion();'
                        });
                        if(displayToast){
                             toast('您当前已为最新版本');
                        }
                    }
                    else{
                       var verTipDoneVer=$api.getStorage('verTipDoneVer');
                       //alert("verTipDoneVer"+verTipDoneVer);
                       if(verTipDoneVer==api.appVersion){
                          $api.setStorage('verTipDoneVer',"");
                          //alert("appVersion"+api.appVersion);
                          toast('您当前已为最新版本');
                          if(api.systemType!="ios"){
                            $api.setStorage("app-pkg-remove",0);
					     	removeAppPkg();
					      }
                       }
                    }
                }
                else{
                    if(api.pageParam.type=="my"){
                        api.execScript({
                            frameName:"my",
                            script: 'showVersion("'+ret.data.version+'");'
                        });
                        if(displayToast){
                            verUpdateCallBack("my");
                        }
                    }
                    else{
                        var verTipDoneVer=$api.getStorage('verTipDoneVer');
                        if(verTipDoneVer&&verTipDoneVer!=""&&verTipDoneVer==$api.getStorage('lastestVersion')){
                        
                        }
                        else{
                            $api.setStorage('verTipDoneVer',ret.data.version);
                            verUpdateCallBack("index");
                        }
                    }
                }
            }
            else{
                toast(ret.data);
            }
   	    }
   	    else{
   	        toast("网络异常，请稍后重试");
   	    }
   });

}

function versionCompare(left, right) {
    if (typeof left + typeof right != 'stringstring')
      return false;

    var a = left.split('.')
      , b = right.split('.')
      , i = 0, len = Math.max(a.length, b.length);

    for (; i < len; i++) {
      if ((a[i] && !b[i] && parseInt(a[i]) > 0) || (parseInt(a[i]) > parseInt(b[i]))) {
        return 1;
      } else if ((b[i] && !a[i] && parseInt(b[i]) > 0) || (parseInt(a[i]) < parseInt(b[i]))) {
        return -1;
      }
    }
    return 0;
}
function toast(msg) { 
    api.toast({
        msg: msg,
        duration:1000,
        location: 'middle'
    });
}
function checkVer(){
    var currVer =api.appVersion;
    var lastestVer=$api.getStorage('lastestVersion');
    if(versionCompare(currVer,lastestVer)==0){
         return true;
   }
   else if(versionCompare(currVer,lastestVer)<0){
         return false;
   }
   else{
      return true;
   }
}
function verUpdateCallBack(type){
    var preUrl="..";
    if(type=="index"){
        preUrl="html";
    }
    var lastestVer=$api.getStorage('lastestVersion');
    myConfirm('<p>提高稳定性使用更流畅，<br>马上更新</p>',type,"立即升级","V"+lastestVer+"更新",preUrl);
}
function myConfirm(tipInfo,type,modalButtonOk,title,preUrl){
   api.openFrame({
        name: 'confirm',
        url: preUrl+'/common/confirm.html',
        rect:{
            x:0,
            y:0,
            w:'auto',
            h:'auto'
            },
            pageParam:{text:tipInfo,type:type,modalButtonOk:modalButtonOk,title:title},
            bounces: false
   });
}
function removeAppPkg(){
    if($api.getStorage("app-pkg-remove")!="1"){
        var fs = api.require('fs');
        fs.remove({
            path: appSavePath
        },function( ret, err ){        
            if( ret.status ){
                $api.setStorage("app-pkg-remove",1);
            }else{
                $api.setStorage("app-pkg-remove",0);
            }
        });
    }
}