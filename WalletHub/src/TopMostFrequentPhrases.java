import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

public class TopMostFrequentPhrases {
    public static void main(String[] args) throws IOException {
        String fileName = "c:/tmp/wallethub/test.txt";
        Stream<Map.Entry<String, Integer>> topPhrases = getTopMostFrequentPhrases(fileName, "|", 100); // replace to 100000

        topPhrases.forEach(System.out::println);
    }

    private static Stream<Map.Entry<String, Integer>> getTopMostFrequentPhrases(String fileName, String splitter, int top) throws IOException {
        DB db = DBMaker.tempFileDB().fileMmapEnable().make();
        Map<String, Integer> topPhrases = db.treeMap("phrases", Serializer.STRING, Serializer.INTEGER).create();

        String splitPattern = String.format("\\%s", splitter);

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            reader.lines().forEach(line ->
                    Arrays.stream(line.split(splitPattern))
                            .map(String::trim)
                            .filter(phrase -> !phrase.isEmpty())
                            .forEach(phrase -> topPhrases.put(phrase, topPhrases.getOrDefault(phrase, 0) + 1)));
        }

        return topPhrases.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder(Integer::compareTo)))
                .limit(top);
    }
}
