/*TMODJS:{"version":2,"md5":"91d0d7857776bc6f5a27a9a820ed6678"}*/
template('video/online',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,$out='';$each(data,function(value,i){
$out+=' <div class="video-btn video-btn';
$out+=$string(i+1);
$out+='" data-cid="';
$out+=$string(value.cid);
$out+='" data-aid="';
$out+=$string(value.aid);
$out+='"> <div class="video video';
$out+=$string(i+1);
$out+='"><h3>';
$out+=$string(value.title);
$out+='</h3>';
$out+=$string(value.content);
$out+='</div> </div> ';
});
return new String($out);
});