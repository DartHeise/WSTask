import lombok.Builder;

import java.util.Formatter;
import java.util.List;

@Builder
public class Employee implements Comparable<Employee> {
    private String firstName;
    private String lastName;
    private String description;
    private List<String> characteristics;
    private Post post;

    public String getName(){
        return lastName + " " + firstName;
    }

    @Override
    public String toString(){
        Formatter f = new Formatter();
        f.format("Name: %s\n"
                + "About me: %s\n"
                + "Characteristics: %s\n"
                + "Post: %s\n",
                getName(), description, String.join(", ", characteristics), post.getPostName());
        return f.toString();
    }

    @Override
    public int compareTo(Employee o) {
        return getName().compareTo(o.getName()); // Сравнение по имени и фамилии
    }
}
