import java.util.Random;

public class LRUBenchmark {

    public static void main(String[] args) {
        int operations = 1_000_000;
        LRUCache<Integer, Integer> cache = new LRUCache<>(10000, true);
        Random random = new Random();

        long start = System.nanoTime();

        for (int i = 0; i < operations; i++) {
            int key = random.nextInt(20000);
            cache.put(key, i);

            if (i % 2 == 0) {
                cache.get(key);
            }
        }

        long end = System.nanoTime();
        double seconds = (end - start) / 1_000_000_000.0;

        System.out.println("Operations: " + operations);
        System.out.println("Time: " + seconds + " seconds");
        System.out.println("Ops/sec: " + (operations / seconds));
    }
}
