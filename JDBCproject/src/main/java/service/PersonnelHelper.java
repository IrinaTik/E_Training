package service;

import dao.PersonDAO;
import data.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonnelHelper implements PersonDAO {

    public static final String DB_NAME = "personnel";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_PAY = "pay";
    public static final String COLUMN_DEPARTMENTID = "department_id";
    private static final String COLUMNS = "(" + COLUMN_NAME + ", " + COLUMN_POSITION + ", " + COLUMN_PAY + ", " + COLUMN_DEPARTMENTID + ")";
    private final DepartmentHelper dh;

    public PersonnelHelper(DepartmentHelper dh) {
        this.dh = dh;
    }

    @Override
    public List<Person> getAll() {
        List<Person> personnel = new ArrayList<>();
        try {
            Statement statement = DBHelper.getStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + DB_NAME + ";");
            while (result.next()) {
                Person person = new Person();
                person.setId(result.getInt(COLUMN_ID));
                person.setName(result.getString(COLUMN_NAME));
                person.setPosition(result.getString(COLUMN_POSITION));
                person.setPay(result.getInt(COLUMN_PAY));
                int dep_id = result.getInt(COLUMN_DEPARTMENTID);
                // проверка на наличие департамента
                if (dep_id != 0) {
                    person.setDepartment(dh.getById(result.getInt(COLUMN_DEPARTMENTID)));
                }
                personnel.add(person);
            }
            if (personnel.isEmpty()) {
                System.out.println("No personnel in DB");
            }
            result.close();
            DBHelper.closeStatement(statement);
        } catch (SQLException e) {
            System.out.println("Problem with getting all personnel");
            e.printStackTrace();
        }
        return personnel;
    }

    @Override
    public Person getById(int id) {
        Person person = new Person();
        try {
            Statement statement = DBHelper.getStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + DB_NAME + " WHERE " + COLUMN_ID + " = " + id + " ;");
            if (result.next()) {
                person.setId(result.getInt(COLUMN_ID));
                person.setName(result.getString(COLUMN_NAME));
                person.setPosition(result.getString(COLUMN_POSITION));
                person.setPay(result.getInt(COLUMN_PAY));
                int dep_id = result.getInt(COLUMN_DEPARTMENTID);
                // проверка на наличие департамента
                if (dep_id != 0) {
                    person.setDepartment(dh.getById(result.getInt(COLUMN_DEPARTMENTID)));
                }
            } else {
                System.out.println("No personnel with id = " + id + " found");
            }
            result.close();
            DBHelper.closeStatement(statement);
        } catch (SQLException e) {
            System.out.println("Problem with getting personnel with id = " + id);
            e.printStackTrace();
        }
        return person;
    }

    // получение записи в БД по имени и значению поля
    public Person getByField(String fieldName, String value) {
        Person person = new Person();
        try {
            Statement statement = DBHelper.getStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM " + DB_NAME + " WHERE " + fieldName + " = " + value + " ;");
            if (result.next()) {
                person.setId(result.getInt(COLUMN_ID));
                person.setName(result.getString(COLUMN_NAME));
                person.setPosition(result.getString(COLUMN_POSITION));
                person.setPay(result.getInt(COLUMN_PAY));
                person.setDepartment(dh.getById(result.getInt(COLUMN_DEPARTMENTID)));
            } else {
                System.out.println("No personnel where " + fieldName + " = " + value + " found");
            }
            result.close();
            DBHelper.closeStatement(statement);
        } catch (SQLException e) {
            System.out.println("Problem with getting personnel");
            e.printStackTrace();
        }
        return person;
    }

    @Override
    public int add(Person object) {
        int insertedIndex = 0;
        try {
            String query = "INSERT INTO " + DB_NAME + " " + COLUMNS
                    + " VALUES ('" + object.getName() + "', '"
                    + object.getPosition() + "', " + object.getPay() + ", "
                    + object.getDepartment().getId() + ");";
            PreparedStatement statement = DBHelper.getPreparedStatement(query);
            statement.executeUpdate();
            // выясняем id добавленной записи
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                insertedIndex = rs.getInt(1);
            }
            DBHelper.closeStatementAndCommitChanges(statement);
        } catch (SQLException e) {
            System.out.println("Problem with adding personnel " + object);
            insertedIndex = -1;
            e.printStackTrace();
        }
        return insertedIndex;
    }

    @Override
    public void update(Person object) {
        try {
            Statement statement = DBHelper.getStatement();
            String query = "UPDATE " + DB_NAME
                    + " set " + COLUMN_NAME + " = '" + object.getName()
                    + "', " + COLUMN_POSITION + " = '" + object.getPosition()
                    + "', " + COLUMN_PAY + " = " + object.getPay()
                    + ", " + COLUMN_DEPARTMENTID + " = " + object.getDepartment().getId()
                    + " where " + COLUMN_ID + " = " + object.getId() + ";";
            statement.executeUpdate(query);
            DBHelper.closeStatementAndCommitChanges(statement);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Person object) {
        String query = "DELETE from " + DB_NAME + " where " + COLUMN_ID + " = " + object.getId() + ";";
        delete(query);
    }
}
