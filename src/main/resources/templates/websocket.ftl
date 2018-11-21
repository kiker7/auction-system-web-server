<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>

    <title>Test websocket</title>
</head>
<body>

<div class="container wrapper">
    <p>Echo - delay 1 sec</p>
    <p>
        Input<br />
        <textarea id="input" name="input" cols="40"></textarea>
    </p>

    <p>
        Output<br />
    <div id="output"></div>
    </p>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>


<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

<script type="application/javascript">

    var token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJuZXctdXNlcjEiLCJhdWQiOiJ1bmtub3duIiwiZXhwIjoyMTQ3NTUzOTMyLCJpYXQiOjE1NDI3NTM5MzJ9.scHMoVSlSoUn5zhD9uH26VZlnxnzWKxNn-CmuioKjhrd6xQRMC7oZ11C_oFGograXdKEv-f-GyfcFKVGNHjj3g";

    var socket = new WebSocket("ws://localhost:8090/api/ws/notifications?token=" + token);

    function addEvent(evnt, elem, func) {
        if (elem.addEventListener)  // W3C DOM
            elem.addEventListener(evnt,func,false);
        else if (elem.attachEvent) { // IE DOM
            elem.attachEvent("on"+evnt, func);
        }
        else { // No much to do
            elem[evnt] = func;
        }
    }

    function print(message) {
        var elem = document.createElement('span');
        elem.innerHTML = message + '<br />';
        document.getElementById('output').appendChild(elem);
    }


    addEvent("keypress", document.getElementById('input'), function(event) {
        socket.send(event.key);
    });
    var output = document.getElementById("output");
    socket.onmessage = function(e) {
        output.innerHTML = output.innerHTML + e.data;
    }
</script>
</body>
</html>