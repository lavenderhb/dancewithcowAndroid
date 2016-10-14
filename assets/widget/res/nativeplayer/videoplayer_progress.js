//var videoPlayer;
////当前视频总时间
//var videotime = 0;
////判断是暂停还是播放状态0为正在播放
//var isplaying = 1;
////当前视频播放进度 时间
//var progressvalue = 0;
//var isFullScreen=0;
//var fontSize=20;
//var frmHgt=0;
//var frmWdt=0;
//var winHgt=0;
//var winWdt=0;
//var $overlay=null;
//var $contro=null;
//var isFirst=true;
//var $overlayImg=null;
//var timer=-1;
//apiready=function(){
//  $overlay=$("#progress-overlay");
//  $contro=$("#contro");
//  $overlayImg=$("#overlay-img");
//  winHgt=api.winHeight;
//  winWdt=api.winWidth;
//  var videotime=api.pageParam.videotime;
//  $("#inline-range").attr("max", videotime);
//  document.getElementById("alltime").innerHTML = changestom(videotime);
//  initProgress();
//};
//function setNowTime(progressval){
//  var el = document.getElementById("inline-range");
//  document.getElementById("nowtime").innerHTML = changestom(progressval);
//  progressvalue=progressval;
//  $api.val(el, progressval);
//}
//function setStopTime(){
//   var el = document.getElementById("inline-range");
//  document.getElementById("nowtime").innerHTML = changestom(0);
//  progressvalue = 0;
//  $api.val(el, 0);
//}
//function initProgress(){
//	var rangeList = document.querySelectorAll('input[type="range"]');
//	for (var i = 0, len = rangeList.length; i < len; i++) {
//		rangeList[i].addEventListener('input', function() {
//			if (this.id.indexOf('field') >= 0) {
//				console.log(this.value + "--");
//			} else {
//			    clearTimeout(timer);
//				//当拖动进度等于总时间的时候 停止播放
//				if (this.value == videotime) {
//				    api.execScript({
//				        frameName:"native_video",
//	                    script: 'toStopVideo();'
//                  });
//				} else {
//				    var val=this.value;
//				    api.execScript({
//				        frameName:"native_video",
//	                    script: 'toSeek('+val+');'
//                  });
//					if(isplaying==0){
//						api.execScript({
//					        frameName:"native_video",
//		                    script: 'toStartVideo();'
//	                    });
//					}
//					else{
//						api.execScript({
//					        frameName:"native_video",
//		                    script: 'toPauseVideo();'
//	                    });
//					}
//					document.getElementById("nowtime").innerHTML = changestom(this.value);
//					var el = document.getElementById("inline-range");
//					//拖动的时候 进度时间也要改
//					progressvalue = this.value;
//					$api.val(el, this.value);
//					console.log(this.value + "++");
//				}
//				timer=setTimeout(function(){
//		        	$contro.hide();
//		        },3000);
//			}
//		});
//	}
//	$("#play_button").on("click",function(e){
//	    e.preventDefault();
//		palying();
//	});
//	$("#play_qp").on("click",function(){
//	    if(isFullScreen==1){
//	        isFullScreen=0;
//	        exitFullScreen();
//	    }
//	    else{
//	        isFullScreen=1;
//	        enterFullScreen();
//	    }
//	});
//	$overlay.on("click",function(e){
//		e.preventDefault();
//		palying();
//	
//	});
//}
//function getStyle(obj, attr) {
//  return obj.currentStyle ? obj.currentStyle[attr] : getComputedStyle(obj)[attr];
//}
//function getFontSize() {
//  fontSize = getStyle(document.documentElement, "fontSize");
//  fontSize = fontSize.substring(0, fontSize.length - 2);
//} 
//function enterFullScreen() {
//  api.setScreenOrientation({
//      orientation: 'auto_landscape'
//  });
//  api.setFrameAttr({
//	    name: 'native_video',
//	    rect: {
//	        x: 0,
//	        y: 0,
//	        w: winHgt,
//	        h: winWdt
//	    }
//	});
//	$overlay.css("height",winWdt);
//	api.setFrameAttr({
//	    name: 'native_video_progress',
//	    rect: {
//	        x: 0,
//	        y: 0,
//	        w: winHgt,
//	        h: winWdt
//	    }
//	});
//	api.execScript({
//      frameName:"native_video",
//      script: 'toFullScreen();'
//  });
//}
//function exitFullScreen(){
//	api.setScreenOrientation({
//      orientation: 'portrait_up'
// 	});
// 	var videoY=api.pageParam.videoY;
// 	var progressY=api.pageParam.progressY;
// 	api.setFrameAttr({
//	    name: 'native_video',
//	    rect: {
//	        x: 0,
//	        y: videoY,
//	        w: winWdt,
//	        h: 200
//	    }
//	});
//	$overlay.css("height",200);
//	api.setFrameAttr({
//	    name: 'native_video_progress',
//	    rect: {
//	        x: 0,
//	        y: videoY,
//	        w: winWdt,
//	        h: 200
//	    }
//	});
//	api.execScript({
//      frameName:"native_video",
//      script: 'exitScreen();'
//  });
//}
//function changestom(time) {
//	if (time < 60) {
//		if (time < 10) {
//			return ("00:0" + time);
//		} else {
//			return ("00:" + time);
//		}
//	} else {
//		var minute = parseInt(time / 60);
//		if (minute < 10) {
//			var second = time - (minute * 60);
//			if (second < 10) {
//				second = "0" + second;
//			}
//			return ("0" + minute + ":" + second);
//		} else {
//			var second = time - (minute * 60);
//			if (second < 10) {
//				second = "0" + second;
//			}
//			return (minute + ":" + second);
//		}
//	}
//}
///**
// *添加监听时间 用来进行简单的暂停 播放动作
// */
//function addplaytimeevent() {
//	videoPlayer = api.require('videoPlayer');
//	videoPlayer.addEventListener({
//		name : 'play'
//	}, function(ret, err) {
//		if (ret) {
//			var el = document.getElementById("inline-range");
//			if (ret.eventType == 'playing') {
//				var timenow = ret.current;
//				progressvalue = parseInt(timenow);
//				document.getElementById("nowtime").innerHTML = changestom(progressvalue);
//				$api.val(el, progressvalue);
//			}
//			if (ret.eventType == 'stop') {
//				document.getElementById("nowtime").innerHTML = changestom(0);
//				progressvalue = 0;
//				$api.val(el, 0);
//			}
//		} else {
//			alert(JSON.stringify(err));
//		}
//	});
//}
//
//function palying() {
//	if (isplaying == 1) {
//		isplaying = 0;
//		$overlay.css("opacity",0);
//		if(isFirst){
//			isFirst=true;
//			$contro.show();
//		}
//		document.getElementById("idplaying").src = "../../images/nativeplayer/zanting.png";
//		$overlayImg[0].src = "../../images/nativeplayer/zanting.png";
//		api.execScript({
//	        frameName:"native_video",
//          script: 'toStartVideo();'
//      });
//      clearTimeout(timer);
//      timer=setTimeout(function(){
//      	$contro.hide();
//      },3000);
//	} else {
//		isplaying = 1;
//		$overlay.css("opacity",1);
//		document.getElementById("idplaying").src = "../../images/nativeplayer/bofang.png";
//		$overlayImg[0].src = "../../images/nativeplayer/bofang.png";
//		api.execScript({
//	        frameName:"native_video",
//          script: 'toPauseVideo();'
//      });
//      $contro.show();
//	}
//}