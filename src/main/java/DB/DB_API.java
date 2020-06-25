package DB;


import java.sql.*;

public class DB_API {
    public static void create(String TableName) {

        String sqlQuery = "CREATE TABLE IF NOT EXISTS " + TableName + " (product_id INTEGER PRIMARY KEY AUTOINCREMENT, product_name TEXT)";

        try {

            Statement statement = DB.connection.createStatement();

            statement.execute(sqlQuery);

            System.out.println("Table " + TableName + " created");
            System.out.println();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static Integer insert(String TableName, String product_name) {

        String sqlQuery = "INSERT INTO " + TableName + " (product_name) VALUES (?)";

        try {

            if (selectOneByTitle(TableName, product_name).next()) {

                System.err.println("Name exists in table");

                return -1;
            } else {

                PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);

                preparedStatement.setString(1, product_name);

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

    public static String insert(String TableName, int product_id, String product_name) {

        String sqlQuery = "INSERT INTO " + TableName + " (product_id, product_name) VALUES (?, ?)";

        try {

            if (findById(TableName, product_id).next()) {

                System.err.println("Id exists in table");

                return "Id exists in table";

            } else if (selectOneByTitle(TableName, product_name).next()) {

                System.err.println("Name exists in table");

                return "Name exists in table";

            } else {

                PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

                preparedStatement.setInt(1, product_id);
                preparedStatement.setString(2, product_name);

                preparedStatement.executeUpdate();

                System.out.println("Inserted " + product_id + " " + product_name);
                System.out.println();

                return "Inserted " + product_id + " " + product_name;

            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static void update(String TableName, int product_id, String product_name) {

        String sqlQuery = "UPDATE " + TableName + " SET product_name = ? WHERE product_id = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, product_name);
            preparedStatement.setInt(2, product_id);

            preparedStatement.executeUpdate();

            System.out.println("Updated " + product_id + " " + product_name);
            System.out.println();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public static ResultSet selectOneByTitle(String TableName, String product_name) {
        String sqlQuery = "SELECT * FROM " + TableName + " WHERE product_name = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setString(1, product_name);

            return preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


        return null;
    }

    public static ResultSet selectOneLimitOffset(String TableName, int limit, int offset) {
        String sqlQuery = "SELECT * FROM " + TableName + " LIMIT ?, ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, limit);

            return preparedStatement.executeQuery();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static ResultSet findById(String TableName, int product_id) {

        String sqlQuery = "SELECT * FROM " + TableName + " WHERE product_id = ?";

        try {

            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, product_id);

            return preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }


    public static ResultSet selectAll(String TableName) {

        String sqlQuery = "SELECT * FROM " + TableName;

        try {
            Statement statement = DB.connection.createStatement();

            return statement.executeQuery(sqlQuery);

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return null;
    }

    public static void delete(String TableName, int product_id) {
        String sqlQuery = "DELETE FROM " + TableName + " WHERE product_id = ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, product_id);

            preparedStatement.executeUpdate();

            System.out.println("Deleted " + product_id);
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public static void truncate(String TableName) {

        String sqlQuery = "DELETE FROM " + TableName;

        try {
            Statement statement = DB.connection.createStatement();

            statement.execute(sqlQuery);

            System.out.println("Table " + TableName + " truncated");
            System.out.println();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static boolean checkIfExist(String TableName) {

        try {

            DatabaseMetaData dbm = DB.connection.getMetaData();

            ResultSet tables = null;

            tables = dbm.getTables(null, null, TableName, null);

            if (tables.next()) {

                return true;

            } else {

                return false;

            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return false;
    }


    public static ResultSet listMoreThanId(String TableName, int product_id) {

        String sqlQuery = "SELECT * FROM " + TableName + " WHERE product_id > ?";

        try {
            PreparedStatement preparedStatement = DB.connection.prepareStatement(sqlQuery);

            preparedStatement.setInt(1, product_id);

            System.out.println("Listed more than " + product_id);
            System.out.println();

            return preparedStatement.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }


    public static void printResultSet(String resultSetName, ResultSet resultSet) {
        System.out.println(resultSetName + ":");
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("product_id") + "\t" + resultSet.getString("product_name"));
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        System.out.println();
    }


}
