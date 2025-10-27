document.addEventListener("DOMContentLoaded", () => {
    cargarAlertas();

    async function cargarAlertas() {
        const container = document.getElementById('alertasContainer');
        try {
            // Asegúrate de usar la ruta correcta
            const response = await axios.get('/DonGalleto/rest/alertas/obtener');
            const alertas = response.data;

            if (!alertas || alertas.length === 0) {
                container.innerHTML = `<div class="text-center text-muted">No hay alertas para mostrar.</div>`;
            } else {
                container.innerHTML = '';
                alertas.forEach(alerta => {
                    const alertElement = document.createElement('div');
                    alertElement.className = 'alert-item';

                    alertElement.innerHTML = `
                        <h5>${alerta.tipoAlerta} - Lote ${alerta.loteId}</h5>
                        <p><strong>Descripción:</strong> ${alerta.descripcion}</p>
                        <p><strong>Fecha de Alerta:</strong> ${alerta.fechaAlerta}</p>
                        <p><strong>Estado:</strong> ${alerta.atendida ? 'Atendida' : 'Pendiente'}</p>
                        ${
                            !alerta.atendida
                                ? `<button class="btn btn-sm btn-success btn-mark" onclick="marcarComoAtendida(${alerta.idAlerta})">Marcar como atendida</button>`
                                : ''
                        }
                    `;
                    container.appendChild(alertElement);
                });
            }
        } catch (error) {
            console.error("Error al cargar las alertas:", error);
            container.innerHTML = `<div class="text-center text-danger">Error al cargar las alertas. Intente nuevamente.</div>`;
        }
    }

    window.marcarComoAtendida = async (idAlerta) => {
        try {
            const response = await axios.put(`/DonGalleto/rest/alertas/marcarAtendida/${idAlerta}`);
            if (response.status === 200) {
                alert('Alerta marcada como atendida.');
                cargarAlertas();
            } else {
                alert('No se pudo marcar la alerta como atendida. Intente nuevamente.');
            }
        } catch (error) {
            console.error("Error al marcar como atendida:", error);
            alert('Ocurrió un error al marcar la alerta como atendida.');
        }
    };
});
