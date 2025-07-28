package com.taskdb.model;


import com.taskdb.enums.TaskPriority;
import com.taskdb.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "team")
public class Team extends BaseEntity {

    private String name;

    private String description;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<User> members;

}
