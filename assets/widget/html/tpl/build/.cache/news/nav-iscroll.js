/*TMODJS:{"version":6,"md5":"b28100961206d50e6b158702c35f3b1b"}*/
template('news/nav-iscroll',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,$out='';$out+='<div id="wrapper"> <div id="scroller" class="content-inner"> <ul> ';
$each(data,function(value,i){
$out+=' ';
if(i==0){
$out+=' <li data-index="';
$out+=$string(i);
$out+='" id="';
$out+=$string(value.id);
$out+='" class="nav_active" tapmode="" > ';
$out+=$string(value.title);
$out+='</li> ';
}else{
$out+=' <li data-index="';
$out+=$string(i);
$out+='" id="';
$out+=$string(value.id);
$out+='" tapmode="" > ';
$out+=$string(value.title);
$out+='</li> ';
}
$out+=' ';
});
$out+=' </ul> </div> </div> ';
return new String($out);
});