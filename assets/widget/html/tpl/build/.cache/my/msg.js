/*TMODJS:{"version":3,"md5":"3da39a6a11f39506e4043b47939ef93c"}*/
template('my/msg',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,item=$data.item,j=$data.j,$out='';$each(data,function(value,i){
$out+=' <div class="msgList"> <div class="timeStamp" data-src="';
$out+=$string(value.date);
$out+='"> <span>';
$out+=$string(value.date);
$out+='</span> </div> <ul > ';
$each(value.sub,function(item,j){
$out+=' <li> <a href="#" class="item-link item-content external"> <div class="item-inner"> <div class="item-text"> ';
$out+=$string(item.content);
$out+=' </div> <div class="item-date"> ';
$out+=$string(item.time);
$out+=' </div> </div> </a> </li> ';
});
$out+=' </ul> </div> ';
});
return new String($out);
});