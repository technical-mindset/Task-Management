package com.taskdb.repository;

import com.taskdb.enums.TaskStatus;
import com.taskdb.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface TaskRepository extends JpaRepository<Task, Integer>, TaskCustomRepository {

    @Modifying
    @Query("UPDATE Task t SET t.status = ?2, t.modifiedAt = current_timestamp WHERE t.id = ?1")
    int updateTaskStatus(int id, TaskStatus status);

    @Query("SELECT COUNT(*) FROM Task t WHERE t.id = ?1")
    long findCountById(Integer id);

    @Query("SELECT t FROM Task t WHERE t.assignedTo.id = ?1")
    List<Task> findByUserId(Integer userId);
}

