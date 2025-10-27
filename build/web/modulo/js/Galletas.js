// Archivo: Galletas.js

// Inicialización al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    inicializar();
});

// Función de inicialización
function inicializar() {
    if (!configurarEventos()) {
        console.error("Error al configurar eventos: algunos elementos no existen en el DOM.");
        return;
    }
    mostrarTabla("unidad");
    cargarProductos();
}

// Configuración de eventos de las pestañas
function configurarEventos() {
    const unidadTab = document.getElementById("unidad-tab");
    const cajaUnKiloTab = document.getElementById("caja-un-kilo-tab");
    const cajaMedioKiloTab = document.getElementById("caja-medio-kilo-tab");

    if (!unidadTab || !cajaUnKiloTab || !cajaMedioKiloTab) {
        console.error("Error: Uno o más elementos del menú no existen.");
        return false;
    }

    unidadTab.addEventListener("click", () => mostrarTabla("unidad"));
    cajaUnKiloTab.addEventListener("click", () => mostrarTabla("caja-un-kilo"));
    cajaMedioKiloTab.addEventListener("click", () => mostrarTabla("caja-medio-kilo"));
    return true;
}

// Mostrar la tabla seleccionada y ocultar las demás
function mostrarTabla(tabla) {
    const unidadTabla = document.getElementById("unidad-tabla");
    const cajaUnKiloTabla = document.getElementById("caja-un-kilo-tabla");
    const cajaMedioKiloTabla = document.getElementById("caja-medio-kilo-tabla");

    unidadTabla.style.display = "none";
    cajaUnKiloTabla.style.display = "none";
    cajaMedioKiloTabla.style.display = "none";

    document.getElementById("unidad-tab").classList.remove("active");
    document.getElementById("caja-un-kilo-tab").classList.remove("active");
    document.getElementById("caja-medio-kilo-tab").classList.remove("active");

    const tablaSeleccionada = document.getElementById(`${tabla}-tabla`);
    if (tablaSeleccionada) {
        tablaSeleccionada.style.display = "block";
        document.getElementById(`${tabla}-tab`).classList.add("active");
    } else {
        console.error(`Error: Tabla '${tabla}' no encontrada.`);
    }
}

// Cargar productos desde el backend
async function cargarProductos() {
    try {
        // Hacer las solicitudes a ambos endpoints
        const galletasResponse = await fetch("http://localhost:8080/DonGalleto/api/galletas/getAll");
        const inventarioResponse = await fetch("http://localhost:8080/DonGalleto/api/inventario/obtener");

        if (!galletasResponse.ok || !inventarioResponse.ok) {
            throw new Error("Error al cargar los productos o el inventario.");
        }

        // Obtener los productos y el inventario
        const galletas = await galletasResponse.json();
        const inventario = await inventarioResponse.json();

        // Combinar los productos con la cantidad del inventario
        const productosConCantidad = combinarDatos(galletas, inventario);

        // Llenar las tablas con los datos combinados
        llenarTablas(productosConCantidad);
    } catch (error) {
        console.error("Error al cargar productos:", error);
        mostrarAlertas("No se pudieron cargar los productos. Intente más tarde.", "error");
    }
}

// Función para combinar los datos de galletas e inventario basados en el nombre de la galleta o el ID
function combinarDatos(galletas, inventario) {
    return galletas.map(galleta => {
        // Buscar la cantidad correspondiente en el inventario
        const inventarioProducto = inventario.find(item => item.galletaId === galleta.idGalleta);

        // Si se encuentra el producto en el inventario, asignar la cantidad, de lo contrario, asignar 0
        const cantidad = inventarioProducto ? inventarioProducto.cantidad : 0;

        // Retornar el producto con la cantidad incluida
        return {
            ...galleta,
            cantidad: cantidad
        };
    });
}


// Llenar las tablas con los datos
function llenarTablas(productos) {
    const tablaUnidad = document.getElementById("productoTableBodyUnidad");
    const tablaCajaUnKilo = document.getElementById("productoTableBodyCajaUnKilo");
    const tablaCajaMedioKilo = document.getElementById("productoTableBodyCajaMedioKilo");

    tablaUnidad.innerHTML = "";
    tablaCajaUnKilo.innerHTML = "";
    tablaCajaMedioKilo.innerHTML = "";

    if (!productos || productos.length === 0) {
        mostrarAlertas("No hay productos disponibles.", "info");
        return;
    }

    productos.forEach(producto => {
        const fila = `
            <tr>
                <td>${producto.tipo || "N/A"}</td>
                <td>${producto.galleta || "N/A"}</td>
                <td>${producto.costo || "N/A"}</td>
                <td>${producto.existencia || "N/A"}</td>
                <td>
                    ${producto.tipo === "CAJA" || producto.tipo === "MEDIA_CAJA" ? 
                    `<button class="btn btn-success btn-sm" onclick="mostrarModalAgregar('${producto.galleta}')">Agregar</button>` : ""}
                    <button class="btn btn-warning btn-sm" onclick="editarProducto(${producto.idGalleta})">Editar</button>
                    <button class="btn btn-danger btn-sm" onclick="eliminarProducto(${producto.idGalleta})">Eliminar</button>
                </td>
            </tr>
        `;

        if (producto.tipo === "UNIDAD") {
            tablaUnidad.innerHTML += fila;
        } else if (producto.tipo === "CAJA") {
            tablaCajaUnKilo.innerHTML += fila;
        } else if (producto.tipo === "MEDIA_CAJA") {
            tablaCajaMedioKilo.innerHTML += fila;
        }
    });
}

// Mostrar el modal para agregar
function mostrarModalAgregar(galleta) {
    const modal = new bootstrap.Modal(document.getElementById("modalAgregarGalleta"));
    document.getElementById("selectGalleta").value = galleta;
    modal.show();
}

// Guardar inventario
function guardarInventario() {
    const galleta = document.getElementById("selectGalleta").value;
    const cantidad = document.getElementById("inputCantidad").value;

    if (!galleta || !cantidad) {
        mostrarAlertas("Todos los campos son obligatorios.", "warning");
        return;
    }

    const nuevoInventario = { galleta, cantidad: parseInt(cantidad) };

    fetch("http://localhost:8080/DonGalleto/api/inventario/insert", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(nuevoInventario)
    })
        .then(response => response.json())
        .then(data => {
            if (data.error) {
                mostrarAlertas(data.error, "error");
            } else {
                mostrarAlertas("Inventario actualizado correctamente.", "success");
                cargarProductos();
                const modal = bootstrap.Modal.getInstance(document.getElementById("modalAgregarGalleta"));
                modal.hide();
            }
        })
        .catch(error => {
            console.error("Error al guardar inventario:", error);
            mostrarAlertas("Error al guardar el inventario. Intente más tarde.", "error");
        });
}


function guardarProducto() {
    // Obtén los valores del formulario
    const tipo = document.getElementById('selectTipo').value;
    const galleta = document.getElementById('selectGalleta').value;
    const cantidad = document.getElementById('inputCantidad').value;
    const fecha = document.getElementById('inputFecha').value;
    const costo = document.getElementById('inputCosto').value;  // Captura el costo

    // Asignar hora automática
    const hora = new Date().toISOString().split('T')[1].slice(0, 8);  // Obtener la hora actual en formato HH:MM:SS

    // Validar que todos los campos tengan valores válidos
    if (!tipo || !galleta || !cantidad || !fecha || !costo) {
        alert('Por favor, asegúrese de que todos los campos estén completos.');
        return;
    }

    // Construir el objeto para enviar al servidor
    const data = {
        tipo: tipo,
        galleta: galleta,
        cantidad: parseInt(cantidad, 10),
        fecha: fecha,
        costo: parseFloat(costo),  // Asegúrate de que el costo se pase correctamente
        hora: hora  // Se coloca la hora automática
    };

    // Verifica los datos antes de enviarlos
    console.log("Datos a enviar:", data);

    // Realiza la llamada al servidor
    fetch('http://localhost:8080/DonGalleto/api/galletas/insert', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al guardar el producto');
            }
            return response.json();
        })
        .then(result => {
            console.log('Producto guardado exitosamente:', result);
            alert('Producto guardado exitosamente');
            // Recargar o actualizar la lista de productos
            cargarProductos();  // Asegúrate de que esta función esté definida y recargue la tabla correctamente
            // Cerrar el modal después de guardar
            const modal = bootstrap.Modal.getInstance(document.getElementById("modalAgregarGalleta"));
            modal.hide();
        })
        .catch(error => {
            console.error('Error al guardar el producto:', error);
            alert('Error al guardar el producto');
        });
}






// Mostrar alertas dinámicas
function mostrarAlertas(mensaje, tipo) {
    const alertas = document.getElementById("alertas");
    alertas.style.display = "block";
    alertas.innerHTML = `<div class="alert alert-${tipo}">${mensaje}</div>`;
    setTimeout(() => {
        alertas.style.display = "none";
        alertas.innerHTML = "";
    }, 5000);
}

// Función para editar un producto (placeholder)
function editarProducto(idGalleta) {
    alert(`Editar producto con ID: ${idGalleta}`);
}

// Función para eliminar un producto (placeholder)
function eliminarProducto(idGalleta) {
    alert(`Eliminar producto con ID: ${idGalleta}`);
}



 // Función para cargar las galletas desde el backend
   function cargarGalletas() {
    fetch('http://localhost:8080/DonGalleto/api/galletas/getAll') // Ajusta esta URL si es necesario
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al obtener las galletas');
            }
            return response.json();
        })
        .then(data => {
            const selectGalleta = document.getElementById('selectGalleta');
            selectGalleta.innerHTML = ''; // Limpia las opciones previas

            data.forEach(galleta => {
                const option = document.createElement('option');
                option.value = galleta.idGalleta; // El ID de la galleta
                option.textContent = galleta.galleta; // El nombre de la galleta
                selectGalleta.appendChild(option);
            });
        })
        .catch(error => {
            console.error('Error al cargar las galletas:', error);
        });
}

// Llama a cargarGalletas cuando se abra el modal
document.getElementById('modalAgregarGalleta').addEventListener('shown.bs.modal', cargarGalletas);
