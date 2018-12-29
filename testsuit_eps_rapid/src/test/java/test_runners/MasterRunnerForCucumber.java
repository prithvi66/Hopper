package test_runners;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/main/resources",
        glue = "steps_definations.BasicStepDefination",
        dryRun = false,
        strict = false,
        monochrome = true

)
public class MasterRunnerForCucumber {
    @BeforeClass
    public static void beforeClass() {
        System.out.println("**********************************");
    }

    @AfterClass
    public static void afterClass() {
        System.out.println("***********************************");
    }
}
