import data.Department;
import data.Person;
import service.DBHelper;
import service.DepartmentHelper;
import service.PersonnelHelper;

public class Main {

    public static void main(String[] args) {

        DBHelper.initConnection();

        DepartmentHelper dhelper = new DepartmentHelper();
        PersonnelHelper phelper = new PersonnelHelper(dhelper);

        fillDB(dhelper, phelper);

        // заполнение БД
        System.out.println("====== Initial DB fill ======");
        dhelper.getAll().forEach(System.out::println);
        phelper.getAll().forEach(System.out::println);

        // апдейт полей
        System.out.println("====== update department 3 ======");
        System.out.println("====== update person 5 ======");
        Department department = dhelper.getById(3);
        department.setPhone("new_phone_who_dis");
        dhelper.update(department);
        Person person = phelper.getById(5);
        person.setPosition("BOSS");
        phelper.update(person);
        dhelper.getAll().forEach(System.out::println);
        phelper.getAll().forEach(System.out::println);

        // удаление записей
        System.out.println("====== delete department 3 ======");
        System.out.println("====== delete person 5 ======");
        phelper.delete(person);
        dhelper.delete(department, phelper);
        dhelper.getAll().forEach(System.out::println);
        phelper.getAll().forEach(System.out::println);

        // удаление департамента без работников
        System.out.println("====== delete department 11 ======");
        Department department11 = dhelper.getById(11);
        dhelper.delete(department11, phelper);
        dhelper.getAll().forEach(System.out::println);
        phelper.getAll().forEach(System.out::println);

        DBHelper.closeConnection();

    }

    private static void fillDB(DepartmentHelper dhelper, PersonnelHelper phelper) {
        for (int i = 1; i <= 10; i++) {
            addEntry(dhelper, phelper, i);
        }
        addDepartment(dhelper, "test_lonely", "no_phone", 404);
    }

    private static void addEntry(DepartmentHelper dhelper, PersonnelHelper phelper, int index) {
        Department department = addDepartment(dhelper, "test" + index, "phone" + index, index);
        addPerson(phelper, "person" + index, "position" + index, 10000 + index * 100, department);
    }

    private static Department addDepartment(DepartmentHelper dhelper, String name, String phone, Integer roomNumber) {
        Department department = new Department();
        department.setName(name);
        department.setPhone(phone);
        department.setRoomNumber(roomNumber);
        int index = dhelper.add(department);
        department.setId(index);
        return department;
    }

    private static Person addPerson(PersonnelHelper phelper, String name, String position, int pay, Department department) {
        Person person = new Person();
        person.setName(name);
        person.setPosition(position);
        person.setPay(pay);
        person.setDepartment(department);
        int index = phelper.add(person);
        person.setId(index);
        return person;
    }

}
