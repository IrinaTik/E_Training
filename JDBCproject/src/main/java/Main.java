import entity.Department;
import entity.Person;
import dao.DBHelper;
import service.DepartmentService;
import service.PersonnelService;

public class Main {

    public static void main(String[] args) {

        DBHelper.initConnection();

        DepartmentService dservice = new DepartmentService();
        PersonnelService pservice = new PersonnelService();

        // заполнение БД
        fillDB(dservice, pservice);
        outputAll(dservice, pservice);

        // апдейт полей
        updateSome(dservice, pservice);
        outputAll(dservice, pservice);

        // удаление записей
        deleteSome(dservice, pservice);
        outputAll(dservice, pservice);

        // удаление департамента без работников
        deleteEmptyDepartment(dservice);
        outputAll(dservice, pservice);

        DBHelper.closeConnection();

    }

    private static void deleteEmptyDepartment(DepartmentService dservice) {
        System.out.println("====== delete department 11 ======");
        Department department11 = dservice.getById(11);
        dservice.delete(department11);
    }

    private static void deleteSome(DepartmentService dservice, PersonnelService pservice) {
        System.out.println("====== delete department 3 ======");
        System.out.println("====== delete person 5 ======");
        Department department = dservice.getById(3);
        Person person = pservice.getById(5);
        pservice.delete(person);
        dservice.delete(department);
    }

    private static void updateSome(DepartmentService dservice, PersonnelService pservice) {
        System.out.println("====== update department 3 ======");
        System.out.println("====== update person 5 ======");
        Department department = dservice.getById(3);
        Person person = pservice.getById(5);
        department.setPhone("new_phone_who_dis");
        dservice.update(department);
        person.setPosition("BOSS");
        pservice.update(person);
    }

    private static void outputAll(DepartmentService dservice, PersonnelService pservice) {
        dservice.getAll().forEach(System.out::println);
        pservice.getAll().forEach(System.out::println);
    }

    private static void fillDB(DepartmentService dservice, PersonnelService pservice) {
        System.out.println("====== Initial DB fill ======");
        for (int i = 1; i <= 10; i++) {
            addEntry(dservice, pservice, i);
        }
        addDepartment(dservice, "test_lonely", "no_phone", 404);
    }

    private static void addEntry(DepartmentService dservice, PersonnelService pservice, int index) {
        Department department = addDepartment(dservice, "test" + index, "phone" + index, index);
        addPerson(pservice, "person" + index, "position" + index, 10000 + index * 100, department);
    }

    private static Department addDepartment(DepartmentService dservice, String name, String phone, Integer roomNumber) {
        Department department = new Department();
        department.setName(name);
        department.setPhone(phone);
        department.setRoomNumber(roomNumber);
        int index = dservice.add(department);
        department.setId(index);
        return department;
    }

    private static Person addPerson(PersonnelService pservice, String name, String position, int pay, Department department) {
        Person person = new Person();
        person.setName(name);
        person.setPosition(position);
        person.setPay(pay);
        person.setDepartment(department);
        int index = pservice.add(person);
        person.setId(index);
        return person;
    }

}
