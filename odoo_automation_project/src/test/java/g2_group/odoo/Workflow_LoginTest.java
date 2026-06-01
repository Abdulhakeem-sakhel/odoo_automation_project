package g2_group.odoo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class Workflow_LoginTest {
    WebDriver driver;
    LoginPage loginPage;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        loginPage.navigateToLogin();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void TC_LG_01_SuccessfulLogin() {
        loginPage.enterUsername("abodbarca18@gmail.com");
        loginPage.enterPassword("ab548220-*");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isLoginSuccessful(), "TC_LG_01 Failed: Could not login with valid credentials.");
    }

    @Test(priority = 2)
    public void TC_LG_02_LoginWithInvalidPassword() {
        loginPage.enterUsername("abodbarca18@gmail.com");
        loginPage.enterPassword("WrongPassword123");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "TC_LG_02 Failed: Error message not displayed for wrong password.");
    }

    @Test(priority = 3)
    public void TC_LG_03_LoginWithInvalidUsername() {
        loginPage.enterUsername("fakeuser@gmail.com");
        loginPage.enterPassword("ab548220-*");
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "TC_LG_03 Failed: Error message not displayed for wrong username.");
    }

    @Test(priority = 4)
    public void TC_LG_04_LoginWithBlankFields() {
        loginPage.enterUsername("");
        loginPage.enterPassword("");
        loginPage.clickLogin();
        Assert.assertFalse(loginPage.isLoginSuccessful(), "TC_LG_04 Failed: Allowed login with blank fields.");
    }

    @Test(priority = 5)
    public void TC_LG_05_LoginWithBlankPasswordOnly() {
        loginPage.enterUsername("abodbarca18@gmail.com");
        loginPage.enterPassword("");
        loginPage.clickLogin();
        Assert.assertFalse(loginPage.isLoginSuccessful(), "TC_LG_05 Failed: Allowed login with blank password.");
    }

    @Test(priority = 6)
    public void TC_LG_06_LoginWithCaseSensitivePassword() {
        loginPage.enterUsername("abodbarca18@gmail.com");
        loginPage.enterPassword("AB548220-*"); 
        loginPage.clickLogin();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "TC_LG_06 Failed: Password is not case-sensitive.");
    }

    @Test(priority = 7)
    public void TC_LG_07_LoginWithExtraWhitespaceInUsername() {
        loginPage.enterUsername("  abodbarca18@gmail.com  ");
        loginPage.enterPassword("ab548220-*");
        loginPage.clickLogin();
        boolean handledSafely = loginPage.isLoginSuccessful() || loginPage.isErrorMessageDisplayed();
        Assert.assertTrue(handledSafely, "TC_LG_07 Failed: System crashed with whitespaces.");
    }

    @Test(priority = 8)
    public void TC_LG_08_LoginWithSQLInjectionPayload() {
        loginPage.enterUsername("' OR '1'='1");
        loginPage.enterPassword("ab548220-*");
        loginPage.clickLogin();
        Assert.assertFalse(loginPage.isLoginSuccessful(), "TC_LG_08 Failed: System vulnerable to SQL Injection.");
    }

    @Test(priority = 9)
    public void TC_LG_09_LoginWithMaximumCharacterBoundary() {
        String longEmail = "A".repeat(240) + "@gmail.com";
        loginPage.enterUsername(longEmail);
        loginPage.enterPassword("ab548220-*");
        loginPage.clickLogin();
        Assert.assertFalse(loginPage.isLoginSuccessful(), "TC_LG_09 Failed: System allowed login with invalid long boundary email.");
    }
}