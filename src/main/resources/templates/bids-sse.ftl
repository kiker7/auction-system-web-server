<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>Auction bids - Demo</title>
</head>
<body>

<div class="container">
    <div>
        <h2 class="mt-3">Auction bids DEMO</h2>
        <p class="mt-2">Pull auction bids using EventSource (SSE)</p>
    </div>
    <div class="pt-4">
        <input type="text" class="form-control " style="width: 50%;" id="auctionId" placeholder="Auction ID">
        <button id="changeAuctionButton" class="btn btn-primary mt-3">Change auction</button>
    </div>
    <div class="mt-4">
        <div id="output">
            <div id="bids"></div>
        </div>
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
        var output = $("#output");

        $("#changeAuctionButton").click(function () {
            source.close();
            output.empty();
            var id = $("#auctionId").val();

            // EventSource
            source = new EventSource(pref + id + suff);
            source.onmessage = function (evt) {
                bid(evt.data);
            };
        });

        function bid(data) {
            var bid = JSON.parse(data);
            var date = new Date(bid.requestTime);
            output.prepend("<div class='d-flex p-3 bg-light border rounded mt-2' style='width: 50%;'><div>Offer: " + bid.offer + "$ request time: " + date.toLocaleDateString() + "</div></div>");
        }
    });
</script>
</body>
</html>