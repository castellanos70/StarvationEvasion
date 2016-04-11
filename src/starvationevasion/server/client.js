var Client = (function (window, $) {

    /**
     * start the app
     * @param {JSON} [settings]
     */
    var init = function (settings){
        Client.connection = null;

        // Config used as cache to prevent re traversal of DOM
        Client.config = {
            inputArea:     $('#input-area'),
            connectArea:   $('#connection'),
            address:       $('#address-in'),
            connectBtn:    $('#connect-btn'),
            restartBtn:    $('#restart-btn'),
            loginBtn:      $('#login-btn'),
            endBtn:        $('#end-btn'),
            pwdText:       $('#pwd-field'),
            inputText:     $('#text-input'),
            sendBtn:       $('#send-btn'),
            responses:     $('#stream')
        };

        // If settings arg is valid object then merge.
        if(typeof settings === 'object'){
            Client.config = $.extend(true, Client.config);
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
        Client.config.connectBtn.click(function () {
            Client.connection = new WebSocket('ws://'.concat(Client.config.address.val()));

            
            Client.connection.onopen = function (event) {
                Client.config.connectArea.hide();
                Client.config.inputArea.fadeIn();

            };
            
            Client.connection.onclose = function (event) {
                Client.config.inputArea.fadeOut();
                Client.config.connectArea.fadeIn();
                Client.config.loginBtn.fadeIn();
            };

            Client.connection.onerror = function (event) {
                console.log("error");
                // Client.config.inputArea.fadeOut();
                // Client.config.connectArea.fadeIn();
                // Client.config.loginBtn.fadeIn();
            };

            Client.connection.onmessage = function (event) {
                var json_data = JSON.parse(event.data);
                Client.config.responses.append(event.data+ "\n");
                Client.config.responses.scrollTop(Client.config.responses.prop("scrollHeight"));

                if(json_data.type.name === "AUTH" && json_data.message === "SUCCESS")
                {
                    Client.config.loginBtn.fadeOut();
                }
            };
            
        });

        Client.config.endBtn.click(function()
        {
            Client.connection.send(_getTime() + " stop_game");
        });

        Client.config.restartBtn.click(function()
        {
            Client.connection.send(_getTime() + " restart_game");
        });


        Client.config.loginBtn.click(function()
        {
            Client.config.pwdText.fadeIn();
        });

        Client.config.pwdText.keydown(function (event) {
            var keypressed = event.keyCode || event.which;
            if (keypressed == 13) {
                var _data = Client.config.pwdText.val();
                Client.config.pwdText.val('');
                Client.connection.send(_getTime() + " login " + JSON.stringify({ username: "admin", password:_data}));
                Client.config.pwdText.fadeOut();
            }
        });

        Client.config.inputText.keydown(function (event) {
            var keypressed = event.keyCode || event.which;
            if (keypressed == 13) {
                var _data = Client.config.inputText.val();
                Client.config.inputText.val('');
                Client.connection.send(_getTime() + " " + _data);
            }
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