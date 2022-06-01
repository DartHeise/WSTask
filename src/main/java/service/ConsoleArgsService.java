package service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import model.SearchingParameters;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Getter
public class ConsoleArgsService {

    private final String[] args;

    public String getPathName() throws Exception {
        if (args.length == 0)
            throw new Exception("Missing pathname console argument");

        return args[0];
    }

    public SearchingParameters getSearchingParameters() throws Exception {
        if (args.length == 1)
            return new SearchingParameters();

        Pattern pattern = Pattern.compile("(?<name>.+|)/(?<postId>.+|)");
        Matcher matcher = pattern.matcher(args[1]);
        if(!matcher.find())
            throw new Exception("Incorrect searching string format. Use {firstName/lastName}/{postId} instead");

        return new SearchingParameters(matcher.group("name"), matcher.group("postId"));
    }
}
