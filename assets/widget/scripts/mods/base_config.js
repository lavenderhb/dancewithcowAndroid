var BASE_SERVER_IP = '这是一个ip';

//表情存放路径
var BASE_EMOTION_PATH = 'widget://images/emotion';
//会话相关图片存储路径
var BASE_CHATBOX_PATH = 'widget://images/chatbox';
//附件等下载地址
var BASE_DOWNLOAD_YY_PATH = '';
//平台类型，
var BASE_APP_TYPE = 1;
//var BASE_APP_TYPE = ;
// 请求统一路径
var BASE_URL_ACTION = '';
//接收头像默认值
var BASE_RECEIVER_DEFAULT_IMG;
if (BASE_APP_TYPE == 1) {
	BASE_RECEIVER_DEFAULT_IMG = '';
} else {
	BASE_RECEIVER_DEFAULT_IMG = BASE_URL_ACTION + '.jpg';
}
//cchelper的appKey
var BASE_CCHELPER_KEY_IOS = '';
var BASE_CCHELPER_KEY_ANDROID = '';

var BASE_MY_MENU = '{}';
//分页每页条数
var BASE_PAGE_SIZE = 10;

//当前开通应用数组
var BASE_YY_OPEN = [];
//通知模块
BASE_YY_OPEN[0] = 'tongzhi_index_window';
//课程表模块
BASE_YY_OPEN[1] = 'kcb_index_window';
//资源模块
BASE_YY_OPEN[2] = 'zy_index_window';
//数据库存放fs路径
var BASE_FS_SQDB = '';

var url_path_action;
function getContextActionPath() {
	var pathName = document.location.pathname;

	var index = pathName.substr(1).indexOf("/html");
	var result = pathName.substr(0, index + 1);

	result = "";
	return result;
}

//未登录时调用的action路径
var url_path_action_login = getContextActionPath();
//前台action路径
url_path_action = getContextActionPath() + "/";

/*一个工具方法：可以获取所有表情图片的名称和真实URL地址，以JSON对象形式返回。其中以表情文本为 属性名，以图片真实路径为属性值*/
function getImgsPaths(sourcePathOfChatBox, callback) {
	var jsonPath = sourcePathOfChatBox + "/emotion.json";
	//表情的JSON数组
	api.readFile({
		path : jsonPath
	}, function(ret, err) {
		if (ret.status) {
			var emotionArray = JSON.parse(ret.data);
			var emotion = {};
			var httpEmotion = {};
			for (var idx in emotionArray) {
				var emotionItem = emotionArray[idx];
				var emotionText = emotionItem["text"];
				var emotionUrl = "../../images/emotion/" + emotionItem["name"] + ".png";
				var emotionHttpUrl =EMOTION_HTTP_PATH+emotionItem["name"] + ".png";
				emotion[emotionText]= emotionUrl;
				httpEmotion[emotionText]= emotionHttpUrl;
			}
			/*把emotion对象 回调出去*/
			if ("function" === typeof (callback)) {
				callback(emotion,httpEmotion);
			}
		}
	});
}

/*
 * 获取当前设备操作系统
 * 返回值：安卓:android 苹果：ios
 * 周枫
 * 2015.08.03
 */
function getPhoneSystem() {
	return api.systemType;
}

/**
 * 获取json对象的长度
 * 周枫
 * 2015.08.26
 * @param {Object} jsonObj
 */
function getJsonObjLength(jsonObj) {
	var Length = 0;
	for (var item in jsonObj) {
		Length++;
	}
	return Length;
}

/**
 *  在artTemplate的标签中使用外部函数的方法
 * 周枫
 * 2015.08.26
 * @param {Object} data
 */
function beforeRenderTemplate(data) {

}

/**
 * 获取群组信息
 * 周枫
 * 2015.12.05
 */
function getGroupListById(target_id, callback) {
	api.ajax({
		url : BASE_URL_ACTION,
		method : 'get',
		dataType : 'text'
	}, function(ret, err) {
		var obj = eval('(' + ret + ')');
		$api.setStorage('group_list_data', obj);
		if (obj) {
			callback(obj);
		}
	});
}

//替换所有的回车换行
function transferBr(content) {
	var string = content;
	try {
		string = string.replace(/\r\n/g, "<br>")
		string = string.replace(/\n/g, "<br>");
	} catch(e) {
		alert(e.message);
	}
	return string;
}

//action提交随机数，6位
var random_num = 0;
function creatRandomNum() {
	for (var i = 0; i < 6; i++) {
		random_num += Math.floor(Math.random() * 100000);
	}
	return random_num;
}

function closeAppForAndroid() {
	var mkeyTime = new Date().getTime();
	api.addEventListener({
		name : "keyback"
	}, function(ret, err) {
		//如果当前时间减去标志时间大于2秒，说明是第一次点击返回键，反之为2秒内点了2次，则退出。
		if ((new Date().getTime() - mkeyTime) > 2000) {
			mkeyTime = new Date().getTime();
			api.toast({
				msg : '再按一次退出程序',
				duration : 2000,
				location : 'middle'
			});
		} else {
			api.closeWidget({
				silent : true
			});
		}
	});
}

//替换所有字符
String.prototype.replaceAll = function(s1, s2) {
	return this.replace(new RegExp(s1, "gm"), s2);
}
function html_decode(str) {
	var s = "";
	if (str.length == 0)
		return "";
	s = str.replace(/&gt;/g, "&");
	s = s.replace(/&#60;/g, "<");
	s = s.replace(/&#62;/g, ">");
	s = s.replace(/&nbsp;/g, " ");
	s = s.replace(/&#39;/g, "\'");
	s = s.replace(/&quot;/g, "\"");
	s = s.replace(/<br>/g, "\n");
	return s;
}

//获取guid
function newGuid() {
	var guid = "";
	for (var i = 1; i <= 32; i++) {
		var n = Math.floor(Math.random() * 16.0).toString(16);
		guid += n;
		if ((i == 8) || (i == 12) || (i == 16) || (i == 20))
			guid += "-";
	}
	return guid.toUpperCase();
}

/**
 * 延迟加载
 * 周枫
 * 2015.12.22
 */
function echoInit() {
	echo.init({
		offset : 0,
		throttle : 0 //设置图片延迟加载的时间
	});
}