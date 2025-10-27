package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.utl.dsm.controller.ControllerInsumo;
import org.utl.dsm.model.AlertaInsumo;
import org.utl.dsm.model.Insumo;

/**
 *
 * @author ascen
 */
@Path("insumos")
public class RESTInsumos {

    private ControllerInsumo controllerinsumo;

    public RESTInsumos() {
        controllerinsumo = new ControllerInsumo();
    }
    
    //Mostrar todo
    @Path("getAllInsumos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllinsumos() {
        String out = "";
        try {
            ControllerInsumo objCc = new ControllerInsumo();
            List<Insumo> listaInsumos= objCc.obtenerInsumos();

            Gson objGson = new Gson();
            out = objGson.toJson(listaInsumos);
            if (listaInsumos == null || listaInsumos.isEmpty()) {
                out = "{\"message\":\"No se encontraron insumos\"}";
            }
        } catch (Exception e) {
            out = "{\"error\":\"Se produjo un error en la ejecución\"}";
            e.printStackTrace();
        }
        return Response.ok(out).build();
    }
    
    //Buscar por orden de compra
    @Path("numeroOrden")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response buscarDescripcionCompra(@QueryParam("numeroOrden") String numeroOrden) {
        String out = "";
        try {
            // Llamada al controlador para obtener la descripción de la compra
            String descripcion = controllerinsumo.buscarDescripcionCompra(numeroOrden);

            // Si no se encontró la descripción
            if (descripcion == null || descripcion.isEmpty()) {
                out = "{\"message\":\"No se encontró ninguna compra con el número de orden proporcionado.\"}";
            } else {
                // Si se encontró la descripción
                out = "{\"descripcion\":\"" + descripcion + "\"}";
            }
        } catch (Exception e) {
            // Manejo de errores
            out = "{\"error\":\"Se produjo un error en la ejecución.\"}";
            e.printStackTrace();
        }
        return Response.ok(out).build(); // Responder con el resultado
    }





    
    //Actualizar agregando
    @Path("actualizarInsumos")
@POST
@Produces(MediaType.APPLICATION_JSON)
public Response actualizarInsumos(@FormParam("numeroOrden") String numeroOrden) {
    if (numeroOrden == null || numeroOrden.trim().isEmpty()) {
        // Validación de parámetros
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity("{\"error\":\"El número de orden no puede estar vacío.\"}")
                       .build();
    }

    try {
        // Llamada al controlador para actualizar el estatus de la compra
        controllerinsumo.actualizarEstatusCompra(numeroOrden);
        
        // Respuesta exitosa
        String out = "{\"message\":\"Estatus de la compra actualizado correctamente.\"}";
        return Response.ok(out).build();
    }
            // Manejo de error específico de base de datos
             catch (Exception e) {
        // Manejo de error genérico
        e.printStackTrace();
        String errorMessage = "Error inesperado: " + e.getMessage();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("{\"error\":\"" + errorMessage + "\"}")
                       .build();
    }
            // Manejo de error de I/O
            
}

    
    //Merma insumos
@Path("mermaInsumos")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response mermaInsumos(@FormParam("idInsumo") int idInsumo, 
                                 @FormParam("cantidadMerma") double cantidadMerma) {
        String out;
        try {
            Insumo insumo = new Insumo();
            insumo.setId_insumo(idInsumo);
            insumo.setCantidad((int) cantidadMerma);

            controllerinsumo.actualizarInsumoMerma(insumo);
            out = "{\"message\":\"Merma completada correctamente.\"}";
        } catch (Exception e) {

            out = "{\"error\":\"Se produjo un error al actualizar la merma del insumo: " + e.getMessage() + "\"}";
            e.printStackTrace();
        }
        return Response.ok(out).build();
    }
    
    //Alertas
@Path("alertas")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response obtenerAlertas() {
        String jsonOutput;
        try {
            ControllerInsumo controllerInsumo = new ControllerInsumo();

            List<AlertaInsumo> alertas = controllerInsumo.verificarAlertas();

            Gson gson = new Gson();
            if (alertas == null || alertas.isEmpty()) {
                jsonOutput = "{\"message\":\"No hay insumos en alerta.\"}";
            } else {
                jsonOutput = gson.toJson(alertas);
            }

            return Response.ok(jsonOutput).build();
        } catch (Exception e) {
            e.printStackTrace();
            String errorResponse = "{\"error\":\"Ocurrió un error al procesar las alertas.\"}";
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(errorResponse)
                    .build();
        }
    }
}
