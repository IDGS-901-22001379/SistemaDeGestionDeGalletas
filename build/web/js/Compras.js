// Cargar los proveedores
function cargarProveedores() {
    fetch('http://localhost:8080/DON_GALLETO_Ventas/api/compra/getAllProveedores')
            .then(response => response.json())
            .then(data => {
                const select = document.getElementById('proveedor');

                if (data && data.length > 0) {
                    data.forEach(proveedor => {
                        const option = document.createElement('option');
                        option.value = proveedor.id_proveedor;
                        option.textContent = proveedor.nombreProveedor;
                        select.appendChild(option);
                    });
                } else {
                    const option = document.createElement('option');
                    option.value = "";
                    option.textContent = "No se encontraron proveedores";
                    select.appendChild(option);
                }
            })
            .catch(error => {
                console.error('Error al cargar proveedores:', error);
                const select = document.getElementById('proveedor');
                const option = document.createElement('option');
                option.value = "";
                option.textContent = "Error al cargar proveedores";
                select.appendChild(option);
            });
}

window.onload = cargarProveedores;

//Cargar los insumos
function cargarInsumos(idProveedor) {
    fetch(`http://localhost:8080/DON_GALLETO_Ventas/api/compra/getAllInsumos/${idProveedor}`)
            .then(response => response.json())
            .then(data => {
                const selectInsumos = document.getElementById('insumocarrito');
                // Limpiar las opciones previas
                selectInsumos.innerHTML = '<option value="" selected disabled>Seleccione el insumo</option>';

                if (Array.isArray(data) && data.length > 0) {
                    data.forEach(insumo => {
                        const option = document.createElement('option');
                        option.value = insumo.id_insumo;
                        option.textContent = insumo.nombreInsumo;
                        selectInsumos.appendChild(option);
                    });
                } else {
                    const option = document.createElement('option');
                    option.value = "";
                    option.textContent = "No hay insumos disponibles para este proveedor";
                    selectInsumos.appendChild(option);
                }
            })
            .catch(error => {
                console.error('Error al cargar los insumos:', error);
            });
}

// Obtener el ID del proveedor
function obtenerProveedorSeleccionado() {
    const proveedorId = document.getElementById("proveedor").value;
    return {id: proveedorId};
}
// Enviar el carrito
async function enviarCarrito() {
    if (carrito.length === 0) {
        Swal.fire({
            title: "Carrito vacío",
            text: "Por favor, agregue artículos antes de comprar.",
            icon: "warning",
            confirmButtonText: "Aceptar"
        });
        return;
    }

    const proveedorSeleccionado = obtenerProveedorSeleccionado();
    const id_proveedor = proveedorSeleccionado ? proveedorSeleccionado.id : null;

    if (!id_proveedor) {
        Swal.fire({
            title: "Proveedor no seleccionado",
            text: "Por favor, seleccione un proveedor.",
            icon: "warning",
            confirmButtonText: "Aceptar"
        });
        return;
    }

    // Estructura del objeto
    const compra = {
        proveedor: {id_proveedor: id_proveedor},
        fecha: new Date().toISOString().split("T")[0],
        cantidad: carrito.reduce((sum, item) => sum + parseInt(item.cantidad), 0),
        peso: carrito.reduce((sum, item) => sum + parseFloat(item.peso || 0), 0),
        precio: carrito.reduce((sum, item) => sum + parseFloat(item.precio || 0), 0),
    };

    const insumosJson = JSON.stringify(carrito); // Convertir el carrito a JSON

    try {
        const response = await fetch("http://localhost:8080/DON_GALLETO_Ventas/api/compra/insertarCompra", {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded",
            },
            body: new URLSearchParams({
                compra: JSON.stringify(compra),
                insumosJson: insumosJson,
            }),
        });

        if (response.ok) {
            const result = await response.json();
            Swal.fire({
                title: "Éxito",
                text: result.result || "Compra realizada con éxito.",
                icon: "success",
                confirmButtonText: "Aceptar"
            });
            carrito = [];
            actualizarTabla();
            obtenerCompras();
        } else {
            const error = await response.json();
            Swal.fire({
                title: "Error",
                text: error.error || "Hubo un error al procesar la compra.",
                icon: "error",
                confirmButtonText: "Aceptar"
            });
        }
    } catch (error) {
        console.error("Error al enviar el carrito:", error);
        Swal.fire({
            title: "Error",
            text: "No se pudo enviar la compra. Por favor, intente nuevamente.",
            icon: "error",
            confirmButtonText: "Aceptar"
        });
    }
}

document.getElementById("btnComprar").addEventListener("click", enviarCarrito);


// /////////////////////////////////////////////////////////////////////////////
//Funcionalidad del modal
let carrito = [];
//Funcionalidad del modal
function guardar() {
    const insumo = document.getElementById('insumocarrito').options[document.getElementById('insumocarrito').selectedIndex].text;
    const proveedor = document.getElementById('proveedorcarrito').value;
    const cantidad = document.getElementById('cantidadcarrito').value;
    const precio = document.getElementById('preciocarrito').value;
    const total = document.getElementById('totalcarrito').value;
    const peso = document.getElementById('pesocarrito').value;
    const fecha = document.getElementById('fechacarrito').value;

    // Validar campos obligatorios
    if (!insumo || !proveedor || !cantidad || !precio || !total || !peso || !fecha) {
        Swal.fire({
            title: "Error",
            text: "Todos los campos son obligatorios. Por favor, complételos.",
            icon: "error",
            confirmButtonText: "Aceptar"
        });
        return;
    }

    // Mostrar en la tabla
    const nuevoItem = {
        insumo,
        proveedor,
        cantidad,
        precio,
        total,
        fecha,
        peso
    };
    carrito.push(nuevoItem);

    console.log("Carrito actualizado:", JSON.stringify(carrito, null, 2));

    // Limpiar el formulario
    limpiarCampos();

    // Actualizar la tabla
    actualizarTabla();
}

// Calcular el total
function calcularTotal() {
    const cantidad = parseFloat(document.getElementById('cantidadcarrito').value) || 0;
    const precio = parseFloat(document.getElementById('preciocarrito').value) || 0;
    const total = cantidad * precio;

    document.getElementById('totalcarrito').value = total.toFixed(2);
}

// /////////////////////////////////////////////////////////////////////////////

//Actualizar tabla
function actualizarTabla() {
    const tabla = document.querySelector('.table tbody');
    // Limpiar la tabla
    tabla.innerHTML = '';

    carrito.forEach((item, index) => {
        const fila = document.createElement('tr');
        fila.innerHTML = `
            <td>${item.insumo}</td>
            <td>${item.proveedor}</td>
            <td>${item.cantidad}</td>
            <td>${item.precio}</td>
            <td>
                <button class="btn-delete" onclick="eliminarItem(${index})">
                <img src="img/eliminnar.png" alt="Eliminar" style="width: 20px; height: 20px;" />
            </button>
            </td>
        `;
        tabla.appendChild(fila);
    });
}

// Función para eliminar un elemento del carrito
function eliminarItem(index) {
    carrito.splice(index, 1);

    // Actualizar la tabla
    actualizarTabla();
}

// /////////////////////////////////////////////////////////////////////////////

//Solo permite numeros
function validarNumeros(cantidadcarrito) {
    cantidadcarrito.value = cantidadcarrito.value.replace(/[^0-9]/g, '');
}
//Solo permite nummeros y pubto decimal
function validarNumero(pesocarrito) {
    pesocarrito.value = pesocarrito.value.replace(/[^0-9.]/g, '');

    if ((pesocarrito.value.match(/\./g) || []).length > 1) {
        pesocarrito.value = pesocarrito.value.replace(/\.(?=.*\.)/g, '');
    }
}
function validarNumer(preciocarrito) {
    preciocarrito.value = preciocarrito.value.replace(/[^0-9.]/g, '');

    if ((preciocarrito.value.match(/\./g) || []).length > 1) {
        preciocarrito.value = preciocarrito.value.replace(/\.(?=.*\.)/g, '');
    }
}

// /////////////////////////////////////////////////////////////////////////////

function limpiarCampos() {
    // Obtener referencias a los elementos del formulario
    var insumocarrito = document.getElementById('insumocarrito');
    var cantidadcarrito = document.getElementById('cantidadcarrito');
    var pesocarrito = document.getElementById('pesocarrito');
    var precciocarrito = document.getElementById('preciocarrito');
    var totalcarrito = document.getElementById('totalcarrito');
    var fecha = document.getElementById('fechacarrito');

    // Limpiar el valor de cada campo
    insumocarrito.value = '';
    cantidadcarrito.value = '';
    pesocarrito.value = '';
    precciocarrito.value = '';
    totalcarrito.value = '';
    fecha.value = new Date().toISOString().split('T')[0];
}

function obtenerCompras() {
    const url = 'http://localhost:8080/DON_GALLETO_Ventas/api/compra/getAll';

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener los datos de la API');
            }
            return response.json();  
        })
        .then(data => {
            mostrarComprasEnTabla(data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function mostrarComprasEnTabla(compras) {
    const tablaBody = document.querySelector('.tabla-ventas tbody'); 
    tablaBody.innerHTML = ''; 

    compras.forEach(compra => {
        const fila = document.createElement('tr'); 

        const celdaFecha = document.createElement('td');
        celdaFecha.textContent = compra.fecha; 
        fila.appendChild(celdaFecha);

        const celdaHora = document.createElement('td');
        celdaHora.textContent = compra.numeroOrden || 'No disponible'; 
        fila.appendChild(celdaHora);

        const celdaCosto = document.createElement('td');
        celdaCosto.textContent = "$" + compra.precio; 
        fila.appendChild(celdaCosto);

        const celdaEstatus = document.createElement('td');
        celdaEstatus.textContent = (compra.estatus === 0) ? 'Pendiente' : 'Entregado';
        fila.appendChild(celdaEstatus);

        fila.addEventListener('click', () => {
            mostrarDescripcionCompra(compra);
        });

        tablaBody.appendChild(fila);
    });

}

function mostrarDescripcionCompra(compra) {
    const descripcionContenedor = document.querySelector('.descripcion-compra p');
    
    // Realizar una solicitud a la API de detalleCompra
    fetch('http://localhost:8080/DON_GALLETO_Ventas/api/detalleCompra/getAll')
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener los detalles de la compra');
            }
            return response.json();
        })
        .then(detalles => {
            // Filtrar el detalle correspondiente a la compra seleccionada
            const detalleCompra = detalles.find(detalle => detalle.compra.id_comprasRealizadas === compra.id_comprasRealizadas);
            
            if (detalleCompra) {
                const descripcionJSON = JSON.parse(detalleCompra.descripcion);
                let detallesHTML = descripcionJSON.map(det => `
                    <li>
                        <strong>Insumo:</strong> ${det.insumo} <br>
                        <strong>Unidad:</strong> ${det.peso}<br>
                        <strong>Precio Unitario:</strong> $${det.precio} <br>
                        <strong>Cantidad:</strong> ${det.cantidad} <br>
                        <strong>Total:</strong> $${det.total_precio} <br>
                    </li>
                `).join('');

                descripcionContenedor.innerHTML = `
                    <ul>${detallesHTML}</ul>
                `;
            } else {
                descripcionContenedor.textContent = 'No se encontraron detalles para esta compra.';
            }
        })
        .catch(error => {
            console.error('Error:', error);
            descripcionContenedor.textContent = 'Error al obtener los detalles de la compra.';
        });
}



document.addEventListener('DOMContentLoaded', function() {
    obtenerCompras();  
});

function abrirModalVENTAS() {
    const modal = document.getElementById("modalVENTAS");
    modal.style.display = "flex"; 
    obtenerCompras();  
}

function cerrarModalVENTAS() {
    const modal = document.getElementById("modalVENTAS");
    modal.style.display = "none"; 
}


// /////////////////////////////////////////////////////////////////////////////

//Funcion del modal abrir y cerrar
function abrirModalCompras() {
    const proveedorID = document.getElementById("proveedor").value;

    if (proveedorID) {
        document.getElementById('modalCarrito').style.display = 'flex';
        const proveedorNombre = document.querySelector("#proveedor option:checked").text;
        document.getElementById('proveedorcarrito').value = proveedorNombre;
        cargarInsumos(proveedorID);
        // Obtener la fecha actual en YYYY-MM-DD
        const fechaActual = new Date();
        const fechaFormateada = fechaActual.toISOString().split('T')[0]; // Formatea a YYYY-MM-DD
        document.getElementById('fechacarrito').value = fechaFormateada; // Establece la fecha en el campo
    }
}

function cerrarModalCompras() {
    document.getElementById('modalCarrito').style.display = 'none';
}
// Detectar el cambio en el select
document.getElementById('proveedor').addEventListener('change', function () {
    var proveedorSeleccionado = this.value;
    // Abrir si hay un proveedor seleccionado (ID mayor a 0)
    if (proveedorSeleccionado) {
        abrirModalCompras();
    }
});

function abrirModal() {
    document.getElementById('modal').style.display = 'flex';
}


function cerrarModal() {
    document.getElementById('modal').style.display = 'none';
}


function abrirModalPROVEEDOR() {
    document.getElementById('modalVENTAS').style.display = 'flex'; // Cambié 'modalPROVEEDOR' a 'modalVENTAS'
}

function cerrarModalPROVEEDOR() {
    document.getElementById('modalVENTAS').style.display = 'none'; // Lo mismo aquí
}


function redireccionar(url) {
    window.location.href = "index.html";
}

function agregarProveedor(event) {
    event.preventDefault();  // Prevenir el comportamiento por defecto del formulario

    // Obtener el valor del campo "proveedor" usando el nuevo id
    var nombreProveedor = document.getElementById('nombreProveedorInput').value.trim();
    
    console.log("Valor de 'nombreProveedor' capturado:", nombreProveedor);  // Verificar valor antes de continuar

    // Verificar si el campo está vacío
    if (!nombreProveedor) {
        console.log("El nombre del proveedor está vacío.");
        
        // Reemplazar alert() por SweetAlert
        Swal.fire({
            icon: 'error',
            title: 'Error',
            text: 'El nombre del proveedor no puede estar vacío.'
        });

        return; // Detener el envío si el campo está vacío
    }

    // Crear el objeto proveedor para enviar a la API
    var proveedor = {
        nombreProveedor: nombreProveedor
    };

    console.log("Objeto proveedor enviado:", proveedor);  // Verificar el objeto

    // Crear la petición AJAX
    var xhr = new XMLHttpRequest();
    xhr.open("POST", 'http://localhost:8080/DON_GALLETO_Ventas/api/proveedor/add', true);
    xhr.setRequestHeader("Content-Type", "application/json");

    xhr.onload = function() {
        if (xhr.status === 200) {
            // Reemplazar alert() por SweetAlert
            Swal.fire({
                icon: 'success',
                title: 'Proveedor Agregado',
                text: 'Proveedor agregado con éxito: ' + nombreProveedor
            });

            cerrarModal();  // Cerrar el modal tras la inserción exitosa
        } else {
            console.log("Error al agregar proveedor. Estado:", xhr.status);  // Mostrar código de error
            
            // Reemplazar alert() por SweetAlert
            Swal.fire({
                icon: 'error',
                title: 'Error',
                text: 'Hubo un error al agregar el proveedor.'
            });
        }
    };

    // Enviar el objeto proveedor como JSON
    xhr.send(JSON.stringify(proveedor));
}
