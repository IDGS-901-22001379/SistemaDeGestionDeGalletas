package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.controller.ControllerGalleta;
import org.utl.dsm.model.Galleta;

@Path("galleta")
public class RESTGalleta {
    
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllGalletas() {
        String out = "";
        try {
            ControllerGalleta cg = new ControllerGalleta();
            List<Galleta> listaGalletas =cg.getAllGalletas();
            Gson objGson = new Gson();
            out = objGson.toJson(listaGalletas);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            out = "{\"error\":\"No se encontraron Galletas Registradas\n" + ex.getMessage() + "\"}";
        }
        return Response.ok(out).build();
    }
    
    @Path("disminuirCantidad")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response disminuirCantidad(@QueryParam("nombreGalleta") String nombreGalleta, @QueryParam("cantidad") int cantidad) {
        String out = "";

        try {
            ControllerGalleta controllerGalleta = new ControllerGalleta();
            controllerGalleta.disminuirCantidadPorGalleta(nombreGalleta, cantidad);
            out = "{\"message\":\"Cantidad disminuida correctamente para la galleta: " + nombreGalleta + "\"}";
        } catch (SQLException | ClassNotFoundException | IOException e) {
            out = "{\"error\":\"No se pudo disminuir la cantidad de la galleta: " + nombreGalleta + ". Error: " + e.getMessage() + "\"}";
        } catch (IllegalArgumentException e){
            out = "{\"error\":\"Error: " + e.getMessage() + "\"}";
        }

        return Response.ok(out).build();
    }
}
