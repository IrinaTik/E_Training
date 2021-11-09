import data.Department;
import service.DBHelper;
import service.DepartmentHelper;

public class Main {

    public static void main(String[] args) {

        DBHelper.initConnection();

        DepartmentHelper dhelper = new DepartmentHelper();

        System.out.println(dhelper.getAll());

        Department department = new Department();
        department.setId(1);
        department.setName("test");
        department.setPhone("phone");
        department.setRoomNumber(1);

        dhelper.add(department);

        System.out.println(dhelper.getAll());

        DBHelper.closeConnection();

    }
}
