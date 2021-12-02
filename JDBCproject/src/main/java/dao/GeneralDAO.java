package dao;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface GeneralDAO<T> {

    List<T> getAll() throws SQLException;

    T getById(int id) throws SQLException;

    int add(T object) throws SQLException;

    default void update(String query) throws SQLException {
        Statement statement = DBHelper.getStatement();
        statement.executeUpdate(query);
        DBHelper.closeStatementAndCommitChanges(statement);
    };

    default void delete(String query) throws SQLException {
        Statement statement = DBHelper.getStatement();
        statement.executeUpdate(query);
        DBHelper.closeStatementAndCommitChanges(statement);

    }

}
