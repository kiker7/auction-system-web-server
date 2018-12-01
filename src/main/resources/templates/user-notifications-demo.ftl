<!doctype html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <title>User notifications - Demo</title>
</head>
<body>

<div class="container">
    <div>
        <h2 class="mt-3">User notifications DEMO</h2>
        <p class="mt-2">Pull user notifications using EventSource (SSE)</p>
    </div>
    <div>
        <div class="pt-4">
            <input type="text" class="form-control " style="width: 50%;" id="userId" placeholder="UserID">
            <button id="changeUserButton" class="btn btn-primary mt-3">Change user</button>
        </div>
        <div class="mt-4">
            <div id="output"></div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.2.1.slim.min.js"
        integrity="sha384-KJ3o2DKtIkvYIK3UENzmM7KCkRr/rE9/Qpg6aAZGJwFDMVNA/GpGFF93hXpG5KkN"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js"
        integrity="sha384-ApNbgh9B+Y1QKtv3Rn7W3mgPxhU9K/ScQsAP7hUibX39j7fakFPskvXusvfa0b4Q"
        crossorigin="anonymous"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/js/bootstrap.min.js"
        integrity="sha384-JZR6Spejh4U02d8jOt6vLEHfe/JQGiRRSQQxSfFWpi1MquVdAyjUar5+76PVCmYl"
        crossorigin="anonymous"></script>

<script type="application/javascript">
    $(function () {
        var pref = "http://localhost:8090/api/notifications/";
        var source = new EventSource(pref);
        var output = $("#output");

        $("#changeUserButton").click(function () {
            source.close();
            output.empty();
            var id = $("#userId").val();

            // Eventsource
            source = new EventSource(pref + id);
            source.onmessage = function (evt) {
                notification(evt.data);
            };
        });

        function notification(data) {
            var notification = JSON.parse(data);
            output.prepend("<div class='d-flex p-3 bg-light border rounded mt-2' style='width: 50%;'><div>" + notification.message + "</div></div>");
        }
    });
</script>
</body>
</html>