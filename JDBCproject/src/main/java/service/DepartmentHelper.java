package service;

import dao.DepartmentDAO;
import data.Department;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepartmentHelper implements DepartmentDAO {

    private static final String DB_NAME = "departments";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_ROOM = "room_number";
    private static final String COLUMNS = "(" + COLUMN_ID + ", " + COLUMN_NAME + ", " + COLUMN_PHONE + ", " + COLUMN_ROOM + ")";

    @Override
    public List<Department> getAll() {
        List<Department> departments = new ArrayList<>();
        try {
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
            statement.close();
        } catch (SQLException e) {
            System.out.println("Problem with getting all departments");
            e.printStackTrace();
        }
        return departments;
    }

    @Override
    public Department getById(int id) {
        return null;
    }

    @Override
    public void add(Department object) {
        try {
            Statement statement = DBHelper.getStatement();
            String query = "INSERT INTO " + DB_NAME + " " + COLUMNS
                    + " VALUES (" + object.getId() + ", '" + object.getName() + "', '"
                    + object.getPhone() + "', " + object.getRoomNumber() + ");";
            statement.executeUpdate(query);
            statement.close();
            DBHelper.commitChanges();
        } catch (SQLException e) {
            System.out.println("Problem with adding department " + object);
            e.printStackTrace();
        }
    }

    @Override
    public void update(Department object) {
    }

    @Override
    public void delete(Department object) {
    }

}
