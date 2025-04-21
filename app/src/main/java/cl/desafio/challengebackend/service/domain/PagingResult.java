package cl.desafio.challengebackend.service.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Clase que permite la paginación de la información consultada.
 */
@Data
@NoArgsConstructor
public class PagingResult<T> {
    private Collection<T> content;
    private Integer totalPages;
    private Long totalElements;
    private Integer size;
    private Integer page;
    private boolean empty;

    public PagingResult(Collection<T> content, Integer totalPages, Long totalElements, Integer size, Integer page
            , boolean empty) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.page = page;
        this.empty = empty;
    }
}
