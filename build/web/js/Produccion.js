// Cargar el stock inicial de todas las galletas
function cargarStock() {
    fetch("http://localhost:8080/DON_GALLETO_Ventas/api/produccion/obtenerStock")
            .then(response => {
                if (!response.ok) {
                    console.error("HTTP Error:", response.status, response.statusText);
                    throw new Error("Error al obtener el stock.");
                }
                return response.json();
            })
            .then(stockMap => {
                console.log("Stock recibido:", stockMap);
                // Actualizar cada contador con el ID correspondiente
                for (const id in stockMap) {
                    const contador = document.getElementById(`contador-${id}`);
                    if (contador) {
                        contador.textContent = stockMap[id]; // Asigna la existencia al contador

                        // Cambiar el color del círculo según la cantidad
                        const circulo = document.getElementById(`circle-${id}`);
                        const cantidad = stockMap[id];
                        if (cantidad > 400) {
                            circulo.className = "status-circle status-green";
                        } else if (cantidad > 150) {
                            circulo.className = "status-circle status-yellow";
                        } else {
                            circulo.className = "status-circle status-red";
                        }
                    }
                }
            })
            .catch(error => console.error("Error al cargar el stock:", error));
}

// Sumar stock a una galleta
function addCookie(event, id) {
    event.stopPropagation(); // Evita que active el volteo de la tarjeta
    const contador = document.getElementById(`contador-${id}`);
    let cantidad = parseInt(contador.textContent, 10);
    cantidad++;
    contador.textContent = cantidad;

    // Petición al servidor para actualizar el stock en la base de datos
    fetch("http://localhost:8080/DON_GALLETO_Ventas/api/produccion/sumarStock", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({id_galleta: id, existencia: 250})
    })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Error al actualizar el stock en la base de datos." + response.status);
                }
                return response.json();
            })
            .then(() => {
                console.log(`Stock actualizado para la galleta con ID: ${id}`);
            })
            .catch(error => console.error("Error al actualizar el stock:", error));
}

function registrarMermaGalletas() {
    const idGalleta = parseInt(document.getElementById("tipoGalleta").value);
    const descripcion = document.getElementById("descripcionGalleta").value;
    const cantidad = parseInt(document.getElementById("cantidadGalleta").value);
    const fecha = document.getElementById("fechaGalleta").value;

    if (!idGalleta || !cantidad || cantidad <= 0 || !descripcion || !fecha) {
        alert("Por favor, completa todos los campos correctamente.");
        return;
    }


    let params = {g: JSON.stringify(idGalleta), c: JSON.stringify(cantidad), d: JSON.stringify(descripcion)};

    fetch("http://localhost:8080/DON_GALLETO_Ventas/api/produccion/registrarMermaGalletas", {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
        body: new URLSearchParams(params)
    })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Error al registrar la merma de galletas.");
                }
                return response.json();
            })
            .then(() => {
                Swal.fire({
                    title: "Éxito",
                    text: "Merma de galletas descontada correctamente.",
                    icon: "success",
                    confirmButtonText: "Aceptar"
                });
                cerrarModal("modalMermaGalletas");
            })
            .catch(error => console.error("Error al registrar merma de galletas:", error));
}

// Registrar merma de insumos
function registrarMermaInsumos() {
    const idInsumo = parseInt(document.getElementById("tipoInsumo").value);
    const descripcion = document.getElementById("descripcionInsumo").value;
    const cantidad = parseInt(document.getElementById("cantidadInsumo").value);
    const fecha = document.getElementById("fechaInsumo").value;

    if (!idInsumo || !cantidad || cantidad <= 0 || !descripcion || !fecha) {
        alert("Por favor, completa todos los campos correctamente.");
        return;
    }

    let params = {g: JSON.stringify(idInsumo), c: JSON.stringify(cantidad), d: JSON.stringify(descripcion)};

    console.log(params);
    fetch("http://localhost:8080/DON_GALLETO_Ventas/api/produccion/registrarMermaInsumos", {
        method: "POST",
        headers: {"Content-Type": "application/x-www-form-urlencoded;charset=UTF-8"},
        body: new URLSearchParams(params)
    })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Error al registrar la merma de insumos." + response.status);
                }
                return response.json();
            })
            .then(() => {
                cerrarModal("modalMermas");
                Swal.fire({
                    title: "Éxito",
                    text: "Merma de insumos descontada correctamente.",
                    icon: "success",
                    confirmButtonText: "Aceptar"
                });
            })
            .catch(error => console.error("Error al registrar merma de insumos:", error));
}

// Cerrar un modal
function cerrarModal(id) {
    const modal = document.getElementById(id);
    if (modal) {
        modal.style.display = "none";
    }
}

// Abrir un modal
function abrirModal(id) {
    const modal = document.getElementById(id);
    if (modal) {
        modal.style.display = "block";
    }
}

// Función para alternar la tarjeta (volteo)
function toggleCard(event) {
    const card = event.currentTarget;
    if (card.classList.contains("is-flipped")) {
        card.classList.remove("is-flipped");
    } else {
        card.classList.add("is-flipped");
    }
}

// Inicializar los datos al cargar la página
document.addEventListener("DOMContentLoaded", () => {
    cargarStock(); // Cargar stock inicial
});
function redireccionar(url) {
    window.location.href = "index.html";
}

