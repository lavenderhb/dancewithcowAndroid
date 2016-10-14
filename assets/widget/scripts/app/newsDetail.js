apiready = function() {
	$(function() {
		require(['scripts/mods/header_level2'], function(header) {
			var header = new header.header_level2({
				type : 'newsDetail'
			});
            var headerH=$("#header").height();
            var pageParamObj={};
            pageParamObj.headerH=headerH;
            pageParamObj.aid=api.pageParam.aid;
            pageParamObj.cid=api.pageParam.cid;
			api.openFrame({
				name : 'newsDetail_frm',
				url : 'newsDetail_frm.html',
				rect : {
					x : 0,
					y : headerH,
					w : 'auto',
					h : 'auto'
				},
				showProgress : 'true',
				pageParam:pageParamObj
			});

			api.addEventListener({
				name : 'keyback'
			}, function(ret, err) {
				//video需要
				if($api.getStorage("noFullScreen")=="false"){
					api.sendEvent({
						name : 'changeFullScreen',
						extra : {
							src : 'keyback'
						}
					});
				}
				else{
					api.closeWin();
				}
				
			});
		});
	});
}; 