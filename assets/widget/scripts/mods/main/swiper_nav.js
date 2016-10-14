define(['scripts/lib/template'], function(tpl) {

	function swiper_nav(opts) {

		this.opts = $.extend({}, swiper_nav.DEFAULTS, opts);
		this.domSel = this.opts.domSel;
		this.$dom =null;
//		this.isLogined=false;//$api.getStorage(uid)?true:false;
//		this.hasService=false;
        this.finalData=undefined;
		this.init();
	}


	swiper_nav.DEFAULTS = {
		domSel:"#swiper_nav"//导航id
	};
	swiper_nav.prototype = {
		init : function() {
			var _this = this;
			_this.$dom = $(_this.domSel);	
			_this.ajaxData();
			_this.bindEvt();
			//未读信息0:私聊;1:群发
			var uid=$api.getStorage("uid");
            if(uid&&uid!=""){
                var csid=$api.getStorage("csid");
                if(!csid||csid==""){
                     _this.getAjaxData(_this.cusCallBack);
                }
                else{
                     _this.cusCallBack();
                }
            }
		},
		getMsg:function(unRead,$msgTip){
            if(unRead==0){
                $msgTip.hide();
            }
            else{
                //$msgTip.html(unRead);
                $msgTip.show();
            }
       },
        getUnReadDataAjax:function(callback,type,pageType){
            var csid=$api.getStorage("csid");
            var uid=$api.getStorage("uid");
            var id="msg_unread_"+type+"_"+csid;
            var folder = "cartype";
            var url=td.domain+"/chat/unread/"+csid+"/"+uid+"/"+type+"?token="+td.token;
            var method="get";
            var params="";
            var async="true";
            doCache(folder,id,url,method,params,async,function(data){
                //alert(JSON.stringify(data));
                var finalData=data.data;
                if(data.code==200){
                   var $msgTip=$("#"+pageType+"Tip");
                   //alert("finalData.count type pageType"+finalData.count+" "+type+" "+pageType);
                   callback(finalData.count,$msgTip);
                }
                else{
                    //toast(data.data);
                }
            });
        },
        clearUnreadStatus:function(pageType){
            var $msgTip=$("#"+pageType+"Tip");
            $msgTip.hide();
        },
		toInteract:function(){
		    var _this=this;
			var uid=$api.getStorage("uid");
            if(!uid||uid==""){
            	api.openWin({
					name : 'login',
					url : '../common/login.html',
					bounces : false,
					animation : 'push',
					scrollToTop : true,
					rect : {
						x : 0,
						y : 0,
						w : 'auto',
						h : 'auto'
					}
				});
            }
            else{
               if(api.connectionType=="none"){
               	   _this.toast("请检查网络连接");
                   return;
               }
                var csid=$api.getStorage("csid");
                if(!csid||csid==""){
                     _this.getAjaxData(_this.interactCallBack,true);
                }
                else{
                    _this.interactCallBack();
                }
            }
		},
		interactCallBack:function(){
		    var csid=$api.getStorage("csid");
            var cname=$api.getStorage("cname");
            if(csid&&csid!=""){
                api.openWin({
                    name : 'interact',
                    url : 'interact.html',
                    bounces : false,
                    delay : 200,
                    pageParam:{csid:csid,cname:cname}
                });
            }
		},
		cusCallBack:function(){
		    var _this=this;
		    var csid=$api.getStorage("csid");
            if(csid&&csid!=""){
                _this.getUnReadDataAjax(_this.getMsg,0,"interact");
                _this.getUnReadDataAjax(_this.getMsg,1,"msg");
            }
		},
		getAjaxData:function(callback,displayToast){
		    var _this=this;
            
            var uid=$api.getStorage("uid");
            try{

                api.ajax({
                    url: td.domain+"/isallowcus/"+uid+"?token="+td.token,
                    method: "get"
                },function(ret, err){
                    if (ret) {
                         if(ret.code==200){
                            $api.setStorage("csid",ret.data.csid);
                            $api.setStorage("cname",ret.data.name);
                            callback();
                         }
                         else if(ret.code==201){
                           if(displayToast){
                                _this.toast("未分配客服");
                           }
                           
                         }
                         else{
                            if(displayToast){
                                _this.toast("没有会员ID");
                            }
                         } 
                         
                    } else {
                        toast("网络异常，请稍后重试");
                    }
                });   
            }catch(err){
            	_this.toast("系统异常，请稍后重试");
            }
        },
		ajaxData:function(){
			var _this=this;
            var id="navpanel";
			var folder = "cartype";
			var url=td.domain+"/navpanel?token="+td.token;
			var method="get";
			var params="";
			var async="true";
			doCache(folder,id,url,method,params,async,function(data){
			    var finalData=data.data;
				if(data.code==200){
                    if(finalData.length>0){
                        // _this.builtData(finalData);
		                 _this.$dom.html(tpl("tpl-nav", {data:finalData}));
		            }
                }
                else{
                	toast(finalData);
                }
			});
		},
		builtData:function(finalData){
		    var _this=this;
		    var dataArr=[];
		    var len=finalData.length;
		    var groups=0;
		    if(len%4==0){
		    	groups=len/4;
		    }
		    else{
		    	groups=parseInt(len/4)+1;
		    }
			for(var i=0;i<groups;i++){
				var arr=[];
			    if(groups==1){
	    	    	arr=finalData.slice(0);
	    	    }
	    	    else{
	    	        var start=i*4;
	    	        var end=(i+1)*4;
	    	        if(i+1>=groups){
	    	        	arr=finalData.slice(start);
	    	        }
	    	        else{
	    	        	arr=finalData.slice(start,end);
	    	        }
	    	    }
	    	    dataArr.push(arr);
			}
			_this.dataArr=dataArr;
		},
		bindEvt : function() {
			var _this = this;
			_this.$dom.on("click", ".slide-nav", function(e) {
			    var cid=$(this).attr("data-cid");
			    var onecid=$(this).attr("data-onecid");
			    var id=$(this).attr("id");
			    var title=$(this).attr("data-title");
			    if(onecid=="452"||onecid=="487"){
			    	api.execScript({
		                script: 'window.nav_b.openTab("news","'+onecid+'","'+cid+'");'
	                });
			    }
			    else{
			    	if(id=="slidenav0"){
			    	//自选股
						api.sendEvent({
							name:'start fiveonefive activity'
						})
			    	
			    	}
//			    	else if(id=="slidenav4"){
//			    	//选股宝
//			    	 
//			    	}
			        else if(id=="slidenav5"){
			    	//盘中播报
					   api.openWin({
			              name: 'panzhong_win',
			              url: '../common/common-header-level2.html',
			              slidBackEnabled:false,
			              pageParam:{page:"panzhong",pageurl:"../gaoshou/gaoshou.html",title:title,isshow_btnsave:true,btnsave:"保存",param:{page:"panzhong",cid:cid}}
			           });
			    	}
			    	else if(id=="slidenav7"){
			    	//理财助手
			    		api.openWin({
			              name: 'cuser_win',
			              url: '../common/common-header-level2.html',
			              slidBackEnabled:false,
			              pageParam:{page:"customeService",pageurl:"../customeService/customeService.html",title:"",isshow_tab:true,tabname1:"理财团队", isshow_btnsave:true,btnsave:"保存",param:{page:"finaceCustomeService",cid:cid}}
			           });
			    	}
			    
			    }
			    
			});
		},
		openWin:function(type){
		    var _this = this;
		    if(type=="online"){
		    	api.openWin({
					name : type,
					url : '../video/'+type+'.html',
					bounces : false,
					delay : 200
				});
		    
		    }
		    else if(type=="interact"){
	           _this.interactDetail();
		    }
		    else if(type=="customeService"){
//		        if(_this.isLogined&&_this.hasService){
//		        	api.openWin({
//						name : 'interact',
//						url : 'interact.html',
//						bounces : false,
//						delay : 200
//					});
//		        }
//		        else if(_this.isLogined){
//		        	alert("您还没有分配客服，没有权限");
//		        }
//		        else{
		        	api.execScript({
		                script: 'openCusSerTab("'+type+'");'
	                });
		       // }
		    }
		    else if(type=="msg"){
		        _this.clearUnreadStatus(type);
		        var uid=$api.getStorage("uid");
		    	if(!uid||uid==''){
		    		_this.goLogin();
		    		return;
		    	}
		    	else{
		    		api.openWin({
						name : type,
						url : '../my/'+type+'.html',
						bounces : false,
						delay : 200
					});
		    	}
		    }
		},
		goLogin:function(){
			api.openWin({
				name : 'login',
				url : '../common/login.html',
				bounces : false,
				animation : 'push',
				scrollToTop : true,
				rect : {
					x : 0,
					y : 0,
					w : 'auto',
					h : 'auto'
				}
			});
		},
		toast:function(msg){
			api.toast({
		        msg: msg,
		        duration:3000,
		        location: 'middle'
		    });
		},
		interactDetail:function(){
			var _this=this;
			_this.clearUnreadStatus("interact");
		    _this.toInteract();
		}
	};

	return {
		swiper_nav : swiper_nav
	};

});
