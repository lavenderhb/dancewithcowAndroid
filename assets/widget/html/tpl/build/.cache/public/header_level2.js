/*TMODJS:{"version":18,"md5":"30964825988291065da451dae37e656d"}*/
template('public/header_level2',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,$string=$utils.$string,data=$data.data,$out='';$out+='<header class="bar bar-nav" id="header"> <div> <a class="td-icon back pull-left" id="back"></a> <h1 class="title">';
$out+=$string(data["title"]);
$out+='</h1> </div> ';
if(data["header"]["h_display"]){
$out+=' <div> ';
if(data["header"]["btn1_display"]){
$out+=' <button class="button button-link button-nav pull-right"> ';
$out+=$string(data["header"]["btnname1"]);
$out+=' ';
if(data["header"]["btn1_icon_display"]){
$out+=' <span class="icon icon-search" id="search"></span> ';
}
$out+=' </button> ';
}
$out+=' ';
if(data["header"]["btn2_display"]){
$out+=' <button class="button button-link button-nav pull-right btn-save external" id="btn-save" data-from="';
$out+=$string(data['header']['from']);
$out+='"> ';
$out+=$string(data["header"]["btnname2"]);
$out+=' ';
if(data["header"]["btn2_icon_display"]){
$out+=' <span class="icon icon-right"></span> ';
}
$out+=' </button> ';
}
$out+=' </div> ';
}
$out+=' ';
if(data["headerTab"]["h_display"]){
$out+=' <div class="buttons-row"> <a href="#tab1" class="tab-link button" data-from="';
$out+=$string(data['headerTab']['from1']);
$out+='">';
$out+=$string(data["headerTab"]['tabname1']);
$out+='</a> <a href="#tab2" class="tab-link button" data-from="';
$out+=$string(data['headerTab']['from2']);
$out+='">';
$out+=$string(data["headerTab"]['tabname2']);
$out+='</a> </div> ';
}
$out+=' </header> ';
return new String($out);
});