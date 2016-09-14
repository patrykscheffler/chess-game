package game;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PGNLoader {

    public static String readFile(String fileName) {
        InputStream in = Class.class.getResourceAsStream("/games/" + fileName + ".pgn");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        String line;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(" ");
            }

            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }

    public static List<String> moveStringsFromPGN(String pgn) {
        List<String> allMoveStrings = new ArrayList<>();

        boolean readingComment = false;
        boolean readingMove = false;

        String moveStartChars = "abcdefghrnbkqo";
        String moveChars = "12345678abcdefghrnbkqo=x+#";
        String currentMoveString = "";

        for (int i = 0; i < pgn.length(); i++) {
            String currentCharStringLower = pgn.toLowerCase().charAt(i) + "";

            String currentCharString = pgn.charAt(i) + "";
            if (currentCharStringLower.equals("[") || currentCharStringLower.equals("{")) {
                readingComment = true;
            } else if (currentCharStringLower.equals("]") || currentCharStringLower.equals("}")) {
                readingComment = false;
            }

            if (!readingComment) {
                if (readingMove) {
                    if (currentCharStringLower.equals(" ")) { // space between moves
                        allMoveStrings.add(currentMoveString);
                        currentMoveString = "";
                        readingMove = false;
                    } else if (moveChars.contains(currentCharStringLower)) {
                        currentMoveString += currentCharString;
                    }
                } else if (moveStartChars.contains(currentCharStringLower)) {
                    i--; // return to last char to begin reading at move start next iteration
                    readingMove = true;
                }

            }
        }

        if (readingMove) {
            allMoveStrings.add(currentMoveString);
        }


        return allMoveStrings;
    }

}
