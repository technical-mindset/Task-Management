package com.taskdb.repository;

import com.taskdb.enums.TaskStatus;
import com.taskdb.model.Task;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Transactional(readOnly = true)
public class TaskCustomRepositoryImpl extends AbstractPersistenceManager<Task> implements TaskCustomRepository {

    @Override
    public List<Task> findAllBySupportFilters(String status, Integer teamId, Integer userId, Integer enable, Integer pageNumber, Integer pageSize, long[] count) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        StringBuilder where = new StringBuilder(" WHERE ");

        /** Fetching against the status */
        if(status != null && !status.trim().equals("")) {
            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());
            where.append(" (status = :status) AND ");
            parameters.put("status", taskStatus);
        }

        /** Fetching against the task assigned to user */
        if (userId != null && userId > 0) {
            where.append(" task.assignedTo.id =:userId AND ");
            parameters.put("userId", userId);
        }

        /** Fetching against the team */
        if (teamId != null && teamId > 0) {
            where.append(" task.team.id =:teamId AND ");
            parameters.put("teamId", teamId);
        }

        /** Fetching against enable */
        if(enable != null){
            if(enable == 1) {
                where.append(" task.enable = :enable AND ");
                parameters.put("enable", true);

            } else if (enable == 2){
                where.append(" task.enable = :enable AND ");
                parameters.put("enable", false);
            }
        }

        where.append("1=1");

        // ✅ Append GROUP BY clause before passing it to getMaxResults
//        where.append(" GROUP BY task.id ");

        return getMaxResults(where + " ORDER BY task.id DESC ", parameters,pageNumber,pageSize,count);
    }
}

