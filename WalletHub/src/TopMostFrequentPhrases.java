import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TopMostFrequentPhrases {
    public static void main(String[] args) throws IOException {
        String fileName = "c:/tmp/wallethub/test.txt";
        Map<String, Integer> topPhrases = getTopMostFrequentPhrases(fileName, "|", 100); // replace to 100000

        topPhrases.entrySet().forEach(System.out::println);
    }

    private static Map<String, Integer> getTopMostFrequentPhrases(String fileName, String splitter, int top) throws IOException {
        Map<String, Integer> topPhrases = new LinkedHashMap<>();
        String splitPattern = "\\"+splitter; //"(^\\s*)|(\\s*\\" + splitter + "\\s*)|(\\s*$)";

//        try (Stream<String> fileLines = Files.lines(Paths.get(fileName))) {
//            fileLines.forEach(s ->
//                    Arrays.stream(s.split(splitPattern))
//                            .map(String::trim)
//                            .filter(w -> !w.isEmpty())
//                            .forEach(w -> topPhrases.put(w, topPhrases.getOrDefault(w, 0) + 1)));
//        }
//
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            reader.lines().forEach(line ->
                    Arrays.stream(line.split(splitPattern))
                            .map(String::trim)
                            .filter(phrase -> !phrase.isEmpty())
                            .forEach(phrase -> topPhrases.put(phrase, topPhrases.getOrDefault(phrase, 0) + 1)));
        }

        return topPhrases.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder(Integer::compareTo)))
                .limit(top)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (s, i) -> s, LinkedHashMap::new));
    }
}
