/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var wsUri = "ws://" + document.location.host + "/RIMS-HH/socket";
var websocket = new WebSocket(wsUri);
var debug = true;
websocket.onerror = function(event) {
    onError(event)
};

websocket.onopen = function(event) {
    onOpen(event)
};

websocket.onmessage = function(event) {
    onMessage(event)
};
window.onbeforeunload = function(e){
    websocket.close();
};

function onMessage(evt) {
    if (debug) {
        console.log("onMessage");
        console.log(evt);
    }
    $("#poll").text(evt.data);
}

function onError(evt) {
    if (debug) {
        console.log("onError");
        console.log(evt);
    }
}

function onOpen(evt) {
    if (debug) {
        console.log("onOpen");
        console.log(evt);
    }
}