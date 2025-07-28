package com.taskdb.model;


import com.taskdb.enums.TaskPriority;
import com.taskdb.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "task")
public class Task extends BaseEntity {

    private String name;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "due_date")
    private Date dueDate;

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @ManyToOne
    @JoinColumn(name = "assigned_to", referencedColumnName = "id")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;
}
