package service;

import java.sql.*;

public class DBHelper {

    private static final String DB_PATH = "jdbc:postgresql://localhost:5432/jdbcproject";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "1234";

    private static Connection connection;

    public static void initConnection() {
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(DB_PATH, USERNAME, PASSWORD);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            System.out.println("Problem with Postgres JDBC Driver");
            e.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("Opening connection failed: problem with db connection");
            ex.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Closing connection failed: problem with db connection");
            e.printStackTrace();
        }
    }

    public static Statement getStatement() throws SQLException {
        return connection.createStatement();
    }

    // для добавления записей, чтобы можно было определить id добавленной записи с помощью ключа RETURN_GENERATED_KEYS
    public static PreparedStatement getPreparedStatement(String query) throws SQLException {
        return connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
    }

    public static void closeStatement(Statement statement) throws SQLException {
        statement.close();
    }

    public static void commitChanges() throws SQLException {
        connection.commit();
    }

    public static void closeStatementAndCommitChanges(Statement statement) throws SQLException {
        closeStatement(statement);
        commitChanges();
    }

}
