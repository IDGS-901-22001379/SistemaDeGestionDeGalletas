package org.utl.dsm.appservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import org.utl.dsm.viewmodel.DetalleVentaViewModel;

public class DetalleVentaExternaAppService {
    private final String url = "http://10.16.21.34:8080/DON_GALLETO_Ventas/api/detalleVenta/getAll?activo=true";

    public List<DetalleVentaViewModel> getAll() {
        List<DetalleVentaViewModel> detalleVentasExternas = new ArrayList<>();
        Gson gson = new Gson();
        HttpClient client = HttpClient.newHttpClient();

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(url))
                    .GET()
                    .header("Content-Type", "application/json")
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Verificar si la solicitud fue exitosa (código 200)
            if (response.statusCode() == 200) {
                // Convertir la respuesta JSON a una lista de VentaViewModel y agregar a ventasExternas
                List<DetalleVentaViewModel> detalleVentas = gson.fromJson(response.body(), new TypeToken<List<DetalleVentaViewModel>>() {}.getType());
                detalleVentasExternas.addAll(detalleVentas);
            } else {
                System.err.println("Error al consultar el API externo en " + url + ": Código de respuesta " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al consultar el API externo en " + url + ": " + e.getMessage());
        }

        return detalleVentasExternas;
    }
}
