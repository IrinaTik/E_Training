import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    private final static int PEOPLE_COUNT = 10000;

    public static void main(String[] args) {

        int peopleCount = 0;
        List<Person> people = new ArrayList<>();
        while (peopleCount <= PEOPLE_COUNT) {
            Person person = Person.generateRandomPerson(Person.AGE_HIGH_BOUND, false);
            people.add(person);
            peopleCount = peopleCount + 1;
            // если есть дети, то добавляем их к остальным людям
            if (!person.getChildren().isEmpty()) {
                peopleCount = peopleCount +  person.getChildren().size();
                person.getChildren().forEach(child -> people.add(child));
            }
        }
//        Задания:
//        1. Вывести количество мужчин
        long maleCount = people.stream().filter(person -> person.getSex() == Sex.MALE).count();
        System.out.println("Всего " + maleCount + " мужчин");
        System.out.println("==============");

//        2. Вывести количество женщин старше 25 лет
        long femaleOlderThen25 = people.stream().filter(person -> (person.getSex() == Sex.FEMALE) && (person.getAge() > 25)).count();
        System.out.println("Всего " + femaleOlderThen25 + " женщин старше 25 лет");
        System.out.println("==============");

//        3. Вывести инфу о 10 самых возрастных людей в порядке убывания возраста
        List<Person> oldestPeople = people.stream()
                .sorted((p1, p2) -> Integer.compare(p2.getAge(), p1.getAge()))
                .limit(10)
                .collect(Collectors.toList());
        System.out.println("10 самых возрастных людей: ");
        oldestPeople.forEach(System.out::println);
        System.out.println("==============");

//        4. Вывести инфу о 25 самых низких мужчинах по убыванию веса
        List<Person> shortestMen = people.stream()
                .filter(person -> person.getSex() == Sex.MALE)
                .sorted(Comparator.comparingInt(Person::getHeight))
                .limit(25)
                .sorted((p1, p2) -> Integer.compare(p2.getWeight(), p1.getWeight()))
                .collect(Collectors.toList());
        System.out.println("25 самых низких мужчин по убыванию веса: ");
        shortestMen.forEach(System.out::println);
        System.out.println("==============");

//        5. Вывести информацию о 10 самых молодых детях
        List<Person> youngestChildren = people.stream()
                .flatMap(person -> person.getChildren().stream())
                .sorted(Comparator.comparingInt(Person::getAge))
                .limit(10)
                .collect(Collectors.toList());
        System.out.println("10 самых молодых детей: ");
        youngestChildren.forEach(System.out::println);

    }
}
