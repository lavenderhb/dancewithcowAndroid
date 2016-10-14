var isContinue=false;
var timer = null;
apiready = function() {
	$(function() {
		require(['scripts/mods/header_level2', 'scripts/mods/search_bar'], function(header, search_bar) {
			var header = new header.header_level2({
				type : 'register'
			});
			var aliasName_bar = new search_bar.search_bar({
				domSel : "#aliasName",
				haveBack : false
			});
			var valid_bar = new search_bar.search_bar({
				domSel : "#valid-input",
				haveBack : false
			});
			$("#btn").on("click", function() {
//				if (!checkMobileAlias()) {
//					return;
//				}
//				if(!checkValidCode()){
//					return;
//				}
                checkForm();
				if(isContinue){
				    resetValidBtn();
				    commitForm($("#aliasName").val());
				}
				
			});
			$("#goLogin").on("click", function() {
				goLogin();
			});
			var $smsCodeBtn = $("#smsCodeBtn");
            $smsCodeBtn.bind("click", countDown);
			function goLogin() {
				api.openWin({
					name : 'login',
					url : 'login.html',
					rect : {
						x : 0,
						y : 0,
						w : 'auto',
						h : 'auto'
					},
					bounces : false
				});
			}
            function checkForm(){
                checkMobileAlias();
                if(isContinue){
                    checkValidCode();
                }
            }
			function checkMobileAlias() {
			    isContinue=false;
				var isMobile = false;
				var regex = /^[1]\d{10}$/;
				var val = $("#aliasName").val();
				if (regex.test(val)) {
					isMobile = true;
					isContinue=true;
				} else {
					myAlert('手机号码格式错误', "register");
				}
				//return isMobile;
			}
            function checkValidCode(){
                isContinue=false;
            	var isValid = false;
				var val = $.trim($("#valid-input").val());
				var code=$api.getStorage("code");
				if (val==code) {
				    checkTimeout();
//				    alert("validtel"+$api.getStorage("validtel"));
//				    alert("aliasName"+$("#aliasName").val());
//				    alert("isContinue"+isContinue);
				    if($api.getStorage("validtel")==$("#aliasName").val()&&isContinue){
				        isValid = true;
				        isContinue=true;
				    }
				    else if(!isContinue){
				    	myAlert('验证码已失效，请重新获取', "register");
				    }
				    else{
				        myAlert('手机号码已改变，请重新获取验证码', "register");
				        isContinue=false;
				    }
					
				} else {
					myAlert('请输入正确验证码', "register");
				}
				//return isValid;
            }
            function checkTimeout(){
            	 var d = new Date().getTime();
                 var ls_d=$api.getStorage("validTime");
                 if(ls_d&&ls_d!=""){
                   ls_d=parseInt(ls_d);
                   var diff=d-ls_d;
                   //alert("diff"+diff);
                   if(diff>=0&&diff<=2*60*1000){
                   		isContinue=true;
                   }
                   else{ 
                        myAlert('验证码已失效', "register");
                   		isContinue=false;
                   }
                 }
                 else{
                 	isContinue=false;
                 }
            }
            function commitForm(val){
               api.ajax({
                    url: td.domain+"/user/reg?token="+td.token,
                    method: 'post',
                    data: {
                        values: { 
                            cellphone: val
                        },
                    }
                },function(ret, err){
                    if (ret) {
                         if(ret.code==200){
                         	 myAlert(ret.data, "register",1);
                         }
                         else{
                         	 myAlert(ret.data, "register");
                         }
                        
                    } else {
                         toast("网络异常，请稍后重试");
                    }
                });

            }
			function myAlert(tipInfo, type,isCloseWin) {
				api.openFrame({
					name : 'alert',
					url : 'alert.html',
					rect : {
						x : 0,
						y : 0,
						w : 'auto',
						h : 'auto'
					},
					bounces : false,
					pageParam : {
						text : tipInfo,
						type : type,
						isCloseWin:isCloseWin
					}

				});
				
			}
           
            function getCodeAjax(val,callback){
//				$.ajax({
//					type:"post",
//					url:td.domain+"/user/getcode?token="+td.token,
//					data:{cellphone:val},
//					success:function(data){
//						var data = JSON.parse(data); 
//	                    if(data.code==200){
//	                    	$api.setStorage("code",data.data.code);
//	                    	$api.setStorage("validtel",val);
//	                    	callback();
//	                    	alert(data.data.code);
//	                    }
//	                    else{
//	                    	toast(data.data);
//	                    }
//					},
//					error:function(err){
//						toast("网络异常，请稍后重试");
//					}
//				});
				api.ajax({
                    url: td.domain+"/user/getcode?token="+td.token,
                    method: "post",
                    data: {
                        values: { 
                            cellphone: val
                        },
                    }
                },function(ret, err){
//                  alert(JSON.stringify(ret));
//                  alert(JSON.stringify(err));
                    if (ret) {
                        if(ret.code==200){
                            var d = new Date().getTime();
                            $api.setStorage("code",ret.data.code);
                            $api.setStorage("validtel",val);
                            $api.setStorage("validTime",d);
                            callback();
                           // alert(ret.data.code);
                        }
                        else{
                            toast(ret.data);
                        }
                    } else {
                        toast("网络异常，请稍后重试");
                    }
                });
			}
			function countDown(){
			     checkMobileAlias();
			     if(isContinue){
                    doClock();
                 }
//			     if (!checkMobileAlias()) {
//                  return;
//               }
//               doClock();
			}
			function doClock() {
			    if(api.connectionType=="none"){
                    toast("请检查网络连接");
                    return;
                }
			    var val = $("#aliasName").val();
            	getCodeAjax(val,clockDetail);
			    
			}
			function clockDetail(){
			    $smsCodeBtn.addClass("btn-grey");
                var sec = 60;
                $smsCodeBtn.html(sec + "秒后重发");
                $smsCodeBtn.unbind("click", countDown);
                timer = window.setInterval(function() {
                    if (sec === 1) {
                        resetValidBtn();
                        return;
                    }
                    
                    $smsCodeBtn.html((--sec) + "秒后重发");
                }, 1000);
			}
			function toast(msg) {
			    api.toast({
			        msg: msg,
			        duration:1000,
			        location: 'middle'
			    });
			}
			function resetValidBtn(){
			    window.clearInterval(timer);
                timer = null;
                $smsCodeBtn.html("获取验证码");
                $smsCodeBtn.removeClass("btn-grey");
                $smsCodeBtn.bind("click", countDown);
			}
		});

	});
};
function closeWin(){
    setTimeout(function(){
        api.closeWin({
        });
    },600);
}