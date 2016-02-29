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
            };

            Client.connection.onerror = function (event) {
                Client.config.inputArea.fadeOut();
                Client.config.connectArea.fadeIn();
            };

            Client.connection.onmessage = function (event) {
                Client.config.responses.append("\n------\n" + event.data);
                Client.config.responses.scrollTop(Client.config.responses.prop("scrollHeight"));
            };
            
        });

        Client.config.sendBtn.click(function () {

            var _data = Client.config.inputText.val();
            Client.connection.send(_getTime() + " " + _data);
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