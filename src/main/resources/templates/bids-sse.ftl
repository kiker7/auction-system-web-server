<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>Auction Bids SSE - Test</title>
</head>
<body>

<div class="container">
    <h4>Auction bids as Server-Sent-Events</h4>
    <div class="p-4">
        <input type="text" class="form-control " style="width: 50%;" id="auctionId" placeholder="Auction ID">
        <button id="changeAuctionButton" class="btn btn-primary mt-3">Change auction</button>
    </div>
    <div>
        <h4>Offers:</h4>
        <div id="output"></div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js" integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js" integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q" crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js" integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl" crossorigin="anonymous"></script>
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
</script>
</body>
</html>