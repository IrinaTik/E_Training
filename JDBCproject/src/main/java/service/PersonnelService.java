package service;

import dao.impl.DepartmentDAOImpl;
import dao.impl.PersonDAOImpl;
import entity.Person;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PersonnelService {

    private final DepartmentDAOImpl depDAO;
    private final PersonDAOImpl personDAO;

    public PersonnelService() {
        this.depDAO = new DepartmentDAOImpl();
        this.personDAO = new PersonDAOImpl();
    }

    public List<Person> getAll() {
        try {
            List<Person> personnel = personDAO.getAll();
            if (personnel.isEmpty()) {
                System.out.println("No personnel in DB");
            }
            for (Person person : personnel) {
                departmentCheck(person);
            }
            return personnel;
        } catch (SQLException e) {
            System.out.println("Problem with getting all personnel");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // присвоение департамента, если он есть
    private void departmentCheck(Person person) throws SQLException {
        Integer dep_id = person.getDepartment().getId();
        if (dep_id != null) {
            person.setDepartment(depDAO.getById(dep_id));
        }
    }


    public Person getById(int id) {
        try {
            Person person = personDAO.getById(id);
            if (person.getId() != null) {
                departmentCheck(person);
            } else {
                System.out.println("No personnel with id = " + id + " found");
            }
            return person;
        } catch (SQLException e) {
            System.out.println("Problem with getting personnel with id = " + id);
            e.printStackTrace();
        }
        return new Person();
    }


    public Person getByDepartmentId(int departmentId) {
        try {
            Person person = personDAO.getByDepartmentId(departmentId);
            if (person.getId() == null) {
                System.out.println("No personnel where department id = " + departmentId + " found");
            } else {
                person.setDepartment(depDAO.getById(departmentId));
            }
            return person;
        } catch (SQLException e) {
            System.out.println("Problem with getting personnel with department_id = " + departmentId);
            e.printStackTrace();
        }
        return new Person();
    }


    public int add(Person object) {
        int insertedIndex;
        try {
            insertedIndex = personDAO.add(object);
        } catch (SQLException e) {
            System.out.println("Problem with adding personnel " + object);
            insertedIndex = -1;
            e.printStackTrace();
        }
        return insertedIndex;
    }


    public void update(Person object) {
        try {
            personDAO.update(object);
        } catch (SQLException e) {
            System.out.println("Problem with updating personnel " + object);
            e.printStackTrace();
        }
    }

    public void delete(Person object) {
        try {
            personDAO.delete(object);
        } catch (SQLException e) {
            System.out.println("Problem with deleting personnel " + object);
            e.printStackTrace();
        }
    }
}
