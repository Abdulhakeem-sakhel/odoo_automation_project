package g2_group.odoo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import g2_group.odoo.util.ConfigReader;

import org.testng.annotations.DataProvider;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ProjectTaskTest extends BaseTest {

	
	private String LoginPath = "/web/login";
	private LoginPage loginPage = null;
	private ProjectTaskPage projectTask = null;
	
	@Override
	protected String getPath() {
		return "/odoo/project/148/tasks";
	}
	
	@BeforeClass
	public void startClean() {
		loginPage = new LoginPage(driver);
		projectTask = new ProjectTaskPage(driver);
		driver.manage().deleteAllCookies();
		String email = ConfigReader.getProperty("email");
		String password = ConfigReader.getProperty("password");
		driver.get(BASE_URL + LoginPath);
		loginPage.loginFromUI(email, password);
		Assert.assertTrue(loginPage.isLoggedIn());
		driver.get(BASE_URL + getPath());
	}


	@DataProvider(name = "taskDataProvider")
	public Object[][] getTaskData() throws IOException {
	    List<Object[]> data = new ArrayList<>();
	    String csvFile = "src/test/resources/task_data.csv"; 
	    String line;
	    
	    try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
	        br.readLine(); // Skip header row
	        
	        while ((line = br.readLine()) != null) {
	            // Split by comma, ensuring trailing empty cells are preserved
	            String[] rowData = line.split(",", -1);
	            
	            String scenarioId = rowData[0].trim();
	            String taskTitle = rowData[1]; // Keep exact spacing for whitespace testing
	            String rawAssignees = rowData[2].trim();
	            String dueDate = rowData[3].trim();
	            String expectedBehavior = rowData[4].trim();
	            
	            // 1. Handle Dynamic Date for Scenario 2
	            if ("TODAY".equalsIgnoreCase(dueDate)) {
	                dueDate = LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
	            }
	            
	            // 2. Handle Assignees list safely
	            List<String> assigneesList = new ArrayList<>();
	            if (!rawAssignees.isEmpty()) {
	                assigneesList = Arrays.asList(rawAssignees.split("\\s*;\\s*"));
	            }
	            
	            data.add(new Object[]{scenarioId, taskTitle, assigneesList, dueDate, expectedBehavior});
	        }
	    }
	    return data.toArray(new Object[0][]);
	}
	
	@Test(dataProvider = "taskDataProvider")
	public void executeTaskTests(String scenarioId, String taskTitle, List<String> assignees, String dueDate, String expectedBehavior) throws InterruptedException {
	    
	    System.out.println("Running Scenario #" + scenarioId + " - Behavior: " + expectedBehavior);
	    
	    switch (expectedBehavior) {
	        
	        case "NORMAL":
	            // Covers Scenarios: 1, 2, 3, 5, 7, 8, 9
	            projectTask.createTask(taskTitle, dueDate, assignees);
	            break;
	            
	        case "EXPECT_FAILURE":
	            // Covers Scenario: 4
	            String errorMsg = projectTask.createTaskExpectingFailure();
	            Assert.assertTrue(errorMsg.contains("Missing required fields"), "Error message mismatch in scenario 4!");
	            break;
	            
	        case "EXPECT_INVALID_TITLE":
	            // Covers Scenario: 6
	            String titleError = projectTask.createTaskWithInvalidTitle(taskTitle);
	            Assert.assertTrue(titleError.contains("Missing required fields"), "Error message mismatch in scenario 6!");
	            break;
	            
	        case "TITLE_ONLY":
	            // Covers Scenario: 10
	            projectTask.createTask(taskTitle);
	            break;
	            
	        default:
	            throw new IllegalArgumentException("Unknown behavior workflow specified in CSV: " + expectedBehavior);
	    }
	    
	    // Unified navigation cleanup after each row completes
	    String targetUrl = BASE_URL + getPath();
	    try {
	        driver.get(targetUrl);
	    } catch (Exception e) {
	        ((JavascriptExecutor) driver).executeScript("window.location.href='" + targetUrl + "';");
	    }
	}
}
