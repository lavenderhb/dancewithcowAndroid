apiready = function() {
	$(function() {
		require(['scripts/mods/header_level2', 'scripts/mods/search_bar'], function(header, search_bar) {
			var header = new header.header_level2({
				type : 'login',
				toPage:api.pageParam.toPage
			});
			var username_bar = new search_bar.search_bar({
				domSel : "#username",
				haveBack : false
			});
			var pwd_bar = new search_bar.search_bar({
				domSel : "#pwd",
				haveBack : false
			});
			
			if(api.pageParam&&api.pageParam.username){
				$("#username").val(api.pageParam.username);
			}
			
			$("#btn-login").on("click", function() {
				if (!checkForm()) {
					return;
				}
				var username =$.trim($("#username").val());
				var pwd=$.trim($("#pwd").val());
				var isSuccess=postAjaxData(username,pwd,loginCallBack);
				
			});
			$("#goRegister").on("click", function() {
				goRegister();
			});
			$("#forgetPass").on("click", function() {
				doPass();
			});
			function loginCallBack(){
			     api.execScript({
                    name:"root",
                    script: 'updateUserName();'
                });
                api.execScript({
                    name:"root",
                    script: 'initAjPush();'
                });
                api.execScript({
                    name:"root",
                    frameName:"my",
                    script: 'renderMsg();'
                });
                api.closeWin({});
			}
			//var isSuccess=false;
			function postAjaxData(username,password,callBack){
			    try{
//			    	$.ajax({
//						type:"post",
//						url:td.domain+"/user/login?token="+td.token,
//						async : false,
//						data:{username:username,password:password},
//						success:function(data){
//							var data = JSON.parse(data); 
//		                    if(data.code==200){
//		                    	var finalData=data.data;
//		                    	$api.setStorage("uid",finalData.uid);
//		                    	$api.setStorage("username",finalData.username);
//		                    	$api.setStorage("name",finalData.name);
//		                    	$api.setStorage("qq",finalData.qq);
//		                    	$api.setStorage("content",finalData.content);
//		                    	isSuccess=true;
//		                    	
//		                    }
//		                    else{
//		                    	toast(data.data);
//		                    }
//						},
//						error:function(err){
//							toast("网络异常，请稍后重试");
//						}
//					});
			        api.ajax({
                        url: td.domain+"/user/login?token="+td.token,
                        method: "post",
                        data: {
                            values: { 
                                username:username,
                                password:password
                            },
                        }
                    },function(ret, err){
                        if (ret) {
                            if(ret.code==200){
                                var finalData=ret.data;
                                $api.setStorage("uid",finalData.uid);
                                $api.setStorage("username",finalData.username);
                                $api.setStorage("name",finalData.name);
                                $api.setStorage("qq",finalData.qq);
                                $api.setStorage("content",finalData.content);
                                callBack();
                            }
                            else{
                                toast(ret.data);
                            }
                        } else {
                            toast("网络异常，请稍后重试");
                        }
                    });
			    }catch(err){
			    	toast("系统异常，请稍后重试");
			    }
			}
			function goRegister() {
				api.openWin({
					name : 'register',
					url : 'register.html',
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
            	var isValid = false;
				var username = $.trim($("#username").val());
				var pwd = $.trim($("#pwd").val());
				if (username=="") {
					myAlert('请输入账号', "login");
				} 
				else if(pwd=="") {
				    myAlert('请输入密码', "login");
				}
				else{
					isValid = true;
				}
				return isValid;
            }
			function myAlert(tipInfo, type) {
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
						type : type
					}

				});
				
			}

			function doPass(){
				api.openWin({
					name : 'contact_us',
					url : 'contact_us.html',
					rect : {
						x : 0,
						y : 0,
						w : 'auto',
						h : 'auto'
					},
					bounces : false
				});
			}
			function toast(msg){
				api.toast({
					msg : msg,
					duration : 2000,
					location : 'middle'
				});
			}
		});

	});
};
