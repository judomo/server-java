package dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductsGroup{

    private  Integer id;
    private  String group_name;
    private  String group_description;
    ArrayList<Product> products;

}
