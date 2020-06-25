package shop.units;


public class Main {

    public static void main(String[] args) {


        Storage storage = new Storage();

        storage.addProductToStorage("Wheet Classic", 10.0);

        Product product_1 = new Product("Wheet Premium", 15.0, Storage.getCatalog().size());

        storage.addProductToStorage(product_1);

        storage.deleteProduct("Wheet Classic");

        storage.deleteProduct("Wheet Premium");

        storage.printStorage();

        Products_Group products_group = new Products_Group("Wheet");

        products_group.addProductNameToTheGroup("Wheet Premiuim");

        products_group.addProductNameToTheGroup("Wheet Classic");

        System.out.println(products_group);


    }


}
