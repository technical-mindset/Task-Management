package com.task.controller;


import com.task.dto.UserDTO;
import com.task.response.GenericResponse;
import com.task.service.UserService;
import com.taskdb.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping(value = "/roles")
public class RolesController {


    @Autowired
    RoleRepository repository;

    @Transactional
    @GetMapping("")
    public ResponseEntity<?> getAllRoles(){
        return ResponseEntity.ok(this.repository.findAll());
    }
}
