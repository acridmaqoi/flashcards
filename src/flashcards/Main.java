package flashcards;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    static Map<String, String> cards = new LinkedHashMap<>();
    static Map<String, Integer> failedCards = new HashMap<>();

    public static void main(String[] args) {

//        for (String str : args) {
//            IO.output("e: " + str);
//        }

        if (args.length == 2) {
            if (args[0].equals("-import")) {
                importFile(args[1]);
            }
        } else if (args.length == 4) {
            if (args[0].equals("-import")) {
                importFile(args[1]);
            } else if (args[2].equals("-import")) {
                importFile(args[3]);
            }
        }

        menu();

        if (args.length == 2) {
            if (args[0].equals("-export")) {
                exportFile(args[1]);
            }
        } else if (args.length == 4) {
            if (args[0].equals("-export")) {
                exportFile(args[1]);
            } else if (args[2].equals("-export")) {
                exportFile(args[3]);
            }
        }


    }

    public static void menu() {

        boolean quit = false;
        while (!quit) {
            IO.output("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):");
            String action = IO.input();

            switch (action) {
                case "add":
                    add();
                    break;
                case "remove":
                    remove();
                    break;
                case "import":
                    IO.output("File name:");
                    String fileName = IO.input();
                    importFile(fileName);
                    break;
                case "export":
                    IO.output("File name:");
                    fileName = IO.input();
                    exportFile(fileName);
                    break;
                case "ask":
                    ask();
                    break;
                case "exit":
                    IO.output("Bye bye!");
                    quit = true;
                    break;
                case "log":
                    IO.output("File name:");
                    fileName = IO.input();
                    IO.log(fileName);
                    break;
                case "hardest card":
                    hardestCards();
                    break;
                case "reset stats":
                    failedCards.clear();
                    IO.output("Card statistics has been reset.");
                    break;
            }

            IO.output("");
        }
    }

    public static void add() {
        IO.output("The card:");
        String question = IO.input();
        if (cards.containsKey(question)) {
            IO.output("The card \"" + question + "\" already exists.");
            return;
        }

        IO.output("The definition of the card:");
        String answer = IO.input();
        if (cards.containsValue(answer)) {
            IO.output("The definition \"" + answer + "\" already exists.");
            return;
        }

        cards.put(question, answer);
        IO.output("The pair (\"" + question + "\":\"" + answer + "\") has been added.");
    }

    public static void remove() {
        IO.output("The card:");
        String card = IO.input();

        if (cards.remove(card) == null) {
            IO.output("Can't remove \"" + card + "\": there is no such card.");
        } else {
            failedCards.remove(card);
            IO.output("The card has been removed.");
        }

    }

    public static void ask() {
        IO.output("How many times to ask?");
        int times = Integer.parseInt(IO.input());


        for (int i = 0; i < times; i++) {
            String[] pair = getRandomPair();

            String question = pair[0];
            String answer = pair[1];

            IO.output("Print the definition of \"" + question + "\":");
            String answerAttempt = IO.input();
            if (answerAttempt.equals(answer)) {
                IO.output("Correct answer.");
            } else {
                IO.output("Wrong answer. The correct one is \"" + answer + "\", you've just written the definition of \"" +
                        getKeyOrDefault(answerAttempt, "a card that doesn't exist") + "\":");
                failedCards.put(question, failedCards.getOrDefault(question, 0) + 1);
            }
        }
    }

    public static void hardestCards() {
        if (failedCards.size() == 0) {
            IO.output("There are no cards with errors.");
        } else {
            /*Map.Entry<String, Integer> hardestCard = Collections.max(failedCards.entrySet(),
                    Map.Entry.comparingByValue());*/

            List<Map.Entry<String, Integer>> hardestCards = new ArrayList<>();
            int maxValue = Collections.max(failedCards.values());
            for (Map.Entry<String, Integer> entry : failedCards.entrySet()) {
                if (entry.getValue() == maxValue) {
                    hardestCards.add(entry);
                }
            }

            System.out.print("The hardest " + (hardestCards.size() > 1 ? "cards are " : "card is "));
            int errors = 0;
            for (Map.Entry<String, Integer> entry : hardestCards) {
                System.out.print("\"" + entry.getKey() + "\"");

                if (!(hardestCards.indexOf(entry) == hardestCards.size() - 1)) {
                    System.out.print(", ");
                } else {
                    System.out.print(". ");
                }

                /*errors += entry.getValue();*/
                errors = entry.getValue();
            }

            IO.output("You have " + errors + " errors answering " + (hardestCards.size() > 1 ? "them." : "it."));
        }
    }

    public static void exportFile(String fileName) {
        File file = new File("." + File.separator + fileName);
        try (FileWriter writer = new FileWriter(file)) {
            for (String question : cards.keySet()) {
                writer.write(question + ", " + cards.get(question) + ", " + failedCards.getOrDefault(question, 0) + "\n");
            }
        } catch (IOException e) {
            System.out.printf("An exception occurs %s", e.getMessage());
        }

        IO.output(cards.size() + " cards have been saved.");
    }

    public static void importFile(String fileName) {
        File file = new File("." + File.separator + fileName);
        try (Scanner scanner = new Scanner(file)) {
            int i = 0;
            while (scanner.hasNext()) {
                String[] card = scanner.nextLine().split(",\\s");
                cards.put(card[0], card[1]);
                if (Integer.parseInt(card[2]) > 0) {
                    failedCards.put(card[0], Integer.parseInt(card[2]));
                }
                i++;
            }
            IO.output(i + " cards have been loaded.");
        } catch (IOException e) {
            IO.output("File not found: " + fileName);
        }
    }

    private static String getKeyOrDefault(String value, String def) {
        for (Map.Entry<String, String> entry : cards.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return def;
    }

    private static String[] getRandomPair() {
        Random random = new Random();

        String[] questions = cards.keySet().toArray(new String[0]);
        String[] answers = cards.values().toArray(new String[0]);
        int randomNumber = random.nextInt(questions.length);

        String randomQuestions = questions[randomNumber];
        String randomAnswers = answers[randomNumber];

        return new String[]{randomQuestions, randomAnswers};
    }
}
