package action;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import mapper.EmployeeMapper;
import model.CreateEmployeeArgument;
import model.Employee;
import service.EmployeeService;
import service.FileService;
import service.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class AddEmployeesAction {

    private final EmployeeService employeeService;

    private final FileService fileService;

    private final PostService postService;

    private final EmployeeMapper employeeMapper;

    public void addEmployeesFromFile(String pathName) throws Exception {
        CreateEmployeeArgument[] employeeArguments = new ObjectMapper().readValue(fileService.getFile(pathName), CreateEmployeeArgument[].class);
        List<Employee> employees = new ArrayList<>();

        for (CreateEmployeeArgument employeeArgument : employeeArguments) {
            employees.add(employeeMapper.toEmployee(employeeArgument, postService.getPostByUUID(UUID.fromString(employeeArgument.getPostId()))));
        }

        employeeService.addEmployees(employees);
    }
}
