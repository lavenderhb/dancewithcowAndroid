/*TMODJS:{"version":11,"md5":"19b9e57d5f75cf650f481922f7ed1875"}*/
template('main/newsList',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,$out='';$each(data,function(value,i){
$out+=' <li data-aid="';
$out+=$string(value.aid);
$out+='" data-cid="';
$out+=$string(value.cid);
$out+='"><a class="external">';
$out+=$string(value.title);
$out+='</a></li> ';
});
return new String($out);
});