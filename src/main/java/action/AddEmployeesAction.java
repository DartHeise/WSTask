package action;

import com.fasterxml.jackson.databind.ObjectMapper;
import mapper.EmployeeMapperImp;
import model.CreateEmployeeArgument;
import model.Employee;
import service.EmployeeService;
import service.FileService;
import service.PostService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AddEmployeesAction {

    private final EmployeeService employeeService;

    private final FileService fileService;

    private final PostService postService;

    public AddEmployeesAction(EmployeeService employeeService, FileService fileService, PostService postService){
        this.employeeService = employeeService;
        this.fileService = fileService;
        this.postService = postService;
    }

    public void addEmployeesFromFile(String pathName) throws Exception {
        fileService.addPathName(pathName);

        CreateEmployeeArgument[] createEmployeeArguments = new ObjectMapper().readValue(fileService.getFile(), CreateEmployeeArgument[].class);
        List<Employee> employees = new ArrayList<>();

        EmployeeMapperImp employeeMapperImp = new EmployeeMapperImp();
        for (CreateEmployeeArgument employeeArgument : createEmployeeArguments) {
            employees.add(employeeMapperImp.toEmployee(employeeArgument, postService.getPostByUUID(UUID.fromString(employeeArgument.getPostId()))));
        }

        employeeService.addEmployees(employees);
    }
}
