import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.PublicKey;

public class EffectsTest {

    @Test
    public void testMonsterRebornEffect() throws Exception {
        Assertions.assertTrue(runTestNumber("1"));
    }
    private boolean runTestNumber(String number) throws Exception {
        String parentFile = "./src/test/resources/" + number +"/";
        String deckFile = Files.readString(Path.of(parentFile + "deck.txt")),
              inputFile = Files.readString(Path.of(parentFile + "input.txt")),
             outputFile = Files.readString(Path.of(parentFile + "output.txt"));
        Files.writeString(Path.of("./src/main/resources/Database/Decks/firstDeck.json"), deckFile);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        ByteArrayInputStream in = new ByteArrayInputStream(inputFile.getBytes());
        System.setIn(in);
        Main.main(null);
        String finalOutput = outContent.toString().replace("\n" , "");
        String finalExpectedOutput = outputFile.replace("\n" , "");
        return finalExpectedOutput.equals(finalOutput.toString());
    }
}
