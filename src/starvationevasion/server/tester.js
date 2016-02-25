var TestApp =(function (window, $) {

    /**
     * start the app
     * @param {JSON} [settings]
     */
    var init = function (settings){
        TestApp.connection = null;

        // Config used as cache to prevent re traversal of DOM
        TestApp.config = {
            connectBtn:         $('#connect-btn'),
            connectResult:      $('#connect-result')
        };

        // If settings arg is valid object then merge.
        if(typeof settings === 'object'){
            TestApp.config = $.extend(true, TestApp.config, settings);
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

         modal continue - what happens when user clicks cont in modal
         modal restart - what happens when user clicks restart in modal
         next - when user clicks next button
         back - when user click back button

         */
        TestApp.config.connectBtn.click(function () {
            TestApp.connection = new WebSocket('ws://phoebe.cs.unm.edu:8003');

            TestApp.connection.onopen = function (event) {
                TestApp.config.connectResult.text(JSON.stringify(event));
            };
            //
            //connection.onmessage = function (event) {
            //    console.log(event);
            //}

        });

    };


    return {
        init: init
    };





})(window, jQuery);