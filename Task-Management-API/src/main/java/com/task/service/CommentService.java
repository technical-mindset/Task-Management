package com.task.service;


import com.task.dto.CommentDTO;
import com.task.response.GenericListResponse;
import com.task.response.GenericResponse;
import com.taskdb.model.Comment;
import com.taskdb.model.Task;
import com.taskdb.repository.CommentRepository;
import com.taskdb.repository.TaskRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class CommentService extends BaseService<Comment, CommentDTO, CommentRepository> {

    @Autowired
    private TaskRepository taskRepository;


    public CommentService(CommentRepository repository) {
        super(repository);
    }

    @Override
    public CommentDTO mapEntityToDto(Comment entity) {
        CommentDTO dto = new CommentDTO();
        BeanUtils.copyProperties(entity, dto);

        dto.setTask(entity.getTask().getId());
        dto.setCreatedAt(entity.getCreatedAt().getTime());
        dto.setModifiedAt( entity.getModifiedAt().getTime () );
        return dto;
    }

    @Override
    public Comment mapDtoToEntity(CommentDTO dto) {
        Comment entity = new Comment();
        BeanUtils.copyProperties(dto, entity);

        /** Persisting task */
        Optional<Task> task = this.taskRepository.findById(dto.getTask());
        entity.setTask(task.isPresent() ? task.get() : null);

        if (dto.getId() > 0) {
            entity.setModifiedAt(new Date(System.currentTimeMillis()));
            entity.setCreatedAt(new Date(dto.getCreatedAt()));
        } else {
            entity.setCreatedAt(new Date(System.currentTimeMillis()));
            entity.setModifiedAt(new Date(System.currentTimeMillis()));
        }
        entity.setTimestamp(LocalDateTime.now());
        return entity;

    }


    public ResponseEntity<GenericResponse<?>> addComment(Integer taskId, CommentDTO dto) {

        /**  for checking duplicate value and respective task existence  */
        try {
            Optional<Task> task = this.taskRepository.findById(taskId);
            Optional<Comment> comment = this.repository.findById(dto.getId());

            if(task.isEmpty() || comment.isPresent() || taskId != dto.getTask()){
                return ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(new GenericResponse(404, "Invalid request!", null));
            }

            CommentDTO commentDTO = this.save(dto);
            return ResponseEntity.ok(new GenericResponse(201, "Data has been saved!", commentDTO));
        }
        catch (Exception e){
            return ResponseEntity
                    .badRequest()
                    .body(new GenericResponse(400, "Baq Request", null));
        }
    }

    /**  Retrieving the comments  */
    public ResponseEntity<?> findAllCommentsByTask(Integer taskId) {

        List<CommentDTO> dto = null;

        List<Comment> comments = this.repository.findAllByTaskId(taskId);

        if (comments.isEmpty()){
         return ResponseEntity.ok(new GenericResponse<>(200, "No data found", null));
        }

        dto = comments
                .stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(GenericListResponse.success(dto, dto.size()));

    }


}
