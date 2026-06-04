package g2_group.odoo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProjectTaskTest extends BaseTest {

	
	private String LoginPath = "/web/login";
	private LoginPage loginPage = null;
	private ProjectTaskPage projectTask = null;
	
	@Override
	protected String getPath() {
		// TODO Auto-generated method stub
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
	
//	@BeforeClass
//	public void initializePage() {
//		loginPage = new LoginPage(driver);
//		projectTask = new ProjectTaskPage(driver);
//	}
	

	
//	@AfterMethod
//	public void backToDashboard() {
//	    // 1. Construct the complete destination URL
//	    String destinationUrl = BASE_URL + getPath();
//	    
//	    try {
//	        // 2. Perform standard navigation
//	        driver.get(destinationUrl);
//	    } catch (Exception e) {
//	        // 3. Fallback: If an Odoo validation lock or modal overlay blocks driver.get(),
//	        // JavaScript will forcefully redirect the browser window anyway.
//	        ((JavascriptExecutor) driver).executeScript("window.location.href='" + destinationUrl + "';");
//	    }
//	}
	
//	@Test(priority=1)
//	public void CreateTask1() throws InterruptedException {
//		String taskTitle = "All Positive";
//		List<String> assignees = Arrays.asList("Fadi Abuaita");
//		String dueDate = "07/25/2026";
//		projectTask.createTask(taskTitle, dueDate, assignees);
//		driver.get(BASE_URL + getPath());
//	}
	
	
	
//	@Test(priority=2)
//	public void CreateTask2() throws InterruptedException {
//	    String taskTitle = "Urgent";
//	    List<String> assignees = Arrays.asList("Fadi Abuaita");
//	    LocalDate today = LocalDate.now();
//	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
//	    String dueDate = today.format(formatter);
//	    projectTask.createTask(taskTitle, dueDate, assignees);
//	    
//	    driver.get(BASE_URL + getPath());
//	}
	
//	@Test(priority=3)
//	public void CreateTask3() throws InterruptedException {
//		String taskTitle = "Multiple Assignee";
//		String dueDate = "05/30/2026";
//		List<String> assignees = Arrays.asList("Fadi Abuaita", "Abdulhakeem Sakhel", "Bashar Abuhwila");
//		projectTask.createTask(taskTitle, dueDate, assignees);
//		driver.get(BASE_URL + getPath());
//	}
	
//	@Test(priority=4)
//	public void CreateTask4() {
//		
//	    String errorMessage = projectTask.createTaskExpectingFailure();
//	    
//	    Assert.assertTrue(errorMessage.contains("Missing required fields"));
//	    
//	    
//	    String targetUrl = BASE_URL + getPath();
//	    
//	    try {
//	        driver.get(targetUrl);
//	    } catch (Exception e) {
//	        ((JavascriptExecutor) driver).executeScript("window.location.href='" + targetUrl + "';");
//	    }
//	}
	
//	@Test(priority=5)
//	public void CreateTask5() throws InterruptedException {
//		String taskTitle = "in the Past";
//		List<String> assignees = Arrays.asList("Fadi Abuaita");
//		String dueDate = "02/25/2026";
//		projectTask.createTask(taskTitle, dueDate, assignees);
//		
//		driver.get(BASE_URL + getPath());
//	}
	
//	@Test(priority=6)
//	public void CreateTaskWithWhitespaceTitle() {
//	    String whitespaceTitle = "     "; 
//	    
//	    String errorMessage = projectTask.createTaskWithInvalidTitle(whitespaceTitle);
//	    Assert.assertTrue(errorMessage.contains("Missing required fields"));
//	    
//	    driver.get(BASE_URL + getPath());
//	}
	
//	@Test(priority=7)
//	public void CreateTaskWithWhitespaceTitle() {
//	    String whitespaceTitle = "    ";
//	    
//	    // 1. Trigger the action
//	    projectTask.createTaskWithInvalidTitle(whitespaceTitle);
//	    
//	    // 2. Assert directly using the new boolean wait helper 
//	    // (Change "Missing required fields" to the exact string if it differs)
//	    boolean isErrorCorrect = projectTask.verifyInvalidTitleErrorVisible("Missing required fields");
//	    Assert.assertTrue(isErrorCorrect, "The expected error message was not displayed!");
//	    
//	    // 3. Clear/Redirect
//	    String targetUrl = BASE_URL + getPath();
//	    ((JavascriptExecutor) driver).executeScript("window.location.href='" + targetUrl + "';");
//	}
	
//	@Test(priority=7)
//	public void CreateTask7() throws InterruptedException {
//		String taskTitle = "!@#$%^&**";
//		List<String> assignees = Arrays.asList("Fadi Abuaita");
//		String dueDate = "07/25/2026";
//		projectTask.createTask(taskTitle, dueDate, assignees);
//		
//		driver.get(BASE_URL + getPath());
//	}
	
//	@Test(priority=8)
//	public void CreateTask8() throws InterruptedException {
//		String taskTitle = "InvalidDate";
//		List<String> assignees = Arrays.asList("Fadi Abuaita");
//		String dueDate = "99/99/9999";
//		projectTask.createTask(taskTitle, dueDate, assignees);
//		
//		driver.get(BASE_URL + getPath());
//	}
	
//	@Test(priority=9)
//	public void CreateTask9() throws InterruptedException {
//		String taskTitle = "Far FutureD";
//		List<String> assignees = Arrays.asList("Fadi Abuaita");
//		String dueDate = "03/20/2036";
//		projectTask.createTask(taskTitle, dueDate, assignees);
//		
//		driver.get(BASE_URL + getPath());
//	}
	
//	@Test(priority=10)
//	public void CreateTask10() throws InterruptedException {
//		String taskTitle = "Minimal";
//		List<String> assignees = Arrays.asList("");
//		String duoDate = "";
//		projectTask.createTask(taskTitle, duoDate, assignees);
//		projectTask.createTask(taskTitle);
//		
//		driver.get(BASE_URL + getPath());
//	}
	
}
