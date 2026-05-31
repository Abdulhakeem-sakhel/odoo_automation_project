package g2_group.odoo;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import g2_group.odoo.util.RandomStringUtil;

public class AllTasksPageTest extends BaseTest {

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

    @BeforeClass
    public void startClean() {
        driver.manage().deleteAllCookies();
        String email = ConfigReader.getProperty("userEmail");
        String password = ConfigReader.getProperty("userPassword");
        
        driver.get(BASE_URL + LoginPath);
        loginPage.loginFromUI(email, password);
        Assert.assertTrue(loginPage.isLoggedIn());

        driver.get(BASE_URL + Path);
    }

    @BeforeMethod
    public void clearSearchBox() {
        driver.get(BASE_URL + Path);
        tasksPage.clearSearchBox();
    }

    @Test (priority = 1)
    public void SearchingGroupingFiltering() {
        String taskName = "Database Config";
        String field = "priority";
        String operator = "is equal to";
        String value = "High priority";

        tasksPage.searchForTask(taskName);
        tasksPage.addGrouping("stage_id");
        tasksPage.addCustomFilter(field, operator, value);

        Assert.assertTrue(tasksPage.allTittleContains(taskName));
        Assert.assertTrue(tasksPage.checkGrouping(List.of("Done")));
        Assert.assertTrue(tasksPage.checkPriorityFilter(value));
    }

    @Test (priority = 2)
    public void searchingForTask() {
        String taskName = "API";

        tasksPage.searchForTask(taskName);
        Assert.assertTrue(tasksPage.allTittleContains(taskName));
    }
    
    @Test (priority = 3)
    public void multipleFilters() {

        tasksPage.filterUnassigned();
        String field = "priority";
        String operator = "is equal to";
        String value = "High priority";
        tasksPage.addCustomFilter(field, operator, value);
        Assert.assertTrue(tasksPage.checkPriorityFilter(value));
        Assert.assertTrue(tasksPage.checkUnassignedFilter());
    }
    
    @Test (priority = 4)
    public void multipleSearchTerms() {
        tasksPage.searchForTask("API");
        tasksPage.searchForTask("UI");
        Assert.assertTrue(tasksPage.TittleContainsOnOF(List.of("API", "UI")));  
    }
    @Test (priority = 5)
    public void multipleGrouping() throws InterruptedException {
        tasksPage.selectMyTasks();
        tasksPage.addGrouping("project_id");
        tasksPage.addGrouping("stage_id");
        tasksPage.expandAllGroups();
        Assert.assertTrue(tasksPage.checkGrouping(List.of(
            "Backend Upgrade", "To Do", "In Progress", "Done",
            "Frontend Revamp", "To Do", "In Progress", "Done")));
    }
    
    @Test (priority = 6)
    void saveSearchFave() {
        String field = "priority";
        String operator = "is equal to";
        String value = "High priority";
        tasksPage.addCustomFilter(field, operator, value);
        tasksPage.saveSearchFave("High Pr");
        tasksPage.clearSearchBox();
        tasksPage.clickSavedSearch("High Pr");
        Assert.assertTrue(tasksPage.checkPriorityFilter(value));
    }

    @Test (priority = 7)
    public void searchingRandomLetter() {
        String taskName = "xyz123999";
        tasksPage.searchForTask(taskName);
        Assert.assertTrue(tasksPage.checkNoResults());
    }

    @Test (priority = 8) 
    public void filteringItemThatDoesNotExist() { 
        tasksPage.searchForTask("Create Logo");
        tasksPage.filterUnassigned();
        Assert.assertTrue(tasksPage.checkNoResults());
    }

    @Test (priority = 9)
    public void groupingEmptyResult() {
        String taskName = "xyz123999";
        tasksPage.searchForTask(taskName);
        tasksPage.addGrouping("project_id");
        Assert.assertTrue(tasksPage.checkNoResults());
    }

    @Test (priority = 10)
    public void searchCaseInsensitive() {
        String taskName = "api";
        tasksPage.searchForTask(taskName);
        Assert.assertTrue(tasksPage.allTittleContains(taskName));
    }

    @Test (priority = 11)
    public void handleExtraSpace() {
        String taskName = "                12 Task             ";
        tasksPage.searchForTask(taskName);
        Assert.assertTrue(tasksPage.allTittleContains(taskName.trim()));
    }

    @Test (priority = 12)
    public void longSearchTaskName() {
        String taskName = RandomStringUtil.randomString(10000);
        tasksPage.searchForTask(taskName);
        Assert.assertTrue(tasksPage.checkNoResults());
    }

    @Test (priority = 13)
    public void simpleScriptInjection() {
        String taskName = "<script>alert(\"test\")</script>";  
        tasksPage.searchForTask(taskName);
        Assert.assertTrue(tasksPage.checkNoResults());
        Assert.assertFalse(tasksPage.checkAlerts());
    }
}
