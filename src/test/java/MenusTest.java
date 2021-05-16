import controller.ErrorChecker;
import controller.menus.DeckMenuController;
import controller.menus.ProfileMenuController;
import controller.menus.RegisterMenuController;
import controller.menus.ShoppingMenuController;
import models.Database;
import models.Deck;
import models.Player;
import models.cards.Card;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class MenusTest {
    static Database database = Database.getInstance();
    static RegisterMenuController registerMenuController = RegisterMenuController.getInstance();
    static ProfileMenuController profileMenuController = ProfileMenuController.getInstance();
    static ShoppingMenuController shoppingMenuController = ShoppingMenuController.getInstance();
    static DeckMenuController deckMenuController = DeckMenuController.getInstance();


    @BeforeAll
    public static void createUserTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        database.loadMonsters();
        registerMenuController.createUser("mhdi", "aliz", "1234");

    }

    //register menu tests:
    @Test
    public void duplicateUsernameTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        registerMenuController.createUser("mhdi", "aliz123", "1212334");
        Assertions.assertEquals("user with username " + "mhdi" + " already exists\n", outContent.toString());

    }

    @Test
    public void duplicateNicknameTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        registerMenuController.createUser("mhdi123", "aliz", "1212334");
        Assertions.assertEquals("user with nickname " + "aliz" + " already exists\n", outContent.toString());

    }

    @Test
    public void loginExitUserTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        registerMenuController.login("mhdis", "aliz1");
        Assertions.assertEquals("username and password didn't match!\n", outContent.toString());

    }

    @Test
    public void loginWrongPassTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        registerMenuController.login("mhdi", "aliz1");
        Assertions.assertEquals("username and password didn't match!\n", outContent.toString());

    }

    @Test
    public void loginCorrectTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        registerMenuController.login("mhdi", "1234");
        Player player = database.getPlayerByUsername("mhdi");

        Assertions.assertNotNull(player);
        Assertions.assertEquals("user loggedIn successfully!\n", outContent.toString());

    }

    //profile menu tests:
    @Test
    public void changeNicknameTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Player player = database.getPlayerByUsername("mhdi");

        profileMenuController.changeNickname(player, "mhdializ");
        Assertions.assertEquals(player.getNickname(), "mhdializ");
    }

    @Test
    public void changePassTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Player player = database.getPlayerByUsername("mhdi");

        profileMenuController.changePassword(player, "1234", "mhdi2");
        Assertions.assertEquals(player.getPassword(), "mhdi2");
    }

    //Shopping menu tests:
    @Test
    public void buyCardEnoughMoneyTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Player player = database.getPlayerByUsername("mhdi");

        player.setMoney(2800);
        shoppingMenuController.buyCard(player, "Battle Ox");
        Assertions.assertEquals("not enough money\n", outContent.toString());

    }

    @Test
    public void buyCardCorrectTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Player player = database.getPlayerByUsername("mhdi");

        player.setMoney(2900);
        shoppingMenuController.buyCard(player, "Battle Ox");
        Assertions.assertFalse(!player.getAllPlayerCard().getMainCards().contains(database.getCardByName("Battle Ox")));
        Assertions.assertEquals(player.getMoney(), 0);
        Assertions.assertEquals("Card purchased\n", outContent.toString());

    }

    //Deck menu tests:
    @Test
    public void creatDeckCorrectTest() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        Player player = database.getPlayerByUsername("mhdi");

        deckMenuController.createDeck("deck", player);
        Deck deck = database.getDeckByName("deck");

        Assertions.assertNotNull(deck);
        Assertions.assertEquals("deck created successfully!\n", outContent.toString());
        Assertions.assertFalse(ErrorChecker.isDeckNameUnique("deck"));

    }


    @Test
    public void addCardToDeckTest() {
        Player player = database.getPlayerByUsername("mhdi");
        Deck deck = database.getDeckByName("deck");
        Card card = database.getCardByName("Battle Ox");

        Assertions.assertNotNull(card);
        Assertions.assertNotNull(player);
        Assertions.assertNotNull(deck);

        player.addCardToAllPlayerCard(card);
        player.addCardToAllPlayerCard(card);

        deckMenuController.addCardToDeck("Battle Ox", "deck", player, true);
        deckMenuController.addCardToDeck("Battle Ox", "deck", player, false);

        Assertions.assertFalse(!deck.getMainCards().contains(card));
        Assertions.assertFalse(!deck.getSideCards().contains(card));

    }

    @Test
    public void removeCardFromDeckTest() {
        database.loadMonsters();
        registerMenuController.createUser("mhdi", "aliz", "1234");
        Player player = database.getPlayerByUsername("mhdi");

        deckMenuController.createDeck("deck", player);
        Deck deck = database.getDeckByName("deck");

        shoppingMenuController.buyCard(player, "Battle Ox");
        shoppingMenuController.buyCard(player, "Battle Ox");

        Card card = database.getCardByName("Battle Ox");

        Assertions.assertNotNull(card);
        Assertions.assertNotNull(player);
        Assertions.assertNotNull(deck);

        deckMenuController.addCardToDeck("Battle Ox", "deck", player, true);
        deckMenuController.addCardToDeck("Battle Ox", "deck", player, false);

        //deckMenuController.removeCardFromDeck("Battle Ox", "deck", player, true);
        //deckMenuController.removeCardFromDeck("Battle Ox", "deck", player, false);
        Assertions.assertFalse(deck.getMainCards().contains(card));
        //Assertions.assertFalse(deck.getSideCards().contains(card));

    }


}
