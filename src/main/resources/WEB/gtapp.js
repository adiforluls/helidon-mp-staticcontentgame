function userRegister(){

    var usr = document.getElementById("newusername").value;

    var xhr = new XMLHttpRequest();

    xhr.open("POST", "/game/user", true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onload = function(){

        var msg = JSON.parse(xhr.responseText);

        if(xhr.status == 202){
            /*iframe.contentDocument.body.innerHTML=msg["success"];*/
            document.getElementById("status1").innerHTML=msg["success"];
        }
        else {
            /*iframe.contentDocument.body.innerHTML=msg["error"];*/
            document.getElementById("status1").innerHTML=msg["error"];
        }
    }

  /*iframe.onload = function()
    {
        console.log(iframe.contentDocument.body.innerHTML);
    }*/

     xhr.send(JSON.stringify({username: usr}));
}

function userGuess(){

    var usr = document.getElementById("regusername").value;
    var gus =  document.getElementById("guess").value;

    var xhr = new XMLHttpRequest();

    xhr.open("PUT", "/game", true);
    xhr.setRequestHeader('Content-Type', 'application/json');

    xhr.onload = function(){

        var msg = JSON.parse(xhr.responseText);

        if(xhr.status == 202){
          //  iframe.contentDocument.body.innerHTML=msg["success"];
          document.getElementById("status2").innerHTML=msg["success"];
        }
        else {
           // iframe.contentDocument.body.innerHTML=msg["error"];
           document.getElementById("status2").innerHTML=msg["error"];
        }
    }
   /* iframe.onload = function()
    {
        console.log(iframe.contentDocument.body.innerHTML);
    }
    */
    xhr.send(JSON.stringify({username: usr, guess: gus}));
}

function getWinner(){

    var xhr =  new XMLHttpRequest();
    xhr.open("GET", "/game/winner", true);

    xhr.onload = function(){

        var msg = JSON.parse(xhr.responseText);
        
        if(xhr.status == 202){
            document.getElementById("win").innerHTML = msg["Winner"];
        }

    }

    xhr.send();
}
