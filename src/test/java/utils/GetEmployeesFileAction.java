package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Employee;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class GetEmployeesFileAction {

    public static List<Employee> getEmployeesFile(String pathName) throws IOException {
        return Arrays.asList(new ObjectMapper().readValue(new File(pathName), Employee[].class));
    }
}
