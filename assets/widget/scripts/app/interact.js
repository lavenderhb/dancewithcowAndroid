var header_t;
var header_h;
var target_id;

var conver_type;
var h_from;
var person_name;
var socketManager;
var ws=false,key='all';
var content="";
var myid="";
var targetid="";
var name="";
var isFirst=true;
var isConnected=false;
var chatBox=null;
var avatar_url=null;
var old_msg_id=null;
apiready = function() {
   // clearTimeout(window.frameTimer);
	//定位header位置，留出上面电池等空隙，苹果需要
	var header = $api.byId('aui-header');
	$api.fixIos7Bar(header);
	header_t = $api.offset($api.byId('cloud'));
	header_h = $('#aui-header').height();
	//当前会话用户id和当前会话历史消息从消息列表页点击传递进来
	targetid = api.pageParam.csid;
	//真实姓名
	person_name = api.pageParam.cname;
    myid=$api.getStorage("uid");
    name=$api.getStorage("username");
    
	conver_type = 'PRIVATE';
	avatar_url = api.pageParam.avatar_url;

	//从哪个页面进入的聊天界面
	h_from = api.pageParam.h_from;
	initHeaer();
    creatSocket(); 
	//当前target_id的最大会话id
	old_msg_id = api.pageParam.old_msg_id;
	//打开聊天内容frame页面
	var w=api.winWidth;
	var h=api.winHeight - header_h - 50;
	//window.frameTimer=setTimeout(function(){
		api.openFrame({
			name : 'interact_frm',
			scrollToTop : true,
			allowEdit : true,
			url : '../interact/interact_frm.html',
			pageParam : {
				'targetid' : targetid,
				'old_msg_id' : old_msg_id,
				'conver_type' : conver_type,
				'avatar_url' : avatar_url,
				'header_h' : header_h
			},
			rect : {
				x : 0,
				y : header_h,
				w : w,
				h : h,
			},
			//页面是否弹动 为了下拉刷新使用
			bounces : true
		});
	//},1000);
	
     
	//安卓关闭
	if (api.systemType == 'android') {
		backFromChatForAndroid();
	}
	if (conver_type != 'SYSTEM') {
		//加载uichatbox模块
		initUichatbox();
	}
	
};
function creatSocket(){
	if(api.systemType=="ios"){
		websocket();
	}
	else
	{
		socketManager();
	}
}
function socketManager(){
    
	socketManager = api.require('socketManager');
	socketManager.createSocket({
	    host: td.host,
	    port: td.port
	}, function(ret, err){
	    if(ret){
	        var state = ret.state;
	        var sid = ret.sid;
	        var data = ret.data;
	        var stateStr = "";
	        if(101 === state){
	            stateStr = "创建成功";
	        }else if(102 === state){
	        	 var sid = ret.sid
                 $api.setStorage('sid',sid );//把SID储存在本地
	             stateStr = "连接成功";
	             isConnected=true;
	        }else if(103 === state){
	            stateStr = "收到消息";
	            //alert("收到消息"+JSON.stringify(data));
	            //var str=JSON.stringify(data);
	           	//alert("data"+data);
				var newstr=data.replace(/(.*)\{/gi,"");
				//alert("newstr"+newstr);
				var json="{"+newstr;
				eval('var dataObj='+json);
				//alert("eval dataObj"+JSON.stringify(dataObj));
				receiveMsgDetail(dataObj);
	        }else if(201 === state){
	            stateStr = "创建失败";
	            isConnected=false;
	        }else if(202 === state){
	            stateStr = "连接失败";
	            isConnected=false;
	        }else if(203 === state){
	            stateStr = "异常断开";
	            isConnected=false;
	        }else if(204 === state){
	            stateStr = "正常断开";
	            isConnected=false;
	        }else if(205 === state){
	            stateStr = "发生未知错误";
	            isConnected=false;
	        }
//	        var msg = 'sid: '+sid+'\nstate: '+stateStr+'\ndata: '+(data);
//	        api.alert({msg:msg});
	    }
	});
}

function websocket(){
	//socket监听
		var url='ws://'+td.host+':'+td.port;
		ws=new WebSocket(url);
		ws.onopen=function(){
			//console.log("握手成功");
			//alert('握手成功');
			if(ws.readyState==1){
				//ws.send("id="+myid+'&content='+content+'&key='+key+'&name='+name+'&targetid='+targetid);
				isConnected=true;
			}
		}

		ws.onclose=function(){
			ws=false;
			isConnected=false;
		}
		ws.onerror = function(){
			console.log("error");
			isConnected=false;
		};
		ws.onmessage=function(msg){
		    eval('var data='+msg.data);
		    receiveMsgDetail(data);
		}
}

function receiveMsgDetail(data){
	if(data.type=='msg'){
//		if(myid==data.id && myid!='' && targetid==data.targetid){
//			
//		    var  script="sendText('" + content + "','"+ conver_type + "','"+data.time+"')";
//		    api.execScript({
//				name : 'interact',
//				frameName : 'interact_frm',
//				script : script
//			});
//		    sendmsgToServer();
//		    
//		    
//		}else 
		if(targetid==data.id && myid!='' && myid==data.targetid){
			//$(".win-mg").append("<dl><dt><span>"+msgdata.name+"</span><q>"+msgdata.time+"</q></dt><dd><span class='jian2'></span>"+msgdata.content+"</dd></dl>");
		   // alert("onmessage"+data);
		    //对方发的
			api.sendEvent({
				name : 'getNewMessage',
				extra : {
					data : data
				}
			});
		}
	}
}
function sendmsgToServer(){
   // alert("id:myid,targetid:targetid,content:content"+myid+"  "+targetid+" "+content);
    try{
	    api.ajax({
            url: td.domain+"/sendmsg?token="+td.token,
            method: "post",
            data: {
                values: { 
                    id:myid,targetid:targetid,content:content
                }
            }
        },function(ret, err){
            if (ret) {
                if(ret.code==200){
                }
            } else {
                toast("网络异常，请稍后重试");
            }
        });
     }catch(err){
    	 toast("系统异常，请稍后重试");
    }
}


/**
 * 安卓点击返回的时候
 * 周枫
 * 2015.08.31
 */
function backFromChatForAndroid() {
	api.addEventListener({
		name : "keyback"
	}, function(ret, err) {
		back();
	});
}

/**
 *返回会话列表页面
 * 周枫
 * 2015.08.08
 */
function back() {
	//        api.alert({
	//                msg : 'back'
	//  },function(ret,err){
	//          //coding...
	//  });
	if(chatBox){
		chatBox.close();
	}
	api.closeFrame({
	    name: 'interact_frm'
    });
    api.execScript({
        name:"root",
        frameName: 'main',
	    script: 'clearUnreadStatus();'
    });
	api.closeWin();
}

function reloadTxlIndex() {
	//        api.alert({
	//                msg : '11111'
	//        }, function(ret, err) {
	//                //coding...
	//        });
	api.execScript({
		name : 'interact_start',
		frameName : 'txl_index',
		script : 'loadData();'
	});
}

/**
 *加载uichatbox模块
 * 周枫
 * 2015.08.08
 */
function initUichatbox() {
	//引入chatbox
	chatBox = api.require('UIChatBox');
	//获取表情存放路径
	var sourcePath = BASE_EMOTION_PATH;
	//表情存放目录
	var emotionData;
	var httpEmotionData;
	//存储表情
	getImgsPaths(sourcePath, function(emotion,httpEmotion) {
	          emotionData = emotion;
	          httpEmotionData=httpEmotion;
	});
	chatBox.open({
		placeholder : '',
		//输入框显示的最大行数（高度自适应）
		maxRows : 4,
		//自定义表情文件夹（表情图片所在的文件夹，须同时包含一个与该文件夹同名的.json配置文件）的路径
		//.json文件内的 name 值必须与表情文件夹内表情图片名对应
		emotionPath : sourcePath,
		//聊天输入框模块可配置的文本
//		texts : {
//			//（可选项）JSON对象；录音按钮文字内容
//			recordBtn : {
//				//（可选项）字符串类型；按钮常态的标题，默认：'按住 说话'
//				normalTitle : '按住 说话',
//				//（可选项）字符串类型；按钮按下时的标题，默认：'松开 结束'
//				activeTitle : '松开 结束'
//			}
//		},
		//模块各部分的样式集合
		styles : {
			//（可选项）JSON对象；输入区域（输入框及两侧按钮）整体样式
			inputBar : {
				borderColor : '#d9d9d9',
				bgColor : '#f2f2f2'
			},
			//（可选项）JSON对象；输入框样式
			inputBox : {
				borderColor : '#B3B3B3',
				bgColor : '#FFFFFF'
			},
			//JSON对象；表情按钮样式
			                      emotionBtn : {
			                              normalImg : BASE_CHATBOX_PATH + '/face1.png'
			                      },
			//                      //（可选项）JSON对象；附加功能按钮样式，不传则不显示附加功能按钮
			//                      extrasBtn : {
			//                             // normalImg : BASE_CHATBOX_PATH + '/chatBox_add1.png'
			//                      },
			//                      //JSON对象；键盘按钮样式
			                      keyboardBtn : {
			                              normalImg : BASE_CHATBOX_PATH + '/key3.png'
			                      },
			//                      //（可选项）JSON对象；输入框左侧按钮样式，不传则不显示左边的语音按钮
			//                      speechBtn : {
			//                             // normalImg : BASE_CHATBOX_PATH + '/chatBox_key1.png'
			//                      },
			//                      //JSON对象；“按住 录音”按钮的样式
			//                      recordBtn : {
			//                              //（可选项）字符串类型；按钮常态的背景，支持rgb，rgba，#，图片路径（本地路径，fs://，widget://）；默认：'#c4c4c4'
			//                              normalBg : '#c4c4c4',
			//                              //（可选项）字符串类型；按钮按下时的背景，支持rgb，rgba，#，图片路径（本地路径，fs://，widget://）；默认：'#999999'；
			//                              //normalBg 和 activeBg 必须保持一致，同为颜色值，或同为图片路径
			//                              activeBg : '#999999',
			//                              color : '#000',
			//                              size : 14
			//                      },
			//                      //（可选项）JSON对象；表情和附加功能面板的小圆点指示器样式，若不传则不显示该指示器
			                      indicator : {
			                              //（可选项）字符串类型；配置指示器的显示区域；默认：'both'
			                              //取值范围：
			                              //both（表情和附加功能面板皆显示）
			                              //emotionPanel（表情面板显示）
			                              //extrasPanel（附加功能面板显示）
			                              target : 'both',
			                              color : '#c4c4c4',
			                              activeColor : '#9e9e9e'
			                      },
			                       sendBtn: {                         //（可选项）JSON对象；发送按钮样式，本参数对 IOS 平台上的键盘内发送按钮无效
								        bg: '#EE4A49',                 //（可选项）字符串类型；发送按钮背景颜色，支持rgb、rgba、#、img；默认：#4cc518
								        titleColor: '#ffffff',          //（可选项）字符串类型；发送按钮标题颜色；默认：#ffffff
								        activeBg: '#f6383a',            //（可选项）字符串类型；发送按钮背景颜色，支持rgb、rgba、#、img；默认：无
								        titleSize: 13                    //（可选项）数字类型；发送按钮标题字体大小；默认：13
								    }
		},
		//（可选项）点击附加功能按钮，打开的附加功能面板的按钮样式，配合 extrasBtn 一起使用，若 extrasBtn 参数内 normalImg 属性不传则此参数可不传
		extras : {
			//                      titleSize : 10,
			//                      titleColor : '#a3a3a3',
			//                      //数组类型；附加功能按钮的样式
			//                      btns : [{
			//                              title : '图片',
			//                              //（可选项）字符串类型；按钮常态的背景图片
			//                              normalImg : BASE_CHATBOX_PATH + '/chatBox_album1.png',
			//                              //（可选项）字符串类型；按钮按下时的背景图片
			//                              activeImg : BASE_CHATBOX_PATH + '/chatBox_album2.png'
			//                      }, {
			//                              title : '拍照',
			//                              normalImg : BASE_CHATBOX_PATH + '/chatBox_cam1.png',
			//                              activeImg : BASE_CHATBOX_PATH + '/chatBox_cam2.png'
			//                      }]
		}
	}, function(ret, err) {
		//字符串类型；回调的事件类型，
		//取值范围：
		//show（该模块打开成功）
		//send（用户点击发送按钮）
		//clickExtras（用户点击附加功能面板内的按钮）
		//数字类型；当 eventType 为 clickExtras 时，此参数为用户点击附加功能按钮的索引，否则为 undefined
		//字符串类型；当 eventType 为 send 时，此参数返回输入框的内容，否则返回 undefined

		//点击附加功能面板
//		if (ret.eventType == 'clickExtras') {
//			var c_index = ret.index;
//			switch(c_index) {
//				case 0:
//					//                                        api.confirm({
//					//                                                title : "提示",
//					//                                                msg : "您想要从哪里选取图片 ?",
//					//                                                buttons : ["现在照", "相册选", "取消"]
//					//                                        }, function(ret, err) {
//					//                                                //定义图片来源类型
//					//                                                var sourceType;
//					//                                                if (1 == ret.buttonIndex) {/* 打开相机*/
//					//                                                        sourceType = "camera";
//					//                                                } else if (2 == ret.buttonIndex) {
//					//                                                        sourceType = "album";
//					//                                                } else {
//					//                                                        return;
//					//                                                }
//					//相册选
//					//getPicture("album");
//					//                                        });
//					break;
//				case 1:
//					//现在照
//					//getPicture("camera");
//					break;
//			}
//
//		}
//      if(ret.eventType == 'show'){
//      	//打开聊天内容frame页面
//			api.openFrame({
//				name : 'interact_frm',
//				scrollToTop : true,
//				allowEdit : true,
//				url : '../interact/interact_frm.html',
//				pageParam : {
//					'targetid' : targetid,
//					'old_msg_id' : old_msg_id,
//					'conver_type' : conver_type,
//					'avatar_url' : avatar_url,
//					'header_h' : header_h
//				},
//				rect : {
//					x : 0,
//					y : header_h,
//					w : api.winWidth,
//					h : api.winHeight - header_h - 50,
//				},
//				//页面是否弹动 为了下拉刷新使用
//				bounces : true
//			});
//      
//      }
		//点击发送按钮
		if (ret.eventType == 'send') {
			/*
			 *1.用户输入文字或表情
			 */
			/*用户输入表情或文字*/
			/*使用读文件方法，读json*/
			content= transText(ret.msg);
			//alert("content"+content);
			if ($api.trimAll(content).length != 0) {
				//发送消息的函数，后面会有介绍
				//发送消息
				sendTextDetail(content);
				
				/*将文字中的表情符号翻译成图片，并可自定义图片尺寸*/
				function transText(text, imgWidth, imgHeight) {
					var imgWidth = imgWidth || 24;
					var imgHeight = imgHeight || 24;
					var regx = /\[(.*?)\]/gm;
					var textTransed = text.replace(regx, function(match) {
						var imgSrc = emotionData[match];
						var httpSrc=httpEmotionData[match];
						if (!imgSrc) {
							//说明不对应任何表情，直接返回
							return match;
						}
						var img = '<img   class="emotion-img"   data-httpsrc="'+httpSrc+'"  src="' + imgSrc + '" width=' + imgWidth + ' height=' + imgHeight +'>';
						//alert("img："+img);
						return img;
					});
					textTransed = transferBr(textTransed);
					return textTransed;
				}

			} else {
				//为ipad写的
				toast('对不起，消息不能为空');
			}
		}
	});
    function nofind(){
        var img=event.srcElement;
        img.src=$(img).attr("data-httpsrc");
        img.onerror=null; //控制不要一直跳动
    }
    function sendTextDetail(content){
    	if(api.systemType=="ios"){
    		 sendTextIos(content)
    	}
    	else
    	{
    		sendTextAndRoid(content);
    	}
    }
    function sendTextIos(content){
        if(!isConnected||!ws){
	       websocket();
	       if(!isConnected||!ws){
	       		toast("网络异常，请检查");
	       		return;
	       }
	    }
    	ws.send("id="+myid+'&content='+content+'&key='+key+'&name='+name+'&targetid='+targetid);
    	appendContendAndToServer(content);
    }
    function sendTextAndRoid(content){
         if(!isConnected){
         	creatSocket();
         	if(!isConnected){
         		toast("网络异常，请检查");
	       		return;
         	}
         }
    	 var sid=$api.getStorage('sid');
		 if(isFirst){
		 	socketManager.write({
			    sid: sid,            //由createSocket方法获取得到
			    data: "id="+myid+'&content='+content+'&key='+key+'&name='+name+'&targetid='+targetid
			}, function(ret, err){
			    if(ret.status){
			        
			    }else{
			        api.alert({msg:'error'});
			    }
			});
	        socketManager.write({
			    sid: sid,            //由createSocket方法获取得到
			    data: "id="+myid+'&content='+content+'&key='+key+'&name='+name+'&targetid='+targetid
			}, function(ret, err){
			    if(ret.status){
			       
			    }else{
			        api.alert({msg:'error'});
			    }
			});
			isFirst=false;
		 }
		 else{
		 	socketManager.write({
			    sid: sid,            //由createSocket方法获取得到
			    data: "id="+myid+'&content='+content+'&key='+key+'&name='+name+'&targetid='+targetid
			}, function(ret, err){
			    if(ret.status){
			        
			    }else{
			        api.alert({msg:'error'});
			    }
			});
		 }
		 appendContendAndToServer(content);
		  
    }
    function appendContendAndToServer(content){
    	var currSendTime=formatDate(new Date,"YYYY-MM-DD hh:MM:ss");
    	//alert("currSendTime:"+currSendTime);
		var  script="sendText('" + content + "','"+ conver_type + "','"+currSendTime+"')";
		//alert("script:"+script);
	    api.execScript({
			name : 'interact',
			frameName : 'interact_frm',
			script : script
		});
	    sendmsgToServer();
    }
    function toast(msg){
		api.toast({
	        msg: msg,
	        duration:1000,
	        location: 'bottom'
	    });
	}
	//加载录音按钮事件
	/**
	 press（按下录音按钮）
	 press_cancel（松开录音按钮）
	 move_out（按下录音按钮后，从按钮移出）
	 move_out_cancel（按下录音按钮后，从按钮移出并松开按钮）
	 move_in（move_out 事件后，重新移入按钮区域）
	 */
//	chatBox.addEventListener({
//		target : 'recordBtn',
//		name : 'press'
//	}, function(ret, err) {
//		//开始录音
//		startRecord();
//	});
//	//（松开录音按钮）
//	chatBox.addEventListener({
//		target : 'recordBtn',
//		name : 'press_cancel'
//	}, function(ret, err) {
//		setTimeout(function() {
//			stopRecord();
//		}, 1000);
//
//	});
//	//move_out（按下录音按钮后，从按钮移出）
//	chatBox.addEventListener({
//		target : 'recordBtn',
//		name : 'move_out'
//	}, function(ret, err) {
//		api.execScript({
//			name : '',
//			frameName : 'hh_voice_window',
//			script : 'moveOut()'
//		});
//	});
//	//move_out_cancel（按下录音按钮后，从按钮移出并松开按钮）
//	chatBox.addEventListener({
//		target : 'recordBtn',
//		name : 'move_out_cancel'
//	}, function(ret, err) {
//		api.stopRecord(function(ret, err) {
//			if (ret) {
//				removefile(ret.path);
//			}
//		});
//		api.closeFrame({
//			name : 'hh_voice_window'
//		});
//	});
//	//move_in（move_out 事件后，重新移入按钮区域）
//	chatBox.addEventListener({
//		target : 'recordBtn',
//		name : 'move_in'
//	}, function(ret, err) {
//		api.execScript({
//			name : '',
//			frameName : 'hh_voice_window',
//			script : 'moveIn()'
//		});
//	});
	//输入框绑定
	/**
	 *
	 move（输入框所在区域弹动事件）
	 change（输入框所在区域高度改变）
	 showRecord（用户点击左侧语音按钮）
	 showEmotion（用户点击表情按钮）
	 showExtras（用户点击右侧附加功能按钮，如果 open 时传了 extras 参数才会有此回调）

	 */
	//move（输入框所在区域弹动事件）  就是输入框收起和弹出变化
	chatBox.addEventListener({
		target : 'inputBar',
		name : 'move'
	}, function(ret, err) {
		//                  api.toast({msg: JSON.stringify(ret),location: 'top'}); //50
		//                            api.toast({msg: JSON.stringify(err),location: 'middle'}); //283
		//点击输入框时会话界面高度发生变化
		setChatFrameByInputMove(ret.inputBarHeight, ret.panelHeight);
	});

	//change（输入框所在区域高度改变）
	chatBox.addEventListener({
		target : 'inputBar',
		name : 'change'
	}, function(ret, err) {
		//                  api.toast({msg: JSON.stringify(ret),location: 'top'}); //50
		//                            api.toast({msg: JSON.stringify(err),location: 'middle'}); //283
		//点击输入框时会话界面高度发生变化
		setChatFrameByInputChange(ret.inputBarHeight, ret.panelHeight);
	});
}

/**
 *发送消息
 * 周枫
 * 2015.08.08
 * @param {Object} sendMsg
 */
//function chat(sendMsg) {
//        //向会话列表页发送消息事件
//        api.sendEvent({
//                name : 'sendMessage',
//                extra : {
//                        type : 'text',
//                        targetId : '' + target_id + '',
//                        content : sendMsg,
//                        conversationType : 'PRIVATE',
//                        extra : ''
//                }
//        })
//}

/**
 * 删除文件
 * 周枫
 * 2015.08.10
 * @param {Object} path
 */
function removefile(path) {
//	var fs = api.require('fs');
//	fs.remove({
//		path : path
//	}, function(ret, err) {
//		if (ret.status != true) {
//			//                        api.alert({
//			//                                msg : err.msg
//			//                        }, function(ret, err) {
//			//                                //coding...
//			//                        });
//		}
//	});
}

/**
 *开始录音
 * 周枫
 * 2015.08.10
 */
function startRecord() {
	//先删除再录音
	api.stopRecord(function(ret, err) {
		if (ret) {
			removefile(ret.path);
		}
	});
	api.openFrame({
		name : 'hh_voice_window',
		url : '../../html/huihua/hh_voice_window.html',
		scrollToTop : true,
		rect : {
			x : 0,
			y : 0,
			w : api.winWidth,
			h : api.winHeight - 50,
		},
	});
	//点击后播放开启录音的声音
	api.startPlay({
		path : 'widget://res/LowBattery.mp3'
	}, function() {
		api.startRecord();
	});
}

/**
 * 结束录音
 * path:'',              //字符串，返回的音频地址
 duration:0            //数字类型，音频的时长
 * 周枫
 * 2015.08.10
 */
function stopRecord() {
	api.stopRecord(function(ret, err) {
		if (ret) {
			if (ret.duration == 0 || ret.duration < 1) {
				api.execScript({
					name : '',
					frameName : 'hh_voice_window',
					script : 'moveShort()'
				});
				removefile(ret.path);
			} else if (ret.duration > 60) {
				api.execScript({
					name : '',
					frameName : 'hh_voice_window',
					script : 'moveLong()'
				});
				removefile(ret.path);
			} else {
				api.sendEvent({
					name : 'setVoice',
					extra : {
						voice_result : ret,
						conver_type : conver_type
					}
				});
			}
			setTimeout("api.closeFrame({name: 'hh_voice_window'})", 400);
		} else {
			api.execScript({
				name : '',
				frameName : 'hh_voice_window',
				script : 'moveShort()'
			});
			setTimeout("api.closeFrame({name: 'hh_voice_window'})", 400);
		}
	});
}

/**
 * 点击输入框时会话界面高度发生变化
 *  inputBarHeight: 50,    //数字类型；输入框及左右按钮整体区域的高度，仅当监听 inputBar 的 move 和 change 事件时本参数有值
 panelHeight: 300       //数字类型；输入框下边缘距离屏幕底部的高度，仅当监听 inputBar 的 move 和 change 事件时本参数有值
 * 周枫
 * 2015.08.10
 */
function setChatFrameByInputMove(inputBarHeight, panelHeight) {
    //alert("move"+panelHeight);
	//if (inputBarHeight > 0) {//输入框打开时
		api.setFrameAttr({
			name : 'interact_frm',
			rect : {
				x : 0,
				y : header_h,
				w : api.winWidth,
				h : api.winHeight - header_h - inputBarHeight - panelHeight-15,
			},
		});
		if(panelHeight==0){
		   var $wrap=$("#wrap");
		   setTimeout(function(){
//		   		api.execScript({
//					name : '',
//					frameName : 'interact_frm',
//					script : 'refreshPage()'
//				});
				$wrap.focus();
				$wrap.click();
//				api.sendEvent({
//				    name: 'myclick'
//				});
		   },500);
			
		}
//	} else {//关闭时
//		api.setFrameAttr({
//			name : 'interact_frm',
//			rect : {
//				x : 0,
//				y : header_h,
//				w : api.winWidth,
//				h : api.winHeight - header_h - inputBarHeight,
//			},
//		});
//	}
	setTimeout('setBottom()', 200);
}

/**
 * 输入框内文字行数，现设置为最多4行
 *  inputBarHeight: 50,    //数字类型；输入框及左右按钮整体区域的高度，仅当监听 inputBar 的 move 和 change 事件时本参数有值
 panelHeight: 300       //数字类型；输入框下边缘距离屏幕底部的高度，仅当监听 inputBar 的 move 和 change 事件时本参数有值
 * 周枫
 * 2015.08.10
 */
function setChatFrameByInputChange(inputBarHeight, panelHeight) {
    //alert("Change"+panelHeight);
	api.setFrameAttr({
		name : 'interact_frm',
		rect : {
			x : 0,
			y : header_h,
			w : api.winWidth,
			h : api.winHeight - header_h - inputBarHeight - panelHeight-15,
		},
	});
	setTimeout('setBottom()', 200);
}

function setBottom() {
	api.execScript({
		name : '',
		frameName : 'interact_frm',
		script : 'goBottom()'
	});
}

/**
 *  通过系统相册或拍照获取图片和视频
 sourceType：（可选项）图片源类型，从相册、图片库或相机获取图片，  library：图片库，camera：相机，album：相册
 encodingType：（可选项）返回图片类型，jpg或png，默认值：png
 mediaValue：（可选项）媒体类型，图片或视频 ，pic：图片，video：视频
 destinationType：（可选项）返回数据类型，指定返回图片地址或图片经过base64编码后的字符串
 allowEdit：（可选项）是否可以选择图片后进行编辑，只支持iOS，默认值：false
 quality：（可选项）图片质量，只针对jpg格式图片（0-100整数），默认值：50
 targetWidth：（可选项）压缩后的图片宽度，图片会按比例适配此宽度，默认值：原图宽度
 targetHeight：（可选项）压缩后的图片高度，图片会按比例适配此高度，默认值：原图高度
 saveToPhotoAlbum：（可选项）拍照或录制视频后是否保存到相册，默认值：false
 callback
 {
 data:"",                //图片路径
 base64Data:"",          //base64数据，destinationType为base64时返回
 duration:0              //视频时长（数字类型）
 }
 * 通过系统相册或拍照获取图片和视频
 * 周枫
 * 2015.08.11
 * @param {Object} sourceType
 */
//function getPicture(sourceType) {
//	switch(sourceType) {
//		case 'camera':
//			//获取一张图片
//			api.getPicture({
//				sourceType : sourceType,
//				encodingType : 'png',
//				mediaValue : 'pic',
//				allowEdit : false,
//				quality : 80,
//				//                targetWidth : 100,
//				//                targetHeight : 1280,
//				saveToPhotoAlbum : true
//			}, function(ret, err) {
//				if (ret) {
//					var imgSrc = ret.data;
//					if (imgSrc != "") {
//						api.sendEvent({
//							name : 'setPicurl',
//							extra : {
//								imgSrc : imgSrc,
//								pic_source : 'camera',
//								conver_type : conver_type
//							}
//						});
//					}
//					//                                        sendPic(imgSrc);
//				}
//			});
//			break;
//		case 'album':
//			//UIMediaScanner 是一个多媒体扫描器，可扫描系统的图片、视频等多媒体资源
//			var obj = api.require('UIMediaScanner');
//			obj.open({
//				//返回的资源种类,picture（图片）,video（视频）,all（图片和视频）
//				type : 'picture',
//				//（可选项）图片显示的列数，须大于1
//				column : 4,
//				max : 4,
//				//（可选项）图片排序方式,asc（旧->新）,desc（新->旧）
//				sort : {
//					key : 'time',
//					order : 'desc'
//				},
//				//（可选项）模块各部分的文字内容
//				texts : {
//					stateText : '已选择*项',
//					cancelText : '取消',
//					finishText : '完成'
//				},
//				styles : {
//					bg : '#fff',
//					mark : {
//						icon : '',
//						position : 'bottom_right',
//						size : 20
//					},
//					nav : {
//						bg : '#eee',
//						stateColor : '#000',
//						stateSize : 18,
//						cancleBg : 'rgba(0,0,0,0)',
//						cancelColor : '#000',
//						cancelSize : 18,
//						finishBg : 'rgba(0,0,0,0)',
//						finishColor : '#000',
//						finishSize : 18
//					}
//				}
//			}, function(ret) {
//				//callback
//				// list: [{                    //数组类型；返回选定的资源信息数组
//				//path: '',                    //字符串类型；资源路径，返回资源在本地的绝对路径
//				//thumbPath: '',               //字符串类型；缩略图路径，返回资源在本地的绝对路径
//				//suffix: '',                  //字符串类型；文件后缀名，如：png，jpg, mp4
//				//size: 1048576,               //数字类型；资源大小，单位（Bytes）
//				//time: '2015-06-29 15:49'     //字符串类型；资源创建时间，格式：yyyy-MM-dd HH:mm:ss
//				//}]
//				if (ret) {
//					if (getJsonObjLength(ret.list) != 0) {
//						api.sendEvent({
//							name : 'setPicurl',
//							extra : {
//								image_list : ret.list,
//								pic_source : 'album',
//								conver_type : conver_type
//							}
//						});
//					}
//				}
//			});
//			break;
//	}
//
//}

/**
 * 根据群组id获取群组信息页面
 * 周枫
 * 2015.09.11
 */
function showGroupListById() {
	var group_list_sto = $api.getStorage('group_list_data');
	group_list_sto=[];
	if ( typeof (group_list_sto) != 'undefined') {
		api.openWin({
			name : 'hh_group_window',
			url : '../interact/hh_group_window.html',
			bounces : true,
			delay : 300,
			animation : {
				duration : 400
			},
			scrollToTop : true,
			pageParam : {
				'targetId' : targetid,
				'header_h' : header_h,
				'group_list' : group_list_sto,
				'group_name' : person_name
			}
		});
	} else {
		getGroupListById(function(target_id, group_list) {
			api.openWin({
				name : 'hh_group_window',
				url : '../interact/hh_group_window.html',
				bounces : true,
				delay : 300,
				animation : {
					duration : 400
				},
				scrollToTop : true,
				pageParam : {
					'targetId' : targetid,
					'header_h' : header_h,
					'group_list' : group_list,
					'group_name' : person_name
				}
			});
		});
	}

}

function initHeaer() {
	//如果是群组会话，则显示右上角群组成员
	if (conver_type == 'GROUP') {
		$api.css($api.byId('menu'), 'display:inline;');
	}
	$api.html($api.byId('mTitle'), person_name);
}
function formatDate(date, format) {
	if (arguments.length < 2 && !date.getTime) {
		format = date;
		date = new Date();
	}
	typeof format != 'string' && (format = 'YYYY年MM月DD日 hh时mm分ss秒');
	var week = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', '日', '一', '二', '三', '四', '五', '六'];
	return format.replace(/YYYY|YY|MM|DD|hh|mm|ss|星期|周|www|week/g, function(a) {
		switch (a) {
		case "YYYY": return date.getFullYear();
		case "YY": return (date.getFullYear()+"").slice(2);
		case "MM": return date.getMonth() + 1<10?"0"+(date.getMonth()+1):date.getMonth()+1;
		case "DD": return date.getDate()<10?"0"+date.getDate():date.getDate();
		case "hh": return date.getHours()<10?"0"+date.getHours():date.getHours();
		case "mm": return date.getMinutes()<10?"0"+date.getMinutes():date.getMinutes();
		case "ss": return date.getSeconds()<10?"0"+date.getSeconds():date.getSeconds();
		case "星期": return "星期" + week[date.getDay() + 7];
		case "周": return "周" +  week[date.getDay() + 7];
		case "week": return week[date.getDay()];
		case "www": return week[date.getDay()].slice(0,3);
		}
	});
}
