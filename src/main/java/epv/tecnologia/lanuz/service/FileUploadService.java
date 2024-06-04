package epv.tecnologia.lanuz.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileUploadService {
    @Value("${media.location:mediafiles}")
    private String mediaLocation;
    private Path rootLocation;
    @PostConstruct
    public void init() {
        rootLocation = Paths.get(mediaLocation+"/producto-img");
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public String store(MultipartFile file) {
        try {


            if(file.isEmpty()){
                throw new RuntimeException("El archivo esta vacio");
            }

            String fileName = file.getOriginalFilename();
            Path destinationFile = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();

            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream,destinationFile, StandardCopyOption.REPLACE_EXISTING);

            }
            return fileName;
        }catch (RuntimeException | IOException e){
            throw  new RuntimeException("No se puede leer el archiv"+ e.getMessage());
        }
    }


    public Resource loadAsResource(String filename) {
        try{
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() | resource.isReadable()){
                return resource;
            }else{

                throw  new RuntimeException("No se puede leer el archivo"+filename);
            }

        }catch (MalformedURLException mf){
            throw  new RuntimeException("Url mal formada"+mf.getMessage());
        }
    }



    public void deleteResorce(String filename){
        try{
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() | resource.isReadable()){
                Path destinationFile = rootLocation.resolve(Paths.get(filename)).normalize().toAbsolutePath();
                System.out.println("destination file: "+destinationFile);
                Files.delete(destinationFile);

            }else{

                throw  new RuntimeException("No se puede leer el archivo"+filename);
            }

        }catch (MalformedURLException mf){
            throw  new RuntimeException("Url mal formada"+mf.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


}
