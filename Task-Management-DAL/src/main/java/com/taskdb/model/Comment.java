package com.taskdb.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    private String commentText;
    
    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    private LocalDateTime timestamp;
}