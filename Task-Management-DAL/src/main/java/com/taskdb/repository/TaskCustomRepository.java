package com.taskdb.repository;

import com.taskdb.model.Task;

import java.util.List;


/**
 * @see com.taskdb.repository.TaskCustomRepositoryImpl for the custom query Iimplementation
 * */

public interface TaskCustomRepository {
    List<Task> findAllBySupportFilters(String status, Integer teamId, Integer userId, Integer enable, Integer pageNumber, Integer pageSize, long[] count);
}
