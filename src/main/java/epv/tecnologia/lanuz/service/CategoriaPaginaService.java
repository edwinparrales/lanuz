package epv.tecnologia.lanuz.service;

import epv.tecnologia.lanuz.domain.CategoriaPagina;
import epv.tecnologia.lanuz.domain.Pagina;
import epv.tecnologia.lanuz.model.CategoriaPaginaDTO;
import epv.tecnologia.lanuz.repos.CategoriaPaginaRepository;
import epv.tecnologia.lanuz.repos.PaginaRepository;
import epv.tecnologia.lanuz.util.NotFoundException;
import epv.tecnologia.lanuz.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CategoriaPaginaService {

    private final CategoriaPaginaRepository categoriaPaginaRepository;
    private final PaginaRepository paginaRepository;

    public CategoriaPaginaService(final CategoriaPaginaRepository categoriaPaginaRepository,
            final PaginaRepository paginaRepository) {
        this.categoriaPaginaRepository = categoriaPaginaRepository;
        this.paginaRepository = paginaRepository;
    }

    public List<CategoriaPaginaDTO> findAll() {
        final List<CategoriaPagina> categoriaPaginas = categoriaPaginaRepository.findAll(Sort.by("id"));
        return categoriaPaginas.stream()
                .map(categoriaPagina -> mapToDTO(categoriaPagina, new CategoriaPaginaDTO()))
                .toList();
    }

    public CategoriaPaginaDTO get(final Long id) {
        return categoriaPaginaRepository.findById(id)
                .map(categoriaPagina -> mapToDTO(categoriaPagina, new CategoriaPaginaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CategoriaPaginaDTO categoriaPaginaDTO) {
        final CategoriaPagina categoriaPagina = new CategoriaPagina();
        mapToEntity(categoriaPaginaDTO, categoriaPagina);
        return categoriaPaginaRepository.save(categoriaPagina).getId();
    }

    public void update(final Long id, final CategoriaPaginaDTO categoriaPaginaDTO) {
        final CategoriaPagina categoriaPagina = categoriaPaginaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(categoriaPaginaDTO, categoriaPagina);
        categoriaPaginaRepository.save(categoriaPagina);
    }

    public void delete(final Long id) {
        categoriaPaginaRepository.deleteById(id);
    }

    private CategoriaPaginaDTO mapToDTO(final CategoriaPagina categoriaPagina,
            final CategoriaPaginaDTO categoriaPaginaDTO) {
        categoriaPaginaDTO.setId(categoriaPagina.getId());
        categoriaPaginaDTO.setNombre(categoriaPagina.getNombre());
        categoriaPaginaDTO.setDescripcion(categoriaPagina.getDescripcion());
        return categoriaPaginaDTO;
    }

    private CategoriaPagina mapToEntity(final CategoriaPaginaDTO categoriaPaginaDTO,
            final CategoriaPagina categoriaPagina) {
        categoriaPagina.setNombre(categoriaPaginaDTO.getNombre());
        categoriaPagina.setDescripcion(categoriaPaginaDTO.getDescripcion());
        return categoriaPagina;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final CategoriaPagina categoriaPagina = categoriaPaginaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Pagina pagina = paginaRepository.findFirstByCategoria(categoriaPagina);
        if (categoriaPagina != null) {
            referencedWarning.setKey("categoriaPagina.pagina.categoria.referenced");
            referencedWarning.addParam(categoriaPagina.getId());
            return referencedWarning;
        }
        return null;
    }

}
