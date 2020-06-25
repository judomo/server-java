package dto;

import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    private Integer id;
    private String name;
    private double price;
    private String descr;
    private String manufacturer;
    private Integer amount;
    private Integer group_id;
    private String group_name;


}
