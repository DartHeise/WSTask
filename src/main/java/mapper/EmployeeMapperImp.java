package mapper;

import model.CreateEmployeeArgument;
import model.Employee;
import model.Post;

public class EmployeeMapperImp implements EmployeeMapper {

    @Override
    public Employee toEmployee(CreateEmployeeArgument argument, Post post) {
        if (argument == null && post == null)
            return null;

        Employee.EmployeeBuilder employee = Employee.builder();
        if (argument != null) {
            employee.firstName(argument.getFirstName());
            employee.lastName(argument.getLastName());
            employee.description(argument.getDescription());
            employee.characteristics(argument.getCharacteristics());
        }
        if (post != null)
            employee.post(post);

        return employee.build();
    }
}
