import action.AddEmployeesAction;
import mapper.EmployeeMapperImpl;
import model.SearchingParameters;
import service.ConsoleArgsService;
import service.EmployeeService;
import service.FileService;
import service.PostService;

public class Main {

    public static void main(String[] args) throws Exception {
        ConsoleArgsService consoleArgsService = new ConsoleArgsService(args);
        String pathName = consoleArgsService.getPathName();
        EmployeeService employeeService = new EmployeeService();
        AddEmployeesAction addEmployeesAction = new AddEmployeesAction(employeeService,
                                                                       new FileService(),
                                                                       new PostService(),
                                                                       new EmployeeMapperImpl());
        addEmployeesAction.addEmployeesFromFile(pathName);
        SearchingParameters searchParams = consoleArgsService.getSearchingParameters();
        employeeService.getAllOrdered(searchParams).forEach(System.out::println);
    }
}
