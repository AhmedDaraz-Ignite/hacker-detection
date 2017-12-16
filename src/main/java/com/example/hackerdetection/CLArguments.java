package com.example.hackerdetection;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CLArguments {

    private static final String ARGUMENTS_REGEX = "--([^=]*)(=)(.*)?";
    public static final String LOG_FILE = "logfile";
    private final Map<String, String> arguments;
    private final boolean isValid;

    public CLArguments(String[] args) {
        arguments = Arrays.asList(args).stream()
                          .filter(a -> a.startsWith("--"))
                          .collect(Collectors.toMap(s -> s.replaceAll(ARGUMENTS_REGEX, "$1"),
                                  s -> s.replaceAll(ARGUMENTS_REGEX, "$3")));

        List<String> required = Arrays.asList(LOG_FILE);
        isValid = required.stream().allMatch(arguments.keySet()::contains);
    }

    public String getArgument(String argName) {
        return isValid ? arguments.get(argName) : null;
    }
}
