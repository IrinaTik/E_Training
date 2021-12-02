package dao.impl;

import dao.DBHelper;
import dao.PersonDAO;
import entity.Person;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PersonDAOImpl implements PersonDAO {

    public static final String DB_NAME = "personnel";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_POSITION = "position";
    public static final String COLUMN_PAY = "pay";
    public static final String COLUMN_DEPARTMENTID = "department_id";
    private static final String COLUMNS = "(" + COLUMN_NAME + ", " + COLUMN_POSITION + ", " + COLUMN_PAY + ", " + COLUMN_DEPARTMENTID + ")";

    @Override
    public List<Person> getAll() throws SQLException {
        List<Person> personnel = new ArrayList<>();
        Statement statement = DBHelper.getStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM " + DB_NAME + ";");
        while (result.next()) {
            Person person = new Person();
            person.setId(result.getInt(COLUMN_ID));
            person.setName(result.getString(COLUMN_NAME));
            person.setPosition(result.getString(COLUMN_POSITION));
            person.setPay(result.getInt(COLUMN_PAY));
            result.getInt(COLUMN_DEPARTMENTID);
            personnel.add(person);
        }
        result.close();
        DBHelper.closeStatement(statement);
        return personnel;
    }

    @Override
    public Person getById(int id) throws SQLException {
        Person person = new Person();
        Statement statement = DBHelper.getStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM " + DB_NAME + " WHERE " + COLUMN_ID + " = " + id + " ;");
        if (result.next()) {
            person.setId(result.getInt(COLUMN_ID));
            person.setName(result.getString(COLUMN_NAME));
            person.setPosition(result.getString(COLUMN_POSITION));
            person.setPay(result.getInt(COLUMN_PAY));
            person.getDepartment().setId(result.getInt(COLUMN_DEPARTMENTID));
        }
        result.close();
        DBHelper.closeStatement(statement);
        return person;
    }

    public Person getByDepartmentId(int departmentId) throws SQLException {
        Person person = new Person();
        Statement statement = DBHelper.getStatement();
        ResultSet result = statement.executeQuery("SELECT * FROM " + DB_NAME + " WHERE " + COLUMN_DEPARTMENTID + " = " + departmentId + " ;");
        if (result.next()) {
            person.setId(result.getInt(COLUMN_ID));
            person.setName(result.getString(COLUMN_NAME));
            person.setPosition(result.getString(COLUMN_POSITION));
            person.setPay(result.getInt(COLUMN_PAY));
            person.getDepartment().setId(departmentId);
        }
        result.close();
        DBHelper.closeStatement(statement);
        return person;
    }

    @Override
    public int add(Person object) throws SQLException {
        int insertedIndex = 0;
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
        return insertedIndex;
    }

    public void update(Person object) throws SQLException {
        String query = "UPDATE " + DB_NAME
                + " set " + COLUMN_NAME + " = '" + object.getName()
                + "', " + COLUMN_POSITION + " = '" + object.getPosition()
                + "', " + COLUMN_PAY + " = " + object.getPay()
                + ", " + COLUMN_DEPARTMENTID + " = " + object.getDepartment().getId()
                + " where " + COLUMN_ID + " = " + object.getId() + ";";
        update(query);
    }

    public void delete(Person object) throws SQLException {
        String query = "DELETE from " + DB_NAME + " where " + COLUMN_ID + " = " + object.getId() + ";";
        delete(query);
    }

}
