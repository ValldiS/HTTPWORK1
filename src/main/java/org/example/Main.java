package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import java.io.FileOutputStream;
import java.io.IOException;


public class Main {

    public static final String WAY = "https://api.nasa.gov/planetary/apod?api_key=HmgizXBLQ7FRhtR09MLVjNnoQchNR5LLDXZKj1Wu";

    public static void main(String[] args) throws IOException {

        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();

        HttpGet firstRequest = new HttpGet(WAY);

        CloseableHttpResponse firstResponse = httpClient.execute(firstRequest);


        String name = "StanHonda12024TSEMagogCanada1200.jpg";

        Format answer = result(firstResponse);

        HttpGet request = new HttpGet(answer.getUrl());

        CloseableHttpResponse response = httpClient.execute(request);

        try (FileOutputStream fos = new FileOutputStream(name)) {

            byte[] bytes = response
                    .getEntity()
                    .getContent()
                    .readAllBytes();

            fos.write(bytes, 0, bytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Format result(CloseableHttpResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Format format = mapper.readValue(
                response.getEntity().getContent(), new
                        TypeReference<>() {
                        });
        return format;
    }
}