package com.task.controller;


import com.task.dto.CommentDTO;
import com.task.response.GenericResponse;
import com.task.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;

@RestController
public class CommentController {

    @Autowired
    private CommentService service;


    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @Transactional
    @PostMapping(value = "/tasks/{taskId}/comments")
    public ResponseEntity<GenericResponse<?>> create(@PathVariable("taskId") Integer taskId, @RequestBody @Valid CommentDTO dto) {
        return this.service.addComment(taskId, dto);
    }


    @Transactional
    @GetMapping(value = "/tasks/{taskId}/comments")
    public ResponseEntity<?> findAllByTaskId(@PathVariable("taskId") Integer taskId) {
        return this.service.findAllCommentsByTask(taskId);
    }
}

