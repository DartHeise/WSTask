import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    private static final Map<UUID, Post> posts = new HashMap<>();

    public static void main(String[] args) throws Exception {
        createPost(UUID.fromString("854ef89d-6c27-4635-926d-894d76a81707"), "Backend");
        createPost(UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0"), "Fullstack");
        createPost(UUID.fromString("606b99c0-b621-4f50-b0b6-58ed19ce6be1"), "Frontend");
        Scanner in = new Scanner(System.in);
        System.out.print("Write file path: ");
        File employeesFile = new File(in.nextLine()); // Файл с данными работников
        String[] employeesInfoMassive = read(employeesFile); // Считываем данные из файла в массив
        List<Employee> employees = new ArrayList<>();
        for (String info : employeesInfoMassive) {
            employees.add(parse(info)); // Парсим элементы массива и заполняем список работников
        }
        print(employees); // Выводим полученную информацию в консоль
    }

    private static void createPost(UUID uuid, String name) {
        posts.put(uuid, new Post(uuid, name)); //Добавление должности в словарь
    }
    private static String[] read(File file) throws IOException {
        List<String> employeesInfoList = new ArrayList<>();
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        for (int i = 0; line != null; i++){
            // Добавляем строку файла в список, пока строка не является null
            employeesInfoList.add(line);
            while ((line = bufferedReader.readLine()) != null && !line.isEmpty()){
                    // Добавляем в конец элемента списка строку, пока строка не является пустой или null
                    employeesInfoList.set(i, employeesInfoList.get(i) + line);
            }
        }
        fileReader.close();
        return employeesInfoList.toArray(new String[0]); // Преобразуем список в массив и возвращаем
    }

    private static Employee parse(String employee){
        Pattern pattern = Pattern.compile(
                "firstName: (?<firstName>.+)" + // Именованная группа с именем
                "lastName: (?<lastName>.+)" + // Именованная группа с фамилией
                "description: (?<description>.+|)" + // Именованная группа с описанием работника
                "characteristics: (?<characteristics>.+)" + // Именованная группа с характеристиками
                "postId: (?<postId>.+)"); // Именованная группа с кодом должности
        Matcher m =pattern.matcher(employee);
        m.find();
        return Employee // Создаем и возвращаем карточку работника
                .builder()
                .firstName(m.group("firstName"))
                .lastName(m.group("lastName"))
                .description(m.group("description"))
                .characteristics(Arrays.stream(m.group("characteristics").split(", ")).sorted().toList())
                .post(posts.get(UUID.fromString(m.group("postId"))))
                .build();
    }

    private static void print(List<Employee> employees){
        employees.stream().
                sorted(). // Сортируем работников по имени и фамилии
                forEach(System.out::println); // Выводим результат на консоль
    }
}
