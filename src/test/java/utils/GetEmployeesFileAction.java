package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import mapper.EmployeeMapperImpl;
import model.CreateEmployeeArgument;
import model.Employee;
import service.PostService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GetEmployeesFileAction {

    public static List<Employee> getEmployeesFile(String pathName) throws IOException {
        CreateEmployeeArgument[] employee = new ObjectMapper().readValue(new File(pathName), CreateEmployeeArgument[].class);
        List<Employee> employeesList = new ArrayList<>();
        EmployeeMapperImplementation employeeMapperImp = new EmployeeMapperImplementation();
        for (CreateEmployeeArgument argument : employee) {
            employeesList.add(employeeMapperImp.toEmployee(argument, new PostService().getPostByUUID(UUID.fromString(argument.getPostId()))));
        }

        return employeesList;
    }
}
