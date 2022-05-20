package amazon;

import base.CommonAPI;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.BasePage;
import pages.HarryPotterPage;

public class TestSingleItemSearch extends CommonAPI {

    @Test
    public void searchUsingClickButton1() {
        BasePage bp = new BasePage(getDriver());
        HarryPotterPage searchJavaBookPage = new HarryPotterPage(getDriver());
        bp.searchElement("Harry Potter Book");
        Boolean searchForIsDisplayed = searchJavaBookPage.searchForPresence();
        Assert.assertTrue(searchForIsDisplayed);
    }

    @Test
    public void searchUsingClickButton2() {
        BasePage bp = new BasePage(getDriver());
        HarryPotterPage searchJavaBookPage = new HarryPotterPage(getDriver());
        bp.searchElement("Harry Potter Book");
        String resultFor = searchJavaBookPage.getSearchForText();
        System.out.println("expected results for \"Harry Potter Book\"");
        System.out.println("actual result for: "+resultFor);
        Assert.assertEquals(resultFor, "\"Harry Potter Book\"");

    }

    @Test
    public void searchUsingEnter() {
        BasePage bp = new BasePage(getDriver());
        bp.searchElementAndEnter("PS5");
    }

    @Test
    public void searchForIPhone(){
        BasePage bp = new BasePage(getDriver());
        bp.selectFromMenuDropdown("Electronics");
        bp.searchElementAndEnter("IPhone");
    }

    @Test
    public void searchForShoes(){
        BasePage bp = new BasePage(getDriver());
        bp.selectFromMenuDropdown("Men");
        bp.searchElementAndEnter("Dress Shoes");
    }

}
