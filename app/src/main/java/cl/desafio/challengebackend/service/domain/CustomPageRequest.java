package cl.desafio.challengebackend.service.domain;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;

public class CustomPageRequest implements Pageable, Serializable {
    private static final long serial = 12344;
    private final int offset;
    private final int limit;
    private final Sort sort;

    public CustomPageRequest(int offset, int limit, Sort sort) throws IllegalAccessException {
        if (offset < 0) {
            throw new IllegalAccessException("El número de página no puede ser menor que cero");
        }
        if (limit < 1) {
            throw new IllegalAccessException("La cantidad de elementos a buscar no puede ser menor que 1");
        }
        this.offset = offset;
        this.limit = limit;
        this.sort = sort;
    }

    public CustomPageRequest(int offset, int limit, Sort.Direction direction, String... sortField) throws IllegalAccessException {
        this(offset, limit, Sort.by(direction, sortField));
    }

    public CustomPageRequest(int offset, int limit) throws IllegalAccessException {
        this(offset, limit, Sort.unsorted());
    }

    @Override
    public int getPageNumber() {
        return offset / limit;
    }

    @Override
    public int getPageSize() {
        return limit;
    }

    @Override
    public long getOffset() {
        return offset;
    }

    @NotNull
    @Override
    public Sort getSort() {
        return sort;
    }

    @NotNull
    @SneakyThrows
    @Override
    public Pageable next() {
        return new CustomPageRequest((int) (getOffset() + getPageSize()), getPageSize(), getSort());
    }

    @SneakyThrows
    public CustomPageRequest previous() {
        return hasPrevious() ? new CustomPageRequest((int) (getOffset() - getPageSize()), getPageSize(), getSort()) : this;
    }

    @NotNull
    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @NotNull
    @SneakyThrows
    @Override
    public Pageable first() {
        return new CustomPageRequest(0, getPageSize(), getSort());
    }

    @Override
    public Pageable withPage(int pageNumber) {
        return null;
    }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }

}
