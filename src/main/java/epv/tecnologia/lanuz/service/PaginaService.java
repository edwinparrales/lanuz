package epv.tecnologia.lanuz.service;

import epv.tecnologia.lanuz.domain.CategoriaPagina;
import epv.tecnologia.lanuz.domain.ImagenPagina;
import epv.tecnologia.lanuz.domain.Pagina;
import epv.tecnologia.lanuz.model.PaginaDTO;
import epv.tecnologia.lanuz.repos.CategoriaPaginaRepository;
import epv.tecnologia.lanuz.repos.ImagenPaginaRepository;
import epv.tecnologia.lanuz.repos.PaginaRepository;
import epv.tecnologia.lanuz.util.NotFoundException;
import epv.tecnologia.lanuz.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaginaService {

    private final PaginaRepository paginaRepository;
    private final CategoriaPaginaRepository categoriaPaginaRepository;
    private final ImagenPaginaRepository imagenPaginaRepository;

    public PaginaService(final PaginaRepository paginaRepository,
            final CategoriaPaginaRepository categoriaPaginaRepository,
            final ImagenPaginaRepository imagenPaginaRepository) {
        this.paginaRepository = paginaRepository;
        this.categoriaPaginaRepository = categoriaPaginaRepository;
        this.imagenPaginaRepository = imagenPaginaRepository;
    }

    public List<PaginaDTO> findAll() {
        final List<Pagina> paginas = paginaRepository.findAll(Sort.by("id"));
        return paginas.stream()
                .map(pagina -> mapToDTO(pagina, new PaginaDTO()))
                .toList();
    }

    public PaginaDTO get(final Long id) {
        return paginaRepository.findById(id)
                .map(pagina -> mapToDTO(pagina, new PaginaDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final PaginaDTO paginaDTO) {
        final Pagina pagina = new Pagina();
        mapToEntity(paginaDTO, pagina);
        return paginaRepository.save(pagina).getId();
    }

    public void update(final Long id, final PaginaDTO paginaDTO) {
        final Pagina pagina = paginaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paginaDTO, pagina);
        paginaRepository.save(pagina);
    }

    public void delete(final Long id) {
        paginaRepository.deleteById(id);
    }

    private PaginaDTO mapToDTO(final Pagina pagina, final PaginaDTO paginaDTO) {
        paginaDTO.setId(pagina.getId());
        paginaDTO.setTitulo(pagina.getTitulo());
        paginaDTO.setContenido(pagina.getContenido());
        paginaDTO.setCategoria(pagina.getCategoria() == null ? null : pagina.getCategoria().getId());
        return paginaDTO;
    }

    private Pagina mapToEntity(final PaginaDTO paginaDTO, final Pagina pagina) {
        pagina.setTitulo(paginaDTO.getTitulo());
        pagina.setContenido(paginaDTO.getContenido());
        final CategoriaPagina categoria = paginaDTO.getCategoria() == null ? null : categoriaPaginaRepository.findById(paginaDTO.getCategoria())
                .orElseThrow(() -> new NotFoundException("categoria not found"));
        pagina.setCategoria(categoria);
        return pagina;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Pagina pagina = paginaRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ImagenPagina paginaImagenPagina = imagenPaginaRepository.findFirstByPagina(pagina);
        if (paginaImagenPagina != null) {
            referencedWarning.setKey("pagina.imagenPagina.pagina.referenced");
            referencedWarning.addParam(paginaImagenPagina.getId());
            return referencedWarning;
        }
        return null;
    }

}
