$( document ).ready(function() {

  // SUBMIT FORM
    $("#smsForm").submit(function(event) {
    // Prevent the form from submitting via the browser.
    event.preventDefault();
    ajaxPost();
  });


    function ajaxPost(){

      // PREPARE FORM DATA
      var formData = {
        number : $("#number").val(),
        message : $("#message").val(),
        sender :  $("#sender").val()
      }

      // DO POST
      $.ajax({
      type : "POST",
      contentType : "application/json",
      url : "https://app.rawinet.pl/smsgate/new",
      data : JSON.stringify(formData),
      dataType : 'json',
      success : function(result) {
        if(result.status == "OK"){
          $("#postResultDiv").html("<p style='background-color:#7FA7B0; color:white; padding:5px 5px 5px 5px'>" +
                        "Wysłano wiadomość! <br>" +
                        "Numer: " +
                        result.data.number + "<br>Treść: " + result.data.message + "</p>");
        }else{
          $("#postResultDiv").html("<strong>Error</strong>");
        }
        console.log(result);
      },
      error : function(e) {
        alert("Error!")
        console.log("ERROR: ", e);
      }
    });

      // Reset FormData after Posting
      resetData();
      $('#smsModal').modal('hide');

    }

    function resetData(){
      $("#message").val("");
    }
})