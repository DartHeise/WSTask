package actionTest;

import action.AddEmployeesAction;
import model.Employee;
import model.Post;
import org.junit.jupiter.api.Test;
import service.EmployeeService;
import service.FileService;
import service.PostService;
import utils.EmployeeMapperImplementation;
import utils.GetEmployeesFileAction;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class AddEmployeeActionTest {

    private final String pathName = "src\\test\\java\\resources\\jsons\\SortedEmployees.json";

    @Test
    public void addEmployeesFromFile() throws Exception {
        // Arrange
        List<Employee> expected;
        PostService postService = mock(PostService.class);
        FileService fileService = mock(FileService.class);
        EmployeeService employeeService = mock(EmployeeService.class);
        EmployeeMapperImplementation employeeMapper = new EmployeeMapperImplementation();
        AddEmployeesAction addEmployeesAction = new AddEmployeesAction(employeeService,
                                                                       fileService,
                                                                       postService,
                                                                       employeeMapper);
        when(fileService.getFile()).thenReturn(new File(pathName));
        when(postService.getPostByUUID(UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0")))
                .thenReturn(new Post(UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0"), "Frontend"));
        when(postService.getPostByUUID(UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707")))
                .thenReturn(new Post(UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"), "Backend"));
        expected = GetEmployeesFileAction.getEmployeesFile(pathName);
        // Act
        addEmployeesAction.addEmployeesFromFile(pathName);
        // Assert
        verify(employeeService).addEmployees(expected);
    }
}
