import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Map<UUID, Post> posts = new HashMap<>();
    private static final Pattern employeesFilePattern = Pattern.compile(
            "firstName: (?<firstName>.+)" +
                    "lastName: (?<lastName>.+)" +
                    "description: (?<description>.+|)" +
                    "characteristics: (?<characteristics>.+)" +
                    "postId: (?<postId>.+)");

    public static void main(String[] args) throws Exception {
        if (args[0] == null)
            throw new Exception("Missing console argument");
        createPosts();
        File employeesFile = new File(args[0]);
        List<String> employeesInfoMassive = read(employeesFile);
        List<Employee> employees = new ArrayList<>();
        for (String info : employeesInfoMassive) {
            employees.add(parse(info));
        }
        print(employees);
    }

    private static void createPosts() {
        posts.put(UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"),
                new Post(UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"), "Backend"));
        posts.put(UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0"),
                new Post(UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0"), "Frontend"));
        posts.put(UUID.fromString("606b99c0-b621-4f50-b0b6-58ed19ce6be1"),
                new Post(UUID.fromString("606b99c0-b621-4f50-b0b6-58ed19ce6be1"), "Fullstack"));
    }
    private static List<String> read(File file) throws IOException {
        List<String> employeesInfoList = new ArrayList<>();
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)){
            String line = bufferedReader.readLine();
            for (int i = 0; line != null; i++){
                // Добавляем строку файла в список, пока строка не является null
                employeesInfoList.add(line);
                while ((line = bufferedReader.readLine()) != null && !line.isEmpty()){
                    // Добавляем в конец элемента списка строку, пока строка не является пустой или null
                    employeesInfoList.set(i, employeesInfoList.get(i) + line);
                }
            }
        }
        return employeesInfoList;
    }

    private static Employee parse(String employee) throws Exception {
        Matcher m = employeesFilePattern.matcher(employee);
        if(!m.find())
            throw new Exception("Incorrect file: " + employee);
        Employee e = Employee.builder()
                .firstName(m.group("firstName"))
                .lastName(m.group("lastName"))
                .description(m.group("description"))
                .characteristics(Arrays.stream(m.group("characteristics").split(", ")).sorted().toList())
                .post(posts.get(UUID.fromString(m.group("postId"))))
                .build();
        if (e.getFirstName().isBlank() || e.getLastName().isBlank() || e.getCharacteristics().isEmpty() || e.getPost() == null)
            throw new Exception("Every field (except description) must be filled");
        return e;
    }

    private static void print(List<Employee> employees){
        employees.stream().
                sorted().
                forEach(System.out::println);
    }
}
