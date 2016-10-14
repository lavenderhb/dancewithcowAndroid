/*TMODJS:{"version":10,"md5":"e2d68b71e13d0960b71a4332965900a0"}*/
template('news/news',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,$out='';$each(data,function(value,i){
$out+=' <li> <a href="#" class="item-link item-content external" data-aid="';
$out+=$string(value.aid);
$out+='" data-cid="';
$out+=$string(value.cid);
$out+='"> <div class="item-media"><img src="../../images/lazyImg.jpg" class="cache" data-echo="';
$out+=$string(value.image);
$out+='" width="80"> </div> <div class="item-inner"> <div class="item-title-row"> <div class="item-title"> ';
$out+=$string(value.title);
$out+=' </div> <div class="item-after"> ';
$out+=$string(value.date);
$out+=' </div> </div> <div class="item-text"> ';
$out+=$string(value.content);
$out+=' </div> </div> </a> </li> ';
});
return new String($out);
});