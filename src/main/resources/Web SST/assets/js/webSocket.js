var webSocket;
var tiempoReconectar = 5000;
var tiempoRemover = 600000;
var comprobar=0;
var prueba=0;
var conMas=0;
var sinMas=0;
var datadias= [0,0,0,0,0,0,0];
var usuario="NO Usuario";




/**
 *
 * @param mensaje
 */

function enviarinfoServer(data){
    webSocket.send(data);
}


function recibirInformacionServidor(mensaje){
    if(mensaje.data.startsWith("usuario:")){

        var element = document.getElementById("usuario");
        var u=mensaje.data.substring(8);
        if(u==="null"){
            element.innerText=  "No User";
            usuario="NO USER";
            console.log("no validado");
        }else{
            element.innerText=  u;
            usuario=u;
            console.log(usuario);
            console.log("validado");

        }
        console.log(mensaje.data.substring(8));
    }
    if(mensaje.data.startsWith("contGeneral:")){
        //prueba=mensaje.data.substring(5);

        var element = document.getElementById("contGeneral");
        element.innerText=  mensaje.data.substring(12);
        console.log(mensaje.data.substring(12));
        // console.log(document.getElementById("cvs").getAttributeNames());

    }
     if(mensaje.data.startsWith("cont:")){
         //prueba=mensaje.data.substring(5);

        var element = document.getElementById("cont");
       // element.setAttribute("value",mensaje.data.substring(5));
        element.innerText=  mensaje.data.substring(5);
        console.log(mensaje.data.substring(5));
       // console.log(document.getElementById("cvs").getAttributeNames());

    }
    if(mensaje.data.startsWith("conMasc:")){
        conMas = mensaje.data.substring(8);
        console.log(conMas);
        actualizar();
    }
    if(mensaje.data.startsWith("sinMasc:")){
        sinMas = mensaje.data.substring(8);
        console.log(sinMas);
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
        if(text<10){
            //alert("Favor verificar el nivel de líquido, estado:"+text+"%");
            //bootbox.alert("Hello world!");
            Swal.fire({
                position: 'top-end',
                icon: 'warning',
                title: "Favor verificar el nivel de líquido, estado:"+text+"%",
                showConfirmButton: false,
                timer: 5000
            })
        }
        element.style.width= text+"%";
        //element.setAttribute("value",mensaje.data.substring(5));
        document.getElementById("textnivel").innerText= text+"%";
    }
    if(mensaje.data.startsWith("lu:")){
        datadias[0]= mensaje.data.substring(3);
        actualizarGraf1();
    }
    if(mensaje.data.startsWith("ma:")){
        datadias[1]= mensaje.data.substring(3);
        actualizarGraf1();
    }
    if(mensaje.data.startsWith("mi:")){
        datadias[2]= mensaje.data.substring(3);
        actualizarGraf1();
    }
    if(mensaje.data.startsWith("ju:")){
        datadias[3]= mensaje.data.substring(3);
        actualizarGraf1();
    }
    if(mensaje.data.startsWith("vi:")){
        datadias[4]= mensaje.data.substring(3);
        actualizarGraf1();
    }
    if(mensaje.data.startsWith("sa:")){
        datadias[5]= mensaje.data.substring(3);
        actualizarGraf1();
    }
    if(mensaje.data.startsWith("dom:")){
        datadias[6]= mensaje.data.substring(4);
        console.log(datadias[7]);
        actualizarGraf1();
    }
    if(mensaje.data.startsWith("error")){
            console.log("error")
            Swal.fire({
                icon: 'error',
                title: "Usurio y/o contraseña incorrectos",
                showConfirmButton: false
            })
        }

};
function conectar() {
    //Esto es para el WebSocket Secure para el HTTPS
    webSocket= new WebSocket("wss://" + location.hostname + ":" + location.port + "/WSEnvio");
    Esto es para el WebSocket para el HTTP
    //webSocket= new WebSocket("ws://" + location.hostname + ":" + location.port + "/WSEnvio");

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

