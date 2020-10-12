var webSocket;
var tiempoReconectar = 5000;
var tiempoRemover = 600000;
var comprobar=0;
var prueba=0;
/*$(document).ready(function() {

    conectar();
});*/
/**
 *
 * @param mensaje
 */

function recibirInformacionServidor(mensaje){
     if(mensaje.data.startsWith("cont:")){
        var element = document.getElementById("cont");
       // element.setAttribute("value",mensaje.data.substring(5));
        element.innerText=  mensaje.data.substring(5);
        prueba=mensaje.data.substring(5);
        console.log(mensaje.data.substring(5));
       // console.log(document.getElementById("cvs").getAttributeNames());
        actualizar();
    }
    if(mensaje.data.startsWith("estado:")){
        var element = document.getElementById("estado");
        var text=mensaje.data.substring(7);
        element.innerText= text;
       // element.setAttribute("value",text);
        console.log(mensaje.data.substring(7));
        if(text ==="Encendido"){
            element.style.color= "green";
        }
        if(text ==="Apagado"){
            element.style.color= "red";
        }
    }
    if(mensaje.data.startsWith("nivel:")){
        console.log(mensaje.data);
        var element = document.getElementById("nivel");
        var text=mensaje.data.substring(6);
        element.style.width= text+"%";
        //element.setAttribute("value",mensaje.data.substring(5));
        document.getElementById("textnivel").innerText= text+"%";
    }



};
function conectar() {
    //Esto es para el WebSocket Secure para el HTTPS
    //webSocket= new WebSocket("wss://" + location.hostname + ":" + location.port + "/WSEnvio");
    //Esto es para el WebSocket para el HTTP
    webSocket= new WebSocket("ws://" + location.hostname + ":" + location.port + "/WSEnvio");

    webSocket.onmessage = function(data){recibirInformacionServidor(data); console.log("Recibido");}
    webSocket.onopen  = function(e){ console.log("Conectado - status "+this.readyState); };
    webSocket.onclose = function(e){
        console.log("Desconectado - status "+this.readyState);
    };

}
function verificarConexion(){
    if(!webSocket || webSocket.readyState == 3){
        conectar();
    }
}
/*function removerPrimerHijoNotificaciones() {
    if(comprobar>0) {
        //console.log("entro"+ new Date())
        var element = document.getElementById("notifi");
        try {
            if (element.firstChild.valueOf())
                element.removeChild(element.lastChild);
        } catch (err) {
            console.log("Aun no hay hijos")
        }
    }
}*/


setInterval(verificarConexion,tiempoReconectar);
//setInterval(removerPrimerHijoNotificaciones,tiempoRemover);

