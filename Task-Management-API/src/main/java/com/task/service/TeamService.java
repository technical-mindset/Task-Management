package com.task.service;


import com.task.dto.MembersDTO;
import com.task.dto.TeamDTO;
import com.task.response.GenericListResponse;
import com.task.response.GenericResponse;
import com.taskdb.model.Team;
import com.taskdb.model.User;
import com.taskdb.repository.TeamRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class TeamService extends BaseService<Team, TeamDTO, TeamRepository> {


    public TeamService(TeamRepository repository) {
        super(repository);
    }

    @Override
    public TeamDTO mapEntityToDto(Team entity) {
        TeamDTO dto = new TeamDTO();
        BeanUtils.copyProperties(entity, dto);

        List<Integer> membersId = null;

        if (entity.getMembers() != null) {
            membersId =  entity.getMembers().stream()
                    .map(User ::getId)
                    .collect(Collectors.toList());

        }
        dto.setMembers(membersId);

        dto.setCreatedAt(entity.getCreatedAt().getTime());
        dto.setModifiedAt( entity.getModifiedAt().getTime () );
        return dto;
    }

    @Override
    public Team mapDtoToEntity(TeamDTO dto) {
        Team entity = new Team();
        BeanUtils.copyProperties(dto, entity);

        /** Persisting members */
        List<User> members = this.userRepository.findByMemberList(dto.getMembers());
        entity.setMembers(members.size() > 0 ? members : null);

        if (dto.getId() > 0) {
            entity.setModifiedAt(new Date(System.currentTimeMillis()));
            entity.setCreatedAt(new Date(dto.getCreatedAt()));
        } else {
            entity.setCreatedAt(new Date(System.currentTimeMillis()));
            entity.setModifiedAt(new Date(System.currentTimeMillis()));
        }
        return entity;

    }


    public ResponseEntity<GenericResponse> addTeam(TeamDTO dto) {

        /**  for checking duplicate value  */
        try {
            long teamCount = this.repository.findTeamByName(dto.getName());
            if(teamCount > 0){
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new GenericResponse(409, "Team is already exists!", null));
            }

            TeamDTO teamDTO = this.save(dto);
            return ResponseEntity.ok(new GenericResponse(201, "Data has been saved!", teamDTO));
        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(400, "Baq Request", null));
        }
    }

    /** Persisting the team-members */
    public ResponseEntity<GenericResponse<?>> addMembers(Integer teamId, MembersDTO dto) {

        Optional<Team> optionalTeam = this.repository.findById(teamId);

        if (optionalTeam.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(400, "Team is not exists!", null));
        }

        Team team = optionalTeam.get();

        /** Setting the FK into User for team */
        List<User> users = this.userRepository.findAllById(dto.getMembers());
        for (User user : users) {
            user.setTeam(team);
        }

        this.userRepository.saveAll(users);

        TeamDTO teamDTO = this.mapEntityToDto(team);

        return ResponseEntity
                .ok()
                .body(new GenericResponse(200, "Members are persist successfully!", teamDTO));
    }


    /**  Retrieving the teams  */
    public ResponseEntity<?> findAllEnable() {

        List<TeamDTO> dto = null;

        if (repository.findAll().isEmpty()){
         return ResponseEntity.ok(new GenericResponse<>(200, "No data found", null));
        }

        dto = repository.findAll()
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(GenericListResponse.success(dto, dto.size()));

    }


}
