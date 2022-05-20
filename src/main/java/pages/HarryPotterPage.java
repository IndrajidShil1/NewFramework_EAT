package pages;

import base.CommonAPI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HarryPotterPage extends CommonAPI {

    public HarryPotterPage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = ".a-color-state.a-text-bold")
    WebElement searchForText;

    public boolean searchForPresence(){
        return isPresent(searchForText);
    }

    public String getSearchForText(){
        return getElementText(searchForText);
    }
}
