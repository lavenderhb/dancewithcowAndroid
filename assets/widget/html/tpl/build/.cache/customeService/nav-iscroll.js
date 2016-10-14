/*TMODJS:{"version":3,"md5":"2371a28e79dfca179e9b4c73b6ee259a"}*/
template('customeService/nav-iscroll',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$escape=$utils.$escape,$out='';$out+='<div id="wrapper"> <div id="scroller" class="content-inner"> <ul> ';
$each(data,function(value,i){
$out+=' <li data-index="';
$out+=$escape(i);
$out+='" id="';
$out+=$escape(value.id);
$out+='" class="active" tapmode="" > ';
$out+=$escape(value.name);
$out+='</li> ';
});
$out+=' </ul> </div> </div> ';
return new String($out);
});