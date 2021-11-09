package data;

import java.util.List;

public class Department {

    private int id;
    private String name;
    private int roomNumber;
    private String phone;
    private List<Person> personnel;

    public Department() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Person> getPersonnel() {
        return personnel;
    }

    public void setPersonnel(List<Person> personnel) {
        this.personnel = personnel;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", roomNumber=" + roomNumber +
                ", phone='" + phone + '\'' +
                '}';
    }
}
