requirejs.config({
    baseUrl: 'scripts/lib',
    paths: {
        scripts:'..',
        modernizr: 'modernizr/modernizr',
        requirejs: 'requirejs/require',
        //styles:'../../.tmp/styles',
        styles:'../../styles',
        text:'requirejs-text/text',
        html:'../../html',
        css:'require-css/css',
        'css-builder':'require-css/css-builder',
        'normalize':'require-css/normalize',
        'echo':'../echo'
    },
    shim : {
		echo : {
			exports : 'echo'
		}
	}
});


