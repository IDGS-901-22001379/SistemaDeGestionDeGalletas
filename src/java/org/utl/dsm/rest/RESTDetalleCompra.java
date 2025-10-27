package org.utl.dsm.rest;

import com.google.gson.Gson;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.controller.ControllerDetalleCompra;
import org.utl.dsm.model.DetalleCompra;

@Path("detalleCompra")
public class RESTDetalleCompra {
    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllDetalleCompra() {
        String out = "";
        try {
            ControllerDetalleCompra cdc = new ControllerDetalleCompra();
            List<DetalleCompra> listaDetalleCompra = cdc.getAllDetalleCompras();
            Gson objGson = new Gson();
            out = objGson.toJson(listaDetalleCompra);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            out = "{\"error\":\"No se encontraron Detalles de Compra Registrados\n"+ex.getMessage()+"\"}";
        }
        return Response.ok(out).build();
    }
}
