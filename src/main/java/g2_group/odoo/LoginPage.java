package g2_group.odoo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;

    private By usernameField = By.id("login");
    private By passwordField = By.id("password");
    private By loginButton = By.xpath("//button[@type='submit']");
    
    private By errorMessage = By.xpath("//p[contains(@class, 'alert-danger')] | //div[contains(@class, 'alert-danger')] | //p[@class='alert alert-danger']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void navigateToLogin() {
        driver.get("https://qa-g2.odoo.com/web/login");
    }

    public void enterUsername(String username) {
        WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(usernameField));
        emailInput.clear();
        if (username != null && !username.isEmpty()) {
            emailInput.sendKeys(username);
        }
    }

    public void enterPassword(String password) {
        WebElement passInput = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        passInput.clear();
        if (password != null && !password.isEmpty()) {
            passInput.sendKeys(password);
        }
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();
        try { 
            Thread.sleep(2000); 
        } catch (Exception e) {}
    }

    public boolean isLoginSuccessful() {
        try {
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.contains("/web/login") && !currentUrl.contains("/web#")) {
                return false; 
            }

            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
            return shortWait.until(ExpectedConditions.urlContains("/odoo")) 
                || shortWait.until(ExpectedConditions.urlContains("/web"));
        } catch (Exception e) {
            String currentUrl = driver.getCurrentUrl();
            return currentUrl.contains("/odoo") || (currentUrl.contains("/web") && !currentUrl.contains("/login"));
        }
    }
    public boolean isErrorMessageDisplayed() {
        try {
            WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
            return alert.isDisplayed();
        } catch (Exception e) {
            try {
                List<WebElement> errors = driver.findElements(errorMessage);
                return !errors.isEmpty();
            } catch (Exception ex) {
                return false;
            }
        }
    }
}