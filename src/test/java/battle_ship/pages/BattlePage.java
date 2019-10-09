package battle_ship.pages;

import aquality.selenium.elements.*;
import aquality.selenium.elements.interfaces.IButton;
import aquality.selenium.elements.interfaces.ILabel;
import aquality.selenium.utils.ElementActionRetrier;
import aquality.selenium.waitings.ConditionalWait;
import battle_ship.models.Coordinate;
import org.openqa.selenium.By;

import java.util.*;

public class BattlePage {
    private static final ILabel CONNECT_TO_SERVER = new ElementFactory().getLabel(By.xpath("//div[contains(@class,'__connect-to-server')]"),
            "ConnectToServer", ElementState.DISPLAYED);
    private static final ILabel WAIT_FOR_RIVAL = new ElementFactory().getLabel(By.xpath("//div[contains(@class,'__waiting-for-rival')]"),
            "WaitForRival", ElementState.DISPLAYED);
    private static final ILabel GAME_BEGAN_OPPONENTS_TURN = new ElementFactory().getLabel(By.xpath("//div[contains(@class,'__game-started-move-off')]"),
            "GameBeganOpponentsTurn", ElementState.DISPLAYED);
    private static final ILabel YOUR_TURN = new ElementFactory().getLabel(By.xpath("//div[contains(@class,'__move-on')]"),
            "YourTurn", ElementState.DISPLAYED);
    private static final ILabel RIVAL_TURN = new ElementFactory().getLabel(By.xpath("//div[contains(@class,'notification__move-off')]"),
            "RivalTurn", ElementState.DISPLAYED);
    private static final ILabel NOTIFICATION_MESSAGE = new ElementFactory().getLabel(By.xpath("//div[@class='notification-message']"), "Notification message");
    private static final String CELL_XPATH = "//div[contains(@class,'battlefield__rival')]//div[@data-y='%d'][@data-x='%d']/parent::td";
    private static final String CELL_HIT_CLASS_NAME = "battlefield-cell__hit";
    private static final String CELL_DONE_CLASS_NAME = "battlefield-cell__done";
    private static final String CELL_LAST_CLASS_NAME = "battlefield-cell__last";
    private Random random = new Random();
    private static List<Coordinate> coordinateList = new ArrayList<>();
    private List<Coordinate> preferences = new ArrayList<>();
    private Coordinate coordinateForMove = new Coordinate(0, -3);
    private boolean flag = true;

    public BattlePage() {
        for(int i = 0; i < 10; i++){
            for(int k = 0; k < 10; k++){
                coordinateList.add(new Coordinate(i, k));
            }
        }
    }

    public String startGame(){
        CONNECT_TO_SERVER.state().waitForNotDisplayed();
        WAIT_FOR_RIVAL.state().waitForNotDisplayed();
        GAME_BEGAN_OPPONENTS_TURN.state().waitForNotDisplayed();
        clickOnCell();
        while (true){
            if (YOUR_TURN.state().waitForDisplayed()) {
                clickOnCell();
            }
            else if (!RIVAL_TURN.state().isDisplayed()) {
                break;
            }
        }
        return NOTIFICATION_MESSAGE.getText();
    }

    private void clickOnCell() {
        Coordinate coordinate;
        String cell_class;
        if(preferences.isEmpty()) {
            coordinate = calculateCoordinateForMove();
            coordinateList.remove(coordinate);
            cell_class = clickOnSelectedCell(coordinate).getAttribute("class");
        }
        else {
            coordinate = preferences.get(0);
            cell_class = clickOnSelectedCell(coordinate).getAttribute("class");
            preferences.remove(coordinate);
            if (cell_class.contains(CELL_HIT_CLASS_NAME)) {
                Coordinate pref_coordinate = null;
                for (Coordinate c : preferences) {
                    if (c.getY()==coordinate.getY() || c.getX()==coordinate.getX())
                        pref_coordinate = c;
                }
                preferences.clear();
                if (pref_coordinate != null)
                    preferences.add(pref_coordinate);
            }
        }
        int y = coordinate.getY();
        int x = coordinate.getX();
        if (cell_class.contains(CELL_HIT_CLASS_NAME)) {
            coordinateList.remove(new Coordinate(y + 1, x + 1));
            coordinateList.remove(new Coordinate(y - 1, x - 1));
            coordinateList.remove(new Coordinate(y - 1, x + 1));
            coordinateList.remove(new Coordinate(y + 1, x - 1));
            if (cell_class.contains(CELL_DONE_CLASS_NAME)) {
                coordinateList.remove(new Coordinate(y + 1, x));
                coordinateList.remove(new Coordinate(y - 1, x));
                coordinateList.remove(new Coordinate(y, x + 1));
                coordinateList.remove(new Coordinate(y, x - 1));
                preferences.clear();
            }
            else {
                addPreferences(new Coordinate(y + 1, x));
                addPreferences(new Coordinate(y - 1, x));
                addPreferences(new Coordinate(y, x + 1));
                addPreferences(new Coordinate(y, x - 1));
            }
        }
    }

    private void addPreferences(Coordinate coordinate) {
        if (coordinateList.contains(coordinate)){
            preferences.add(coordinate);
            coordinateList.remove(coordinate);
        }
    }

    private IButton clickOnSelectedCell(Coordinate coordinate){
        Formatter formatter = new Formatter();
        formatter.format(CELL_XPATH, coordinate.getY(), coordinate.getX());
        IButton cell = new ElementFactory().getButton(By.xpath(formatter.toString()), "Cell");
        ElementActionRetrier.doWithRetry(cell::click);
        ConditionalWait.waitForTrue(()->cell.getAttribute("class").contains(CELL_LAST_CLASS_NAME), "The page is loaded");
        return cell;
    }

    private Coordinate calculateCoordinateForMove() { // 0 3 - ok
                                                      // 0 12 - 1 2
        Coordinate coordinate = null;
        while (coordinate == null) {
            if (flag) {
                int y = coordinateForMove.getY();
                int x = coordinateForMove.getX() + 3;
                if (y <= 9) {

                    if (x > 9 && y < 9) {
                        coordinateForMove = new Coordinate(y + 1, x - 10);
                        if (coordinateList.contains(coordinateForMove))
                            coordinate = coordinateForMove;
                    } else if (x <= 9){
                        coordinateForMove = new Coordinate(y, x);
                        if (coordinateList.contains(coordinateForMove))
                            coordinate = coordinateForMove;
                    }
                    else {
                        flag = false;
                    }
                } else {
                    flag = false;
                }
            }
            else {
                coordinate =  coordinateList.get(random.nextInt(coordinateList.size()));
            }
        }
        return coordinate;
    }
}
