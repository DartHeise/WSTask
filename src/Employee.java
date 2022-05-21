import lombok.Builder;
import lombok.Getter;

import java.util.Formatter;
import java.util.List;

@Builder
public class Employee implements Comparable<Employee> {
    @Getter
    private String firstName;
    @Getter
    private String lastName;
    @Getter
    private String description;
    @Getter
    private List<String> characteristics;
    @Getter
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
                getName(), description, String.join(", ", characteristics), post.getName());
        return f.toString();
    }

    @Override
    public int compareTo(Employee o) {
        return getName().compareTo(o.getName());
    }
}
