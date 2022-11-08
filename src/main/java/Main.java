
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class Main {

    public static BlockingQueue<String> aCounterQueue = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> bCounterQueue = new ArrayBlockingQueue<>(100);
    public static BlockingQueue<String> cCounterQueue = new ArrayBlockingQueue<>(100);

    public static int stringsAmount = 10_000;


    public static void main(String[] args) {
        int stringsAmount = 10_000;


        new Thread(() -> { //поток, наполняющий очереди
            for (int i = 0; i < stringsAmount; i++) {
                try {
                    String text = generateText("abc", 100_000);
                    aCounterQueue.put(text);
                    bCounterQueue.put(text);
                    cCounterQueue.put(text);
                } catch (InterruptedException e) {
                    return;
                }
            }
        }).start();

        new Thread(() -> { //поток для подсчется максимального количества А
            maxLetterCounter('a');
        }).start();

        new Thread(() -> { //поток для подсчется максимального количества В
            maxLetterCounter('b');
        }).start();

        new Thread(() -> { //поток для подсчется максимального количества С
            maxLetterCounter('c');
        }).start();
    }

    public static void maxLetterCounter(char x) {
        int count = 0;
        String maxLetterText = null;
        for (int i = 0; i < stringsAmount; i++) {
            try {
                String text = null;
                if (x == 'a') {
                    text = aCounterQueue.take();
                } else if (x == 'b') {
                    text = bCounterQueue.take();
                } else if (x == 'c') {
                    text = cCounterQueue.take();
                }
                assert text != null;
                char[] letters = text.toCharArray();
                int letterCount = 0;
                for (char letter : letters) {
                    if (letter == x) {
                        letterCount++;
                    }
                }
                if (letterCount > count) {
                    count = letterCount;
                    maxLetterText = text;
                }
            } catch (InterruptedException e) {
                return;
            }
        }
        System.out.println("Строка с максимальным количеством символов " + x + " ( " + count + " шт.): ");
        System.out.println(maxLetterText);
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