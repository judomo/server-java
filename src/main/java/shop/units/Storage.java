package shop.units;

import Help_Classes.Pair;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@ToString

public class Storage {


    private static final ConcurrentHashMap<Integer, Pair<Integer, Product>> storage = new ConcurrentHashMap<>();

    private static final ArrayList<Product> storage_catalog = new ArrayList<>();


    public synchronized void addProductToStorage(Product product_to_add) {

        if (catalogContainsName(product_to_add.getProduct_name())) {


            if (storageContainsProduct(product_to_add.getProduct_id())) {

                storage.get(product_to_add.getProduct_id()).setLeft(storage.get(product_to_add.getProduct_id()).getLeft() + 1);
                storage.get(product_to_add.getProduct_id()).getRight().setProduct_price(product_to_add.getProduct_price());

            } else {

                storage.put(product_to_add.getProduct_id(), new Pair<>(1, product_to_add));

            }

        } else {

            storage_catalog.add(product_to_add);

            storage.put(product_to_add.getProduct_id(), new Pair<>(1, product_to_add));

        }

    }

    public synchronized void addProductToStorage(String product_name, Double product_price) {

        if (catalogContainsName(product_name)) {

            Integer tmp_id = storage_catalog.stream().filter(product -> product_name.equals(product.getProduct_name())).findFirst().orElse(null).getProduct_id();

            if (storageContainsProduct(tmp_id)) {

                storage.get(tmp_id).setLeft(storage.get(tmp_id).getLeft() + 1);
                storage.get(tmp_id).getRight().setProduct_price(product_price);

            } else {

                Product tmp_product = storage_catalog.stream().filter(product -> product_name.equals(product.getProduct_name())).findFirst().orElse(null);

                assert tmp_product != null;

                storage.put(tmp_product.getProduct_id(), new Pair<>(1, tmp_product));

            }

        } else {

            storage_catalog.add(new Product(product_name, product_price, storage_catalog.size()));

            Product tmp_product = storage_catalog.stream().filter(product -> product_name.equals(product.getProduct_name())).findFirst().orElse(null);

            assert tmp_product != null;

            storage.put(tmp_product.getProduct_id(), new Pair<>(1, tmp_product));

        }

    }


    public synchronized boolean IsEmpty() {
        return this.getStorage().isEmpty();
    }

    ;

    public synchronized void setPriceByProduct(Product product, Double new_price) {

        if (IsEmpty()) {

            System.err.println("Storage is empty");

        } else if (storageContainsProduct(product.getProduct_id())) {

            storage.get(product.getProduct_id()).getRight().setProduct_price(new_price);
        } else {

            System.err.println("Product is not in the storage");

        }


    }

    public synchronized void setPriceByProductName(String product_name, Double new_price) {

        if (catalogContainsName(product_name)) {

            Integer tmp_id = Objects.requireNonNull(storage_catalog.stream().filter(product -> product_name.equals(product.getProduct_name())).findFirst().orElse(null)).getProduct_id();

            if (storageContainsProduct(tmp_id)) {

                storage.get(tmp_id).getRight().setProduct_price(new_price);

            } else {

                System.err.println("Product is not in the storage");

            }

        } else if (IsEmpty()) {

            System.err.println("Storage is empty");

        } else {

            System.err.println("Product do not exist");

        }

    }

    public synchronized Integer getAmountOfProducts() {

        return storage.size();

    }

    public synchronized void deleteProduct(Product product) {

        if (IsEmpty()) {

            System.err.println("Storage is empty");

        } else if (storageContainsProduct(product.getProduct_id())) {

            if (storage.get(product.getProduct_id()).getLeft() - 1 == 0) {

                storage.remove(product.getProduct_id());

            } else {

                storage.get(product.getProduct_id()).setLeft(storage.get(product.getProduct_id()).getLeft() - 1);

            }
        } else {

            System.err.println("Product is not in the storage");

        }

    }

    public synchronized void deleteProduct(String productName) {

        if (catalogContainsName(productName)) {

            Integer tmp_id = Objects.requireNonNull(storage_catalog.stream().filter(product -> productName.equals(product.getProduct_name())).findFirst().orElse(null)).getProduct_id();

            if (storageContainsProduct(tmp_id)) {

                if ((storage.get(tmp_id).getLeft() - 1 == 0)) {
                    storage.remove(tmp_id);
                } else {
                    storage.get(tmp_id).setLeft(storage.get(tmp_id).getLeft() - 1);
                }


            } else {

                System.err.println("Product is not in the storage");

            }

        } else if (IsEmpty()) {

            System.err.println("Storage is empty");

        } else {

            System.err.println("Product do not exist");

        }


    }

    public synchronized ConcurrentHashMap<Integer, Pair<Integer, Product>> getStorage() {

        return storage;
    }

    private boolean catalogContainsName(final String product_name) {
        return Storage.storage_catalog.stream().anyMatch(o -> o.getProduct_name().equals(product_name));
    }

    private boolean storageContainsProduct(final Integer product_id) {

        return Storage.storage.get(product_id) != null;

    }

    public static ArrayList<Product> getCatalog() {

        return storage_catalog;

    }

    public void printStorage() {

        if (IsEmpty()) {

            System.out.println("Storage is empty");

        } else {


            for (ConcurrentHashMap.Entry<Integer, Pair<Integer, Product>> entry : storage.entrySet()) {

                Integer key = entry.getKey();

                System.out.println("Product id in HashMap: " + key);

                Pair<Integer, Product> value = entry.getValue();

                System.out.println(value.getRight() + " in amount of " + value.getLeft());


            }


        }

    }

}
