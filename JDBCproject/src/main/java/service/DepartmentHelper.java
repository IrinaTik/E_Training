package service;

import dao.DepartmentDAO;
import data.Department;
import data.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DepartmentHelper implements DepartmentDAO {

    public static final String DB_NAME = "departments";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ROOM = "room_number";
    private static final String COLUMNS = "(" + COLUMN_NAME + ", " + COLUMN_PHONE + ", " + COLUMN_ROOM + ")";

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
            if (departments.isEmpty()) {
                System.out.println("No departments in DB");
            }
            result.close();
            DBHelper.closeStatement(statement);
        } catch (SQLException e) {
            System.out.println("Problem with getting all departments");
            e.printStackTrace();
        }
        return departments;
    }

    @Override
    public Department getById(int id) {
        Department department = new Department();
        try {
            Statement statement = DBHelper.getStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + DB_NAME + " WHERE " + COLUMN_ID + " = " + id + " ;");
            if (result.next()) {
                department.setId(result.getInt(COLUMN_ID));
                department.setName(result.getString(COLUMN_NAME));
                department.setPhone(result.getString(COLUMN_PHONE));
                department.setRoomNumber(result.getInt(COLUMN_ROOM));
            } else {
                System.out.println("No departments with id = " + id + " found");
            }
            result.close();
            DBHelper.closeStatement(statement);
        } catch (SQLException e) {
            System.out.println("Problem with getting department with id = " + id);
            e.printStackTrace();
        }
        return department;
    }

    @Override
    public int add(Department object) {
        int insertedIndex = 0;
        try {
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
        } catch (SQLException e) {
            System.out.println("Problem with adding department " + object);
            insertedIndex = -1;
            e.printStackTrace();
        }
        return insertedIndex;
    }

    @Override
    public void update(Department object) {
        try {
            Statement statement = DBHelper.getStatement();
            String query = "UPDATE " + DB_NAME
                    + " set " + COLUMN_NAME + " = '" + object.getName()
                    + "', " + COLUMN_PHONE + " = '" + object.getPhone()
                    + "', " + COLUMN_ROOM + " = " + object.getRoomNumber()
                    + " where " + COLUMN_ID + " = " + object.getId() + ";";
            statement.executeUpdate(query);
            DBHelper.closeStatementAndCommitChanges(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Department object, PersonnelHelper ph) {
        Person person = ph.getByField(PersonnelHelper.COLUMN_DEPARTMENTID, String.valueOf(object.getId()));
        if (person.getId() != null) {
            person.getDepartment().setId(null);
            ph.update(person);
        }
        String query = "DELETE from " + DB_NAME + " where " + COLUMN_ID + " = " + object.getId() + ";";
        delete(query);
    }

}
