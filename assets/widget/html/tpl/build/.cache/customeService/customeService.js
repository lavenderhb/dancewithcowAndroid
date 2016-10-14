/*TMODJS:{"version":9,"md5":"929f18b98d07be95bcb11c6e11740c9f"}*/
template('customeService/customeService',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$each=$utils.$each,data=$data.data,value=$data.value,i=$data.i,$string=$utils.$string,$out='';$each(data,function(value,i){
$out+=' <div class="card"> <div class="card-content"> <div class="list-block media-list"> <a class="custom-link" id="';
$out+=$string(value.id);
$out+='"> <div class="wbox item-info-wrap"> <div class="item-media"> <img src="../../images/lazyImg.jpg" data-echo="';
$out+=$string(value.photo);
$out+='" class="cache" onerror="nofind()" /> </div> <div class="wbox-flex item-info"> <div class="wbox line"> <div class="item-title"> ';
$out+=$string(value.name);
$out+=' </div> <div class="item-subtitle"> <span class="">从业时间:</span><span>';
$out+=$string(value.worktime);
$out+='</span> </div> </div> <p class="major-desc"> <span>';
$out+=$string(value.team);
$out+='</span> </p> </div> </div> </a> </div> </div> </div> ';
});
return new String($out);
});