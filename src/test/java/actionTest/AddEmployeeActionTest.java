package actionTest;

import action.AddEmployeesAction;
import model.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import resources.GetEmployeesFileAction;
import service.EmployeeService;
import service.FileService;
import service.PostService;

import java.util.List;

public class AddEmployeeActionTest {

    private final String pathName = "C:\\Users\\Иван\\IdeaProjects\\WSTask_Maven\\src\\test\\java\\resources\\Employees.json";

    @Test
    public void addEmployeesFromFile() throws Exception {
        // Arrange
        List<Employee> expected;
        List<Employee> actual;
        EmployeeService employeeService = new EmployeeService();
        AddEmployeesAction addEmployeesAction = new AddEmployeesAction(employeeService,
                                                                       new FileService(),
                                                                       new PostService());
        // Act
        addEmployeesAction.addEmployeesFromFile(pathName);
        actual = employeeService.getEmployees();
        expected = GetEmployeesFileAction.getEmployeesFile(pathName);
        // Assert
        Assertions.assertEquals(expected, actual);
    }
}
