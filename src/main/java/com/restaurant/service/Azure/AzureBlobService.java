package com.restaurant.service.Azure;

import com.azure.storage.blob.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class AzureBlobService {

    private final BlobContainerClient containerClient;

    public AzureBlobService() {
        // 🔹 Aquí va tu cadena de conexión completa
        String connectionString = "DefaultEndpointsProtocol=https;AccountName=deliverirecursos;AccountKey=TTkLVqOExEytBfTOSB9WdO1eRn87nIDz/C6GeBpHRSMcUD8mGmRwAdXVnMYr5TyCxUHOxNCytDPs+ASt7M7yGg==;EndpointSuffix=core.windows.net";
        String containerName = "imagenmenus"; // 🔹 El nombre del contenedor

        BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();

        this.containerClient = serviceClient.getBlobContainerClient(containerName);
    }

    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        BlobClient blobClient = containerClient.getBlobClient(fileName);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        return blobClient.getBlobUrl();
    }
}
