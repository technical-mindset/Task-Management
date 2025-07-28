package com.task.dto;

import com.task.utils.Constants;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@NoArgsConstructor
@Setter
@Getter
public class TeamDTO extends BaseDTO{

    @NotBlank(message = " Name " + Constants.RA_EMPTY_MESSAGE)
    @Size(min = Constants.RA_4, max = Constants.RA_20, message = Constants.RA_LENGTH_STRING)
    private String name;

    @NotBlank(message = " Description " + Constants.RA_EMPTY_MESSAGE)
    @Size(min = Constants.RA_10, max = Constants.RA_255, message = Constants.RA_LENGTH_STRING)
    private String description;

    private List<Integer> members;

}
