package actionTest;

import action.AddEmployeesAction;
import mapper.EmployeeMapper;
import mapper.EmployeeMapperImpl;
import model.Employee;
import model.Post;
import org.junit.jupiter.api.Test;
import service.EmployeeService;
import service.FileService;
import service.PostService;
import utils.GetEmployeesFileAction;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class AddEmployeeActionTest {

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

    private final String pathName = "src\\test\\java\\resources\\jsons\\EmployeesForAddEmployeesAction.json";

    @Test
    public void addEmployeesFromFile() throws Exception {
        // Arrange
        List<Employee> expected;
        PostService postService = mock(PostService.class);
        FileService fileService = mock(FileService.class);
        EmployeeService employeeService = mock(EmployeeService.class);
        EmployeeMapper employeeMapper = mock(EmployeeMapperImpl.class);
        AddEmployeesAction addEmployeesAction = new AddEmployeesAction(employeeService,
                                                                       fileService,
                                                                       postService,
                                                                       employeeMapper);
        when(employeeMapper.toEmployee(any(), any())).thenReturn(employees.get(0), employees.get(1));
        when(fileService.getFile(pathName)).thenReturn(new File(pathName));
        expected = GetEmployeesFileAction.getEmployeesFile("src\\test\\java\\resources\\jsons\\NotSortedEmployees.json");

        // Act
        addEmployeesAction.addEmployeesFromFile(pathName);

        // Assert
        verify(employeeService).addEmployees(expected);
    }
}
