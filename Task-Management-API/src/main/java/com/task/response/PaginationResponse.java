package com.task.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaginationResponse<T> {
    private List<T> data;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
