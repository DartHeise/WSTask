package serviceTest;

import action.AddEmployeesAction;
import model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import resources.GetEmployeesFileAction;
import service.EmployeeService;
import service.FileService;
import service.PostService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeServiceTest {

    private final String pathName = "C:\\Users\\Иван\\IdeaProjects\\WSTask_Maven\\src\\test\\java\\resources\\Employees.json";

    private String[] args;

    private EmployeeService employeeService;

    public void SetUp(String consoleArgument) throws Exception {
        employeeService = new EmployeeService();
        args = consoleArgument.split(" ");
        AddEmployeesAction addEmployeesAction = new AddEmployeesAction(employeeService,
                new FileService(),
                new PostService());
        addEmployeesAction.addEmployeesFromFile(pathName);
    }

    @Test
        public void getAllOrderedWithoutSearchingString() throws Exception {
        // Arrange
        SetUp(pathName);
        List<Employee> expected;
        List<Employee> actual;
        // Act
        expected = GetEmployeesFileAction.getEmployeesFile(pathName).stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
        actual = employeeService.getAllOrdered(args);
        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithNameOnly() throws Exception {
        // Arrange
        SetUp(String.format("%s Ivanov_Ivan/", pathName));
        List<Employee> expected;
        List<Employee> actual;
        // Act
        expected = GetEmployeesFileAction.getEmployeesFile
                ("C:\\Users\\Иван\\IdeaProjects\\WSTask_Maven\\src\\test\\java\\resources\\EmployeesWithNameIvanovIvan.json").stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
        actual = employeeService.getAllOrdered(args);
        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithIdOnly() throws Exception {
        // Arrange
        SetUp(String.format("%s _/606b99c0-b621-4f50-b0b6-58ed19ce6be1", pathName));
        List<Employee> expected;
        List<Employee> actual;
        // Act
        expected = GetEmployeesFileAction.getEmployeesFile
                ("C:\\Users\\Иван\\IdeaProjects\\WSTask_Maven\\src\\test\\java\\resources\\EmployeesWithFullstackId.json").stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
        actual = employeeService.getAllOrdered(args);
        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithNameAndId() throws Exception {
        // Arrange
        SetUp(String.format("%s Ivanov_Ivan/762d15a5-3bc9-43ef-ae96-02a680a557d0", pathName));
        List<Employee> expected;
        List<Employee> actual;
        // Act
        expected = GetEmployeesFileAction.getEmployeesFile
                ("C:\\Users\\Иван\\IdeaProjects\\WSTask_Maven\\src\\test\\java\\resources\\EmployeesWithNameIvanovIvanAndIdFrontend.json").stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
        actual = employeeService.getAllOrdered(args);
        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithFirstNameOnly() throws Exception {
        // Arrange
        SetUp(String.format("%s _Ivan/", pathName));
        List<Employee> expected;
        List<Employee> actual;
        // Act
        expected = GetEmployeesFileAction.getEmployeesFile
                        ("C:\\Users\\Иван\\IdeaProjects\\WSTask_Maven\\src\\test\\java\\resources\\EmployeesWithFirstNameIvan.json").stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
        actual = employeeService.getAllOrdered(args);
        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithLastNameOnly() throws Exception {
        // Arrange
        SetUp(String.format("%s Ivanov_/", pathName));
        List<Employee> expected;
        List<Employee> actual;
        // Act
        expected = GetEmployeesFileAction.getEmployeesFile
                        ("C:\\Users\\Иван\\IdeaProjects\\WSTask_Maven\\src\\test\\java\\resources\\EmployeesWithLastNameIvanov.json").stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
        actual = employeeService.getAllOrdered(args);
        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithLastNameAndId() throws Exception {
        // Arrange
        SetUp(String.format("%s Ivanov_/762d15a5-3bc9-43ef-ae96-02a680a557d0", pathName));
        List<Employee> expected;
        List<Employee> actual;
        // Act
        expected = GetEmployeesFileAction.getEmployeesFile
                        ("C:\\Users\\Иван\\IdeaProjects\\WSTask_Maven\\src\\test\\java\\resources\\EmployeesWithLastNameIvanovAndFrontendId.json").stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
        actual = employeeService.getAllOrdered(args);
        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithFirstNameAndId() throws Exception {
        // Arrange
        SetUp(String.format("%s _Ivan/762d15a5-3bc9-43ef-ae96-02a680a557d0", pathName));
        List<Employee> expected;
        List<Employee> actual;
        // Act
        expected = GetEmployeesFileAction.getEmployeesFile
                        ("C:\\Users\\Иван\\IdeaProjects\\WSTask_Maven\\src\\test\\java\\resources\\EmployeesWithFirstNameIvanAndFrontendId.json").stream()
                .sorted(Comparator.comparing(Employee::getLastName)
                        .thenComparing(Employee::getFirstName))
                .collect(Collectors.toList());
        actual = employeeService.getAllOrdered(args);
        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllOrderedWithoutSearchingArguments() throws Exception {
        // Arrange
        SetUp(String.format("%s _/", pathName));
        Exception exception;
        String expectedMessage = "Every searching string argument is blank!";
        // Assert
        exception = Assertions.assertThrows(Exception.class, () -> employeeService.getAllOrdered(args));
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void getAllOrderedWithIncorrectSearchingString() throws Exception {
        // Arrange
        SetUp(String.format("%s someString", pathName));
        Exception exception;
        String expectedMessage = "Incorrect searching string format. Use {lastName}_{firstName}/{postId} instead";
        // Assert
        exception = Assertions.assertThrows(Exception.class, () -> employeeService.getAllOrdered(args));
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
