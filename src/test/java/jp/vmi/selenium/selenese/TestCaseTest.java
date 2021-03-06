package jp.vmi.selenium.selenese;

import org.junit.Before;
import org.junit.Test;

import jp.vmi.selenium.selenese.inject.Binder;
import jp.vmi.selenium.testutils.TestBase;
import jp.vmi.selenium.webdriver.DriverOptions;
import jp.vmi.selenium.webdriver.WebDriverManager;

import static org.junit.Assert.*;

/**
 * Test for {@link TestCase}.
 */
public class TestCaseTest extends TestBase {

    private final Runner runner = new Runner();

    /**
     * Initialize.
     */
    @Before
    public void initialize() {
        WebDriverManager manager = WebDriverManager.getInstance();
        manager.setWebDriverFactory(WebDriverManager.HTMLUNIT);
        manager.setDriverOptions(new DriverOptions());
        runner.setDriver(manager.get());
    }

    /**
     * Test of replaceVariables(String).
     */
    @Test
    public void replaceVars() {
        TestCase c = Binder.newTestCase(null, null, runner, wsr.getBaseURL());
        c.getProc().setVar("XYZ", "a");
        assertEquals("XYZ", c.getProc().replaceVars("${a}"));
    }

    /**
     * Test of replaceVariables(String[]).
     */
    @Test
    public void replaceVarsForArray() {
        TestCase c = Binder.newTestCase(null, null, runner, wsr.getBaseURL());
        c.getProc().setVar("XYZ", "a");
        assertArrayEquals(new String[] { "abc", "XYZ", "abcXYZbca" },
            c.getProc().replaceVarsForArray(new String[] { "abc", "${a}", "abc${a}bca" }));
    }
}
