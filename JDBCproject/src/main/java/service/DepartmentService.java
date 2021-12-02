package service;

import dao.impl.DepartmentDAOImpl;
import dao.impl.PersonDAOImpl;
import entity.Department;
import entity.Person;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentService {

    private final DepartmentDAOImpl depDAO;
    private final PersonDAOImpl personDAO;

    public DepartmentService() {
        this.depDAO = new DepartmentDAOImpl();
        this.personDAO = new PersonDAOImpl();
    }

    public List<Department> getAll() {
        try {
            List<Department> departments = depDAO.getAll();
            if (departments.isEmpty()) {
                System.out.println("No departments in DB");
            }
            return departments;
        } catch (SQLException e) {
            System.out.println("Problem with getting all departments");
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public Department getById(int id) {
        try {
            Department department = depDAO.getById(id);
            if (department.getId() == null) {
                System.out.println("No departments with id = " + id + " found");
            }
            return department;
        } catch (SQLException e) {
            System.out.println("Problem with getting department with id = " + id);
            e.printStackTrace();
        }
        return new Department();
    }


    public int add(Department object) {
        int insertedIndex;
        try {
            insertedIndex = depDAO.add(object);
        } catch (SQLException e) {
            System.out.println("Problem with adding department " + object);
            insertedIndex = -1;
            e.printStackTrace();
        }
        return insertedIndex;
    }


    public void update(Department object) {
        try {
            depDAO.update(object);
        } catch (SQLException e) {
            System.out.println("Problem with updating department " + object);
            e.printStackTrace();
        }
    }

    public void delete(Department object) {
        try {
            Person person = personDAO.getByDepartmentId(object.getId());
            if (person.getId() != null) {
                person.getDepartment().setId(null);
                personDAO.update(person);
            }
            depDAO.delete(object);
        } catch (SQLException e) {
            System.out.println("Problem with deleting department " + object);
            e.printStackTrace();
        }
    }

}
