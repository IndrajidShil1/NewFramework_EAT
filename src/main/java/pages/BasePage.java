package pages;

import base.CommonAPI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class BasePage extends CommonAPI {

    public BasePage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    @FindBy(css = "#twotabsearchtextbox")
    WebElement searchField;
    @FindBy(css = "#nav-search-submit-button")
    WebElement searchButton;
    @FindBy(xpath = "//select[@id='searchDropdownBox']")
    WebElement dropdown;
    @FindBy(css = "span[id='nav-link-accountList-nav-line-1']")
    WebElement floatingMenu;
    @FindBy(xpath = "//div[@id='nav-al-your-account']/a[5]/span")
    WebElement watchListLink;


    public void searchElement(String item){
        type(searchField, item);
        click(searchButton);
    }

    public void searchElementAndEnter(String item){
        typeAndEnter(searchField, item);
    }

    public void searchElementAndEnterAndClearField(String item){
        typeAndEnter(searchField, item);
        clear(searchField);
    }

    public void selectFromMenuDropdown(String option){
        selectFromDropdown(dropdown, option);
    }

    public void selectOptionFromMenuDropdownList(String option){
        selectOptionFromDropdownList(dropdown, option);
    }

    public void hoverOverFloatingMenu(WebDriver driver){
        hoverOver(driver, floatingMenu);
    }

    public void clickOnWatchListLink(){
        click(watchListLink);
    }
}
