/*TMODJS:{"version":17,"md5":"8cb98930116a4420fedb9d1b558878d9"}*/
template('main/swiper_nav',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,$out='';$out+='<div class="swiper-container" data-space-between=\'10\' id="swiper_nav"> <div class="swiper-wrapper"> <div class="swiper-slide"> <ul> ';
$each(data,function(value,i){
$out+=' <li> ';
if(i==0){
$out+=' <div class="slide-nav first" data-type="';
$out+=$string(value.type);
$out+='"> ';
}else{
$out+=' <div class="slide-nav" data-type="';
$out+=$string(value.type);
$out+='"> ';
}
$out+=' <i class="i-nav i-nav-';
$out+=$string(i);
$out+='"> ';
if(value.type=="interact"|| value.type=="msg"){
$out+=' <span class="tip" id="';
$out+=$string(value.type);
$out+='Tip"></span> ';
}
$out+=' </i> <div class="slide-nav-text"> ';
$out+=$string(value.name);
$out+=' </div> </div> </li> ';
});
$out+=' </ul> </div> </div> <div class="swiper-pagination"></div> </div>';
return new String($out);
});