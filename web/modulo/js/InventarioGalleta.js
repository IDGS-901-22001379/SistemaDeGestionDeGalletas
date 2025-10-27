// Archivo: ../js/InventarioGalleta.js

// Función para abrir el formulario
function abrirFormulario() {
    const formulario = document.getElementById("divDetalleLote");
    formulario.style.display = "block";

    // Establecer la fecha actual como valor predeterminado
    document.getElementById("txtFechaIngreso").value = new Date().toISOString().split("T")[0];
}

// Función para cerrar el formulario
function cerrarFormulario() {
    const formulario = document.getElementById("divDetalleLote");
    formulario.style.display = "none";

    // Restablecer el formulario
    document.getElementById("loteForm").reset();
}

// Función para guardar un nuevo lote
function guardarLote() {
    const galleta = document.getElementById("selectGalleta").value;
    const cantidad = document.getElementById("selectCantidad").value;
    const fechaIngreso = document.getElementById("txtFechaIngreso").value;

    if (!galleta || !cantidad || !fechaIngreso) {
        alert("Todos los campos son obligatorios.");
        return;
    }

    const nuevoLote = {
        nombreGalleta: galleta,
        cantidad: parseInt(cantidad),
        fechaIngreso: fechaIngreso
    };

    // Llamada al API para guardar el lote
    fetch("http://localhost:8080/DonGalleto/api/inventario/agregar", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(nuevoLote)
    })
        .then(response => response.json())
        .then(data => {
            alert(data.result || data.error);
            cerrarFormulario();
            inicializarInventario(); // Recargar la tabla de inventario
        })
        .catch(error => {
            console.error("Error al guardar el lote:", error);
            alert("Hubo un error al guardar el lote.");
        });
}

// Función para inicializar el inventario
function inicializarInventario() {
    fetch("http://localhost:8080/DonGalleto/api/inventario/obtener")
        .then(response => response.json())
        .then(data => {
            const tbody = document.getElementById("inventarioTableBody");
            tbody.innerHTML = ""; // Limpiar contenido existente

            data.forEach(lote => {
                tbody.innerHTML += `
                    <tr>
                        <td>${lote.id}</td>
                        <td>${lote.nombreGalleta}</td>
                        <td>${lote.cantidad}</td>
                        <td>${lote.fechaIngreso}</td>
                        <td>${lote.fechaVencimiento}</td>
                        <td>${lote.restante}</td>
                        <td>
                            <button class="btn btn-warning btn-sm" onclick="editarLote(${lote.id})">Editar</button>
                            <button class="btn btn-danger btn-sm" onclick="eliminarLote(${lote.id})">Eliminar</button>
                        </td>
                    </tr>
                `;
            });
        })
        .catch(error => {
            console.error("Error al cargar el inventario:", error);
            alert("Hubo un error al cargar el inventario.");
        });
}

// Función para editar un lote
function editarLote(id) {
    alert(`Editar lote con ID: ${id}`);
}

// Función para eliminar un lote
function eliminarLote(id) {
    if (!confirm("¿Estás seguro de que deseas eliminar este lote?")) {
        return;
    }

    // Llamada al API para eliminar el lote
    fetch(`http://localhost:8080/DonGalleto/api/inventario/eliminar/${id}`, {
        method: "DELETE"
    })
        .then(response => response.json())
        .then(data => {
            alert(data.result || data.error);
            inicializarInventario(); // Recargar la tabla de inventario
        })
        .catch(error => {
            console.error("Error al eliminar el lote:", error);
            alert("Hubo un error al eliminar el lote.");
        });
}

// Función para cargar las opciones de galletas en el selector
function cargarOpcionesGalletas() {
    fetch("http://localhost:8080/DonGalleto/api/galletas/getAll")
        .then(response => response.json())
        .then(data => {
            const selectGalleta = document.getElementById("selectGalleta");
            selectGalleta.innerHTML = ""; // Limpiar opciones existentes

            data.forEach(galleta => {
                selectGalleta.innerHTML += `
                    <option value="${galleta.galleta}">${galleta.galleta}</option>
                `;
            });
        })
        .catch(error => {
            console.error("Error al cargar las galletas:", error);
            alert("Hubo un error al cargar las opciones de galletas.");
        });
}

// Inicializar al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    inicializarInventario();
    cargarOpcionesGalletas();
});
