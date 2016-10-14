/*TMODJS:{"version":4,"md5":"8ba68ab83edcc3e401272ecdb09144a9"}*/
template('index',function($data,$filename
/**/) {
'use strict';var $utils=this,$helpers=$utils.$helpers,include=function(filename,data){data=data||$data;var text=$utils.$include(filename,data,$filename);$out+=text;return $out;},$out='';include('./header');
$out+=' ';
include('./main');
$out+=' ';
include('./footer');
return new String($out);
});