package com.duoc.sistema_pedidos.service;

import com.duoc.sistema_pedidos.dto.GuiaDespachoMensaje;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class RabbitMQClientService {

    @Value("${rabbitmq.ms.url}")
    private String rabbitMsUrl;

    @Value("${azure.tenant-id}")
    private String tenantId;

    @Value("${azure.client-id}")
    private String clientId;

    @Value("${azure.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public void enviarExito(Long guiaId, String numeroGuia) {
        enviar("/producir/exito", guiaId, numeroGuia, "OK", "Guia procesada correctamente");
    }

    public void enviarError(Long guiaId, String numeroGuia, String detalleError) {
        enviar("/producir/error", guiaId, numeroGuia, "ERROR", detalleError);
    }

    private void enviar(String path, Long guiaId, String numeroGuia, String estado, String detalle) {
        String token = obtenerToken();
        System.out.println("Llamando a rabbitMQ-ms: " + rabbitMsUrl + path);

        GuiaDespachoMensaje mensaje = new GuiaDespachoMensaje(guiaId, numeroGuia, estado, detalle);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GuiaDespachoMensaje> request = new HttpEntity<>(mensaje, headers);
        restTemplate.postForObject(rabbitMsUrl + path, request, String.class);
    }

    @SuppressWarnings("unchecked")
    private String obtenerToken() {
        String tokenUrl = "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("scope", "https://mscloudnativeduoc.onmicrosoft.com/" + clientId + "/.default");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        System.out.println("Solicitando token a: " + tokenUrl);
        System.out.println("Client ID usado: " + clientId);

        Map<String, Object> response = restTemplate.postForObject(tokenUrl, request, Map.class);

        System.out.println("Token obtenido correctamente");
        return (String) response.get("access_token");
    }
}