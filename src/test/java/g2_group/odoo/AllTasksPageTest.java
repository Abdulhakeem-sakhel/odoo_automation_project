package g2_group.odoo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import g2_group.odoo.util.ConfigReader;
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

    @DataProvider(name = "searchDataProvider")
    public Object[][] getSearchData() throws IOException {
        List<Object[]> data = new ArrayList<>();
        String csvFile = "src/test/resources/all_tasks_search_data.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // skip header

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] row = line.split(",", -1);

                String scenarioId         = row[0].trim();
                String searchInputsRaw    = resolvePlaceholder(row[1]);
                String postAction         = row[2].trim();
                String expectedContainsRaw = resolvePlaceholder(row[3]);
                String matchMode          = row[4].trim().toUpperCase();
                boolean expectNoResults   = Boolean.parseBoolean(row[5].trim());
                boolean checkAlert        = Boolean.parseBoolean(row[6].trim());
                String description        = row[7].trim();

                List<String> searchInputs = splitPipe(searchInputsRaw);
                List<String> expectedContains = splitPipe(expectedContainsRaw);

                data.add(new Object[]{
                    scenarioId, searchInputs, postAction, expectedContains,
                    matchMode, expectNoResults, checkAlert, description
                });
            }
        }
        return data.toArray(new Object[0][]);
    }

    private List<String> splitPipe(String raw) {
        if (raw == null || raw.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(raw.split("\\|", -1));
    }

    private String resolvePlaceholder(String raw) {
        if (raw == null) {
            return "";
        }
        switch (raw.trim()) {
            case "{LONG_STRING}":
                return RandomStringUtil.randomString(10000);
            case "{XSS_PAYLOAD}":
                return "<script>alert(\"test\")</script>";
            case "{PADDED_TASK}":
                return "  12 Task  ";
            default:
                return raw;
        }
    }

    @Test(dataProvider = "searchDataProvider")
    public void executeSearchTests(String scenarioId,
                                   List<String> searchInputs,
                                   String postAction,
                                   List<String> expectedContains,
                                   String matchMode,
                                   boolean expectNoResults,
                                   boolean checkAlert,
                                   String description) {
        System.out.println("Running Scenario " + scenarioId + " - " + description);

        for (String input : searchInputs) {
            tasksPage.searchForTask(input);
        }
        switch (postAction) {
            case "filter-unassigned":
                tasksPage.filterUnassigned();
                break;
            case "group-project":
                tasksPage.addGrouping("project_id");
                break;
            case "none":
            case "":
                break;
            default:
                Assert.fail(scenarioId + " Failed: unknown postAction '" + postAction + "'");
        }

        // 3. Assertions.
        if (expectNoResults) {
            Assert.assertTrue(tasksPage.checkNoResults(),
                scenarioId + " Failed: expected no-results message. (" + description + ")");
        }

        if (checkAlert) {
            Assert.assertFalse(tasksPage.checkAlerts(),
                scenarioId + " Failed: unexpected browser alert was triggered. (" + description + ")");
        }

        switch (matchMode) {
            case "ALL":
                Assert.assertEquals(expectedContains.size(), 1,
                    scenarioId + " Failed: ALL match mode requires exactly one expected substring.");
                Assert.assertTrue(tasksPage.allTittleContains(expectedContains.get(0)),
                    scenarioId + " Failed: not all titles contain '" + expectedContains.get(0)
                        + "'. (" + description + ")");
                break;
            case "ANY":
                Assert.assertTrue(tasksPage.TittleContainsOnOF(expectedContains),
                    scenarioId + " Failed: not every title contains one of "
                        + expectedContains + ". (" + description + ")");
                break;
            case "NONE":
                // already covered by expectNoResults
                break;
            default:
                Assert.fail(scenarioId + " Failed: unknown matchMode '" + matchMode + "'");
        }
    }

    @Test(priority = 1)
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

    @Test(priority = 3)
    public void multipleFilters() {
        tasksPage.filterUnassigned();
        String field = "priority";
        String operator = "is equal to";
        String value = "High priority";
        tasksPage.addCustomFilter(field, operator, value);
        Assert.assertTrue(tasksPage.checkPriorityFilter(value));
        Assert.assertTrue(tasksPage.checkUnassignedFilter());
    }

    @Test(priority = 5)
    public void multipleGrouping() throws InterruptedException {
        tasksPage.selectMyTasks();
        tasksPage.addGrouping("project_id");
        tasksPage.addGrouping("stage_id");
        tasksPage.expandAllGroups();
        Assert.assertTrue(tasksPage.checkGrouping(List.of(
            "Backend Upgrade", "To Do", "In Progress", "Done",
            "Frontend Revamp", "To Do", "In Progress", "Done")));
    }

    @Test(priority = 6)
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
}
