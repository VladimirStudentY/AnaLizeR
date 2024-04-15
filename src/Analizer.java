import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Analizer {
    public static BlockingQueue<String> literaA = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> literaB = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> literaC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) throws InterruptedException {
        Thread generateText = new Thread(() ->
        {
            int i = 0;
            while (i < 10_000) {
                String text = generateText("abc", 100_000);
                try {
                    literaA.put(text);
                    literaB.put(text);
                    literaC.put(text);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                i++;
            }
        });
        generateText.start();

        Thread a = new Thread(() -> {
            String lineA = null;
            try {
                lineA = literaA.take();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted");
                return;
            }
            int maxA = -1, count = 0;
            count = lineA.length() - lineA.replace(String.valueOf("a"), "").length();
            //  maxA = (count > maxA) ? count : maxA;
            maxA = Math.max(count, maxA);
            System.out.printf("Max qty of  'a'  int all texts: %s\n", maxA);

        });
        a.start();

        Thread b = new Thread(() -> {
            String lineB = null;
            try {
                lineB = literaB.take();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted");
                return;
            }
            int maxB = -1, count = 0;
            count = lineB.length() - lineB.replace(String.valueOf("b"), "").length();
            //    maxB = (count > maxB) ? count : maxB;
            maxB = Math.max(count, maxB);
            System.out.printf("Max qty of  'b'  int all texts: %s\n", maxB);
        });
        b.start();

        Thread c = new Thread(() -> {
            String lineC = null;
            try {
                lineC = literaC.take();
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " was interrupted");
                return;
            }
            int maxC = -1, count = 0;
            count = lineC.length() - lineC.replace(String.valueOf("c"), "").length();
            //   maxC = (count > maxC) ? count : maxC;
            maxC = Math.max(count, maxC);
            System.out.printf("Max qty of  'c'  int all texts: %s\n", maxC);
        });
        c.start();

        a.join();
        b.join();
        c.join();
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}