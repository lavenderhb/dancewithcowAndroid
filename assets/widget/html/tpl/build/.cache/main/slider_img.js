/*TMODJS:{"version":7,"md5":"c2272f204842221c997dd95c310ffcc1"}*/
template('main/slider_img',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,$out='';$out+='<div class="swiper-container" data-space-between=\'10\' id="slider_img"> <div class="swiper-wrapper"> ';
$each(data,function(value,i){
$out+=' <div class="swiper-slide"><a class="external" data-cid="';
$out+=$string(value.cid);
$out+='" data-aid="';
$out+=$string(value.aid);
$out+='" ><img src="';
$out+=$string(value.image);
$out+='" alt="';
$out+=$string(value.title);
$out+='" class="cache" /></a></div> ';
});
$out+=' </div> <div class="swiper-pagination"></div> </div>';
return new String($out);
});