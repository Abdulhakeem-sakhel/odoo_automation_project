# Odoo Automation Project

Automated UI test suite for the [Odoo](https://www.odoo.com/) ERP platform, built as a group project for the QA bootcamp. The suite exercises the **Login**, **Project**, **Project Task**, and **All Tasks** modules of an Odoo instance using Selenium WebDriver with the Page Object Model (POM) pattern, driven by TestNG.

## Team — Group 2

- Abdulhakeem Sakhel
- Bashar
- Fadi Abuaita
- Salah Aldin

## Tech Stack

| Layer | Tool |
|---|---|
| Language | Java |
| Build | Maven |
| UI Automation | Selenium Java `4.39.0` |
| Driver Management | WebDriverManager `6.1.1` |
| Test Runner | TestNG `7.10.2` |
| Design Pattern | Page Object Model |

## Project Structure

```
odoo/
├── docs/                                  # SRS, Test Plan, RTM
│   ├── SRS Odoo.docx
│   ├── Odoo_Test_Plan.docx
│   └── RTM-Odoo.xlsx
├── src/
│   ├── main/java/g2_group/odoo/           # Page Objects
│   │   ├── LoginPage.java
│   │   ├── ProjectPage.java
│   │   ├── ProjectTaskPage.java
│   │   ├── AllTasksPage.java
│   │   └── util/RandomStringUtil.java
│   └── test/
│       ├── java/g2_group/odoo/            # TestNG test classes
│       │   ├── BaseTest.java
│       │   ├── ConfigReader.java
│       │   ├── Workflow_LoginTest.java
│       │   ├── CreationandConfigurationproject.java
│       │   ├── ProjectTaskTest.java
│       │   └── AllTasksPageTest.java
│       └── resources/
│           ├── config.properties.template
│           └── config.properties          # (git-ignored, you create this)
│
├── test-output/                           # TestNG reports (latest run committed for reference)
│   ├── index.html                         # main report entry point
│   ├── emailable-report.html
│   ├── testng-results.xml
│   ├── OdooTestSuite/                     # per-test HTML/XML reports
│   └── junitreports/                      # JUnit-style XML reports
│
├── pom.xml
└── testng.xml                             # TestNG suite definition
```

## Prerequisites

- **JDK 21+**
- **Maven 3.6+**
- **Google Chrome** (WebDriverManager auto-downloads a matching ChromeDriver)
- A working account on the Odoo instance under test: `https://qa-g2.odoo.com`

## Setup

1. Clone the repository.
   ```bash
   git clone https://github.com/abdulhakeem-sakhel/odoo_automation_project.git
   cd odoo_automation_project
   ```

2. Create your local credentials file by copying the template:
   ```bash
   cp src/test/resources/config.properties.template src/test/resources/config.properties
   ```
   Edit `src/test/resources/config.properties` with your Odoo login:
   ```properties
   email=your-email@example.com
   password=your-password
   ```
   > ⚠️ `config.properties` is git-ignored — do **not** commit credentials.

3. Install dependencies:
   ```bash
   mvn clean install -DskipTests
   ```

## Running the Tests

Run the full TestNG suite defined in `testng.xml`:

```bash
mvn test
```

Run a single test class:

```bash
mvn test -Dtest=Workflow_LoginTest
```

Run a single test method:

```bash
mvn test -Dtest=Workflow_LoginTest#TC_LG_01_SuccessfulLogin
```

Reports are generated under `test-output/` after each run.

## Test Coverage

| Suite | Class | Focus |
|---|---|---|
| Login | `Workflow_LoginTest` | Valid/invalid login, blank fields, case sensitivity, whitespace handling, SQL injection, boundary email lengths |
| Project Creation & Configuration | `CreationandConfigurationproject` | Project CRUD, privacy settings, manager assignment, task dependencies, tags, archive/unarchive, deletion |
| Project Tasks | `ProjectTaskTest` | Task lifecycle inside a project |
| All Tasks View | `AllTasksPageTest` | Global task list interactions |

Test cases map back to the requirements in `docs/SRS Odoo.docx` via the Requirements Traceability Matrix (`docs/RTM-Odoo.xlsx`).

## Documentation

- **SRS** — `docs/SRS Odoo.docx`
- **Test Plan** — `docs/Odoo_Test_Plan.docx`
- **RTM** — `docs/RTM-Odoo.xlsx`

## Conventions

- Test classes extend `BaseTest`, which handles driver setup/teardown and navigates to the path returned by `getPath()`.
- Credentials and other environment data are read through `ConfigReader` from `config.properties`.
- Page Objects live in `src/main/java/g2_group/odoo` and expose user-level actions (e.g. `loginFromUI`, `clickNewProject`); locators stay private.
