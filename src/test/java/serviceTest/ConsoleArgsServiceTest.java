package serviceTest;

import model.SearchingParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.ConsoleArgsService;

import java.util.UUID;

public class ConsoleArgsServiceTest {

    private final String pathName = "src\\test\\java\\resources\\jsons\\NotSortedEmployees.json";

    @Test
    public void getPathName() throws Exception {
        // Arrange
        String[] args = {pathName};
        ConsoleArgsService consoleArgsService = new ConsoleArgsService(args);
        String expected = pathName;

        // Act
        String actual = consoleArgsService.getPathName();

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getPathNameWithoutPathNameArgument() {
        // Arrange
        String[] args = {};
        ConsoleArgsService consoleArgsService = new ConsoleArgsService(args);
        Exception exception;
        String expectedMessage = "Missing pathname console argument";

        // Act & Assert
        exception = Assertions.assertThrows(Exception.class, () -> consoleArgsService.getPathName());
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    public void getSearchingParameters() throws Exception {
        // Arrange
        String[] args = {pathName, "Ivanov/762d15a5-3bc9-43ef-ae96-02a680a557d0"};
        ConsoleArgsService consoleArgsService = new ConsoleArgsService(args);
        SearchingParameters expected = new SearchingParameters
                ("Ivanov", UUID.fromString("762d15a5-3bc9-43ef-ae96-02a680a557d0"));

        // Act
        SearchingParameters actual = consoleArgsService.getSearchingParameters();

        // Assert
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getSearchingParametersWithIncorrectStringFormat() {
        // Arrange
        String[] args = {pathName, "someString"};
        ConsoleArgsService consoleArgsService = new ConsoleArgsService(args);
        Exception exception;
        String expectedMessage = "Incorrect searching string format. Use {firstName/lastName}/{postId} instead";

        // Act & Assert
        exception = Assertions.assertThrows(Exception.class, () -> consoleArgsService.getSearchingParameters());
        Assertions.assertTrue(exception.getMessage().contains(expectedMessage));
    }
}
