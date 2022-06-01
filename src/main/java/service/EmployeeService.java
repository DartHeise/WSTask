package service;

import lombok.Getter;
import model.Employee;
import model.SearchingParameters;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class EmployeeService {

    @Getter
    private final ArrayList<Employee> employees = new ArrayList<>();

    public void addEmployees(List<Employee> employees) {
        this.employees.addAll(employees);
    }

    public List<Employee> getAllOrdered(SearchingParameters searchParams) {
        Stream<Employee> stream = employees.stream();

        if (!StringUtils.isBlank(searchParams.getName())) {
            stream = stream.filter(x -> x.getLastName().equalsIgnoreCase(searchParams.getName())
                                || x.getFirstName().equalsIgnoreCase(searchParams.getName()));
        }
        if (!StringUtils.isBlank(searchParams.getPostId())) {
            stream = stream.filter(x -> x.getPost().getId().equals(UUID.fromString(searchParams.getPostId())));
        }

        return stream.sorted(Comparator.comparing(Employee::getLastName)
                .thenComparing(Employee::getFirstName)).toList();
    }
}
