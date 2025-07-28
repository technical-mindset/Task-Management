package com.task.controller;


import com.task.dto.UserDTO;
import com.task.response.GenericResponse;
import com.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/users")
public class UsersController {


    @Autowired
    UserService service;

    @Transactional
    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("")
    public ResponseEntity<GenericResponse> create(@RequestBody @Valid UserDTO dto) {
        return this.service.addUser(dto);
    }

    @Transactional
    @GetMapping("")
    public ResponseEntity<?> getAllUsers(){
        return this.service.findAllEnable();
    }
}
