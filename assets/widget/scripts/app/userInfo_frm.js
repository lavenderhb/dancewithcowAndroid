apiready=function(){
$(function() {
	require(['scripts/mods/header_level2','scripts/mods/search_bar'], function(header,search_bar) {
		var currType =api.pageParam.currType;
		var inputVal="";
		if (currType) {
			if (currType == "fullName" || currType == "qq") {
			    var header = new header.header_level2({
						type : currType
					});
					bindBtnSave();
				var search_bar = new search_bar.search_bar({domSel : "#inputText",haveBack : false});
				if(currType == "fullName"){
				    var fullName_stro=$api.getStorage("name");
				    if(fullName_stro&&fullName_stro!=""){
				    	$("#inputText").val(fullName_stro);
				    }
				}
				else{
					var qq_stro=$api.getStorage("qq");
				    if(qq_stro&&qq_stro!=""){
				    	$("#inputText").val(qq_stro);
				    }
				}
			} else if (currType == "introduce") {
			    var header = new header.header_level2({
						type : currType
					});
					bindBtnSave();
				var introduce_stro=$api.getStorage("content");
				if(introduce_stro&&introduce_stro!=""){
			    	$("#content").val(introduce_stro);
			    }
//				var fontSize = window.getComputedStyle(document.documentElement).fontSize;
//					fontSize = fontSize.substring(0, fontSize.length - 2);
//				var diff=fontSize*0.5;
//				var header_h=$("#header").height();
//				var width=api.winWidth-2*diff;
//				var UIInput = api.require('UIInput');
//				UIInput.open({
//					rect : {
//						x : diff,
//						y : diff+header_h,
//						w : width,
//						h : 170
//					},
//					styles : {
//						bgColor : '#fff',
//						size : 17,
//						color : '#323232',
//						placeholder : {
//							color : '#A5A7B1'
//						}
//					},
//					maxRows : 10,
//					placeholder : ''
//				}, function(ret) {
//					if (ret.status) {
//					    if (ret.eventType == 'show') {
//							//$numHint.show();
//							var introduce_stro=$api.getStorage("content");
//						    if(introduce_stro&&introduce_stro!=""){
//						    	UIInput.value({msg:introduce_stro});
//						    	uiinputVal=introduce_stro;
//						    	//alert("show uiinputVal"+uiinputVal);
//						    }
//						}
//						else if (ret.eventType == 'change') {
//							UIInput.value(function(ret, err) {
//								if (ret.status) {
//									val = ret.msg;
//									uiinputVal=val;
//									//alert("change uiinputVal"+uiinputVal);
//								}
//							});
//						}
//					}
//				});
				
			}
			else if(currType=="userInfo_frm"){
			    var username='';
				username=$api.getStorage("username");
				$("#username").html(username);
		        
				var fullName='';
				fullName=$api.getStorage("name");
		        if(!fullName||fullName==''){
		        	$("#temp-fullName").html("未填写");
		        }
		        else{
		        	$("#temp-fullName").html("");
		        }
		        var qq='';
				qq=$api.getStorage("qq");
		        if(!qq||qq==''){
		        	$("#temp-qq").html("未填写");
		        }
		        else{
		        	$("#temp-qq").html("");
		        }
		        var introduce='';
				introduce=$api.getStorage("content");
		        if(!introduce||introduce==''){
		        	$("#temp-introduce").html("未填写");
		        }
		        else{
		        	$("#temp-introduce").html("");
		        }
			}
		}
		
        
        
        
		$("#myList").on("click", ".item-link", function() {
			var type = $(this).data("src");
			if (type == "username") {
					return;		
			} 
			else {//msg userInfo
				api.openWin({
					name : type,
					url : type + '.html',
					bounces : false,
					delay : 200,
					slidBackEnabled:false,
					pageParam : {
						currType : type
					},
					allowEdit:true
				});
			}

		});
		
		function bindBtnSave(){
			$(document).on("click","#btn-save" ,function() {
				var type = $(this).data("from");
				if (type == "username") {
					return;
				} else if (type == "fullName") {
				    if(checkForm(type)){
				        var oldVal=$api.getStorage("name");
				        if(inputVal==oldVal){
				        	toast("保存成功");
				        	closeWin();
				        }
				        else{
				        	if(api.connectionType=="none"){
				            	toast("请检查网络连接");
				            	return;
				            }
				            postNameAjax(inputVal,nameCallBack);
				            
				        }
				    }
				} else if (type == "qq") {
					if(checkForm(type)){
						var oldVal=$api.getStorage("qq");
					    if(inputVal==oldVal){
				        	toast("保存成功");
				        	closeWin();
				        }
				        else{
				            if(api.connectionType=="none"){
				            	toast("请检查网络连接");
				            	return;
				            }
				        	postQQAjax(inputVal,qqCallBack);
				            
				        	
				        }
				    }
				} else if (type == "introduce") {
					if(checkForm(type)){
					    var oldVal=$api.getStorage("content");
					    if(inputVal==oldVal){
				        	toast("保存成功");
				        	closeWin();
				        }
				        else{
				            if(api.connectionType=="none"){
				            	toast("请检查网络连接");
				            	return;
				            }
				            postContentAjax(inputVal,contentCallBack);
				        	
				        }
				    }
				}
			});
		}
		function nameCallBack(inputVal){
            $api.setStorage("name",inputVal);
            toast("保存成功");
            closeWin();
		}
		function qqCallBack(inputVal){
            $api.setStorage("qq",inputVal);
            toast("保存成功");
            closeWin();
		}
		function contentCallBack(inputVal){
            $api.setStorage("content",inputVal);
            toast("保存成功");
            closeWin();
        }
		function postNameAjax(val,callback){
		    var uid=$api.getStorage("uid");
//			$.ajax({
//				type:"post",
//				url:td.domain+"/user/updateuser?token="+td.token,
//				async : false,
//				data:{uid:uid,name:val},
//				success:function(data){
//					var data = JSON.parse(data); 
//                  if(data.code==200){
//                  	callback(val);
//                  }
//                  else{
//                  	toast(data.data);
//                  }
//					
//				},
//				error:function(err){
//					toast("网络异常，请稍后重试");
//				}
//			});
			api.ajax({
                url: td.domain+"/user/updateuser?token="+td.token,
                method: "post",
                data: {
                    values: { 
                        uid:uid,
                        name:val
                    },
                }
            },function(ret, err){
                if (ret) {
                    if(ret.code==200){
                        callback(val);
                    }
                    else{
                        toast(ret.data);
                    }
                } else {
                    toast("网络异常，请稍后重试");
                }
            });
		}
		function postQQAjax(val,callback){
		    var uid=$api.getStorage("uid");
//			$.ajax({
//				type:"post",
//				url:td.domain+"/user/updateuser?token="+td.token,
//				async : false,
//				data:{uid:uid,qq:val},
//				success:function(data){
//					var data = JSON.parse(data); 
//                  if(data.code==200){
//                  	isSuccess=true;
//                  }
//					else{
//                  	toast(data.data);
//                  }
//				},
//				error:function(err){
//					toast("网络异常，请稍后重试");
//				}
//			});
			api.ajax({
                url: td.domain+"/user/updateuser?token="+td.token,
                method: "post",
                data: {
                    values: { 
                        uid:uid,
                        qq:val
                    }
                }
            },function(ret, err){
                if (ret) {
                    if(ret.code==200){
                        callback(val);
                    }
                    else{
                        toast(ret.data);
                    }
                } else {
                    toast("网络异常，请稍后重试");
                }
            });
		}
		function postContentAjax(val,callback){
		    var uid=$api.getStorage("uid");
//			$.ajax({
//				type:"post",
//				url:td.domain+"/user/updateuser?token="+td.token,
//				async : false,
//				data:{uid:uid,content:val},
//				success:function(data){
//					var data = JSON.parse(data); 
//                  if(data.code==200){
//                  	isSuccess=true;
//                  }
//                  else{
//                  	toast(data.data);
//                  }
//				},
//				error:function(err){
//					toast("网络异常，请稍后重试");
//				}
//			});
			api.ajax({
                url: td.domain+"/user/updateuser?token="+td.token,
                method: "post",
                data: {
                    values: { 
                        uid:uid,
                        content:val
                    }
                }
            },function(ret, err){
                if (ret) {
                    if(ret.code==200){
                        callback(val);
                    }
                    else{
                        toast(ret.data);
                    }
                } else {
                    toast("网络异常，请稍后重试");
                }
            });
		}
        function checkForm(type){ 
             if(type=="qq"){
             	var isNum = false;
				var reg= /^[1-9][0-9]{4,9}$/;
				var val = $("#inputText").val();
				if (reg.test(val)) {
					isNum = true;
					inputVal=val;
				} else {
					myAlert('qq号码格式错误', type);
				}
				return isNum;
             }
             else if(type=="fullName"){
             	var isValid = false;
				var reg =  /^[\u4e00-\u9fa5]{2,20}$/; 
				var pattern= /^[a-zA-Z\/]{2,20}$/;  
				var val = $("#inputText").val();
				
				if(!(pattern.test(val)||reg.test(val))){ 
				   myAlert('姓名格式书写错误', type); 
				}  
				else{
				 	isValid = true;
				 	inputVal=val;
			    }
				return isValid;
             }
             else if(type=="introduce"){
             	var isValid = false;
				//var reg = /^.{10}$/;
				var uiinputVal=$("#content").val();
		        if (uiinputVal!=""&&uiinputVal.length>=10) {
					isValid = true;
					inputVal=uiinputVal;
				} else {
					myAlert('至少输入10个字符', type);
				}
				return isValid;
             }
        }
        function myAlert(tipInfo, type) {
			api.openFrame({
				name : 'alert',
				url : '../common/alert.html',
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
		function toast(msg){
			api.toast({
				msg : msg,
				duration : 2000,
				location : 'middle'
			});
		}
		function closeWin(){
		    setTimeout(function(){
		      api.closeWin({});
		    },1000);
			
		}
	});
});

};