package com.task.dto;

import com.task.utils.Constants;
import com.taskdb.enums.TaskPriority;
import com.taskdb.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;


@NoArgsConstructor
@Setter
@Getter
public class TaskDTO extends BaseDTO {

    @NotBlank(message = "Description " + Constants.RA_EMPTY_MESSAGE)
    @Size(min = Constants.RA_4, max = Constants.RA_500, message = Constants.RA_LENGTH_STRING_500)
    private String description;

    @Pattern(regexp = Constants.REGEX_DUE_DATE, message = "Due-Date must be in format yyyy-MM-dd")
    @NotNull(message = "Due-Date " + Constants.RA_EMPTY_MESSAGE)
    @NotBlank(message = "Due-Date " + Constants.RA_EMPTY_MESSAGE)
    private String dueDate;

    @Size(min = Constants.RA_4, max = Constants.RA_20, message = Constants.RA_LENGTH_STRING_20)
    @NotBlank(message = "Name "  + Constants.RA_EMPTY_MESSAGE)
    private String name;

    @Size(min = Constants.RA_4, max = Constants.RA_20, message = Constants.RA_LENGTH_STRING_20)
    @NotBlank(message = "Title " + Constants.RA_EMPTY_MESSAGE)
    private String title;

    @NotNull(message = "Status " + Constants.RA_EMPTY_MESSAGE)
    private TaskStatus status;

    @NotNull(message = "Priority " + Constants.RA_EMPTY_MESSAGE)
    private TaskPriority priority;

    @Min(value = 1, message = "User " + Constants.RA_EMPTY_MESSAGE)
    private Integer assignedToId;

    @Min(value = 1, message = "Team " + Constants.RA_EMPTY_MESSAGE)
    private Integer teamId;
}


