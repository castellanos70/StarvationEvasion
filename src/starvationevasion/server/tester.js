var TestApp = (function (window, $) {

    /**
     * start the app
     * @param {JSON} [settings]
     */
    var init = function (settings){
        TestApp.connection = null;

        // Config used as cache to prevent re traversal of DOM
        TestApp.config = {
            address:            $('#address-in'),
            connectBtn:         $('#connect-btn'),
            connectResult:      $('#connect-result'),
            loginBtn:           $('#login-btn')
        };

        // If settings arg is valid object then merge.
        if(typeof settings === 'object'){
            TestApp.config = $.extend(true, TestApp.config);
        }

        _setup();
    };




    /**
     * setup is the main function that sets up listeners on buttons and
     * what happens when buttons are clicked
     * @private
     */
    var _setup = function () {

        /*
         set up all listeners for events
         */
        TestApp.config.connectBtn.click(function () {
            TestApp.connection = new WebSocket('ws://'.concat(TestApp.config.address.val()));
            
            TestApp.connection.onopen = function (event) {
                TestApp.config.connectResult.text("Open:\n\n".concat(JSON.stringify(event)));
            };
            
            TestApp.connection.onclose = function (event) {
                TestApp.config.connectResult.text("Closed:\n\n".concat(JSON.stringify(event)));
            };

            TestApp.connection.onerror = function (event) {
                TestApp.config.connectResult.text("Error:\n\n".concat(JSON.stringify(event)));
            };
            
        });

        TestApp.config.loginBtn.click(function () {
            TestApp.connection.send("34839489393 login admin admin");
        });

    };


    return {
        init: init
    };





})(window, jQuery);