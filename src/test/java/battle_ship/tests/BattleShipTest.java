package battle_ship.tests;

import aquality.selenium.browser.BrowserManager;
import battle_ship.pages.BattlePage;
import battle_ship.pages.HomePage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import utils.ProjectSettingsReader;

import java.util.Random;


public class BattleShipTest {

    HomePage homePage = new HomePage();
    BattlePage battlePage = new BattlePage();

    @BeforeMethod
    protected void beforeMethod() {
        BrowserManager.getBrowser().maximize();
        BrowserManager.getBrowser().goTo(ProjectSettingsReader.getInstance().getSiteUrl());
    }

    @AfterMethod
    public void afterTest(){
        BrowserManager.getBrowser().quit();
    }

    @Test
    public void test(){
        homePage.chooseRandomRival();
        Random random = new Random();
        int randomNum = 1 + random.nextInt(15);
        homePage.setShipsRandomly(randomNum);
        homePage.startGame();
        String message = battlePage.startGame();
        Assert.assertTrue(message.contains(ProjectSettingsReader.getInstance().getVictoryNotificationText()), message);
    }
}
