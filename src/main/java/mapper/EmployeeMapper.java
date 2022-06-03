package mapper;

import model.CreateEmployeeArgument;
import model.Employee;
import model.Post;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper {

    Employee toEmployee(CreateEmployeeArgument argument, Post post);
}
