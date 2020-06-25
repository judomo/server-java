package DAO;

import dto.Product;
import dto.Group;
import dto.ProductsGroup;
import lombok.Getter;

import java.sql.*;
import java.util.ArrayList;

public class DaoGroup{


    @Getter
    public static Connection connection;

    public DaoGroup() {

        try {
            Class.forName("org.sqlite.JDBC");

            connection = DriverManager.getConnection("jdbc:sqlite:" + "storedb");

        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Can't find SQLite JDBC class", e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }


    public static Integer update(Integer product_group_id, String product_group_name,  String product_group_descr) {

        String sqlQuery = "UPDATE product_group" + " SET group_name = ?, group_description = ? WHERE id = ?";

        try (final PreparedStatement insertStatement = connection.prepareStatement("select * from 'product_group' where group_name = ?")) {

            insertStatement.setString(1, product_group_name);

            final ResultSet resultSet_search = insertStatement.executeQuery();

            if (resultSet_search.next() && resultSet_search.getInt("id") != product_group_id) {

                System.err.println("Name exists in table");

                return -1;

            } else {

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

                preparedStatement.setString(1, product_group_name);
                preparedStatement.setString(2, product_group_descr);
                preparedStatement.setInt(3, product_group_id);

                preparedStatement.executeUpdate();

                return 1;

            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
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


    public Integer insertProductGroup(String productGroupName, String productGroupDesc) {

        String sqlQuery = "INSERT INTO product_group " + " (group_name, group_description) VALUES (?, ?)";

        try {

            if (selectOneByTitle(productGroupName).next()) {

                System.err.println("Name exists in table");

                return -1;
            } else {

                PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, productGroupName);
                preparedStatement.setString(2, productGroupDesc);


                preparedStatement.executeUpdate();

                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                if (resultSet.next()) {

                    Integer id = resultSet.getInt(1);

                    System.out.println("Inserted " + id + " " + productGroupName);
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

    public static ResultSet selectOneByTitle(String group_name) {

        String sqlQuery = "SELECT * FROM product_group" + " WHERE group_name = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, group_name);

            return preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


        return null;
    }


    public static Integer delete(Integer group_id) {

        String sqlQuery = "DELETE FROM product_group" + " WHERE id = ?";

        try {

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, group_id);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + group_id);
            System.out.println();

            return 1;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }

    public ProductsGroup getById(final int id) {

        try (final PreparedStatement insertStatement = connection.prepareStatement("SELECT * FROM product_group INNER JOIN product ON  product_group.id = product.group_id WHERE product_group.id = ?")) {

            insertStatement.setInt(1, id);

            final ResultSet resultSet = insertStatement.executeQuery();


            if (resultSet.next()) {

                ArrayList<Product> products = new ArrayList<Product>();

                ProductsGroup productsGroup = new ProductsGroup(resultSet.getInt("id"), resultSet.getString("group_name"), resultSet.getString("group_description"),products);

                products.add(new Product(resultSet.getInt(4), resultSet.getString(5), resultSet.getDouble(6), resultSet.getString(7), resultSet.getString(8), resultSet.getInt(9),resultSet.getInt(10), resultSet.getString(2)));

                while (resultSet.next()) {

                    products.add(new Product(resultSet.getInt(4), resultSet.getString(5), resultSet.getDouble(6), resultSet.getString(7), resultSet.getString(8), resultSet.getInt(9),resultSet.getInt(10), resultSet.getString(2)));

                }

                return productsGroup;

            }

            return null;

        } catch (final SQLException e) {
            System.out.println(e);
            throw new RuntimeException("Can't get user by login: " + id, e);
        }

    }

    public static void printResultSet(String resultSetName, ResultSet resultSet) {
        System.out.println(resultSetName + ":");
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt(4) + "\t" + resultSet.getString("product_name"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        System.out.println();
    }


}
