package g2_group.odoo;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private String logInPath = "/web/login";

    private LoginPage loginPage;
    @Override
    protected String getPath() {
        return logInPath;
    }

     @BeforeClass
    public void initPage() {
        loginPage = new LoginPage(driver);
    }

    @Test
    public void userSignIn() {
        String email = ConfigReader.getProperty("email");
        String password = ConfigReader.getProperty("password");
        loginPage.loginFromUI(email, password);
        Assert.assertTrue(loginPage.isLoggedIn(), "The page is not log in");
    }
    
}
