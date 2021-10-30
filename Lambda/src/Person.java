import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Person {

    public static final int AGE_HIGH_BOUND = 100; // верхняя планка возраста
    public static final int AGE_LEGAL_BOUND = 18; // планка совершеннолетия
    public static final int HEIGHT_HIGH_BOUND = 200; // верхняя планка роста (см)
    public static final int WEIGHT_HIGH_BOUND = 120; // верхняя планка веса (кг)
    public static final int HEIGHT_LOW_BOUND = 60; // нижняя планка роста (см)
    public static final int WEIGHT_LOW_BOUND = 10; // нижняя планка веса (кг)
    public static final int MAX_CHILDREN_COUNT = 6; // максимальное количество детей

    // список имен с одинаковым количеством мужских (идут первыми) и женских (идут вторыми)
    private static final List<String> NAMES = new ArrayList<>(Arrays.asList(
            "Иван", "Владимир", "Андрей", "Сергей", "Виталий",
            "Виктор", "Алексей", "Дмитрий", "Олег", "Даниил", "Леонид",
            "Василиса", "Марина", "Валентина", "Мария", "Анастасия", "Людмила",
            "Татьяна", "Ольга", "Екатерина", "Елизавета", "Виктория"));

    private String name;
    private int age;
    private int height;
    private int weight;
    private Sex sex;
    private List<Person> children;

    public Person(String name, int age, int height, int weight, Sex sex) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
        this.children = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public List<Person> getChildren() {
        return children;
    }

    public void addChildren(List<Person> children) {
        this.children.addAll(children);
    }

    public void addChild(Person child) {
        this.children.add(child);
    }

    // генерация рандомного человека
    public static Person generateRandomPerson(int ageBound, boolean isChild) {
        int age = generateRandomIntValue(ageBound);
        int weight = generateRandomIntValue(WEIGHT_HIGH_BOUND) + WEIGHT_LOW_BOUND;
        int height = generateRandomIntValue(HEIGHT_HIGH_BOUND) + HEIGHT_LOW_BOUND;
        int nameIndex = generateRandomIntValue(NAMES.size());
        String name = NAMES.get(nameIndex);
        Sex sex = (nameIndex <= (NAMES.size() / 2)) ? Sex.MALE : Sex.FEMALE;
        Person person = new Person(name, age, height, weight, sex);
        // если человеку больше 18 лет и волею случая у него есть дети, то генерируем детей
        if ((age > 18) && (!isChild) && (generateRandomIntValue(2) == 1)) {
            int childrenCount = generateRandomIntValue(MAX_CHILDREN_COUNT);
            List<Person> children = Stream.generate(() -> generateRandomPerson(age - AGE_LEGAL_BOUND, true)).limit(childrenCount).collect(Collectors.toList());
            person.addChildren(children);
        }
        return person;
    }

    private static int generateRandomIntValue(int bound) {
        return new Random().nextInt(bound);
    }

    // для красивого вывода детей
    private String childrenToString() {
        if (children.isEmpty()) {
            return "детей нет";
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("дети (" + children.size() +"): ");
            for (Person child : children) {
                builder.append("\n\t").append(child.toString());
            }
            return builder.toString();
        }
    }

    @Override
    public String toString() {
        return "Имя: " + name + ", пол: " + sex.getTip() + ", возраст: " + age + ", рост: " + height + ", вес: " + weight +
                (children.isEmpty() ? ", нет детей" : ", дети (" + children.size() + ")");
//                + childrenToString();
    }
}
