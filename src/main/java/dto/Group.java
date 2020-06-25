package dto;

import lombok.Data;
import lombok.ToString;



@Data
@ToString
public class Group{

    private final Integer id;
    private final String group_name;
    private final String group_description;

}


