package com.task.service;


import com.task.dto.TaskDTO;
import com.task.response.GenericResponse;
import com.task.response.PaginationResponse;
import com.task.utils.Constants;
import com.taskdb.enums.TaskStatus;
import com.taskdb.model.Team;
import com.taskdb.model.User;
import com.taskdb.repository.TaskRepository;
import com.taskdb.model.Task;
import com.taskdb.repository.TeamRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


@Log4j2
@Service
public class TaskService extends BaseService<Task, TaskDTO, TaskRepository> {

    @Autowired
    private TeamRepository teamRepository;

    public TaskService(TaskRepository repository) {
        super(repository);
    }

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);


    @Override
    public TaskDTO mapEntityToDto(Task entity) {
        TaskDTO dto = new TaskDTO();
        BeanUtils.copyProperties(entity, dto);

        dto.setDueDate(entity.getDueDate() != null ? this.dateFormat.format(entity.getDueDate()) : "N/A");

        dto.setTeamId(entity.getTeam().getId());
        dto.setAssignedToId(entity.getAssignedTo().getId());

        dto.setCreatedAt(entity.getCreatedAt().getTime());
        dto.setModifiedAt(entity.getModifiedAt().getTime());
        return dto;
    }

    @Override
    public Task mapDtoToEntity(TaskDTO dto) {
        Task entity = new Task();

        log.info("Task Service {}", "Mapping DTO to Entity");

        BeanUtils.copyProperties(dto, entity);

        /** Persisting the team */
        Team team = this.teamRepository.findById(dto.getTeamId()).get();
        entity.setTeam(team);

        /** Persisting the user */
        User user = this.userRepository.findById(dto.getAssignedToId()).get();
        entity.setAssignedTo(user);

        Date taskDate = null;

        try {

            log.info("Task Service {}", "Conversion of Due Date !!");
            taskDate = this.dateFormat.parse(dto.getDueDate());
            entity.setDueDate(taskDate);

        }
        catch (ParseException e) {
            e.printStackTrace();
        }

        log.info("Task Service {}", "Conversion has been completed !!");

        if (dto.getId() > 0) {
            entity.setModifiedAt(new Date(System.currentTimeMillis()));
            entity.setCreatedAt(new Date(dto.getCreatedAt()));
        } else {
            entity.setCreatedAt(new Date(System.currentTimeMillis()));
            entity.setModifiedAt(new Date(System.currentTimeMillis()));
        }
        return entity;
    }


    /** Addition of task */
    public ResponseEntity<GenericResponse<?>> addTask(TaskDTO dto) {

        /**  for checking duplicate value  */
        try {
            long taskCount = this.repository.findCountById(dto.getId());
            if(taskCount > 0){
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new GenericResponse(409, "Task is already exists!", null));
            }

            /** Preventing from conflict in case user from different team */
            long exitByUserAndTeam = this.userRepository.findCountByIdAndTeamId(dto.getTeamId(), dto.getAssignedToId());

            if (exitByUserAndTeam == 0) {
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new GenericResponse(409, "User is not the part of this team!", null));
            }

            TaskDTO taskDTO = this.save(dto);
            return ResponseEntity.ok(new GenericResponse(201, "Task has been created!", taskDTO));
        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(400, "Baq Request", null));
        }
    }

    /** Updating the task */
    public ResponseEntity<GenericResponse<?>> updateTask(Integer taskId, TaskDTO dto) {

        /**  finding the task for updating  */
        if (taskId == dto.getId() || dto.getId() == 0) {
            try {

                long taskCount = this.repository.findCountById(taskId);

                if(taskCount > 0){

                    /** Preventing from conflict in case user from different team */
                    long exitByUserAndTeam = this.userRepository.findCountByIdAndTeamId(dto.getTeamId(), dto.getAssignedToId());

                    if (exitByUserAndTeam == 0) {
                        return ResponseEntity
                                .status(HttpStatus.CONFLICT)
                                .body(new GenericResponse(409, "User is not the part of this team!", null));
                    }

                    TaskDTO taskDTO = this.save(dto);

                    return ResponseEntity.ok(new GenericResponse(200, "Task has been updated!", taskDTO));
                }
            }
            catch (Exception e){
                return ResponseEntity
                        .badRequest()
                        .body(new GenericResponse(400, "Invalid Task", null));
            }
        }
        return ResponseEntity
                .badRequest()
                .body(new GenericResponse(400, "Task could not be updated", null));
    }


    /** For updating the task's status */
    public ResponseEntity<GenericResponse> updateTaskStatusById(Integer taskId, String status){
        GenericResponse genericResponse = new GenericResponse(400, "Status is invalid", null);

        /**  for checking duplicate value  */
        Optional<Task> optionalTask = this.repository.findById(taskId);

        if ((status == null || status.isEmpty()) && optionalTask.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(genericResponse);
        }
        try {

            TaskStatus taskStatus = TaskStatus.valueOf(status.toUpperCase());

            int update = this.repository.updateTaskStatus(taskId, taskStatus);

            log.info("Task Service {}", "Status updation : " +update);

            return ResponseEntity.ok(new GenericResponse<>(200, "Status has been updated", null));
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(genericResponse);
        }
    }

    /** Finding tasks against user */
    public ResponseEntity<GenericResponse<?>> findTasksByUser(Integer userId){
        GenericResponse genericResponse = new GenericResponse(404, "User is invalid", null);
        Optional<User> optionalUser = this.userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(genericResponse);
        }

        try {
            List<Task> taskList = this.repository.findByUserId(userId);

            if (taskList.isEmpty()) {
                genericResponse.setMessage("No Tasks found against the user!");
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(genericResponse);
            }

            List<TaskDTO> taskDTOList = taskList
                    .stream()
                    .map(this::mapEntityToDto)
                    .collect(Collectors.toList());


            log.info("Task Service {}", "Fetched all tasks against user");


            return ResponseEntity.ok(new GenericResponse<>(200, "All tasks has been retrieved", taskDTOList));
        }
        catch (IllegalArgumentException ex) {
            return ResponseEntity
                    .badRequest()
                    .body(genericResponse);
        }
    }


    /** Finding tasks against custom filters */
    public ResponseEntity<GenericResponse<PaginationResponse<?>>> findAllWithCustomFilters(String status, Integer teamId, Integer userId, Integer enable, Integer pageNumber, Integer pageSize) {

        if(pageSize == null || pageSize <= 0 ) {
            pageSize = Constants.MAX_PER_PAGE;
        }
        if(pageNumber == null || pageNumber <= 0) {
            pageNumber = Constants.DEFAUT_START_PAGENUMBER;
        }

        // I use count as array of long instead of normal count due to same name in parameters of query
        long [] count = {0};

        /** @see com.taskdb.repository.TaskCustomRepository Custom Query for filters */
        List<Task> lists = repository.findAllBySupportFilters(status, teamId, userId, enable, (pageNumber - 1 ) * pageSize, pageSize, count);

        if (lists == null || lists.isEmpty()) {
            ResponseEntity.ok(new GenericResponse<>(200, "No data found!", null));
        }

        List<TaskDTO> DTOs = lists
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        PaginationResponse paginationResponse = new PaginationResponse<>(DTOs, pageNumber, pageSize, count[0], totalPages(count, pageSize));

        return ResponseEntity.ok(
                new GenericResponse<>(200, "Data fetched succesfull!", paginationResponse));
    }


}



