package battle_ship.pages;
import aquality.selenium.elements.ElementFactory;
import aquality.selenium.elements.interfaces.IButton;
import org.openqa.selenium.By;

public class HomePage {
    private static final IButton randomRivalBtn = new ElementFactory().getButton(
            By.xpath("//a[@class='battlefield-start-choose_rival-variant-link']"), "RandomRival");
    private static final IButton setShipsRandomlyBtn = new ElementFactory().getButton(
            By.xpath("//li[contains(@class,'placeships-variant__randomly')]//span[@class='placeships-variant-link']"), "SetShipRandomly");
    private static final IButton startGameBtn = new ElementFactory().getButton(
            By.xpath("//div[@class='battlefield-start-button']"), "StartGame");

    public void chooseRandomRival(){
        randomRivalBtn.click();
    }

    public void setShipsRandomly(int numberOfAttempts){
        if(numberOfAttempts > 0) {
            setShipsRandomlyBtn.click();
            numberOfAttempts--;
            setShipsRandomly(numberOfAttempts);
        }
    }

    public void startGame(){
        startGameBtn.click();
    }
}
