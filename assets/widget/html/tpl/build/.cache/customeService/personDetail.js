/*TMODJS:{"version":6,"md5":"37491519e2fd23f54ac1bb27dd66ac09"}*/
template('customeService/personDetail',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$string=$utils.$string,image=$data.image,name=$data.name,worktime=$data.worktime,team=$data.team,content=$data.content,$out='';$out+='<div class="wbox item-info-wrap"> <div class="item-media"> <img src="';
$out+=$string(image);
$out+='" class="cache" onerror="nofind()"> </div> <div class="wbox-flex item-info"> <div class="wbox line"> <div class="item-title"> ';
$out+=$string(name);
$out+=' </div> <div class="item-subtitle"> <span class="">从业时间:</span><span>';
$out+=$string(worktime);
$out+='</span> </div> </div> <p class="major-desc"> <span>';
$out+=$string(team);
$out+='</span> </p> </div> </div> <div class="item-info2"> <div class="wbox mb-m"> <div class="label"> 个人介绍 </div> <div class="wbox-flex pr"> <div class="line"></div> </div> </div> <div>';
$out+=$string(content);
$out+='</div> </div>';
return new String($out);
});