//缓存ID
//var id = api.pageParam.typeId;
////缓存目录，存储地址为 Caches/folder/id.json
//var folder = "cartype";
////请求地址
//var url = "/getCategoryByParentId?parentId=" + id + "&key=" + key;
////读取执行
//doCache(folder, id, url, function(data) {
//	//处理拼接html
//	//图片样式加上cache
//	
//});

//ajax请求
function ajaxRequest(url, method, params,async,callBack) {
	var now = Date.now();
//	api.ajax({
//		url : url,
//		method : method,
//		cache : false,
//		timeout : 30,
//		dataType : 'json',
//		data : {
//			values : datas
//		}
//	}, function(ret, err) {
//		if (ret) {
//			callBack(ret, err);
//		} else {
//			api.alert({
//				msg : ('错误码：' + err.code + '；错误信息：' + err.msg + '网络状态码：' + err.statusCode)
//			});
//		}
//	});
	try{
//		$.ajax({
//			type:method,
//			url:url,
//			async : async,
//			success:function(data){
//				var data = JSON.parse(data); 
//	           // if(data.code==200){
//	            	callBack(data);
//	           // }
//			},
//			error:function(err){
//				toast("网络异常，请稍后重试");
//			}
//		});
		api.ajax({
            url: url,
            method: method
        },function(ret, err){
            if (ret) {
                 //var data = JSON.parse(ret); 
                 callBack(ret);
            } else {
                api.hideProgress();
                toast("网络异常，请稍后重试");
            }
        });
		
	}catch(err){
	    api.hideProgress();
		toast("系统异常，请稍后重试");
	}
}

//读文件
function readFile(path, callBack) {
	//alert(3);
	var cacheDir = api.cacheDir;
	api.readFile({
		path : cacheDir + path
	}, function(ret, err) {
		callBack(ret, err);
	});
}

//写文件
function writeFile(json, id, path) {
	//缓存目录
	var cacheDir = api.cacheDir;
	api.writeFile({
		//保存路径
		path : cacheDir + '/' + path + '/' + id + '.json',
		//保存数据，记得转换格式
		data : JSON.stringify(json)
	}, function(ret, err) {

	});
}

//缓存图片
function iCache(selector) {
	selector.each(function(data) {! function(data) {
			var url = selector.eq(data).attr("src");
			var img = this;
			var pos = url.lastIndexOf("/");
			var filename = url.substring(pos + 1);
			var path = api.cacheDir + "/pic/" + filename;
			var obj = api.require('fs');
			obj.exist({
				path : path
			}, function(ret, err) {
				//msg(ret);
				if (ret.exist) {
					if (ret.directory) {
						//api.alert({msg:'该路径指向一个文件夹'});
					} else {
						//api.alert({msg:'该路径指向一个文件'});
						//selector.eq(data).src=path;
						selector.eq(data).attr('src', null);
						path = api.cacheDir + "/pic/" + filename;
						selector.eq(data).attr('src', path);
						//console.log(selector.eq(data).attr("src"));
					}
				} else {
					api.download({
						url : url,
						savePath : path,
						report : false,
						cache : true,
						allowResume : true
					}, function(ret, err) {
						//msg(ret);
						if (ret) {
							var value = ('文件大小：' + ret.fileSize + '；下载进度：' + ret.percent + '；下载状态' + ret.state + '存储路径: ' + ret.savePath);
						} else {
							var value = err.msg;
						};
					});
				}
			});
		}(data);
	});
};

//缓存方法
function doCache(folder, id, url,method,params,async,callback,notShowProgress) {
//	if(id=="news_nav"){
//		  	alert(2);
//		  }
    if(notShowProgress){
    
    }
    else{
    	api.showProgress();
    }
	readFile('/' + folder + '/' + id + '.json', function(ret, err) {
		//alert(4);
		if (ret.status) {
		  // alert(5);
			//如果成功，说明有本地存储，读取时转换下数据格式
			//拼装json代码
			//alert('取到缓存')
			var cacheData = ret.data;
			var cid=api.pageParam.cid;
			
			//再远程取一下数据，防止有更新
			if(api.connectionType=="none"){
			    callback(JSON.parse(cacheData));
			    iCache($('.cache'));
			    
			    api.hideProgress();
			    toast('请检查网络连接');
				return;
			}

			ajaxRequest(url, method, params,async,function(data) {
				 
				if (data) {
				    callback(data);
		            iCache($('.cache'));
		            
					if (cacheData != JSON.stringify(data)) {
						//有更新处理返回数据
						//alert('更新缓存')
						//callback(data);
						//缓存数据
						writeFile(data, id, folder);
						//iCache($('.cache'));
					}
				} else {
					toast('数据获取失败！');
				}
				api.hideProgress();
			});
		} else {
//		  if(id=="news_nav"){
//		  	alert(6);
//		  }
//		   
			//如果失败则从服务器读取，利用上面的那个ajaxRequest方法从服务器GET数据
			if(api.connectionType=="none"){
			    api.hideProgress();
			    toast('请检查网络连接');
				return;
			}
			ajaxRequest(url, method, params, async,function(data) {
				//alert(7);
				if (data) {
					//处理返回数据
					//alert('没取到缓存')
					callback(data);
					//缓存数据
					writeFile(data, id, folder);
					iCache($('.cache'));
				} else {
					toast('数据获取失败！');
				}
				api.hideProgress();
			});
		}
	});
}
function toast(msg){
	api.toast({
        msg: msg,
        duration:1000,
        location: 'middle'
    });
}
