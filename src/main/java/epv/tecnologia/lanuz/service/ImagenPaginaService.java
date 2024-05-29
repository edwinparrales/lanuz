package epv.tecnologia.lanuz.service;

import epv.tecnologia.lanuz.domain.ImagenPagina;
import epv.tecnologia.lanuz.domain.Pagina;
import epv.tecnologia.lanuz.model.ImagenPaginaDTO;
import epv.tecnologia.lanuz.repos.ImagenPaginaRepository;
import epv.tecnologia.lanuz.repos.PaginaRepository;
import epv.tecnologia.lanuz.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ImagenPaginaService {

    private final ImagenPaginaRepository imagenPaginaRepository;
    private final PaginaRepository paginaRepository;

    public ImagenPaginaService(final ImagenPaginaRepository imagenPaginaRepository,
            final PaginaRepository paginaRepository) {
        this.imagenPaginaRepository = imagenPaginaRepository;
        this.paginaRepository = paginaRepository;
    }

    public List<ImagenPaginaDTO> findAll() {
        final List<ImagenPagina> imagenPaginas = imagenPaginaRepository.findAll(Sort.by("id"));
        return imagenPaginas.stream()
                .map(imagenPagina -> mapToDTO(imagenPagina, new ImagenPaginaDTO()))
                .toList();
    }

    public ImagenPaginaDTO get(final Long id) {
        return imagenPaginaRepository.findById(id)
                .map(imagenPagina -> mapToDTO(imagenPagina, new ImagenPaginaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ImagenPaginaDTO imagenPaginaDTO) {
        final ImagenPagina imagenPagina = new ImagenPagina();
        mapToEntity(imagenPaginaDTO, imagenPagina);
        return imagenPaginaRepository.save(imagenPagina).getId();
    }

    public void update(final Long id, final ImagenPaginaDTO imagenPaginaDTO) {
        final ImagenPagina imagenPagina = imagenPaginaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(imagenPaginaDTO, imagenPagina);
        imagenPaginaRepository.save(imagenPagina);
    }

    public void delete(final Long id) {
        imagenPaginaRepository.deleteById(id);
    }

    private ImagenPaginaDTO mapToDTO(final ImagenPagina imagenPagina,
            final ImagenPaginaDTO imagenPaginaDTO) {
        imagenPaginaDTO.setId(imagenPagina.getId());
        imagenPaginaDTO.setNombre(imagenPagina.getNombre());
        imagenPaginaDTO.setUrlImagen(imagenPagina.getUrlImagen());
        imagenPaginaDTO.setPagina(imagenPagina.getPagina() == null ? null : imagenPagina.getPagina().getId());
        return imagenPaginaDTO;
    }

    private ImagenPagina mapToEntity(final ImagenPaginaDTO imagenPaginaDTO,
            final ImagenPagina imagenPagina) {
        imagenPagina.setNombre(imagenPaginaDTO.getNombre());
        imagenPagina.setUrlImagen(imagenPaginaDTO.getUrlImagen());
        final Pagina pagina = imagenPaginaDTO.getPagina() == null ? null : paginaRepository.findById(imagenPaginaDTO.getPagina())
                .orElseThrow(() -> new NotFoundException("pagina not found"));
        imagenPagina.setPagina(pagina);
        return imagenPagina;
    }

}
