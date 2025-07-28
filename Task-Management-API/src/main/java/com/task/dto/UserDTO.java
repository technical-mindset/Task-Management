package com.task.dto;

import com.task.utils.Constants;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;


@Data
public class UserDTO extends BaseDTO {

    @Pattern(message = Constants.RA_VALID + "Email ", regexp = Constants.REGEX_EMAIL)
    @Size(min = 3, max = 100, message = Constants.RA_MIN_AND_MAX)
    private String email;

    @NotBlank(message = " Name " + Constants.RA_EMPTY_MESSAGE)
    @Size(min = Constants.RA_4,max = Constants.RA_255, message = Constants.RA_LENGTH_STRING)
    private String name;

    @NotEmpty(message = "Roles " + Constants.RA_EMPTY_MESSAGE)
    private List<Integer> roleId;

}
