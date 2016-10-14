/*TMODJS:{"version":16,"md5":"b84c9d74c5681a1198fa15dafa8feb25"}*/
template('news/news_rec',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,item=$data.item,j=$data.j,$out='';$each(data,function(value,i){
$out+=' <div class="content-block newsList"> <div class="timeStamp"><span>';
$out+=$string(value.dates);
$out+='</span></div> <ul> ';
$each(value.sub,function(item,j){
$out+=' <li class="wbox"> <div class="timeLine"> <span>';
$out+=$string(item.times);
$out+='</span><i></i> </div> <p class="wbox-flex rec-news" data-aid="';
$out+=$string(item.aid);
$out+='" data-cid="';
$out+=$string(item.cid);
$out+='" ><a href="#" class="alert-text external">';
$out+=$string(item.content);
$out+='</a></p> </li> ';
});
$out+=' </ul> </div> ';
});
$out+=' ';
return new String($out);
});