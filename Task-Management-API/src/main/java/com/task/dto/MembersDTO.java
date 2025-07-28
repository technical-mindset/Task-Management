package com.task.dto;

import com.task.utils.Constants;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;


@Data
public class MembersDTO extends BaseDTO{

    @NotEmpty(message = "Members " + Constants.RA_EMPTY_MESSAGE)
    private List<Integer> members;

}
