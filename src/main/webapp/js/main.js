// main.js using RequireJS 2.0.1
require.config({
    paths: {
         //Major libraries
        'jquery': 'libs/jquery/1.7.1/jquery-min',
        'underscore': 'libs/underscore/1.3.1-amdjs/underscore-min', // AMD support
        'backbone': 'libs/backbone/0.9.1-amdjs/backbone-min', // AMD support
        // Additional plugins
        'jqueryUI': 'libs/jquery-ui/1.8.18/jquery-ui.custom.min',
        'jqueryValidationEngine': 'libs/jquery-validation-engine/2.5.2/jquery.validationEngine',
        'jqueryValidationEngineTranslate': 'libs/jquery-validation-engine/2.5.2/jquery.validationEngine-en',
        'datatables': 'libs/datatables/1.9.1/jquery.dataTables.min',
         //https://github.com/uzikilon/backbone-poller
        'backbone-poller': 'libs/backbone-poller/1.0/backbone.poller',
        // Arcadian libs
        'arcadianUtil': 'libs/arcadian/arcadian-util',        
        'arcadianErrorHandling': 'libs/arcadian/arcadian-error-handling',        
        // Require.js plugins
        'text': 'libs/require/2.0.1/text',
        'domReady': 'libs/require/2.0.1/domReady',
        'cs': 'libs/require/2.0.1/cs',
        // Just a short cut so we can put our html outside the js dir
        // When you have HTML/CSS designers this aids in keeping them out of the js directory        
        'templates': '../templates'       
    }
});

// java -classpath ../../../../src/main/third-party-jars/rhino/1_7R3/js.jar:../../../../src/main/third-party-jars/closure-compiler/compiler.jar org.mozilla.javascript.tools.shell.Main libs/r.js/r.js -o buildconfig.js
require([
    'domReady', // optional, using RequireJS domReady plugin
    'app',
], function(domReady, app){
    domReady(function () {
        app.initialize();
    });
});