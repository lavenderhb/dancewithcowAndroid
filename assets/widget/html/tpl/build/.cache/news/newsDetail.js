/*TMODJS:{"version":74,"md5":"0fc17a63be418a8ce7d0bd572c455000"}*/
template('news/newsDetail',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$string=$utils.$string,title=$data.title,dates=$data.dates,video=$data.video,wtype=$data.wtype,bodys=$data.bodys,$out='';$out+='<div class="item-title"> <h2>';
$out+=$string(title);
$out+='</h2> </div> <div class="item-subtitle"> <span class="newsSrc"></span><span>';
$out+=$string(dates);
$out+='</span> </div> <div class="text" id="textWrap"> ';
if(video&&video!=""&&wtype=="app"){
$out+=' <div style="position: relative; height: 10rem;" id="videoWrap"> <script type="text/javascript" src="../../res/player/sewise.player.min.js"></script> <div style="position: absolute;right:0;bottom: 0;width:60px;height:56px;z-index: 1000;" id="fakeFullBtn"></div> </div> ';
}
$out+=' ';
if(wtype!="web"){
$out+=' <div> ';
$out+=$string(bodys);
$out+=' </div> ';
}
$out+=' </div> ';
return new String($out);
});