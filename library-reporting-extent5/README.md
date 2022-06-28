## Reporting Library

#### 1. Purpose and Contents

The 'reporting' library can be used in conjunction with the 'cucumber' library and automatically generates output reports when running BDD based cucumber testing.  

The main output is a rich html report (ExtentReport 4) with test run statistics, summary and detailed test results with drilldowns, and embedded screenshots.  A supplementary text report (suitable for email) with tabular results can also be generated.  

From a test project perspective the report generators are effectively codeless.  The listener / plugin will automatically capture and output the results for the test run and require no additional results capture or results logging within the the test project itself.  

The library contains a small set of java helper classes:

|Package| Class                 | Purpose                                                      |
|-------| ----------------------| ------------------------------------------------------------ |
|adapter|ExtentCucumberAdapter  |
|adapter|TestSourcesModel       | Helper class and methids to parse the Gherkin code|
|adapter|URLOutputStream        |A stream that can write to both file and http URLs|
|service|ExtentService          |Instantiate and set up the various extent report as per extent config file entry|
|       | Hooks                 | tear down cucumber hook that marks tests as passed/failed in the ExtentReport and which picks up the paths to screenshots from the test context for failed tests and embeds in report. |
|       | ExtentProperties      | set of helper methods to set context for the ExtentReport such as report file path and name |
|       | Reporter              | set of helper methods to read information from the ExtentReport |
|       | TextReport            | helper class that is invoked after test run completion and which reads the cucumber test results from the ExtentReport and summarises the results in a plain text file that is suitable for outputting to the console/log or attaching to email. |
|       | TextReportData        | utility class used by TextReport class, that holds test data prior to writing to the text report |

#### 2. Usage

To generate the html output report within a project using the 'cucumber' library simply add the following code snippets for @CucumberOptions plugin and @BeforeClass hook to the test projects runner classes**:

```
@CucumberOptions(
        plugin = {"automation.library.reporting.ExtentCucumberFormatter:"},
        .......
        )
```

tip: create a base runner class that includes the above code snippets and then simply extend this class from all subsequent runner classes which means the above code is required to be added to the test project only once.

To generate the supplementary text report add the following code to the test project runner classes:

```
@AfterClass
public void teardown() {
    TextReport tr = new TextReport();
    tr.createReport();
}
```

create a ```extent.properties``` in the resources ```(src/test/resources)``` folder with following content

```
extent.reporter.avent.start=false
extent.reporter.bdd.start=false
extent.reporter.cards.start=false
extent.reporter.email.start=false
extent.reporter.html.start=true
extent.reporter.klov.start=false
extent.reporter.logger.start=true
extent.reporter.tabular.start=false

extent.reporter.avent.config=
extent.reporter.bdd.config=
extent.reporter.cards.config=
extent.reporter.email.config=
extent.reporter.html.config=
extent.reporter.klov.config=
extent.reporter.logger.config=
extent.reporter.tabular.config=

extent.reporter.avent.out=RunReports/AventReport/
extent.reporter.bdd.out=RunReports/BddReport/
extent.reporter.cards.out=RunReports/CardsReport/
extent.reporter.email.out=RunReports/EmailReport/ExtentEmail.html
extent.reporter.html.out=RunReports/ExtentHtml_<yyyyMMdd_HHmmss>.html
extent.reporter.logger.out=RunReports/LoggerReport/
extent.reporter.tabular.out=RunReports/TabularReport/
```

#### 3. Examples

The *sample-cucumber-api* project and *sample-cucumber-selenium* project both include the generation of the output reports.