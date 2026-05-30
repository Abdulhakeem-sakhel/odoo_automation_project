package g2_group.odoo;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class WF4Test extends BaseTest {

    private String Path = "/odoo/all-tasks";
    private String LoginPath = "/web/login";
    
    AllTasksPage tasksPage = null;
    LoginPage loginPage = null;
    @Override
    protected String getPath() {
        return Path;
    }

    @BeforeClass
    public void initPages() {
        tasksPage = new AllTasksPage(driver);
        loginPage = new LoginPage(driver);
    }

    @BeforeMethod
    public void startClean() {
        driver.manage().deleteAllCookies();
        
        String email = ConfigReader.getProperty("email");
        String password = ConfigReader.getProperty("password");
        
        driver.get(BASE_URL + LoginPath);
        loginPage.loginFromUI(email, password);
        Assert.assertTrue(loginPage.isLoggedIn());

        driver.get(BASE_URL + Path);
    }

    @Test
    public void searchingForTask() {
        String taskName = "API";
        tasksPage.clearSearchBox();
        tasksPage.searchForTask(taskName);
        Assert.assertTrue(tasksPage.allTittleContains(taskName));
    }
    
}
