package g2_group.odoo;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import g2_group.odoo.util.RandomStringUtil;

public class Workflow_LoginTest extends BaseTest {
    private final String logInPath = "/web/login";
    private LoginPage loginPage;

    private String email = ConfigReader.getProperty("email");
    private String password = ConfigReader.getProperty("password");
    @Override
    protected String getPath() {
        return logInPath;
    }

    @BeforeClass
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    @BeforeMethod
    public void startClean() {
        driver.manage().deleteAllCookies();
        driver.get(BASE_URL + logInPath);
    }

    @Test(priority = 1)
    public void TC_LG_01_SuccessfulLogin() {
        loginPage.loginFromUI(email, password);
        Assert.assertTrue(loginPage.isLoggedIn(), "TC_LG_01 Failed: Could not login with valid credentials.");
    }

    @Test(priority = 2)
    public void TC_LG_02_LoginWithInvalidPassword() {
        loginPage.loginFromUI(email, "WrongPassword123");
        Assert.assertFalse(loginPage.isLoggedIn(), "TC_LG_02 Failed: Logged in with wrong password.");
    }

    @Test(priority = 3)
    public void TC_LG_03_LoginWithInvalidUsername() {
        loginPage.loginFromUI("fakeuser@gmail.com", password);
        Assert.assertTrue(loginPage.isErrorMessage());
        Assert.assertFalse(loginPage.isLoggedIn(), "TC_LG_03 Failed: Logged in with wrong username.");
    }

    @Test(priority = 4)
    public void TC_LG_04_LoginWithBlankFields() {
        loginPage.loginFromUI("", "");
        Assert.assertFalse(loginPage.isLoggedIn(), "TC_LG_04 Failed: Allowed login with blank fields.");
    }

    @Test(priority = 5)
    public void TC_LG_05_LoginWithBlankPasswordOnly() {
        loginPage.loginFromUI(email, "");
        Assert.assertFalse(loginPage.isLoggedIn(), "TC_LG_05 Failed: Allowed login with blank password.");
    }

    @Test(priority = 6)
    public void TC_LG_06_LoginWithCaseSensitivePassword() {
        loginPage.loginFromUI(email, password.toUpperCase());
        Assert.assertTrue(loginPage.isErrorMessage());
        Assert.assertFalse(loginPage.isLoggedIn(), "TC_LG_06 Failed: Password is not case-sensitive.");
    }

    @Test(priority = 7)
    public void TC_LG_07_LoginWithExtraWhitespaceInUsername() {
        loginPage.loginFromUI(" ".repeat(3) + email + " ".repeat(5), password);
        Assert.assertTrue(loginPage.isErrorMessage());
        Assert.assertFalse(loginPage.isLoggedIn(), "TC_LG_07 Failed: Allowed login with whitespaces.");
    }

    @Test(priority = 8)
    public void TC_LG_08_LoginWithSQLInjectionPayload() {
        loginPage.loginFromUI("' OR '1'='1", password);
        Assert.assertTrue(loginPage.isErrorMessage());
        Assert.assertFalse(loginPage.isLoggedIn(), "TC_LG_08 Failed: System vulnerable to SQL Injection.");
    }

    @Test(priority = 9)
    public void TC_LG_09_LoginWithMaximumCharacterBoundary() {
        String longEmail =  RandomStringUtil.randomString(240) + "@gmail.com";
        loginPage.loginFromUI(longEmail, password);
        Assert.assertTrue(loginPage.isErrorMessage());
        Assert.assertFalse(loginPage.isLoggedIn(), "TC_LG_09 Failed: System allowed login with invalid long boundary email.");
    }
}
