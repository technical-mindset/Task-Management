package com.task.controller;


import com.task.dto.MembersDTO;
import com.task.dto.TeamDTO;
import com.task.response.GenericResponse;
import com.task.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/teams")
public class TeamController {


    @Autowired
    private TeamService service;

    @CrossOrigin(origins = "*", allowedHeaders = "*")
    @PostMapping("")
    @Transactional
    public ResponseEntity<GenericResponse> create(@RequestBody @Valid TeamDTO dto) {
        return this.service.addTeam(dto);
    }

    @PostMapping("{teamId}/members")
    @Transactional
    public ResponseEntity<GenericResponse<?>> addMembers(@PathVariable("teamId") Integer teamId, @RequestBody @Valid MembersDTO dto) {
        return this.service.addMembers(teamId, dto);
    }


    @GetMapping("")
    @Transactional
    public ResponseEntity<?> getAllTeams(){
        return this.service.findAllEnable();
    }

}
