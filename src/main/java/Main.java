import action.AddEmployeesAction;
import service.EmployeeService;
import service.FileService;
import service.PostService;

public class Main {

    public static void main(String[] args) throws Exception {
        String pathName = getPathName(args);
        EmployeeService employeeService = new EmployeeService();
        AddEmployeesAction addEmployeesAction = new AddEmployeesAction(employeeService,
                                                                       new FileService(),
                                                                       new PostService());
        addEmployeesAction.addEmployeesFromFile(pathName);
        employeeService.getAllOrdered(args).forEach(System.out::println);
    }

    private static String getPathName(String[] args) throws Exception {
        if (args[0] == null)
            throw new Exception("Missing pathname console argument");
        return args[0];
    }
}
