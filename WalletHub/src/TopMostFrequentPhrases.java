import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TopMostFrequentPhrases {
    public static void main(String[] args) throws IOException {
        String fileName = "c:/tmp/wallethub/test.txt";
        Map<String, Integer> topPhrases = getTopMostFrequentPhrases(fileName, "|", 100); // replace to 100000

        topPhrases.entrySet().forEach(System.out::println);
    }

    private static Map<String, Integer> getTopMostFrequentPhrases(String fileName, String splitter, int top) throws IOException {
        Map<String, Integer> topPhrases = new LinkedHashMap<>();
        String splitPattern = "(^\\s*)|(\\s*\\" + splitter + "\\s*)|(\\s*$)";

        try (Stream<String> fileLines = Files.lines(Paths.get(fileName))) {
            fileLines.forEach(s ->
                    Arrays.stream(s.split(splitPattern))
                            .filter(w -> !w.isEmpty())
                            .forEach(w -> topPhrases.put(w, topPhrases.getOrDefault(w, 0) + 1)));
        }

        return topPhrases.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder(Integer::compareTo)))
                .limit(top)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (s, i) -> s, LinkedHashMap::new));
    }
}
