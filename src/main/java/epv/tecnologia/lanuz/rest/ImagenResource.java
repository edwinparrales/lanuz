package epv.tecnologia.lanuz.rest;

import epv.tecnologia.lanuz.model.ImagenDTO;
import epv.tecnologia.lanuz.service.FileUploadService;
import epv.tecnologia.lanuz.service.ImagenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.io.IOException;
import java.util.List;
import java.nio.file.Files;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@RestController
@RequestMapping(value = "/api/imagens", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImagenResource {

    private final ImagenService imagenService;
    private final FileUploadService fileUploadService;
    private final HttpServletRequest request;

    public ImagenResource(final ImagenService imagenService,final FileUploadService fileUploadService,final HttpServletRequest request) {
        this.imagenService = imagenService;
        this.fileUploadService = fileUploadService;
        this.request = request;
    }

    @GetMapping
    public ResponseEntity<List<ImagenDTO>> getAllImagens() {
        return ResponseEntity.ok(imagenService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImagenDTO> getImagen(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(imagenService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createImagen(@RequestBody @Valid final ImagenDTO imagenDTO) {
        final Long createdId = imagenService.create(imagenDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateImagen(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final ImagenDTO imagenDTO) {
        imagenService.update(id, imagenDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImagen(@PathVariable(name = "id") final Long id) {

        ImagenDTO imagenDTO = imagenService.get(id);
        fileUploadService.deleteResorce(imagenDTO.getNombre());
        imagenService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/loadFile")
    public ResponseEntity<String> loadFile(@RequestParam("file") MultipartFile file,@RequestParam("idProducto")Long idProducto) throws IOException {

        String nombreArchivo =  fileUploadService.store(file);
        String host = request.getRequestURL().toString().replace(request.getRequestURI(),"");
        String url = ServletUriComponentsBuilder
                .fromHttpUrl(host)
                .path("/api/imagens/getFile/")
                .path(nombreArchivo)
                .toUriString();
    //Alamacenar la url de la imagen

        ImagenDTO imagenDTO = new ImagenDTO();
        imagenDTO.setProducto(idProducto);
        imagenDTO.setUrl(url);
        imagenDTO.setNombre(nombreArchivo);
        imagenService.create(imagenDTO);



        return  ResponseEntity.ok(url);
    }

    @GetMapping("getFile/{filename:.*}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Resource file = fileUploadService.loadAsResource(filename);
        String contentType = Files.probeContentType(file.getFile().toPath());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE,contentType)
                .body(file);

    }

}
