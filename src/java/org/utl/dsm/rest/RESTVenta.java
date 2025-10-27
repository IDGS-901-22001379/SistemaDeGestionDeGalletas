package org.utl.dsm.rest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.utl.dsm.controller.ControllerVenta;
import org.utl.dsm.model.DetalleVenta;
import org.utl.dsm.model.Venta;
import org.utl.dsm.viewmodel.VentaViewModel;

@Path("venta")
public class RESTVenta {

    @Path("insertar")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response insertarVenta(@FormParam("v") @DefaultValue("") String venta, @FormParam("ldv") String lista) {
        Gson objGson = new Gson();
        Venta v = objGson.fromJson(venta, Venta.class);  // Convertir JSON de la venta a un objeto Venta
        String out = "";
        ControllerVenta cv = new ControllerVenta();
        try {
            // Convertir la cadena JSON de detalles de venta a una lista de objetos DetalleVenta
            Type listType = new TypeToken<List<DetalleVenta>>() {
            }.getType();
            List<DetalleVenta> detallesVenta = objGson.fromJson(lista, listType);

            // Ahora puedes pasar la lista de detalles junto con la venta al controlador
            int resultado = cv.insertarVenta(v, detallesVenta);

            if (resultado > 0) {
                out = "{\"result\":\"¡Venta registrada exitosamente!\"}";
            } else if (resultado == -1) {
                out = "{\"error\":\"¡Ha ocurrido un problema al registrar la venta!\"}";
            }
        } catch (IllegalArgumentException | SQLException e) {
            out = "{\"error\":\"¡" + e.getMessage() + "!\"}";
        }

        return Response.ok(out).build();
    }

    @Path("getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllVentas(@QueryParam("activo") @DefaultValue("true") boolean activo) {
        String out = "";
        try {
            ControllerVenta cv = new ControllerVenta();
            List<VentaViewModel> listaVentas = activo ? cv.getAllVentasPublicos(): cv.getAllVentasPublicosTodos();
            Gson objGson = new Gson();
            out = objGson.toJson(listaVentas);
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            out = "{\"error\":\"No se encontraron Ventas Registrados\n" + ex.getMessage() + "\"}";
        }
        return Response.ok(out).build();
    }
}
