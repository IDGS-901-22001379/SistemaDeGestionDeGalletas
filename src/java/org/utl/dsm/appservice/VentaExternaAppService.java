package org.utl.dsm.appservice;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.utl.dsm.viewmodel.VentaViewModel;

public class VentaExternaAppService {    
    private final String url = "http://10.16.21.34:8080/DON_GALLETO_Ventas/api/venta/getAll?activo=true";

    public List<VentaViewModel> getAll() {
        List<VentaViewModel> ventasExternas = new ArrayList<>();
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
                List<VentaViewModel> ventas = gson.fromJson(response.body(), new TypeToken<List<VentaViewModel>>() {}.getType());
                ventasExternas.addAll(ventas);
            } else {
                System.err.println("Error al consultar el API externo en " + url + ": Código de respuesta " + response.statusCode());
            }
        } catch (Exception e) {
            System.err.println("Error al consultar el API externo en " + url + ": " + e.getMessage());
        }

        return ventasExternas;
    }
}
