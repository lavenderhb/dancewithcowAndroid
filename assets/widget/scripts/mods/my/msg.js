define(["scripts/lib/template"],function(tpl){

   function msg(opts) {
       this.opts = $.extend({}, msg.DEFAULTS, opts);
       this.domSel = this.opts.domSel;
       this.$dom=$(this.domSel);
       this.currIndex=0;
       this.prevIndex=0;
       this.curPage=1;
       this.pageNum=1;
       this.csidData=null;
       this.init();
   }
   msg.DEFAULTS = {
        domSel : '#msgList'
    };
   msg.prototype = {
        init:function(){
           var _this = this;
           var uid=$api.getStorage("uid");
           if(!uid||uid==""){
           }
           else{
                var csid=$api.getStorage("csid");
                var cname=$api.getStorage("cname");
                if(!csid||csid==""){ 
                    _this.getCsid(_this.csidCallBack);
                }
                else{
                    _this.csidCallBack();
                }
           }
        },
        getCsid:function(callback){
            var _this=this;
            _this.getCsidAjaxData(callback);
        },
        csidCallBack:function(){
            var _this=this;
            var csid=$api.getStorage("csid");
            var cname=$api.getStorage("cname");
            if(csid&&csid!=""){
                _this.render();
                _this.bindEvt();
                _this.setRefreshHeader();
                _this.setInfiniteScroll();
            }
        },
        getCsidAjaxData:function(callback,noDisplayToast){
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
                           if(!noDisplayToast){
                                _this.toast("未分配客服");
                           }
                         }
                         else{
                            if(!noDisplayToast){
                                _this.toast("没有会员ID");
                            }
                         } 
                         
                    } else {
                        _this.toast("网络异常，请稍后重试");
                    }
                });
                 
            }catch(err){
                _this.toast("系统异常，请稍后重试");
            }
        },
        render:function(isPrepend){
            var _this=this;
            var csid=$api.getStorage("csid");
            var uid=$api.getStorage("uid");
            var id="msg_"+csid;
            var folder = "cartype";
            var url=td.domain+"/chat/mymsg/"+csid+"/"+uid+"/1?token="+td.token;
            var method="get";
            var params="";
            var async="true";
            doCache(folder,id,url,method,params,async,function(data){
                //alert(JSON.stringify(data));
                var finalData=data.data;
                if(data.code==200){
                    if(finalData.length>0){
//                       if(isPrepend){
//                          var firstData=finalData[0];
//                          var $firstDom= _this.$dom.find(".msgList").eq(0);
//                          if($firstDom){
//                             var date= $firstDom.find(".timeStamp").data("src");
//                             if(firstData.date==date){
//                                  $firstDom.html(tpl("my/msg", {data:finalData}));
//                             }
//                             else{
//                                  _this.$dom.prepend(tpl("my/msg", {data:finalData}));
//                             }
//                          }
//                       }
//                       else
                         {
                            _this.$dom.html(tpl("tpl", {data:finalData}));
                            _this.curPage=1;
                            _this.pageNum=data.pageinfo.pagenum;
                         }
                    }
                }
                else{
                    toast(data.data);
                }
            });
        },
        appendRender:function(){
            var _this=this;
            var csid=$api.getStorage("csid");
            var uid=$api.getStorage("uid");
            var id="msg_"+csid+"_"+_this.curPage;
            var folder = "cartype";
            var url=td.domain+"/chat/mymsg/"+csid+"/"+uid+"/"+_this.curPage+"?token="+td.token;
            var method="get";
            var params="";
            var async="true";
            doCache(folder,id,url,method,params,async,function(data){
                //alert(JSON.stringify(data));
                var finalData=data.data;
                if(data.code==200){
                    if(finalData.length>0){
                        _this.$dom.append(tpl("tpl", {data:finalData}));
                    }
                }
                else{
                    toast(finalData);
                }
            });
            
//          var finalData2=data.data;
//          if(finalData2.length>0){
//               _this.$dom.append(tpl("news/news", {data:finalData2}));
//               _this.lazyImg();
//          }
        },
        bindEvt : function() {
            var _this = this;
//          _this.$dom.on("click",".item-link",function(){
//              api.openWin({
//                  name : 'msgDetail',
//                  url : '../news/msgDetail.html',
//                  bounces : false,
//                  delay : 200
//              });
//          });
            var fontSize = window.getComputedStyle(document.documentElement).fontSize;
                fontSize = fontSize.substring(0, fontSize.length - 2);
            _this.diff=fontSize*1.275;
            _this.currIndex=0;
            _this.prevIndex=-1;
            
            $(window).on("scroll",function(){
                setTimeout(function(){
                    _this.doScroll();
                },300);
            });
        },
        doScroll:function(){
           var _this=this;
            _this.$dom.find(".msgList").each(function(){
                    var scrollTop=document.body.scrollTop;
                    if(scrollTop<$(this)[0].offsetHeight+$(this)[0].offsetTop-_this.diff){
                        _this.currIndex=$(this).index();
                        if(_this.prevIndex!=_this.currIndex){ 
                            _this.$dom.find(".timeStamp").eq(0).html("<span>"+_this.$dom.find(".timeStamp").eq(_this.currIndex).data("src")+"</span>");
                            _this.prevIndex=_this.currIndex;
                        }
                        return false; 
                    }

            });
        },
        toast:function(msg){
            api.toast({
                msg: msg,
                duration:1000,
                location: 'middle'
            });
        },
        setRefreshHeader:function() {
            var _this = this;
            api.setRefreshHeaderInfo({
                visible : true,
                bgColor : '#ccc',
                textColor : '#fff',
                textDown : '下拉刷新...',
                textUp : '松开刷新...',
                showTime : true
            }, function(ret, err) {
                _this.render();
                api.refreshHeaderLoadDone();
            });
        },
        setInfiniteScroll:function(){
            var _this=this;
            //if(_this.pageNum>1){
                api.addEventListener({
                    name : 'scrolltobottom'
                }, function(ret, err) {
                   if(_this.curPage>=_this.pageNum){
                        _this.toast("已到最底部");
//                      api.removeEventListener({
//                          name: 'scrolltobottom'
//                      });
                   }
                   else{
                        _this.curPage++;
                        _this.appendRender();
                   }
                });
            //}
        }
   }
   return {
        msg : msg
    };
   

});