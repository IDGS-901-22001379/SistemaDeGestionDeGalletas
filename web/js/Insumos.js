let insumos;
//Alertas
function cargarAlertas() {
    const apiUrl = "http://localhost:8080/DON_GALLETO_Ventas/api/insumos/alertas";

    fetch(apiUrl)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error ${response.status}: ${response.statusText}`);
            }
            return response.json();
        })
        .then(data => {
            console.log("Datos recibidos de las alertas:", data);

            const alertasDiv = document.getElementById("alertas");

            alertasDiv.innerHTML = "";

            if (Array.isArray(data)) {
                data.forEach(alerta => {
                    const alertaElemento = document.createElement("p");

                    alertaElemento.textContent = alerta.mensajeAlerta;

                    if (alerta.mensajeAlerta.includes("está a punto de acabarse")) {
                        alertaElemento.style.background = "yellow";
                    } else if (alerta.mensajeAlerta.includes("tiene que resurtirse")) {
                        alertaElemento.style.background = "red";
                        alertaElemento.style.color = "white";
                    }

                    alertasDiv.appendChild(alertaElemento);
                });
            } else if (data.message) {
                const mensaje = document.createElement("p");
                mensaje.textContent = data.message;
                mensaje.style.color = "gray";
                alertasDiv.appendChild(mensaje);
            } else {
                const errorMensaje = document.createElement("p");
                errorMensaje.textContent = "Formato de respuesta desconocido.";
                errorMensaje.style.color = "gray";
                alertasDiv.appendChild(errorMensaje);
            }
        })
        .catch(error => {
            console.error("Error al obtener las alertas:", error);
            alert("Ocurrió un error al cargar las alertas.");
        });
}


// Cargar todos los insumos
function cargarCatInsumos() {
    fetch("http://localhost:8080/DON_GALLETO_Ventas/api/insumos/getAllInsumos")
        .then(response => response.json())
        .then(response => {
            let mostrar = "";
            insumos = response;
            console.log("Insumos cargados desde la API:", insumos);

            for (var i = 0; i < response.length; i++) {
                let fecha = new Date(response[i].fecha);

                if (isNaN(fecha)) {
                    fecha = new Date();
                }

                mostrar += '<tr>';
                mostrar += '<td>' + response[i].nombreInsumo + '</td>';
                mostrar += '<td>' + response[i].unidad + '</td>';
                mostrar += '<td>' + response[i].cantidad + '</td>';
                mostrar += '<td>' + response[i].total + '</td>';
                mostrar += '<td>' + response[i].fecha + '</td>';
                mostrar += '<td> <button class="boton-tabla" onclick="modificarInsumo(' + i + ');"><img src="img/borrarmerma.png" alt="eliminar" style="width: 20px; height: 20px;"/></td>';
                mostrar += '</tr>';
            }
            document.getElementById("tblInsumos").innerHTML = mostrar;
        })
        .catch(error => {
            console.error("Error al cargar los insumos:", error);
        });
}

window.onload = function() {
    // Cargar alertas e insumos por separado
    cargarCatInsumos();
    setTimeout(cargarAlertas, 1000);
};


//Merma
function enviarMerma() {
    const nombreInsumo = document.getElementById("insumomerma").value;

    const idInsumo = document.getElementById("insumomerma").getAttribute("data-id");

    const cantidadMerma = document.getElementById("cantidadmerma").value;

    if (!idInsumo || isNaN(cantidadMerma) || cantidadMerma <= 0) {
        Swal.fire({
            title: "Advertencia",
            text: "Por favor, ingrese valores válidos para el insumo y la cantidad de merma.",
            icon: "warning",
            confirmButtonText: "Aceptar"
        });
        return;
    }

    fetch("http://localhost:8080/DON_GALLETO_Ventas/api/insumos/mermaInsumos", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded"
        },
        body: new URLSearchParams({
            "idInsumo": idInsumo,
            "cantidadMerma": cantidadMerma
        })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error ${response.status}: ${response.statusText}`);
        }
        return response.json();
    })
    .then(data => {
        if (data.message) {
            Swal.fire({
                title: "Éxito",
                text: data.message,
                icon: "success",
                confirmButtonText: "Aceptar"
            });
            cerrarModal(); // Llama a la función para cerrar el modal
            cargarCatInsumos(); // Recarga el catálogo de insumos
        } else if (data.error) {
            Swal.fire({
                title: "Error",
                text: data.error,
                icon: "error",
                confirmButtonText: "Aceptar"
            });
        }
    })
    .catch(error => {
        console.error("Error al enviar la merma:", error);
        Swal.fire({
            title: "Error",
            text: "Se produjo un error al enviar la merma. Por favor, intente nuevamente.",
            icon: "error",
            confirmButtonText: "Aceptar"
        });
    });
}

////Buscar orden de compra
//function buscarDescripcionCompra(event) {
//    event.preventDefault();  // Previene la recarga de la página
//
//    // Obtener el número de orden desde el input
//    const numeroOrden = document.getElementById('ordenCompra').value.trim();
//
//    if (numeroOrden === "") {
//        // Si no se ingresa un número de orden, muestra un mensaje en el textarea
//        document.getElementById('insumosAgregar').value = "Por favor ingresa un número de orden válido.";
//        return;
//    }
//
//    // Construir la URL de la API REST
//    const url = `http://localhost:8080/DON_GALLETO_Ventas/api/insumos/numeroOrden?numeroOrden=${numeroOrden}`;
//
//    // Realizar la solicitud GET usando fetch
//    fetch(url)
//        .then(response => response.text())  // Usamos 'text()' para ver exactamente lo que estamos recibiendo
//        .then(data => {
//            console.log("Respuesta cruda del servidor:", data);  // Verifica lo que recibimos antes de parsear
//
//            try {
//                // Primero parseamos el JSON principal
//                const parsedData = JSON.parse(data);
//                console.log("Objeto JSON parseado:", parsedData);  // Verifica el objeto parseado
//
//                // Ahora parseamos la cadena contenida en "descripcion"
//                const descripcionData = JSON.parse(parsedData.descripcion);
//                console.log("Objeto JSON parseado de 'descripcion':", descripcionData);
//
//                // Asegurarnos de que 'descripcion' es un array
//                if (descripcionData && Array.isArray(descripcionData)) {
//                    let insumosTexto = "Descripción de la compra:\n";
//                    descripcionData.forEach(item => {
//                        insumosTexto += `Insumo: ${item.insumo}, Precio: ${item.precio}, Cantidad: ${item.cantidad}, Total: ${item.total_precio}\n`;
//                    });
//                    document.getElementById('insumosAgregar').value = insumosTexto;
//                } else {
//                    document.getElementById('insumosAgregar').value = "No se encontró la descripción válida.";
//                }
//            } catch (error) {
//                console.error("Error al procesar el JSON:", error);  // Mostrar el error de parseo
//                document.getElementById('insumosAgregar').value = `Error al procesar el JSON: ${error.message}`;
//            }
//        })
//        .catch(error => {
//            // Manejar cualquier error de la solicitud
//            console.error("Error de red:", error);  // Mostrar el error de red en consola
//            document.getElementById('insumosAgregar').value = `Error de red: ${error.message}`;
//        });
//}
//
//// Asegurarse de que el DOM esté completamente cargado antes de agregar el evento
//document.addEventListener('DOMContentLoaded', function() {
//    // Añadir el evento de clic al botón
//    document.getElementById('buscarBtn').addEventListener('click', buscarDescripcionCompra);
//});



function redireccionar(url) {
    window.location.href = "index.html";
}



//Agregar ya los insumos
function actualizarEstatusCompra(event) {
    if (!event) {
        event = window.event;  // Solo para navegadores antiguos
    }
    // Evita que la página se recargue si es un formulario
    event.preventDefault();

    const numeroOrden = document.getElementById('ordenCompra').value.trim();

    if (numeroOrden === "") {
        Swal.fire({
            icon: 'warning',
            title: 'Por favor ingresa un número de orden válido.',
            confirmButtonText: 'OK'
        });
        return;
    }

    const url = "http://localhost:8080/DON_GALLETO_Ventas/api/insumos/actualizarInsumos";

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        },
        body: `numeroOrden=${encodeURIComponent(numeroOrden)}`  // Datos enviados en el cuerpo de la solicitud
    })
    .then(response => response.json())
    .then(data => {
        if (data.estatus === 1) {
            Swal.fire({
                icon: 'info',
                title: 'La compra ya está registrada.',
                confirmButtonText: 'OK'
            });
        } else if (data.estatus === 0) {
            Swal.fire({
                icon: 'success',
                title: 'La compra puede ser registrada.',
                confirmButtonText: 'OK'
            }).then(() => {
                limpiarCampos();  // Limpia los campos
                cargarCatInsumos();
                });
        } else if (data.message) {
            Swal.fire({
                icon: 'success',
                title: data.message,
                confirmButtonText: 'OK'
            }).then(() => {
                limpiarCampos();  // Limpia los campos
                cargarCatInsumos();  // Carga los insumos
            });
        } else if (data.error) {
            Swal.fire({
                icon: 'error',
                title: data.error,
                confirmButtonText: 'OK'
            });
        }
    })
    .catch(error => {
        console.error("Error de red:", error);
        Swal.fire({
            icon: 'error',
            title: `Error de red: ${error.message}`,
            confirmButtonText: 'OK'
        });
    });
}
// /////////////////////////////////////////////////////////////////////////////
//Modales merma

function modificarInsumo(i) {
    let insumoSeleccionado = insumos[i];

    document.getElementById("insumomerma").value = insumoSeleccionado.nombreInsumo;

    document.getElementById("insumomerma").setAttribute("data-id", insumoSeleccionado.id_insumo);

    document.getElementById("modalMerma").style.display = "flex";

    const fechaActual = new Date();
    const fechaFormateada = fechaActual.toISOString().split('T')[0];
    document.getElementById('fechamerma').value = fechaFormateada; 
}

function cerrarModal() {
    document.getElementById("modalMerma").style.display = "none";
    limpiarCampo();
}
//modal agregar
function Agregarinsumo() {

    document.getElementById("modalAgregar").style.display = "flex";

    const fechaActual = new Date();
    const fechaFormateada = fechaActual.toISOString().split('T')[0];
    document.getElementById('fechaagregar').value = fechaFormateada; 
}

function cerrarModalAgregar() {
    document.getElementById("modalAgregar").style.display = "none";
    limpiarCampos();
}


function limpiarCampos() {
    // Obtener referencias a los elementos del formulario
    var ordenCompra = document.getElementById('ordenCompra');
    var insumosAgregar = document.getElementById('insumosAgregar');
    var  fechaagregar= document.getElementById('fechaagregar');

    // Limpiar el valor de cada campo
    ordenCompra.value = '';
    insumosAgregar.value = '';
    fechaagregar.value = new Date().toISOString().split('T')[0];
}

function limpiarCampo() {
    // Obtener referencias a los elementos del formulario
    var insumomerma = document.getElementById('insumomerma');
    var descripccionerma = document.getElementById('descripccionerma');
    var cantidadmerma = document.getElementById('cantidadmerma');
    var  fechamerma= document.getElementById('fechamerma');

    // Limpiar el valor de cada campo
    insumomerma.value = '';
    descripccionerma.value = '';
    cantidadmerma.value = '';
    fechamerma.value = new Date().toISOString().split('T')[0];
}

function validarNumero(cantidadmerma) {
    cantidadmerma.value = cantidadmerma.value.replace(/[^0-9.]/g, '');

    if ((cantidadmerma.value.match(/\./g) || []).length > 1) {
        cantidadmerma.value = cantidadmerma.value.replace(/\.(?=.*\.)/g, '');
    }
}