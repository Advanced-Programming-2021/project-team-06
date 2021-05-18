package controller.menus;

import controller.FileWorker;
import models.Database;
import models.cards.Card;
import models.cards.Monster;
import models.cards.Spell;
import models.cards.Trap;
import view.Output;

public class ImpExpMenuController {

    private final String importDataBase = "./src/main/resources/Database/ImpExp/";


    private ImpExpMenuController() {
    }

    private static ImpExpMenuController instance;

    public static ImpExpMenuController getInstance() {
        if (instance == null)
            instance = new ImpExpMenuController();
        return instance;
    }


    public void importFromFile(String cardName) {
        String fileAddress = importDataBase + cardName;
        Card card = FileWorker.getInstance().readCardJSON(fileAddress);

        if (card.getTypeCard().equals("Monster")) {
            Monster monsterCard = FileWorker.getInstance().readMonsterJSON(fileAddress);
            Database.allMonsters.add(monsterCard);
        }

        if (card.getTypeCard().equals("Spell")) {
            Spell spellCard = FileWorker.getInstance().readSpellJSON(fileAddress);
            Database.allSpells.add(spellCard);
        }

        if (card.getTypeCard().equals("Trap")) {
            Trap trapCard = FileWorker.getInstance().readTrapJSON(fileAddress);
            Database.allTraps.add(trapCard);
        }

        Database.allCards.add(card);

    }

    public void exportToFile(String cardName) {
        String fileAddress = importDataBase + cardName;
        Card card = Database.getInstance().getCardByName(cardName);
        if (card == null) {
            Output.getInstance().showMessage("card is not exist!");
            return;
        }

        FileWorker.getInstance().writeFileTo(fileAddress, Card.class);
        Output.getInstance().showMessage("card exported!");

    }
}
