package dao;

import service.DBHelper;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public interface GeneralDAO<T> {

    List<T> getAll();

    T getById(int id);

    int add(T object);

    void update(T object);

    default void delete(String query) {
        try {
            Statement statement = DBHelper.getStatement();
            statement.executeUpdate(query);
            DBHelper.closeStatementAndCommitChanges(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
