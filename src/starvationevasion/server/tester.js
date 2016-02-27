var TestApp = (function (window, $) {

    /**
     * start the app
     * @param {JSON} [settings]
     */
    var init = function (settings){
        TestApp.connection = null;

        // Config used as cache to prevent re traversal of DOM
        TestApp.config = {
            loginRqDivs:        $('.login-req'),
            address:            $('#address-in'),
            connectBtn:         $('#connect-btn'),
            connectResult:      $('#connect-result'),
            loginBtn:           $('#login-btn'),
            loginResult:        $('#login-result'),
            getAllUserBtn:      $('#get-all-users'),
            getAllUserRslt:     $('#get-all-users-resp')
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
                TestApp.config.loginRqDivs.fadeIn();
                TestApp.config.connectResult.text("Open:\n\n".concat(JSON.stringify(event)));
                TestApp.config.connectBtn.prop('disabled', true);
            };
            
            TestApp.connection.onclose = function (event) {
                TestApp.config.connectResult.text("Closed:\n\n".concat(JSON.stringify(event)));
                TestApp.config.connectBtn.prop('disabled', false);
            };

            TestApp.connection.onerror = function (event) {
                TestApp.config.loginRqDivs.fadeOut();
                TestApp.config.connectBtn.prop('disabled', false);
                TestApp.config.connectResult.text("Error:\n\n".concat(JSON.stringify(event)));
            };
            
        });

        TestApp.config.loginBtn.click(function () {
            TestApp.connection.onmessage = function (event) {
                TestApp.config.loginResult.text(event.data);
                var _resp = JSON.parse(event.data);
                if(_resp.message == "SUCCESS")
                {
                    TestApp.config.loginBtn.prop('disabled', true);
                }
                else
                {
                    TestApp.config.loginBtn.prop('disabled', false);
                }
            };

            TestApp.connection.send("34839489393.0 login admin admin");
        });

        TestApp.config.getAllUserBtn.click(function () {
            TestApp.connection.onmessage = function (event) {
                // TestApp.config.getAllUserRslt.text(event.data);
                TestApp.connnection.onmessage = null;
            };

            TestApp.connection.send("34839489393.0 users");
        });

    };


    return {
        init: init
    };





})(window, jQuery);