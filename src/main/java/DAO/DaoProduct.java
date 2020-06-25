package DAO;

import DB.DB;
import dto.Product;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;


public class DaoProduct {

    @Getter
    public static Connection connection;

    public DaoProduct(final String filename) {

        try {
            Class.forName("org.sqlite.JDBC");

            this.connection = DriverManager.getConnection("jdbc:sqlite:" + filename);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find SQLite JDBC class", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        initTable();

    }

    private void initTable() {
        try (final Statement statement = connection.createStatement()) {
            statement.execute("create table if not exists 'product'('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'product_name' VARCHAR(250), 'product_price' DECIMAL(10, 3))");
        } catch (final SQLException e) {
            throw new RuntimeException("Can't create table", e);
        }
    }

    public static Integer insertProduct(String product_name, Double product_price, String product_descr, String product_manufact, Integer product_amount, Integer product_group_id) {

        String sqlQuery = "INSERT INTO product " + " (product_name, product_price, descr, manufacturer, amount, group_id) VALUES (?, ?, ?, ?, ?, ?)";

        try {

            if (selectOneByTitle(product_name).next()) {

                System.err.println("Name exists in table");

                return -1;
            } else {

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, product_name);
                preparedStatement.setDouble(2, product_price);
                preparedStatement.setString(3, product_descr);
                preparedStatement.setString(4, product_manufact);
                preparedStatement.setInt(5, product_amount);
                preparedStatement.setInt(6, product_group_id);

                preparedStatement.executeUpdate();

                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {

                    Integer id = resultSet.getInt(1);

                    System.out.println("Inserted " + id + " " + product_name);
                    System.out.println();

                    return id;

                } else {
                    System.err.println("Can't insert :(");
                }
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static ResultSet selectOneByTitle(String product_name) {

        String sqlQuery = "SELECT * FROM product" + " WHERE product_name = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, product_name);

            return preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


        return null;
    }

    public static Integer update(Integer product_id, String product_name, Double product_price,  String descr, String manufacturer, Integer amount, Integer group_id) {

        String sqlQuery = "UPDATE product" + " SET product_name = ?, product_price = ? , descr = ? , manufacturer = ? , amount = ? , group_id = ? WHERE id = ?";

        try (final PreparedStatement insertStatement = connection.prepareStatement("select * from 'product' where id = ?")) {

            insertStatement.setInt(1, product_id);

            final ResultSet resultSet_search = insertStatement.executeQuery();


            ResultSet result_set = selectOneByTitle(product_name);

            if (result_set.next() && result_set.getInt("id") != product_id) {

                System.err.println("Name exists in table");

                return -1;

            } else {

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                preparedStatement.setString(1, product_name);
                preparedStatement.setDouble(2, product_price);
                preparedStatement.setString(3, descr);
                preparedStatement.setString(4, manufacturer);
                preparedStatement.setInt(5, amount);
                preparedStatement.setInt(6, group_id);
                preparedStatement.setInt(7, product_id);


                preparedStatement.executeUpdate();

                return 1;

            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public Product getById(final int id) {

        try (final PreparedStatement insertStatement = connection.prepareStatement("SELECT * FROM product INNER JOIN product_group ON product.group_id = product_group.id WHERE product.id = ?")) {

            insertStatement.setInt(1, id);

            final ResultSet resultSet = insertStatement.executeQuery();


            if (resultSet.next()) {



                Product product = new Product(resultSet.getInt("id"), resultSet.getString("product_name"), resultSet.getDouble("product_price"),resultSet.getString("descr"), resultSet.getString("manufacturer"), resultSet.getInt("amount"), resultSet.getInt("group_id"), resultSet.getString("group_name"));

                return product;
            }


            return null;

        } catch (final SQLException e) {
            System.out.println(e);
            throw new RuntimeException("Can't get user by login: " + id, e);
        }

    }


    public ArrayList<Product> getAllProducts() {

        try (final PreparedStatement insertStatement = connection.prepareStatement("SELECT * FROM product INNER JOIN product_group ON product.group_id = product_group.id")) {


            final ResultSet resultSet = insertStatement.executeQuery();

            ArrayList<Product> products = new ArrayList<>();

            if(resultSet.next()){

                products.add(new Product(resultSet.getInt("id"), resultSet.getString("product_name"), resultSet.getDouble("product_price"), resultSet.getString("descr"), resultSet.getString("manufacturer"), resultSet.getInt("amount"), resultSet.getInt("group_id"), resultSet.getString("group_name")));


                while (resultSet.next()) {

                products.add(new Product(resultSet.getInt("id"), resultSet.getString("product_name"), resultSet.getDouble("product_price"), resultSet.getString("descr"), resultSet.getString("manufacturer"), resultSet.getInt("amount"), resultSet.getInt("group_id"), resultSet.getString("group_name")));

            }

                return products;

            }

            return null;

        } catch (final SQLException e) {

            throw new RuntimeException("Can't get all users");
        }

    }

    public static Integer delete(int product_id) {

        String sqlQuery = "DELETE FROM product" + " WHERE id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, product_id);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + product_id);
            System.out.println();

            return 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public ArrayList<Product> searchProducts(String squery) {

        try (final PreparedStatement insertStatement = connection.prepareStatement("SELECT * FROM product t INNER JOIN product_group ON t.group_id = product_group.id WHERE product_name LIKE ?  OR descr LIKE ? OR manufacturer LIKE ? ")) {

            insertStatement.setString(1, StringUtils.defaultIfBlank(squery, "") + "%");
            insertStatement.setString(2, StringUtils.defaultIfBlank(squery, "") + "%");
            insertStatement.setString(3, StringUtils.defaultIfBlank(squery, "") + "%");



            final ResultSet resultSet = insertStatement.executeQuery();

            ArrayList<Product> products = new ArrayList<>();

            if(resultSet.next()){

                products.add(new Product(resultSet.getInt("id"), resultSet.getString("product_name"), resultSet.getDouble("product_price"), resultSet.getString("descr"), resultSet.getString("manufacturer"), resultSet.getInt("amount"), resultSet.getInt("group_id"), resultSet.getString("group_name")));


                while (resultSet.next()) {

                    products.add(new Product(resultSet.getInt("id"), resultSet.getString("product_name"), resultSet.getDouble("product_price"), resultSet.getString("descr"), resultSet.getString("manufacturer"), resultSet.getInt("amount"), resultSet.getInt("group_id"), resultSet.getString("group_name")));

                }

                return products;

            }



            return null;

        } catch (final SQLException e) {

            throw new RuntimeException("Can't get all users");
        }


    }

    public static ArrayList<Integer> getStat() {

        String sqlQueryGroup = "SELECT COUNT(*) FROM product_group";

        String sqlQuery = "SELECT SUM(amount)*SUM(product_price),COUNT(product.id)  FROM product";

        try {

            PreparedStatement preparedStatementGroup = connection.prepareStatement(sqlQueryGroup);

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            final ResultSet resultSetGroup = preparedStatementGroup.executeQuery();

            final ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<Integer> array = new ArrayList<>();

            if(resultSet.next() && resultSetGroup.next()){

                array.add(resultSetGroup.getInt(1));
                array.add(resultSet.getInt(1));
                array.add(resultSet.getInt(2));

                return array;

            }

            return null;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }



    public static void printResultSet(String resultSetName, ResultSet resultSet) {
        System.out.println(resultSetName + ":");
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + "\t" + resultSet.getString("product_name"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        System.out.println();
    }

    public static void close() {
        try {

            connection.close();

            System.out.println("Connection closed");
            System.out.println();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }


}
