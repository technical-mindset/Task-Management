package com.task.service;



import com.task.dto.BaseDTO;
import com.task.utils.Constants;
import com.taskdb.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Log4j2
public abstract class BaseService<E, D extends BaseDTO, R extends JpaRepository<E, Integer>> {

    protected final R repository;

    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.ENGLISH);

    @Autowired
    UserRepository userRepository;

    public BaseService(R repository) {
        this.repository = repository;
    }

    @Transactional
    public D save(D dto) {

        dto.setCreatedBy (0);
        dto.setModifyBy(0);

        var e = mapDtoToEntity(dto);
        e = repository.save(e);
        return mapEntityToDto(e);
    }

    @Transactional
    public List<D> save(List<D> dtos) {
        var e = dtos.stream().map(this::mapDtoToEntity).collect(Collectors.toList());
        e = repository.saveAll(e);
        return e.stream().map(this::mapEntityToDto).collect(Collectors.toList());
    }

    public abstract D mapEntityToDto(E entity);

    public abstract E mapDtoToEntity(D dto);

    public Pageable pageable(Integer pageNumber, Integer pageSize) {
        if (pageSize == null || pageSize <= 0) {
            pageSize = Constants.MAX_PER_PAGE;
        }
        if (pageNumber == null || pageNumber <= 0) {
            pageNumber = Constants.DEFAUT_START_PAGENUMBER;
        }
        return PageRequest.of(pageNumber - 1, pageSize);
    }


    public int totalPages(long[] count, Integer pageSize) {
        int totalPagesPre = (int) (count[0] / pageSize);
        int totalPages = (count[0] % pageSize) == 0 ? totalPagesPre : totalPagesPre + 1;
        return totalPages;
    }

}
