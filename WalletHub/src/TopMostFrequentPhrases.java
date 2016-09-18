import com.iciql.Db;
import com.iciql.Iciql.IQColumn;
import com.iciql.Iciql.IQIndex;
import com.iciql.Iciql.IQIndexes;
import com.iciql.Iciql.IQTable;
import com.sun.istack.internal.NotNull;
import org.h2.tools.DeleteDbFiles;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TopMostFrequentPhrases {
    private static Db db;
    private static final Phrase p = new Phrase();

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        db = Db.open("jdbc:h2:./phrases", "sa", "");

        String fileWithPhrases = "c:/tmp/wallethub/test.txt";
        List<Phrase> topPhrases = getTopMostFrequentPhrases(fileWithPhrases, "|", 4); // replace to 100000

        topPhrases.forEach(System.out::println);
        db.close();
        DeleteDbFiles.execute("./", "phrases", true);
    }

    private static List<Phrase> getTopMostFrequentPhrases(String fileName, String splitter, int top) throws IOException {

        String splitPattern = String.format("\\%s", splitter);

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(fileName))) {
            reader.lines().forEach(line ->
                    Arrays.stream(line.split(splitPattern))
                            .map(String::trim)
                            .filter(phrase -> !phrase.isEmpty())
                            .forEach(TopMostFrequentPhrases::put));
        }

        return db.from(p).orderByDesc(p.count).orderBy(p.text).limit(top).select();
    }

    private static void put(String phrase) {
        Phrase current = db.from(p).where(p.text).is(phrase).selectFirst();
        if (current != null)
            current.incrementCount();
        else
            current = new Phrase(phrase);
        db.merge(current);
    }

    @IQTable
    @IQIndexes({@IQIndex({"count"})})
    public static class Phrase {
        @IQColumn(primaryKey = true, length = 255, trim = true)
        private String text = "";

        @IQColumn
        private Integer count = 0;

        public Phrase() {
        }

        public Phrase(@NotNull String text) {
            this.text = text;
            ++count;
        }

        void incrementCount() {
            ++count;
        }

        public String getText() {
            return text;
        }

        public Integer getCount() {
            return count;
        }

        @Override
        public String toString() {
            return "Phrase{" +
                    "text='" + text + '\'' +
                    ", count=" + count +
                    '}';
        }
    }
}
