package shop.units;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class Product {

    @Getter
    private static Integer products_count = 0;

    @Getter
    public Integer product_id;

    @Setter
    @Getter
    public String product_name;

    @Setter
    @Getter
    public Double product_price;


    public Product(String product_name, Double product_price, Integer product_id) {

        this.product_id = product_id;
        this.product_name = product_name;
        this.product_price = product_price;

    }


    public Product(Product product) {

        this.product_id = product.product_id;
        this.product_name = product.product_name;
        this.product_price = product.product_price;

    }


}

