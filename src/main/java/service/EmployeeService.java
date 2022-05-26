package service;

import lombok.Getter;
import model.Employee;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class EmployeeService {

    @Getter
    private final ArrayList<Employee> employees = new ArrayList<>();

    public void addEmployees(List<Employee> employees) {
        this.employees.addAll(employees);
    }

    public List<Employee> getAllOrdered(String[] args) throws Exception {
        Stream<Employee> stream = employees.stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName));

        if (args.length == 1) { // Отсутствие аргумента для опционального поиска
            return stream.toList();
        }

        Pattern pattern = Pattern.compile("(?<lastName>.+|)_(?<firstName>.+|)/(?<postId>.+|)");
        Matcher matcher = pattern.matcher(args[1]);
        if(!matcher.find())
            throw new Exception("Incorrect searching string format. Use {lastName}_{firstName}/{postId} instead");

        String lastName = matcher.group("lastName");
        String firstName = matcher.group("firstName");
        String postId = matcher.group("postId");

        if (lastName.isBlank() && firstName.isBlank() && postId.isBlank())
            throw new Exception("Every searching string argument is blank!");

        if (!lastName.isBlank()) {
            stream = stream.filter(x -> x.getLastName().equals(lastName));
        }
        if (!firstName.isBlank()) {
            stream = stream.filter(x -> x.getFirstName().equals(firstName));
        }
        if (!postId.isBlank()) {
            stream = stream.filter(x -> x.getPost().getId().equals(UUID.fromString(postId)));
        }

        return stream.toList();
    }
}
