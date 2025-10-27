package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.controller.ControllerDetalleVenta;
import org.utl.dsm.viewmodel.DetalleVentaViewModel;

@Path("detalleVenta")
public class RESTDetalleVenta {
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDetalleVenta(@QueryParam("activo") @DefaultValue("true") boolean activo) {
        String out = "";
        try {
            ControllerDetalleVenta cdv = new ControllerDetalleVenta();
            List<DetalleVentaViewModel> listaDetalleVenta = activo ? cdv.getAllDetalleVentasPublicos(): cdv.getAllDetallesVentasPublicosTodos();
            Gson objGson = new Gson();
            out = objGson.toJson(listaDetalleVenta);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            out = "{\"error\":\"No se encontraron Detalles de Ventas Registrados\n"+ex.getMessage()+"\"}";
        }
        return Response.ok(out).build();
    }
}
