package dao.impl;

import dao.DBHelper;
import dao.DepartmentDAO;
import entity.Department;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAOImpl implements DepartmentDAO {

    public static final String DB_NAME = "departments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ROOM = "room_number";
    private static final String COLUMNS = "(" + COLUMN_NAME + ", " + COLUMN_PHONE + ", " + COLUMN_ROOM + ")";

    @Override
    public List<Department> getAll() throws SQLException {
        List<Department> departments = new ArrayList<>();
        Statement statement = DBHelper.getStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM " + DB_NAME + ";");
        while (result.next()) {
            Department department = new Department();
            department.setId(result.getInt(COLUMN_ID));
            department.setName(result.getString(COLUMN_NAME));
            department.setPhone(result.getString(COLUMN_PHONE));
            department.setRoomNumber(result.getInt(COLUMN_ROOM));
            departments.add(department);
        }
        result.close();
        DBHelper.closeStatement(statement);
        return departments;
    }

    @Override
    public Department getById(int id) throws SQLException {
        Department department = new Department();
        Statement statement = DBHelper.getStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM " + DB_NAME + " WHERE " + COLUMN_ID + " = " + id + " ;");
        if (result.next()) {
            department.setId(result.getInt(COLUMN_ID));
            department.setName(result.getString(COLUMN_NAME));
            department.setPhone(result.getString(COLUMN_PHONE));
            department.setRoomNumber(result.getInt(COLUMN_ROOM));
        }
        result.close();
        DBHelper.closeStatement(statement);
        return department;
    }

    @Override
    public int add(Department object) throws SQLException {
        int insertedIndex = 0;
        String query = "INSERT INTO " + DB_NAME + " " + COLUMNS
                + " VALUES ('" + object.getName() + "', '"
                + object.getPhone() + "', " + object.getRoomNumber() + ");";
        PreparedStatement statement = DBHelper.getPreparedStatement(query);
        statement.executeUpdate();
        // выясняем id добавленной записи
        ResultSet rs = statement.getGeneratedKeys();
        if (rs.next()) {
            insertedIndex = rs.getInt(1);
        }
        DBHelper.closeStatementAndCommitChanges(statement);
        return insertedIndex;
    }

    public void update(Department object) throws SQLException {
        String query = "UPDATE " + DB_NAME
                + " set " + COLUMN_NAME + " = '" + object.getName()
                + "', " + COLUMN_PHONE + " = '" + object.getPhone()
                + "', " + COLUMN_ROOM + " = " + object.getRoomNumber()
                + " where " + COLUMN_ID + " = " + object.getId() + ";";
        update(query);
    }

    public void delete(Department object) throws SQLException {
        String query = "DELETE from " + DB_NAME + " where " + COLUMN_ID + " = " + object.getId() + ";";
        delete(query);
    }

}
