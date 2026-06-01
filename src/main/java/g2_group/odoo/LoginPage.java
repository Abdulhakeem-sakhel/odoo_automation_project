package g2_group.odoo;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
    private By emailInputBy = new By.ByCssSelector("input#login");
    private By passwordInputBy = new By.ByCssSelector("input#password");
    private By loginButtonBy = new By.ByXPath("//button[contains(text(), 'Log in')]");
    private By projectsLinkBy = new By.ByXPath("//a[@href='/odoo/project']");
    private By errorMessageBy = new By.ByXPath("//p[@class = 'alert alert-danger' and contains(text(), 'Wrong')]");

    WebDriver driver = null;
    WebDriverWait wait = null;

    LoginPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private void enterUsername(String email) {
         wait.until(ExpectedConditions.visibilityOfElementLocated(emailInputBy)).sendKeys(email);
    }

    private void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInputBy)).sendKeys(password);
    }

    private void clickLogin() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(loginButtonBy)).click();
    }

    public void loginFromUI(String email, String password) {     
        enterUsername(email);
        enterPassword(password);
        clickLogin();
    }

    public boolean isLoggedIn() {
        try {
            // If login the href should have /odoo path
            wait.until(ExpectedConditions.urlContains("/odoo"));
            // the account we login for should have the projects app on the home screen
            wait.until(ExpectedConditions.presenceOfElementLocated(projectsLinkBy)); 
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessageBy));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
