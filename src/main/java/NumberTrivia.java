import java.io.*;
import java.net.URL;
import java.util.*;

public class NumberTrivia {

    private static final Random gen = new Random();

    private static final Scanner input = new Scanner(System.in);

    private static final List<String> answerOptions = Arrays.asList("A", "B", "C", "D");

    private static final Map<String, Integer> allGameQuestionsWithAnswers = new HashMap<>();

    private static final int MAX_GAMES = 20;

    private static final int MAX_NUMBER = 999;

    public static void main(String[] args) throws IOException {
        int correctAnswers = 0;
        StringBuilder questionWithWrongAnswer = null;
        for (int i = 0; i < MAX_GAMES; i++) {
            Map<String, Integer> questionWithAnswer = getUniqueQuestionsWithAnswers();
            String question = questionWithAnswer.keySet().toArray()[0].toString();
            int answer = questionWithAnswer.get(question);
            StringBuilder questionWithIs = new StringBuilder(question).append(" is ");
            System.out.println(questionWithIs);
            String correctAnswerOption = Character.toString(gen.nextInt(answerOptions.size()) + 65);
            displayAnswerOption(correctAnswerOption, answer);
            String userAnswer;
            do {
                System.out.println("Please enter the answer(A, B, C or D):");
                userAnswer = input.next();
            } while (!answerOptions.contains(userAnswer));
            if (userAnswer.equals(correctAnswerOption)) {
                System.out.println("Congrats, Your answer is correct.");
                correctAnswers++;
            } else {
                System.out.println("Unfortunately it is not the correct answer - game over :(");
                questionWithWrongAnswer = questionWithIs.append(answer);
                break;
            }
        }
        System.out.println("You answered correctly on " + correctAnswers + " questions.");
        if (questionWithWrongAnswer != null) {
            System.out.println("The correct answer for the last question was:\n" + questionWithWrongAnswer);
        }
    }

    private static Map<String, Integer> getUniqueQuestionsWithAnswers() throws IOException {
        while (true) {
            Map<String, Integer> currentQuestionWithAnswerMap = getQuestionWithAnswer();
            if (!allGameQuestionsWithAnswers.entrySet().containsAll(currentQuestionWithAnswerMap.entrySet())) {
                allGameQuestionsWithAnswers.putAll(currentQuestionWithAnswerMap);
                return currentQuestionWithAnswerMap;
            }
        }
    }

    private static void displayAnswerOption(String correctAnswerOption, int answer) {
        for (String optionLetter : answerOptions) {
            StringBuilder answerOptionText = new StringBuilder(optionLetter);
            answerOptionText.append(": ");
            if (optionLetter.equals(correctAnswerOption)) {
                answerOptionText.append(answer);
            } else {
                int randomNumber;
                do {
                    randomNumber = gen.nextInt(MAX_NUMBER) + 1;
                } while (randomNumber == answer);
                answerOptionText.append(randomNumber);
            }
            System.out.println(answerOptionText);
        }
    }

    private static Map<String, Integer> getQuestionWithAnswer() throws IOException {
        Map<String, Integer> result = new HashMap<>();
        String apiAddress = "http://numbersapi.com/random?max=" + MAX_NUMBER + "/trivia";
        URL link = new URL(apiAddress);
        while (true) {
            BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
            String apiResult = in.readLine();
            in.close();
            try {
                int answer = Integer.parseInt(apiResult.substring(0, apiResult.indexOf(" ")));
                String question = apiResult.substring(apiResult.indexOf("t"), apiResult.length() - 1)
                        .replaceFirst("t", "T");
                result.put(question, answer);
                return result;
            } catch (Exception ignored) {
            }
        }
    }
}
