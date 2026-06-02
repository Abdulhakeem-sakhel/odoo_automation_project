package g2_group.odoo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
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

    @DataProvider(name = "loginDataProvider")
    public Object[][] getLoginData() throws IOException {
        List<Object[]> data = new ArrayList<>();
        String csvFile = "src/test/resources/login_data.csv";
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header row

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                // Preserve trailing empty cells
                String[] row = line.split(",", -1);

                String scenarioId = row[0].trim();
                String emailValue = resolvePlaceholder(row[1]);
                String passwordValue = resolvePlaceholder(row[2]);
                boolean expectError = Boolean.parseBoolean(row[3].trim());
                boolean expectLogin = Boolean.parseBoolean(row[4].trim());
                String description = row[5].trim();

                data.add(new Object[]{scenarioId, emailValue, passwordValue, expectError, expectLogin, description});
            }
        }
        return data.toArray(new Object[0][]);
    }

    private String resolvePlaceholder(String raw) {
        if (raw == null) {
            return "";
        }
        switch (raw.trim()) {
            case "{EMAIL}":
                return email;
            case "{PASSWORD}":
                return password;
            case "{PASSWORD_UPPER}":
                return password.toUpperCase();
            case "{EMAIL_PADDED}":
                return " ".repeat(3) + email + " ".repeat(5);
            case "{LONG_EMAIL}":
                return RandomStringUtil.randomString(240) + "@gmail.com";
            default:
                return raw;
        }
    }

    @Test(dataProvider = "loginDataProvider")
    public void executeLoginTests(String scenarioId, String emailValue, String passwordValue,
                                  boolean expectError, boolean expectLogin, String description) {
        System.out.println("Running Scenario " + scenarioId + " - " + description);

        loginPage.loginFromUI(emailValue, passwordValue);

        if (expectError) {
            Assert.assertTrue(loginPage.isErrorMessage(),
                    scenarioId + " Failed: Expected error message was not displayed. (" + description + ")");
        }

        if (expectLogin) {
            Assert.assertTrue(loginPage.isLoggedIn(),
                    scenarioId + " Failed: Could not login. (" + description + ")");
        } else {
            Assert.assertFalse(loginPage.isLoggedIn(),
                    scenarioId + " Failed: Unexpected successful login. (" + description + ")");
        }
    }
}
