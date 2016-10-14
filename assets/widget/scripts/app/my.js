var $cacheSize = "";
var cacheSize = 0;
var canClear = false;
var $msgTip = null;
apiready = function() {
	$(function() {
	    /*增加版本按钮*/
	    addVersionItem();
		// 缓存计算
		$cacheSize = $('#cacheSize');
		resetCacheSize();
        if(api.systemType!="ios"){
        	getLastestVer();
        }
		$msgTip = $("#msg-tip");
		//render 我的消息
		renderMsg();

		$("#myList").on("click", ".item-link", function() {
			var type = $(this).data("src");
			if (type == "clearCache") {
				clearCache('<p>确定要清除缓存吗？</p>', type);
			} else if (type == "version") {
				getLastestVer(true);
			} else if (type == "msg") {
				var uid = $api.getStorage("uid");
				if (!uid || uid == "") {
					goLogin();
				} else {
					$msgTip.addClass("hide");
					api.openWin({
						name : type,
						url : '../my/' + type + '.html',
						slidBackEnabled:false,
						bounces : false,
						delay : 200
					});
				}
			} else if (type == "userInfo") {
				var uid = $api.getStorage("uid");
				if (!uid || uid == "") {
					goLogin();
				} else {
					api.openWin({
						name : type,
						url : '../my/' + type + '.html',
						bounces : false,
						delay : 200,
						slidBackEnabled:false,
						pageParam : {
							currType : type
						}
					});
				}
			}

		});

		$("#btn").on("click", function() {
			myConfirm('<p>退出当前账号</p>', "logout");
		});
		$("#btn-login").on("click", function() {
			goLogin();
		});
	});
};
function addVersionItem(){
	if(api.systemType!="ios"){
     	var html='<li class="item-content item-link"  data-src="version">'
							+'<div class="item-inner">'
								+'<div class="item-title">'
									+'版本更新'
								+'</div>'
								+'<div class="item-after ver-wrap">'
									+'<span id="verNum"></span>'
									+'<span class="dot" id="dot"></span>'
								+'</div>'
							+'</div>'
						+'</li>';
	    $("#myList").append(html);
     }
}
function goLogin() {
	api.openWin({
		name : 'login',
		url : '../common/login.html',
		bounces : false,
		animation : 'push',
		scrollToTop : true,
		slidBackEnabled:false,
		rect : {
			x : 0,
			y : 0,
			w : 'auto',
			h : 'auto'
		}
	});
}

function reLoginRender() {
	$("#btn-login").css("display", "none");
	$("#btn").css("display", "block");

}

function getMsg(unRead) {
	if (unRead == 0) {
		$msgTip.hide();
	} else {
		$msgTip.html(unRead);
		$msgTip.show();
	}
}

function getUnReadDataAjax(callback, type) {
	var csid = $api.getStorage("csid");
	var uid = $api.getStorage("uid");
	var id = "msg_unread_" + type + "_" + csid;
	var folder = "cartype";
	var url = td.domain + "/chat/unread/" + csid + "/" + uid + "/" + type + "?token=" + td.token;
	var method = "get";
	var params = "";
	var async = "true";
	doCache(folder, id, url, method, params, async, function(data) {
		//alert(JSON.stringify(data));
		var finalData = data.data;
		if (data.code == 200) {
			callback(finalData.count);
		} else {
			//toast(data.data);
		}
	});
}

function cusCallBack() {
	var csid = $api.getStorage("csid");
	if (csid && csid != "") {
		getUnReadDataAjax(getMsg, 1);
	}
}

function getAjaxData(callback, displayToast) {

	var uid = $api.getStorage("uid");
	try {
		api.ajax({
			url : td.domain + "/isallowcus/" + uid + "?token=" + td.token,
			method : "get"
		}, function(ret, err) {
			if (ret) {
				if (ret.code == 200) {
					$api.setStorage("csid", ret.data.csid);
					$api.setStorage("cname", ret.data.name);
					callback();
				} else if (ret.code == 201) {
					if (displayToast) {
						toast("未分配客服");
					}
				} else {
					if (displayToast) {
						toast("没有会员ID");
					}
				}

			} else {
				toast("网络异常，请稍后重试");
			}
		});
	} catch(err) {
		toast("系统异常，请稍后重试");
	}
}

function myConfirm(tipInfo, type, modalButtonOk, title) {
	api.openFrame({
		name : 'confirm',
		url : '../common/confirm.html',
		rect : {
			x : 0,
			y : 0,
			w : 'auto',
			h : 'auto'
		},
		pageParam : {
			text : tipInfo,
			type : type,
			modalButtonOk : modalButtonOk,
			title : title
		},
		bounces : false
	});
}

function clearCache(tipInfo, type) {
	if (canClear) {
		myConfirm(tipInfo, type)
	} else {
		toast("已很干净");
	}
}

function toast(msg) {
	api.toast({
		msg : msg,
		duration : 1000,
		location : 'middle'
	});
}

function renderMsg() {
	//未读信息0:私聊;1:群发
	var uid = $api.getStorage("uid");
	if (!uid || uid == "") {
		$("#btn").css("display", "none");
		$("#btn-login").css("display", "block");
	} else {
		$("#btn").css("display", "block");
		$("#btn-login").css("display", "none");
		var csid = $api.getStorage("csid");
		if (!csid || csid == "") {
			getAjaxData(cusCallBack);
		} else {
			cusCallBack();
		}
	}
}

function hideVersion() {
	$("#verNum").html("");
	$("#dot").hide();
}

function showVersion(ver) {
	$("#verNum").html(ver);
	$("#dot").show();
}

function resetCacheSize() {
	// 缓存计算
	//alert("resetCacheSize");
	api.getCacheSize(function(ret, err) {
		cacheSize = ret.size;
		//10M
		if (cacheSize > 10485760) {
			canClear = true;
			var sizeM = (parseFloat(cacheSize) / 1024) / 1024;
			sizeM = sizeM.toFixed(2);
			$cacheSize.html(sizeM + "M");
		} else {
			canClear = false;
			$cacheSize.html("");
		}
	});
}