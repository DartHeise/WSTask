package serviceTest;

import model.Employee;
import model.Post;
import model.SearchingParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.ConsoleArgsService;
import service.EmployeeService;
import utils.GetEmployeesFileAction;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {

    private final List<Employee> employees = List.of(
            Employee.builder()
                    .firstName("Denis")
                    .lastName("Losev")
                    .description("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras sit \namet dictum felis, eu fringilla eros. Sed et gravida neque. Nullam at egestas \nerat. Mauris vitae convallis nulla. Aenean condimentum lectus magna. \nSuspendisse viverra quam non ante pellentesque, a euismod nunc dapibus. Duis \nsed congue erat")
                    .characteristics(List.of("honest", "introvert", "like criticism", "love of learning", "pragmatism"))
                    .post(new Post(UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"), "Backend"))
                    .build(),
            Employee.builder()
                    .firstName("Ivan")
                    .lastName("Ivanov")
                    .description("")
                    .characteristics(List.of("active", "cynical", "hard-working", "enthusiastic"))
                    .post(new Post(UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0"), "Frontend"))
                    .build());

    private EmployeeService employeeService;

    private ConsoleArgsService consoleArgsService;

    @BeforeEach
    public void setUp(){
        employeeService = new EmployeeService();
        consoleArgsService = mock(ConsoleArgsService.class);
        employeeService.addEmployees(employees);
    }

    @Test
    public void getAllOrderedWithoutSearchingString() throws Exception {
        // Arrange
        List<Employee> expected = GetEmployeesFileAction.getEmployeesFile
                ("src\\test\\java\\resources\\jsons\\SortedEmployees.json");
        when(consoleArgsService.getSearchingParameters())
                .thenReturn(new SearchingParameters());

        // Act
        List<Employee> actual = employeeService.getAllOrdered(consoleArgsService.getSearchingParameters());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithIdOnly() throws Exception {
        // Arrange
        List<Employee> expected = GetEmployeesFileAction.getEmployeesFile
                ("src\\test\\java\\resources\\jsons\\EmployeesWithFullstackId.json");
        when(consoleArgsService.getSearchingParameters())
                .thenReturn(new SearchingParameters(null, UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0")));

        // Act
        List<Employee> actual = employeeService.getAllOrdered(consoleArgsService.getSearchingParameters());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithFirstNameOnly() throws Exception {
        // Arrange
        List<Employee> expected = GetEmployeesFileAction.getEmployeesFile
                ("src\\test\\java\\resources\\jsons\\EmployeesWithFirstNameIvan.json");;
        when(consoleArgsService.getSearchingParameters())
                .thenReturn(new SearchingParameters("Ivan", null));

        // Act
        List<Employee> actual = employeeService.getAllOrdered(consoleArgsService.getSearchingParameters());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithLastNameOnly() throws Exception {
        // Arrange
        List<Employee> expected = GetEmployeesFileAction.getEmployeesFile
                ("src\\test\\java\\resources\\jsons\\EmployeesWithLastNameIvanov.json");;
        when(consoleArgsService.getSearchingParameters())
                .thenReturn(new SearchingParameters("Ivanov", null));

        // Act
        List<Employee> actual = employeeService.getAllOrdered(consoleArgsService.getSearchingParameters());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithLastNameAndId() throws Exception {
        // Arrange
        List<Employee> expected = GetEmployeesFileAction.getEmployeesFile
                ("src\\test\\java\\resources\\jsons\\EmployeesWithLastNameIvanovAndFrontendId.json");;
        when(consoleArgsService.getSearchingParameters())
                .thenReturn(new SearchingParameters("Ivanov", UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0")));

        // Act
        List<Employee> actual = employeeService.getAllOrdered(consoleArgsService.getSearchingParameters());

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithFirstNameAndId() throws Exception {
        // Arrange
        List<Employee> expected = GetEmployeesFileAction.getEmployeesFile
                ("src\\test\\java\\resources\\jsons\\EmployeesWithFirstNameIvanAndFrontendId.json");;
        when(consoleArgsService.getSearchingParameters())
                .thenReturn(new SearchingParameters("Ivan", UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0")));

        // Act
        List<Employee> actual = employeeService.getAllOrdered(consoleArgsService.getSearchingParameters());

        // Assert
        Assertions.assertEquals(expected, actual);
    }
}
