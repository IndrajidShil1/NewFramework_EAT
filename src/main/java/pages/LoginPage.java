package pages;

import base.CommonAPI;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class LoginPage extends CommonAPI {

    public LoginPage(WebDriver driver){
        PageFactory.initElements(driver, this);
    }

    public String getLoginPageTitle(){
        return getPageTitle();
    }
}
