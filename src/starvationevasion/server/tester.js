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
            getAllUserRslt:     $('#get-all-users-resp'),
            usernameInput:      $('#username'),
            pwdInput:           $('#pwd'),
            createUsrBtn:       $('#create-btn'),
            gamestateBtn:       $('#gamestate-btn'),
            gamestateRsp:       $('#gamestate-rsp'),
            createChatBtn:      $('#chat-send-btn'),
            chatRsp:            $('#chat-send-rsp'),
            chatInput:          $('#chat-text'),
            chatDestSelect:     $('#chat-dest')
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
                // parse the json
                var _resp = JSON.parse(event.data);
                if(_resp.message == "SUCCESS")
                {
                    // show all the divs that require login
                    TestApp.config.loginRqDivs.fadeIn();
                    TestApp.config.loginBtn.prop('disabled', true);
                }
                else
                {
                    TestApp.config.loginBtn.prop('disabled', false);
                }
            };
            // onclick send the command
            TestApp.connection.send(_getTime() + " login admin admin");
        });

        TestApp.config.getAllUserBtn.click(function () {
            TestApp.connection.onmessage = function (event) {
                TestApp.config.getAllUserRslt.text(event.data);
            };

            TestApp.connection.send(_getTime() + " users");
        });

        TestApp.config.gamestateBtn.click(function () {
            TestApp.connection.onmessage = function (event) {
                TestApp.config.gamestateRsp.text(event.data);
            };

            TestApp.connection.send(_getTime() + " game_state");
        });

        TestApp.config.createUsrBtn.click(function () {
            TestApp.connection.onmessage = function (event) {
                TestApp.config.getAllUserRslt.text(event.data);
            };
            var _uname = TestApp.config.usernameInput.val();
            var _pwd = TestApp.config.pwdInput.val();
            
            TestApp.connection.send(_getTime() + " user_create " + _uname + " " + _pwd);
        });

        TestApp.config.createChatBtn.click(function () {
            TestApp.connection.onmessage = function (event) {
                TestApp.config.chatRsp.text(event.data);
            };
            var _chat_text = TestApp.config.chatInput.val();
            var _dest = TestApp.config.chatDestSelect.text().toUpperCase();
            var _json = {
                "card": null,
                "text": _chat_text
            }
            
            TestApp.connection.send(_getTime() + " chat " + _dest + " " + JSON.stringify(_json, null, " "));
        });

    };

    var _getTime = function ()
    {
        return (new Date).getTime();
    };


    return {
        init: init
    };





})(window, jQuery);