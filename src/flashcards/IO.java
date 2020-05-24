package flashcards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class IO {

    static List<String> lines = new ArrayList<>();

    private IO() {}

    public static void output(Object o) {
        System.out.println(o);
        lines.add(o.toString());
    }

    public static String input() {
        Scanner scanner = new Scanner(System.in);
        String data = scanner.nextLine();
        lines.add("> " + data);
        return data;
    }

    public static void log(String fileName) {
        File file = new File("." + File.separator + fileName);
        try (FileWriter writer = new FileWriter(file)) {
            for (String line : lines) {
                writer.write(line + "\n");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
