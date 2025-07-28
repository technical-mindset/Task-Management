package com.task.service;


import com.task.dto.UserDTO;
import com.task.response.GenericListResponse;
import com.task.response.GenericResponse;
import com.taskdb.model.Role;
import com.taskdb.model.User;
import com.taskdb.repository.RoleRepository;
import com.taskdb.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Log4j2
@Service
public class UserService extends BaseService<User, UserDTO, UserRepository>{


    @Autowired
    public RoleRepository roleRepository;

    public UserService (UserRepository repository) {
        super(repository);
    }

    @Override
    public UserDTO mapEntityToDto(User entity) {
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(entity, dto);

        //Getting Role id for selection in View
        List<Integer> roleIds =  entity.getRoles().stream()
                .map(Role ::getId)
                .collect(Collectors.toList());


        dto.setRoleId(roleIds);
        dto.setCreatedAt(entity.getCreatedAt().getTime());
        dto.setModifiedAt( entity.getModifiedAt().getTime () );
        return dto;
    }

    @Override
    public User mapDtoToEntity(UserDTO dto) {
        User entity = new User();
        BeanUtils.copyProperties(dto, entity);

        //Setting Roles
        List<Role> roles = this.roleRepository.findByRoleList(dto.getRoleId());
        entity.setRoles(roles);

        if (dto.getId() > 0) {
            entity.setModifiedAt(new Date(System.currentTimeMillis()));
            entity.setCreatedAt(new Date(dto.getCreatedAt()));
        } else {
            entity.setCreatedAt(new Date(System.currentTimeMillis()));
            entity.setModifiedAt(new Date(System.currentTimeMillis()));
        }
        return entity;

    }


    public ResponseEntity<GenericResponse> addUser(UserDTO dto) {

        /**  for checking duplicate value  */
        try {
            List<User> userEmail = this.repository.findUsersByEmail(dto.getEmail());
            System.out.println(":::::::::::::::::::::::::::: EMAIL :::::::::::::::::::: " + userEmail.size());
            if(!userEmail.isEmpty()){
                return ResponseEntity
                        .status(HttpStatus.CONFLICT)
                        .body(new GenericResponse(409, "Email already exists!", null));
            }

            UserDTO userDTO = this.save(dto);
            return ResponseEntity.ok(new GenericResponse(201, "Data has been saved!", userDTO));
        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(400, "Baq Request", null));
        }
    }


    public ResponseEntity<?> findAllEnable() {

        List<UserDTO> dto = null;

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
