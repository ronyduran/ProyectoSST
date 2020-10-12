var myChart2;
var myChart1;


function cargarGrafica() {
    var ctx = document.getElementById('graf2').getContext('2d');
    myChart1 = new Chart(ctx, {
        type: "pie",
        data: {
            labels:["Sin Mascarilla", "Con Mascarilla"],
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

    var ctx = document.getElementById('graf1').getContext('2d');
    myChart2= new Chart(ctx, {
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



}

function actualizar() {

    var data= [100-prueba, prueba];
    myChart1.data.datasets[0].data= data;
    myChart1.update();
}