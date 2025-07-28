package com.task.dto;

import com.task.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@Setter
@Getter
public class CommentDTO extends BaseDTO {

    @NotBlank(message = " Comment " + Constants.RA_EMPTY_MESSAGE)
    @Size(min = Constants.RA_4, max = Constants.RA_20, message = Constants.RA_LENGTH_STRING)
    private String commentText;

    @NotNull(message = "Task " + Constants.RA_EMPTY_MESSAGE)
    @Min(value = 1, message = "Task " + Constants.RA_EMPTY_MESSAGE)
    private Integer task;

    private LocalDateTime timestamp;
}
