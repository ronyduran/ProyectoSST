/*window.onload = function() {

    cargarGrafica();
    
    }*/

    function cargarGrafica() {
        var options = {
            exportEnabled: true,
            animationEnabled: true,
            title:{
                text: "Gráfica de Usuarios con Mascarillas vs Usuarios sin Mascarillas"
            },
            legend:{
                position: "bottom"
            },
            data: [{
                type: "pie",
                showInLegend: true,
                toolTipContent: "<b>{name}</b>: ${y} (#percent%)",
                indexLabel: "{name}",
                legendText: "{name} (#percent%)",
                //indexLabelPlacement: "inside",
                dataPoints: [
                    { y: conMascarilla, name: "Con Mascarillas" },
                    { y: sinMascarilla, name: "Sin Mascarilla" }
                ]
            }]
        };
        $("#chartContainer").CanvasJSChart(options);
    }

    function ActualizarDatos(){
        var options = {
            exportEnabled: true,
           // animationEnabled: true,
            title:{
                text: "Gráfica de Usuarios con Mascarillas vs Usuarios sin Mascarillas"
            },
            legend:{
                position: "bottom"
            },
            data: [{
                type: "pie",
                showInLegend: true,
                toolTipContent: "<b>{name}</b>: ${y} (#percent%)",
                indexLabel: "{name}",
                legendText: "{name} (#percent%)",
                //indexLabelPlacement: "inside",
                dataPoints: [
                    { y: conMascarilla, name: "Con Mascarillas" },
                    { y: sinMascarilla, name: "Sin Mascarilla" }
                ]
            }]
        };
        $("#chartContainer").CanvasJSChart(options);
    };

setInterval(ActualizarDatos,2000);
