package jp.vmi.selenium.selenese.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class PathUtilsTest {

    @Test
    public void testSearchExecutableFiles() {
        assertThat(PathUtils.searchExecutableFile("chromedriver").size(), is(1));
    }
}
