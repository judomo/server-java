package DAO;

import dto.User;

import java.sql.*;

public class UserDao {

    public static Connection connection;

    public UserDao(final String filename) {
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
            statement.execute("create table if not exists 'users'('id' INTEGER PRIMARY KEY AUTOINCREMENT, 'login' VARCHAR(50) NOT NULL, 'password' VARCHAR(250) NOT NULL,'role' VARCHAR(20) NOT NULL, UNIQUE (login))");
        } catch (final SQLException e) {
            throw new RuntimeException("Can't create table", e);
        }
    }

    public int insert(String login, String password, String role) {
        try (final PreparedStatement insertStatement = connection.prepareStatement("insert into 'users'('login', 'password', 'role') values (?, ?, ?)")) {
            insertStatement.setString(1, login);
            insertStatement.setString(2, password);
            insertStatement.setString(3, role);
            insertStatement.execute();

            final ResultSet result = insertStatement.getGeneratedKeys();
            return result.getInt("last_insert_rowid()");
        } catch (SQLException e) {
            throw new RuntimeException("Can't insert user", e);
        }
    }

    public User getByLogin(final String login) {

        try (final PreparedStatement insertStatement = connection.prepareStatement("select * from 'users' where login = ?")) {

            insertStatement.setString(1, login);

            final ResultSet resultSet = insertStatement.executeQuery();


            if (resultSet.next()) {

                User user = new User(resultSet.getInt("id"), resultSet.getString("login"), resultSet.getString("password"), resultSet.getString("role"));

                System.out.println(user);

                return user;
            }

            return null;

        } catch (final SQLException e) {
            throw new RuntimeException("Can't get user by login: " + login, e);
        }
    }


    public static void printResultSet(String resultSetName, ResultSet resultSet) {
        System.out.println(resultSetName + ":");
        try {
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id") + "\t" + resultSet.getString("login") + "\t" + resultSet.getString("password"));
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
