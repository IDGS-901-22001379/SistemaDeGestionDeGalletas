package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.utl.dsm.controller.ControllerProveedor;
import org.utl.dsm.model.Proveedor;

@Path("proveedor")
public class RESTProveedor {
 
    private final ControllerProveedor controllerProveedor = new ControllerProveedor();
    
    @Path("add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProveedor(String jsonProveedor) {
        Gson gson = new Gson();
        Proveedor proveedor = gson.fromJson(jsonProveedor, Proveedor.class);


        try {
        controllerProveedor.insertarProveedor(proveedor);
            String jsonResponse = gson.toJson(proveedor);
            return Response.ok(jsonResponse).build();
        } catch (Exception e) {
            String errorMessage = "No se pudo agregar el proveedor: " + e.getMessage();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
        }
    }
}
