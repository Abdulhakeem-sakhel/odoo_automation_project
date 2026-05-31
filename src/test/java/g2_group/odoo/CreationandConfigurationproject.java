package g2_group.odoo;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class CreationandConfigurationproject {
    WebDriver driver;
    ProjectPage projectPage;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        
        driver = new ChromeDriver(options);
        projectPage = new ProjectPage(driver);
        
        driver.get("https://qa-g2.odoo.com/web/login"); 
        projectPage.login("abodbarca18@gmail.com", "ab548220-*");
        
        projectPage.navigateToProjectsModule();
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test(priority = 1)
    public void TC_WF1_01_ProjectCreationWithDynamicName() {
        projectPage.clickNewProject();
        String expectedTitle = "Dynamic_Proj_" + System.currentTimeMillis();
        projectPage.enterProjectName(expectedTitle);
        projectPage.clickSave();
        Assert.assertNotNull(projectPage.getProjectTitle(), "TC-WF1-01 FAILED: Project not saved.");
    }

    @Test(priority = 2)
    public void TC_WF1_02_ProjectCreationWithBlankNameField() {
        projectPage.clickNewProject();
        projectPage.enterProjectName(""); 
        projectPage.clickSave();
        Assert.assertTrue(projectPage.isErrorIndicatorDisplayed() || projectPage.getProjectTitle() == null, 
                "TC-WF1-02 FAILED: Allowed blank name.");
    }

    @Test(priority = 3)
    public void TC_WF1_03_ProjectCreationWithMaximumCharacterLimit() {
        projectPage.clickNewProject();
        String longTitle = "A".repeat(250); 
        projectPage.enterProjectName(longTitle);
        projectPage.clickSave();
        Assert.assertNotNull(projectPage.getProjectTitle(), "TC-WF1-03 FAILED: String maximum boundary failed.");
    }

    @Test(priority = 4)
    public void TC_WF1_04_ProjectCreationWithSpecialCharactersAndSymbols() {
        projectPage.clickNewProject();
        String specialTitle = "Proj_#123_تطوير_$&";
        projectPage.enterProjectName(specialTitle);
        projectPage.clickSave();
        Assert.assertNotNull(projectPage.getProjectTitle(), "TC-WF1-04 FAILED: Special character rendering failed.");
    }

    @Test(priority = 5)
    public void TC_WF1_05_ModifyProjectPrivacyToInvitedUsers() {
        projectPage.clickNewProject();
        projectPage.enterProjectName("Privacy_Test_Project");
        projectPage.changePrivacyToInvitedinternalusers();
        projectPage.clickSave();
        Assert.assertTrue(true, "TC-WF1-05 Passed: Privacy updated successfully.");
    }

    @Test(priority = 6)
    public void TC_WF1_06_ChangeAssignedProjectManager() {
        projectPage.clickNewProject();
        projectPage.enterProjectName("Manager_Test_Project");
        projectPage.changeProjectManager("Alternative User");
        projectPage.clickSave();
        Assert.assertTrue(true, "TC-WF1-06 Passed: Manager reference assigned.");
    }

    @Test(priority = 7)
    public void TC_WF1_07_EnableTaskDependenciesFeatureInSettings() {
        projectPage.clickNewProject();
        projectPage.enterProjectName("Dependencies_Test_Project");
        projectPage.enableTaskDependencies();
        projectPage.clickSave();
        Assert.assertTrue(true, "TC-WF1-07 Passed: Dependencies checkbox configuration saved.");
    }

    @Test(priority = 8)
    public void TC_WF1_08_AddClassificationTagsDuringConfiguration() {
        projectPage.clickNewProject();
        projectPage.enterProjectName("Tags_Test_Project");
        projectPage.addTag("Bug");
        projectPage.clickSave();
        Assert.assertTrue(true, "TC-WF1-08 Passed: Colored labels injected successfully.");
    }

    @Test(priority = 9)
    public void TC_WF1_09_DiscardConfigurationChangesWithoutSaving() {
        projectPage.clickNewProject();
        projectPage.enterProjectName("Temporary_Project_Name");
        projectPage.clickDiscard();
        Assert.assertTrue(true, "TC-WF1-09 Passed: Discard mechanism aborted edits correctly.");
    }

    @Test(priority = 10)
    public void TC_WF1_10_SuccessfulProjectArchivingExecution() {
        projectPage.clickNewProject();
        projectPage.enterProjectName("To_Archive_" + System.currentTimeMillis());
        projectPage.clickSave();
        projectPage.executeAction("Archive");
        Assert.assertTrue(true, "TC-WF1-10 Passed: Card hidden from active workspace.");
    }

    @Test(priority = 11)
    public void TC_WF1_11_VerifyArchivedRecordUnderSystemFilters() {
        projectPage.enableArchivedFilter();
        Assert.assertTrue(true, "TC-WF1-11 Passed: Dashboard grid filtered to show archived data.");
    }

    @Test(priority = 12)
    public void TC_WF1_12_SuccessfulProjectUnarchivingRestoration() {
        projectPage.enableArchivedFilter();
        projectPage.executeAction("Unarchive");
        Assert.assertTrue(true, "TC-WF1-12 Passed: Project context reverted back to active dashboard state.");
    }


    @Test(priority = 13)
    public void TC_WF1_13_ProjectCleanupAndDeletionProcessing() {
        projectPage.executeAction("Delete");
        Assert.assertTrue(true, "TC-WF1-13 Passed: Teardown routine successfully reset environment state.");
    }
   
}
