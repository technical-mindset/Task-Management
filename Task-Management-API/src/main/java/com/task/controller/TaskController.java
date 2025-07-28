package com.task.controller;


import com.task.dto.TaskDTO;
import com.task.response.GenericResponse;
import com.task.response.PaginationResponse;
import com.task.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class TaskController {

    @Autowired
    private TaskService service;


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Transactional
    @PostMapping(value = "/tasks")
    public ResponseEntity<GenericResponse<?>> create(@RequestBody @Valid TaskDTO dto) {
        return this.service.addTask(dto);
    }

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Transactional
    @PutMapping(value = "/tasks/{id}")
    public ResponseEntity<GenericResponse<?>> update(@PathVariable("id") Integer taskId, @RequestBody @Valid TaskDTO dto) {
        return this.service.updateTask(taskId, dto);
    }

    @Transactional
    @GetMapping(value = "/tasks")
    public ResponseEntity<GenericResponse<PaginationResponse<?>>> findAllView(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "team", required = false) Integer teamId,
            @RequestParam(value = "enable", required = false) Integer enable,
            @RequestParam(value = "user",required = false) Integer userId,
            @RequestParam(value = "ps",required = false) Integer pageSize,
            @RequestParam(value = "pn",required = false) Integer pageNumber) {
        return this.service.findAllWithCustomFilters(status, teamId, userId, enable, pageNumber, pageSize);
    }

    @Transactional
    @GetMapping(value = "/users/{userId}/tasks")
    public ResponseEntity<GenericResponse<?>> findTasksByUser(@PathVariable(value = "userId") Integer userId) {
        return this.service.findTasksByUser(userId);
    }

    @Transactional
    @PutMapping(value = "/tasks/{id}/status")
    public ResponseEntity<GenericResponse> updateTaskStatusById(
            @PathVariable(value = "id") Integer taskId,
            @RequestParam(value = "status", required = false) String status
    ) {
        return this.service.updateTaskStatusById(taskId , status);
    }
}

