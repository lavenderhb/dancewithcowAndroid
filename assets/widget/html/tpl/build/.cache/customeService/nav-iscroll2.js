/*TMODJS:{"version":25,"md5":"bc67d8e49ba1160658eec43e79217b26"}*/
template('customeService/nav-iscroll2',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,$out='';$out+='<h3>客服推荐</h3> <div id="wrapper"> <div id="scroller"> <ul> ';
$each(data,function(value,i){
$out+=' <li data-index="';
$out+=$string(i);
$out+='" id="';
$out+=$string(value.id);
$out+='" tapmode="" class="custom-link"><a class="img-wrap"><img src="../../images/lazyImg.jpg" class="cache" data-echo="';
$out+=$string(value.image);
$out+='" onerror="nofind()"/></a><a class="cus-name">';
$out+=$string(value.name);
$out+='</a></li> ';
});
$out+=' </ul> </div> </div> ';
return new String($out);
});