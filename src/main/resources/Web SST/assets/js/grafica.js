var myChart2;
var myChart1;


function cargarGrafica() {
    var ctx = document.getElementById('graf1').getContext('2d');
    myChart1= new Chart(ctx, {
        type: "bar",
        data: {
            labels: ["Lunes","Martes","Miercoles","Jueves","Viernes","Sábado","Domingo"],
            datasets: [{
                label: "Visitantes",
                backgroundColor: "blue",
                borderColor: "rgba(78, 115, 223, 1)",
                data: [0,50,100,200,300,400,500]}
            ] },
        options: {
            maintainAspectRatio: false,
            legend: {display: false},
            title:{display:false},
            scales:{
                xAxes:[{
                    gridLines:{
                        color:"rgb(234, 236, 244)",
                        zeroLineColor:"rgb(234, 236, 244)",
                        drawBorder: false,
                        drawTicks: false,
                        borderDash: [2],
                        zeroLineBorderDash:[2],
                        drawOnChartArea: false
                    },
                    ticks: {
                        fontColor:"#858796",
                        padding: 20
                    }

                }],
                yAxes:[{
                    gridLines: {
                        color:"rgb(234, 236, 244)",
                        zeroLineColor: "rgb(234, 236, 244)",
                        drawBorder: false,
                        drawTicks: false,
                        borderDash: [2],
                        zeroLineBorderDash: [2]
                    },
                    ticks:{
                        fontColor: "#858796",
                        padding: 20
                    }
                }]
            }

        }
    })

        var ctx = document.getElementById('graf2').getContext('2d');
    myChart2 = new Chart(ctx, {
        type: "pie",
        data: {
            labels:["Con Mascarilla", "Sin Mascarilla"],
            datasets:[{
                    label:"Valores",
                    backgroundColor:["#4e73df","#1cc88a"],
                    borderColor:["#ffffff","#ffffff"],
                    data: [100-prueba,prueba]
                }]
        },
        options: {
            maintainAspectRatio:false,
            legend: {
                display:false
            },
            title:"",
        }
    });







}

function actualizar() {

    var data= [100-prueba, prueba];
    myChart2.data.datasets[0].data= data;
    myChart2.update();
}

function modiGra1f(parametro) {
    console.log(parametro);

}

function modiGra2f(parametro){

    if(!parametro.localeCompare("Usarios con mascarillas vs sin mascarillas")){
        document.getElementById("titulo2").innerText="Cantidad de Usuarios destectados con y sin Mascarillas del Día";
        document.getElementById("legend1graf2").innerHTML="<i class=\"fas fa-circle text-primary\"></i>Con mascarilla";
        document.getElementById("legend2graf2").innerHTML="<i class=\"fas fa-circle text-success\"></i>Sin mascarilla";
        myChart2.data.labels= ["Con Mascarilla", "Sin Mascarilla"];
        myChart2.update();

    }
    if(!parametro.localeCompare("Usuarios hombres vs usarios mujeres")){
        document.getElementById("titulo2").innerText="Cantidad de usuarios hombres y mujeres del Día";
        document.getElementById("legend1graf2").innerHTML="<i class=\"fas fa-circle text-primary\"></i>Hombres";
        document.getElementById("legend2graf2").innerHTML="<i class=\"fas fa-circle text-success\"></i>Mujeres";
        myChart2.data.labels= ["Hombres", "Mujeres"];
        myChart2.update();
    }

}