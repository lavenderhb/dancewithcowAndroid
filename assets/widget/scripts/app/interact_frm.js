//最后一条消息的 Id，获取此消息之前的 count 条消息，没有消息第一次调用应设置为: -1
var old_msg_id;
//定义发送人
var targetid;

var conver_type;
//头像
var avatar_url;
var fs_img;
var header_h;
//第一次进入时置底部
var is_fir = true;
var curPage=1;
var pageNum=1;
var uid=-1;
apiready = function() {
//	api.showProgress({
//		title : '加载中...',
//		modal : false
//	});
	//当前target_id的历史聊天记录
	//        var historyMessages = api.pageParam.historyMessages;
	targetid = api.pageParam.targetid;
	old_msg_id = api.pageParam.old_msg_id;
	conver_type = api.pageParam.conver_type;
	avatar_url = api.pageParam.avatar_url;
	header_h = api.pageParam.header_h;
	uid=$api.getStorage("uid");
	fs_img = api.require('fs');
	//页面加载时获取历史信息
	
	initGetHistory();
	goBottom();
	
	//监听收到新消息写入
	api.addEventListener({
		name : 'getNewMessage'
	}, function(ret) {
//		                api.alert({
//		                        msg : JSON.stringify(ret)
//		      },function(ret,err){
//		              //coding...
//		      });
		if (ret && ret.value) {
			//监听成功
			var newMessageData = ret.value;
			//                                                api.alert({
			//                                                        msg : JSON.stringify(newMessageData)
			//                                                });
			//                                                api.alert({msg:newMessageData.data.objectName });
			//根据targetId和当前会话用户id判断一下，如果相等则写入
			
			if (newMessageData.data.targetid == uid) {
				
				//会话头像
				var receive_img;
				var mes_type = 'PRIVATE';
				if(newMessageData.data.type=="msg"){
					newMessageData.data.objectName='RC:TxtMsg';
				}
				switch(mes_type) {
					case 'PRIVATE':
						receive_img = newMessageData.data.image;
						switch(newMessageData.data.objectName) {
							case 'RC:TxtMsg':
								$api.append($api.byId("message-content"), '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="fl"><div class="time">'+newMessageData.data.time+'</div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><div>' + newMessageData.data.content + '</div></div></div></div>');
								break;
							case 'RC:ImgMsg':
								$api.append($api.byId("message- content"), '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-img"><div class="aui-chat-left-triangle"></div><span><img   class="pic_thumb" onclick="openImage(\'' + newMessageData.data.content.imageUrl + '\')" src="' + newMessageData.data.content.thumbPath + '" onerror="this.src=\'../../image/chatBox/hh_defalut.png\'"></span></div></div>');
								break;
							case 'RC:VcMsg':
								//                                                                                        alert('11111111111111111' + JSON.stringify(newMessageData));
								var con = "";
								con += '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span><img id="voice_' + newMessageData.data.messageId + '" alt="97" src="../../image/chatBox/mrecelog.png" width="40px" height="30px" onclick="playVoice(\'' + newMessageData.data.messageId + '\',\'' + newMessageData.data.content.voicePath + '\',1);"/>' + newMessageData.data.content.duration + '\'\'</span></div></div>';
								//                                        alert(con);
								$api.append($api.byId("message-content"), con);
								break;
						}
						break;
					case 'SYSTEM':
						receive_img = avatar_url;
						$api.append($api.byId("message-content"), '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span>' + newMessageData.data.content.text + '</span></div></div>');
						break;
					case 'GROUP':
						//获取头像和说话人姓名
						var group_data = $api.getStorage('group_list_data');
						var sender_name = '';
						if ( typeof (group_data) != 'undefined') {
							group_data_list = group_data.list;
							for (var i = 0; i < getJsonObjLength(group_data_list); i++) {
								if (newMessageData.data.senderUserId == group_data_list[i].login_name) {
									sender_name = group_data_list[i].person_name;
									receive_img = group_data_list[i].head_img;
									break;
								}
							}
						}
						switch(newMessageData.data.objectName) {
							case 'RC:TxtMsg':
								$api.append($api.byId("message-content"), '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-title"><em>' + sender_name + '</em></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span>' + newMessageData.data.content.text + '</span></div></div>');
								break;
							case 'RC:ImgMsg':
								$api.append($api.byId("message-content"), '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-title"><em>' + sender_name + '</em></div><div class="aui-chat-receiver-img"><div class="aui-chat-left-triangle"></div><span><img   class="pic_thumb" onclick="openImage(\'' + newMessageData.data.content.imageUrl + '\')" src="' + newMessageData.data.content.thumbPath + '" onerror="this.src=\'../../image/chatBox/hh_defalut.png\'"></span></div></div>');
								break;
							case 'RC:VcMsg':
								//                                                                                        alert('11111111111111111' + JSON.stringify(newMessageData));
								var con = "";
								con += '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-title"><em>' + sender_name + '</em></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span><img id="voice_' + newMessageData.data.messageId + '" alt="97" src="../../image/chatBox/mrecelog.png" width="40px" height="30px" onclick="playVoice(\'' + newMessageData.data.messageId + '\',\'' + newMessageData.data.content.voicePath + '\',1);"/>' + newMessageData.data.content.duration + '\'\'</span></div></div>';
								//                                        alert(con);
								$api.append($api.byId("message-content"), con);
								break;
						}
						break;
				}
				goBottom();
			}
		}

	});

	//监听发送新消息写入,这个事件主要来处理发送消息插入到会话窗口中
	api.addEventListener({
		name : 'insertSendMessage'
	}, function(ret) {
		if (ret && ret.value) {
			var newMessageData = ret.value;
			//我的头像
			var sender_img = $api.getStorage('avatar_url');
			var status_msg_id = newMessageData.data.message.messageId;                                                                        
			//alert("aaaa"+JSON.stringify(newMessageData));
			//RC:TxtMsg：文本消息，RC:VcMsg：语音消息，RC:ImgMsg：图片消息，RC:LBSMsg：位置消息
			
			switch (newMessageData.data.message.objectName) {
				case 'RC:TxtMsg':
					//页面写入发送消息
					var html='<div class="aui-chat-sender"><div class="aui-chat-sender-avatar">'
					+'<img onerror="this.src=\'../../images/person/default.jpg\'" src="../../images/person/default.jpg"></div>'
					+'<div class="fr">'
					/*+'<div class="time">'+newMessageData.data.message.sendtime+'</div>'*/
					+'<div class="aui-chat-sender-cont"><div class="aui-chat-right-triangle"></div><div>' + newMessageData.data.message.content.text + '</div><div></div></div>';
					$api.append($api.byId("message-content"), html);
					break;
				case 'RC:ImgMsg':
					//                                        if (api.systemType == 'ios') {
					$api.append($api.byId("message-content"), '<div class="aui-chat-sender"><div class="aui-chat-sender-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + sender_img + '"></div><div class="aui-chat-sender-img"><div class="aui-chat-right-triangle"></div><span><img   class="pic_thumb" onclick="openImage(\'' + newMessageData.data.message.content.imageUrl + '\')" src="' + newMessageData.data.message.content.thumbPath + '"></span></div><img id="status_' + status_msg_id + '" class="send_loading" src="../../image/loading_more.gif"/></div>');
					//                                        }
					//                                        else if (api.systemType == 'android') {
					//                                                $api.append($api.byId("message-content"), '<div class="aui-chat-sender"><div class="aui-chat-sender-avatar"><img src="' + sender_img + '"></div><div class="aui-chat-sender-img"><div class="aui-chat-right-triangle"></div><span><img class="pic_thumb" onclick="openImage(\'' + newMessageData.data.message.content.localPath + '\')" src="' + newMessageData.data.message.content.thumbPath + '"></span></div></div>');
					//                                        }

					break;
				case 'RC:VcMsg':
					var con = "";
					con += '<div class="aui-chat-sender"><div class="aui-chat-sender-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + sender_img + '"></div><div class="aui-chat-sender-cont"><div class="aui-chat-right-triangle"></div><span>' + newMessageData.data.message.content.duration + '\'\'<img id="voice_' + newMessageData.data.message.messageId + '" alt="98" src="../../image/chatBox/msendlog.png" width="40px" height="30px" onclick="playVoice(\'' + newMessageData.data.message.messageId + '\',\'' + newMessageData.data.message.content.voicePath + '\',0);"/></span></div><img id="status_' + status_msg_id + '" class="send_loading" src="../../image/loading_more.gif"/></div>';
					$api.append($api.byId("message-content"), con);
					break;
			}

		}
		setTimeout('goBottom()',200);
	});
	//绑定下拉刷新历史会话事件
	api.setRefreshHeaderInfo({
		visible : true,
		bgColor : '#F5F5F5',
		textColor : '#8E8E8E',
		textDown : '下拉加载更多...',
		textUp : '松开加载...',
		showTime : true
	}, function(ret, err) {
		//从服务器加载数据，完成后调用api.refreshHeaderLoadDone()方法恢复组件到默认状态
		//调用获取历史会话监听
		if(curPage>=pageNum){
	        toast("已到最顶部");
	    }
	   else{
	   		curPage++;
			appendRender();
	   }
	   api.refreshHeaderLoadDone();
	});
	
	
	//从后台返回到前台
	reAppFromBack();
	
};
/**
 *滚动页面底部
 * 周枫
 * 2015.08.11
 */
function goBottom() {
	document.getElementsByTagName('body')[0].scrollTop = document.getElementsByTagName('body')[0].scrollHeight;
}

/**
 * 发送文本消息
 * 周枫
 * 2015.08.08
 * @param {Object} sendMsg
 */
function sendText(content, conver_type,sendtime) {
	//向会话列表页发送消息事件
//	api.sendEvent({
//		name : 'sendMessage',
//		extra : {
//			type : 'text',
//			targetId : '' + target_id + '',
//			content : content,
//			conversationType : conver_type ,
//			extra : ''
//		}
//	});
    
	api.sendEvent({
		name : 'insertSendMessage',
		extra : {
		    data:{
			conversationType : conver_type,
				message:{
						 content:{text:content},
				         messageId:"-1",
				         objectName:"RC:TxtMsg",
				         sendtime:sendtime
			            }
			}
		}
	});
}

/**
 * 展示图片
 * 周枫
 * 2015.08.12
 * @param {Object} img_url
 */
function openImage(img_url) {
//	//        var file_name = '../../file/hh/' + target_id + '_img_url.text';
//	var file_name = 'fs://hh//' + target_id + '_img_url.text';
//
//	var img_obj = api.require('imageBrowser');
//	//                fs_img.remove({
//	//                    path: file_name
//	//                },function(ret,err){
//	//                    var status = ret.status;
//	//                    if (status) {
//	//                        api.alert({msg:'删除文件成功'});
//	//                    }else {
//	//                        api.alert({msg:err.msg});
//	//                    }
//	//                });
//	var img_urls = new Array();
//	var img_data = '';
//	//判断是否存在文件
//	fs_img.exist({
//		path : file_name
//	}, function(ret, err) {
//		if (ret.exist) {
//			if (ret.directory) {
//				api.alert({
//					msg : '该路径指向一个文件夹'
//				});
//			} else {
//				//打开文件
//				fs_img.open({
//					path : file_name,
//					//文件打开方式,读写
//					flags : 'read_write'
//				}, function(ret, err) {
//					var fd = ret.fd;
//					if (ret.status) {
//						//读取文件内容
//						fs_img.read({
//							fd : fd,
//							offset : 0,
//							//读取内容长度
//							length : 0
//						}, function(ret, err) {
//							if (ret.status) {
//								//写入图片路径文件的数据
//								img_data = ret.data;
//								//打开相册的数组
//								img_urls = ret.data.split(',');
//								//当前图片是否已经存在
//								var flag_img = true;
//								//相册有几张图片
//								var img_urls_l = img_urls.length;
//								var img_count = img_urls_l;
//								//如果图片已经存在，则不执行下面写入操作
//								for (var i = 0; i < img_urls_l; i++) {
//									if (img_url == (img_urls[i] + '')) {
//										flag_img = false;
//										img_count = i;
//									}
//								}
//								//如果图片不存在，则写入文件
//								if (flag_img) {
//									img_data = img_data + ',' + img_url
//									img_urls[img_urls_l] = img_url;
//
//									//写入文件内容
//									fs_img.write({
//										//open方法得到的文件句柄，不能为空
//										fd : fd,
//										//写入数据
//										data : img_data,
//										//当前文件偏移量,默认值：0
//										offset : 0
//										//codingType 所要阅读的文本的编码格式，取值范围:gbk、utf8 默认值：utf8
//									}, function(ret, err) {
//										if (ret.status) {
//											//打开相册
//											img_obj.openImages({
//												imageUrls : img_urls,
//												//是否以九宫格方式显示图片
//												showList : false,
//												//                                                                                                showList : true,
//												activeIndex : img_count
//
//											});
//										} else {
//											api.alert({
//												msg : '对不起，2再次写入文件失败' + err.msg
//											});
//										}
//									});
//								} else {
//									//打开相册
//									img_obj.openImages({
//										imageUrls : img_urls,
//										//是否以九宫格方式显示图片
//										showList : false,
//										//                                                                                showList : true,
//										activeIndex : img_count
//
//									});
//								}
//
//							} else {
//								api.alert({
//									msg : '对不起，再次读取文件失败' + err.msg
//								});
//							}
//						});
//
//					} else {
//						api.alert({
//							msg : '对不起，再次创建文件失败' + err.msg
//						});
//					}
//				});
//			}
//		} else {
//			//如果不存在文件，则创建图片路径文件
//			fs_img.createFile({
//				path : file_name
//			}, function(ret, err) {
//				var status = ret.status;
//				if (status) {
//					//打开文件
//					fs_img.open({
//						path : file_name,
//						//文件打开方式,读写
//						flags : 'read_write'
//					}, function(ret, err) {
//
//						if (ret.status) {
//							fs_img.write({
//								//open方法得到的文件句柄，不能为空
//								fd : ret.fd,
//								//写入数据
//								data : img_url + '',
//								//当前文件偏移量,默认值：0
//								offset : 0
//								//codingType 所要阅读的文本的编码格式，取值范围:gbk、utf8 默认值：utf8
//							}, function(ret, err) {
//								if (ret.status) {
//									img_urls[0] = img_url;
//									img_obj.openImages({
//										imageUrls : img_urls,
//										//是否以九宫格方式显示图片
//										showList : false,
//										activeIndex : 0
//
//									});
//								} else {
//									api.alert({
//										msg : '对不起，写入文件失败' + err.msg
//									});
//								}
//							});
//						} else {
//							api.alert({
//								msg : '对不起，打开文件失败' + err.msg
//							});
//						}
//					});
//				} else {
//					api.alert({
//						msg : '对不起，创建文件失败' + err.msg
//					});
//				}
//			});
//		}
//	});
//	//        var img_urls = new Array();
//	//        img_urls[0] = img_url;
//	//        api.alert({
//	//                msg : img_url
//	//  },function(ret,err){
//	//          //coding...
//	//  });
//	//        var obj = api.require('imageBrowser');
//	//        obj.openImages({
//	//                imageUrls : img_urls,
//	//                //是否以九宫格方式显示图片
//	//                showList : false,
//	//                activeIndex : 0
//	//        });
}



/**
 * 获取历史聊天记录
 * 周枫
 * 2015.08.20
 * @param {Object} target_id 会话人
 * @param {Object} old_msg_id 最新会话id
 * @param {Object} conver_type 会话类型
 * @param {Object} msg_count 获取条数
 */
//function initGetHistory(target_id, old_msg_id, conver_type, msg_count) {
//	api.sendEvent({
//		name : 'getHistory',
//		extra : {
//			type : conver_type,
//			old_msg_id : old_msg_id,
//			target_id : target_id,
//			msg_count : msg_count
//		}
//	});
//}       
function initGetHistory(){
    getAjaxData(1,rendHistory);
//  if(data.code==200){
//  	var finalData=data.data;
//	    if(finalData.length>0){
//			 setHistory(finalData);
//	         curPage=1;
//	         pageNum=data.pageinfo.pagenum;
//	    }
//  }
}
function rendHistory(data){
    var finalData=data.data;
	if(finalData.length>0){
		 setHistory(finalData);
         curPage=1;
         pageNum=data.pageinfo.pagenum;
    }
}
function rendAppendHistory(data){
    var finalData=data.data;
    if(finalData.length>0){
       setHistory(finalData);
    }
}

function appendRender(){
    getAjaxData(curPage,rendAppendHistory);
}
function getAjaxData(page,callback){
    var uid=$api.getStorage("uid");
    curPage=page;
    try{
//		$.ajax({
//	         type: "GET",
//	         url: td.domain+"/chat/"+uid+"/"+targetid+"/"+page+"?token="+td.token,
//	         async: false, 
//	         success: function(data){ 
//	            //alert(data);
//	            data=data.replace("\r\n", "\\r\\n");
//	            var data = JSON.parse(data); 
//	            if(data.code==200){
//	                //alert(11);
//	                callback(data);
//	            }
//	            else{
//	              
//	               //alert("data.code"+data.code+" "+22);
//	            	//toast(data.code);
//	            }
//	         },
//	          error:function(err){
//	            // alert(err);
//	          	 toast("网络异常，请稍后重试");
//	          }
//	     });
	    api.ajax({
            url: td.domain+"/chat/"+uid+"/"+targetid+"/"+page+"?token="+td.token,
            method: "GET",
        },function(ret, err){
            if (ret) {
                if(ret.code==200){
                    //alert(11);
                    callback(ret);
                }
                else{
                  
                   //alert("data.code"+data.code+" "+22);
                    //toast(data.code);
                }
            } else {
                toast("网络异常，请稍后重试");
            }
        });
     }catch(err){
         //alert(err);
    	 toast("系统异常，请稍后重试");
    }
      
}
function toast(msg){
	api.toast({
        msg: msg,
        duration:1000,
        location: 'bottom'
    });
}

//获取历史会话监听，渲染页面
 function setHistory(finalData) {
			var historyMessages = finalData;
			//                        api.alert({
			//                                msg:JSON.stringify(historyMessages)
			//          },function(ret,err){
			//                  //coding...
			//          });
			//if (historyMessages != '') {
				var con = '';
				//倒叙循环会话记录
				for (var i =0; i<historyMessages.length; i++) {
					var targetid = historyMessages[i].targetid;
					var content = '';
					//文字还是图片还是声音 RC:TxtMsg：文本消息，RC:VcMsg：语音消息，RC:ImgMsg：图片消息，RC:LBSMsg：位置消息
					var type ="RC:TxtMsg";// historyMessages[i].objectName;
					var dir = "RECEIVE";
					if(historyMessages[i].myid==uid){
						dir="SEND"
					}
					var start = historyMessages[i].sendtime;
					//                                        var end = new Date();
					//var g_time = new getTimeTemplate();

					//我的头像
					var sender_img =historyMessages[i].image;
//					//计算会话时间
//					if (i == historyMessages.length - 1) {
//						con += '<div class="aui-chat-sender history-date"><p>' + g_time.getTime(start, 1) + '</p></div>';
//					} else {
//						var M1 = historyMessages[i].sentTime;
//						var M2 = historyMessages[i + 1].sentTime;
//						if ((M1 - M2) >= 180 * 1000) {
//							con += '<div class="aui-chat-sender history-date"><p>' + g_time.getTime(start, 1) + '</p></div>';
//						}
//					}
					//加载会话内容
					if (dir == 'SEND') {
						switch(type) {
							case 'RC:TxtMsg':
								con += '<div class="aui-chat-sender"><div class="aui-chat-sender-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="../../images/person/default.jpg"></div><div class="fr"><div class="time">'+start+'</div><div class="aui-chat-sender-cont"><div class="aui-chat-right-triangle"></div><div>' + historyMessages[i].content + '</div></div></div></div>';
								break;
							case 'RC:VcMsg':
								con += '<div class="aui-chat-sender"><div class="aui-chat-sender-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + sender_img + '"></div><div class="aui-chat-sender-cont"><div class="aui-chat-right-triangle"></div><span>' + historyMessages[i].content.duration + '\'\'<img id="voice_' + historyMessages[i].messageId + '" alt="98" src="../../image/chatBox/msendlog.png" width="40px" height="30px" onclick="playVoice(\'' + historyMessages[i].messageId + '\',\'' + historyMessages[i].content.voicePath + '\',0);"/></span></div></div>';
								break;
							case 'RC:ImgMsg':
								con += '<div class="aui-chat-sender"><div class="aui-chat-sender-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + sender_img + '"></div><div class="aui-chat-sender-img"><div class="aui-chat-right-triangle"></div><span><img   class="pic_thumb" onclick="openImage(\'' + historyMessages[i].content.imageUrl + '\')" src="' + historyMessages[i].content.thumbPath + '" onerror="this.src=\'../../image/chatBox/hh_defalut.png\'"></span></div></div>'
								break;
						}

						//渲染发送会话
						//                                        $api.prepend($api.byId("message-content"), '<div class="aui-chat-sender"><div class="aui-chat-sender-avatar"><img src="../../image/person/demo2.png"></div><div class="aui-chat-sender-cont"><div class="aui-chat-right-triangle"></div><span>' + historyMessages[i].content.text + '</span></div></div>');
					} else {
						//获取会话列表页数据
						//                                                var hh_index_list = $api.getStorage('hh_index_list');
						//会话头像
						var receive_img;
						var mes_type = 'PRIVATE';//historyMessages[i].conversationType;
						//                                                api.alert({
						//                                                        msg : JSON.stringify(historyMessages[i])
						//                      },function(ret,err){
						//                              //coding...
						//                      });
						switch(mes_type) {
							case 'PRIVATE':
								receive_img =historyMessages[i].image;
								switch(type) {
									case 'RC:TxtMsg':
										con += '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/logo.png\'" src="' + receive_img + '"></div><div class="fl"><div class="time">'+start+'</div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><div>' + historyMessages[i].content + '</div></div></div></div>';
										break;
									case 'RC:VcMsg':
										con += '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span><img id="voice_' + historyMessages[i].messageId + '" alt="97" src="../../image/chatBox/mrecelog.png" width="40px" height="30px" onclick="playVoice(\'' + historyMessages[i].messageId + '\',\'' + historyMessages[i].content.voicePath + '\',1);"/>' + historyMessages[i].content.duration + '\'\'</span></div></div>';
										break;
									case 'RC:ImgMsg':
										con += '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-img"><div class="aui-chat-left-triangle"></div><span><img class="pic_thumb"   onclick="openImage(\'' + historyMessages[i].content.imageUrl + '\')" src="' + historyMessages[i].content.thumbPath + '" onerror="this.src=\'../../image/chatBox/hh_defalut.png\'"></span></div></div>'
										break;

									//渲染接收会话
									//                                        $api.prepend($api.byId("message-content"), '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img src="../../image/person/demo2.png"></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span>' + historyMessages[i].content.text + '</span></div></div>');
								}
								break;
							case 'SYSTEM':
								receive_img = avatar_url;
								con += '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span>' + historyMessages[i].content.text + '</span></div></div>';
								break;
							case 'GROUP':
								//获取头像和说话人姓名
								var group_data = $api.getStorage('group_list_data');
								var sender_name = '';
								if ( typeof (group_data) != 'undefined') {
									group_data_list = group_data.list;
									for (var j = 0; j < getJsonObjLength(group_data_list); j++) {
										if (historyMessages[i].senderUserId == group_data_list[j].login_name) {
											sender_name = group_data_list[j].person_name;
											receive_img = group_data_list[j].head_img;
											break;
										}
									}
								}
								switch(type) {
									case 'RC:TxtMsg':
										con += '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-title"><em>' + sender_name + '</em></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span>' + historyMessages[i].content.text + '</span></div></div>';
										break;
									case 'RC:VcMsg':
										con += '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-title"><em>' + sender_name + '</em></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span><img id="voice_' + historyMessages[i].messageId + '" alt="97" src="../../image/chatBox/mrecelog.png" width="40px" height="30px" onclick="playVoice(\'' + historyMessages[i].messageId + '\',\'' + historyMessages[i].content.voicePath + '\',1);"/>' + historyMessages[i].content.duration + '\'\'</span></div></div>';
										break;
									case 'RC:ImgMsg':
										con += '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img onerror="this.src=\'../../images/person/default.jpg\'" src="' + receive_img + '"></div><div class="aui-chat-receiver-title"><em>' + sender_name + '</em></div><div class="aui-chat-receiver-img"><div class="aui-chat-left-triangle"></div><span><img class="pic_thumb"   onclick="openImage(\'' + historyMessages[i].content.imageUrl + '\')" src="' + historyMessages[i].content.thumbPath + '" onerror="this.src=\'../../image/chatBox/hh_defalut.png\'"></span></div></div>'
										break;

									//渲染接收会话
									// $api.prepend($api.byId("message-content"), '<div class="aui-chat-receiver"><div class="aui-chat-receiver-avatar"><img src="../../image/person/demo2.png"></div><div class="aui-chat-receiver-cont"><div class="aui-chat-left-triangle"></div><span>' + historyMessages[i].content.text + '</span></div></div>');
								}
								break;
						}

					}
					//获取刷新后最后一条ID
					if (i == historyMessages.length - 1) {
						old_msg_id = historyMessages[i].messageId;
					}
				}
				//                                $api.css($api.byId('hh_update_div'), 'display:none;');
				//                                api.alert({
				//                                        msg: con
				//              },function(ret,err){
				//                      //coding...
				//              });
				//加载历史聊天记录
				$api.prepend($api.byId("message-content"), con);
//			} else {
//				old_msg_id = 0;
//			}

		
		
		//通知顶部下拉刷新数据加载完毕，组件会恢复到默认状态
		api.refreshHeaderLoadDone();
		//隐藏进度提示框
		api.hideProgress();

		if (old_msg_id == -1) {
			goBottom();
		}
		//第一次进入时置底部
		if (is_fir) {
			goBottom();
			is_fir = false;
		}
}

/**
 * 从后台返回
 * 周枫
 * 2015.09.02
 */
function reAppFromBack() {
	api.addEventListener({
		name : 'resume'
	}, function(ret, err) {
		goBottom();
	});
}
/**
 * 清除正在发送图标
 * 周枫
 * 2015.12.10
 * @param {Object} msg_id
 */
function removeload(msg_id) {
	var load = $api.byId('status_' + msg_id);
	$api.remove(load);
}
function refreshPage(){
    //alert(0);
//	$("body").trigger("click");
//	$("body").focus();
	$("#refresh").val("555");
}
