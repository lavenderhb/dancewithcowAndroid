define(["scripts/lib/template","scripts/mods/nav-iscroll"], function(tpl,nav) {

	function header(opts) {
		this.opts = $.extend({}, header.DEFAULTS, opts);
		this.domSel = this.opts.domSel;
		this.$dom = $(this.domSel);
		this.curType=this.opts.type;
		this.isRenderNewsHead=true;
		this.username=$api.getStorage("username");
		this.finalData=[];
		var username=this.username;
		this.init();
	}
	header.DEFAULTS = {
		domSel:"#header"//导航id
	};
	header.prototype = {
		init : function() {
			var _this = this;
			var header = $api.byId("header");
			$api.fixIos7Bar(header);
			//头部右侧搜索图标是绝对定位，需要强制沉浸式
			var rightwrap=$api.byId("right-wrap");
			$api.fixIos7Bar(rightwrap);
			api.setStatusBarStyle({
			    style: 'light'
			});
			_this.renderTpl(_this.curType);
			_this.bindEvt();
			_this.getFrameYAndH();
		    api.parseTapmode();
		},
		renderTpl : function(type,cid,subcid) {
			var _this = this;
			_this.$dom = $(_this.domSel);
			_this.curType=type;
			if(type=="my"){
			    $("#zixun-title-wrap").hide();
			    $("#title-wrap").show();
			    $("#main-title").hide();
			    $("#title").show();
			    $("#search").hide();
			    $("#cus-tab").hide();
			    $("#nav").hide();
			    $("#nav452").hide();
			    $("#nav487").hide();
			    var uid=$api.getStorage("uid");
		    	if(!uid||uid==''){
		    		$("#title").html("未登录/未注册");
		    	}
		    	else{
		    		$("#title").html($api.getStorage("username"));
		    	}
			}
			else if(type=="main"){
			    $("#zixun-title-wrap").hide();
			    $("#title-wrap").show();
				$("#main-title").show();
			    $("#title").hide();
			    $("#search").show();
			    $("#cus-tab").hide();
			    $("#nav452").hide();
			    $("#nav487").hide();
			    //render iscroll-nav
			    var data=[{cid:-100,title:"新闻精选 "},{cid:-100,title:"行情数据  "},{cid:491,title:"牛股情报 "},{cid:6,title:"客服团队"}];
			    var html=tpl("tpl",{data:data});
			    $("#nav").html(html);
			    $("#scroller").css({width:"100%"});
			    $("#scroller").find("li").css({width:"25%"});
			    $("#nav").show();
			    _this.bindMainScrollEvt();
			    
			}
			else if(type=="news"){
			    $("#title-wrap").hide();
			    $("#main-title").hide();
			    $("#title").hide();
			    $("#search").show();
			    $("#cus-tab").hide();
			    $("#nav").hide();
			    $("#nav452").hide();
			    $("#nav487").hide();
			    if(!cid||cid==""){
			    	cid="452";
			    }
		    	_this.getNewsOnemenuDataAjax(_this.renderMenu,cid,subcid);
			}
			else if(type=="hangqing"){
				//行情
			    $("#zixun-title-wrap").hide();
			    $("#title-wrap").show();
			    $("#main-title").hide();
			    $("#title").show(); 
			    $("#title").html("行情");
			    $("#search").hide();
			    $("#cus-tab").hide();
			    $("#nav452").hide();
			    $("#nav487").hide();
			     $("#nav").hide();
			}

		},
		bindMainScrollEvt:function(){
		    var _this=this;
			_this.mainScroll = new IScroll("#wrapper", {
				eventPassthrough : true,
				scrollX : true,
				scrollY : false,
				preventDefault : false,
				snap : "li"
			});
		},
		navbottomFn:function(cid){
			//openTab("news",cid);
		},
		renderMenu:function(data,headerObj,cid,subcid){
			var html=tpl("tpl-zixun",{data:data});
		    $("#zixun-title-wrap").html(html);
		    $("#zixun-title-wrap").show();
		    headerObj.setZixunNavSelected(cid);
		    /*专栏 子菜单的 nav*/
		    if(cid=="487"){
		        //if(!headerObj.newsSubNav2){
		            $("#nav452").hide();
		            //alert("487  "+subcid);
		        	headerObj.newsSubNav2 = new nav.NavIscroll({cid:cid,domSel:"#wrapper487",subCid:subcid});
		        //}
		    }
		    else{
		        /*头条 子菜单的 nav*/
		    	//if(!headerObj.newsSubNav){
		    	    $("#nav487").hide();
		    	    //alert("452  "+subcid);
		        	headerObj.newsSubNav = new nav.NavIscroll({cid:cid,domSel:"#wrapper452",subCid:subcid});
		        //}
		    }
		},
		getNewsOnemenuDataAjax:function(callback,cid,subcid){
		    var _this=this;
            var id="news_onemenu";
            var folder = "cartype";
            var url=td.domain+"/news/onemenu?token="+td.token;
            var method="get";
            var params="";
            var async="true";
            doCache(folder,id,url,method,params,async,function(data){
                //alert(JSON.stringify(data));
                var finalData=data.data;
                if(data.code==200){
                   callback(finalData,_this,cid,subcid);
                }
                else{
                    //toast(data.data);
                }
            });
        },
		bindEvt : function() {
			var _this=this;
			_this.$dom.on("click", ".tab-link", function(e) {
			    if($(this).hasClass("active")){
			    	return;
			    }
			    else{
			       _this.clearUnreadStatus("interact");
			    	_this.toInteract();
			    }
			    
			});
			_this.$dom.on("click","#search",function(){
				api.openWin({
					name : 'search',
					url : 'html/common/search_window.html',
					slidBackEnabled:false,
					bounces : true,
					delay : 200
				}); 
			});
			//资讯头事件
			$("#zixun-title-wrap").on("click","li",function(){
				var hideSubNav=$(this).attr("data-hidesubnav");
				var cid=$(this).attr("data-cid");
			    if(hideSubNav){
//			        $("#zixun-title-wrap").find(".nav_active").removeClass("nav_active");
//				    $(this).addClass("nav_active");
				     _this.setZixunNavSelected(cid);
			        //快讯
					api.setFrameGroupAttr({
					    name: 'group452',
					    hidden:true
					});
					api.setFrameGroupAttr({
					    name: 'group487',
					    hidden:true
					});
			    	 $("#nav452").hide();
			    	 $("#nav487").hide();
			    	 _this.openKuaiXunPage("kuaixun",cid);
			    }
				else{
				    if(cid=="487"){
				        //专栏
				       _this.openZhuanlan(cid,$(this));
				    }
				    else{
				        //头条
						_this.openToutiao(cid,$(this));
				    }
				}
			});
			//首页nav事件,比如新闻精选，行情数据，高手追踪，客服团队
			$("#nav").on("click","li",function(){
			   if($(this).attr("data-subnav")) return;
			   var cid=$(this).attr("data-cid");
			   var id=$(this).attr("id");
			   if(id=="mainnav0"){
			   	//新闻精选
			   	window.nav_b.openTab("news");
			   }
			   else if(id=="mainnav1"){
			  		 //行情数据
			   		//window.nav_b.openTab("hangqing");
				   api.sendEvent({
					   name:'start fiveonefive activity'
				   })
			   }
			   else if(id=="mainnav2"){
			    //高手追踪
//	  			   api.openWin({
//		              name: 'gaoshou_win',
//		              url: 'html/common/common-header-level2.html',
//		              slidBackEnabled:false,
//		              pageParam:{page:"gaoshou",pageurl:"../gaoshou/gaoshou.html",title:"高手追踪",isshow_btnsave:true,btnsave:"保存",param:{page:"gaoshou",cid:cid}}
//		           });
		           var onecid=487;
	      		   var cid=481;
		           window.nav_b.openTab("news",onecid,cid);
			   }
			   else if(id=="mainnav3"){
			   	//客服团队
				   api.openWin({
		              name: 'cuser_win',
		              url: 'html/common/common-header-level2.html',
		              slidBackEnabled:false,
		              pageParam:{page:"customeService",pageurl:"../customeService/customeService.html",title:"",isshow_tab:true,tabname1:"客服团队", isshow_btnsave:true,btnsave:"保存",param:{page:"customeService",cid:cid}}
		           });
			   }
			});
		},
		openToutiao:function(cid,$dom){
			var _this=this;
//			$("#zixun-title-wrap").find(".nav_active").removeClass("nav_active");
//		    $dom.addClass("nav_active");
		    _this.setZixunNavSelected(cid);
		    //头条，专栏
			api.closeFrame({
                name: "kuaixun"
            });
			api.setFrameGroupAttr({
			    name: 'group487',
			    hidden:true
			});
			api.setFrameGroupAttr({
			    name: 'group452',
			    hidden:false
			});
			$("#nav487").hide();
    	    $("#nav452").show();
			if(!_this.newsSubNav){
				_this.newsSubNav = new nav.NavIscroll({cid:cid,domSel:"#wrapper452"});
			}
		},
		openZhuanlan:function(cid,$dom){
			var _this=this;
//			$("#zixun-title-wrap").find(".nav_active").removeClass("nav_active");
//		    $dom.addClass("nav_active");
		    _this.setZixunNavSelected(cid);
		    //头条，专栏
			api.closeFrame({
                name: "kuaixun"
            });
			api.setFrameGroupAttr({
			    name: 'group452',
			    hidden:true
			});
			api.setFrameGroupAttr({
			    name: 'group487',
			    hidden:false
			});
			$("#nav452").hide();
    	    $("#nav487").show();
    	    if(!_this.newsSubNav2){
	    		_this.newsSubNav2 = new nav.NavIscroll({cid:cid,domSel:"#wrapper487"});
	    	}
		},
		setZixunNavSelected:function(cid){
			$("#zixun-title-wrap").find(".nav_active").removeClass("nav_active");
			var $dom=$("#zixun-title-wrap").find('[data-cid="'+cid+'"]');
		    $dom.addClass("nav_active");
		},
		openKuaiXunPage:function(type,cid){
		    var _this=this;
		    var headerH=$("#header").height();
		    var h=_this.frameHgt-headerH;
		    $("#nav").hide();
			api.openFrame({
				name : type,
				url : 'html/news/' + type + '.html',
				bounces : type == 'my' ? false : true,
				rect : {
					x : 0,
					y : headerH,
					w : 'auto',
					h : h
				},
				reload:true,
				pageParam:{type:type,cid:cid}
			});
		},
		clearNewsSubNav:function(){
			var _this=this;
			_this.newsSubNav2=undefined;
			_this.newsSubNav=undefined;
		},
		getFrameYAndH:function(){
		    //计算从底部导航中打开的页面（frame）的高度
		    var _this=this;
		    //只有ios做沉浸式头部
			var fontSize = window.getComputedStyle(document.documentElement).fontSize;
			fontSize = fontSize.substring(0, fontSize.length - 2);
//			var y=fontSize*2.5;
//			var n = api.systemType;
//			if ("ios" == n) {
//				var r = api.systemVersion, a = parseInt(r, 10), i = api.fullScreen, o = api.iOS7StatusBarAppearance;
//				a >= 7 && !i && o && (y+=20)
//			}
//			_this.headerH=y;
			//底部导航高度
			var bottomNavH=fontSize*2.5;
			//默认高度，如果页面还有头部，再这个基础上再减去头的高度
			_this.frameHgt=api.winHeight-bottomNavH;
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
                   //alert("finalData.count"+finalData.count);
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
					url : 'html/common/login.html',
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
                    url : 'html/public/interact.html',
                    bounces : false,
                    delay : 200,
                    pageParam:{csid:csid,cname:cname}
                });
            }
		},
		csidCallBack:function(){
		   var _this=this;
		   var csid=$api.getStorage("csid");
           if(csid&&csid!=""){
              _this.getUnReadDataAjax(_this.getMsg,0,"interact");
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
        updateUserName:function(){
            var _this=this;
        	var uid=$api.getStorage("uid");
		    if(uid&&uid!=""){
		        if(_this.curType=="my"){
		            $("#title").html($api.getStorage("username"));
		        }
		    }
		    else{
		    	if(_this.curType=="my"){
		            $("#title").html("未登录/未注册");
		        }
		    }
        },
        toast:function(msg){
			api.toast({
		        msg: msg,
		        duration:1000,
		        location: 'middle'
		    });
		}
	};

	return {
		header : header
	};

});
