import com.sun.istack.internal.NotNull;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

public class KComplementary {
    public static void main(String[] args) {
        int k = -2;
        int arraySize = 10;
        int from = -5, to = 5;
        int[] array = new Random().ints(arraySize, from, to).toArray();
        stream(array).forEach(e -> System.out.print(e + " "));
        System.out.printf("%n%n");

        Map<Integer, Integer> foundPairs = getKComplementaryPairs(k, array);
        foundPairs.entrySet().forEach(System.out::println);
    }

    @NotNull
    private static Map<Integer, Integer> getKComplementaryPairs(int paramK, int[] a) {
        int min = Arrays.stream(a).min().orElse(a[0]);
        int max = Arrays.stream(a).max().orElse(a[0]);
        int shift = (min < 0 ) ? -min : 0;
        int k = paramK + shift + shift;

        Integer[][] p = new Integer[(max - min) + 1][2];
        for (Integer element : a) {
            Integer i = element + shift;
            if (i <= k) {
                try {
                    if (p[k - i][0] + i == k) {
                        p[k - i][1] = i;
                    }
                } catch (Exception e) {
                    if (p[i][0] == null) {
                        p[i][0] = i;
                    }
                }
            }
        }
        return stream(p)
                .filter(e -> e[1] != null)
                .collect(Collectors.toMap(e -> e[0] - shift, e -> e[1] - shift));
    }
}
