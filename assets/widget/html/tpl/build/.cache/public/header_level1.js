/*TMODJS:{"version":62,"md5":"19c2aefa8ac9a5623aadc6537c28ecec"}*/
template('public/header_level1',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,data=$data.data,$string=$utils.$string,$out='';$out+='<header class="bar bar-nav" id="header"> ';
if(data["headerM"]["hm_display"]){
$out+=' <div> <a class="button button-link button-nav pull-left" href="/demos/card" data-transition=\'slide-out\'> <span class="icon icon-left"></span>';
$out+=$string(data["headerM"]["city"]);
$out+='</a> ';
if(data["headerM"]["key"]&&data["headerM"]["key"]=="main"){
$out+=' <h1 class="title"><i class="main-title"></i>';
$out+=$string(data["headerM"]["title"]);
$out+='</h1> ';
}else{
$out+=' <h1 class="title" id="title">';
$out+=$string(data["headerM"]["title"]);
$out+='</h1> ';
}
$out+=' </div> ';
}
$out+=' <div> ';
if(data["header"]["btn1_display"]){
$out+=' ';
if(data["header"]["btn1_icon_display"]){
$out+=' <span class="pull-right icon td-search" id="search" tapmode=""></span> ';
}
$out+=' ';
}
$out+=' ';
if(data["header"]["btn2_display"]){
$out+=' <button class="button button-link button-nav pull-right"> ';
$out+=$string(data["header"]["btnname2"]);
$out+=' ';
if(data["header"]["btn2_icon_display"]){
$out+=' <span class="icon icon-right"></span> ';
}
$out+=' </button> ';
}
$out+=' </div> ';
if(data["headerTab"]["h_display"]){
$out+=' <div class="buttons-row"> <a href="#tab1" class="tab-link active button" data-from="';
$out+=$string(data['headerTab']['from1']);
$out+='"><span class="pr">';
$out+=$string(data["headerTab"]['tabname1']);
$out+='</span></a> <a href="#tab2" class="tab-link button" data-from="';
$out+=$string(data['headerTab']['from2']);
$out+='"><span class="pr">';
$out+=$string(data["headerTab"]['tabname2']);
$out+=' ';
if(data['headerTab']['from2']=="interact"){
$out+=' <span id="interactTip"></span> ';
}
$out+='</span> </a> </div> ';
}
$out+=' </header> <nav id="nav"> </nav> ';
return new String($out);
});