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
    <p>Auction Bids</p>
    <div>
        <input type="text" class="form-control" id="auctionId">
        <button id="changeAuctionButton">Change auction</button>
    </div>
    <p>
        Output <br />
    <div id="output"></div>
    </p>
</div>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.4/jquery.min.js"></script>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

<script type="application/javascript">

    $(function () {

        var pref = "http://localhost:8090/api/auction/";
        var suff = "/bid-sse";
        var source = new EventSource(pref);

        $("#changeAuctionButton").click(function () {
            console.log("cliked ");
            source.close();
            $("#output").empty();

            var id = $("#auctionId").val();
            source = new EventSource(pref + id + suff);
            source.onopen = function (evt) {
              console.log("Opened sse");
            };
            source.onmessage = function (evt) {
                output.innerHTML = output.innerHTML + ('<p>' + evt.data + '</p>');
            };
        });
    });

    // if(typeof(EventSource) !== "undefined"){
    //     var output = document.getElementById("output");
    //     var source = new EventSource("http://localhost:8090/api/auction/5beeffa145c97f7057b98fdc/bid-sse");
    //     source.onmessage = function (evt) {
    //         output.innerHTML = output.innerHTML + ('<p>' + evt.data + '</p>');
    //     }
    //
    // }



</script>
</body>
</html>