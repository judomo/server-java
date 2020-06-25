package shop.units;

import Help_Classes.Pair;
import lombok.Getter;
import lombok.ToString;


import java.util.ArrayList;

@ToString

public class Products_Group {

    public Help_Classes.Pair<String, ArrayList<String>> products_group;

    Products_Group(String groupName, ArrayList<String> namesOfProductsInGroup) {

        this.products_group = new Pair<>(groupName, namesOfProductsInGroup);

    }

    Products_Group(String groupName) {

        ArrayList<String> empty_group_names = new ArrayList<>();

        empty_group_names.add("This group is empty");

        this.products_group = new Pair<>(groupName, empty_group_names);

    }

    void addProductNameToTheGroup(String productName) {

        if (products_group.getRight().contains("This group is empty"))
            products_group.getRight().remove("This group is empty");

        this.products_group.getRight().add(productName);

    }
}


