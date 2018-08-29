package com.sams.test.json;

import android.util.Log;

import com.sams.test.ConsoleLogger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.FileInputStream;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.powermock.api.support.membermodification.MemberMatcher.method;
import static org.powermock.api.support.membermodification.MemberModifier.replace;

/**
 * Created by deepu on 8/24/2018.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({JsonParser.class, Log.class})
public class JsonParserTest {
    JsonParser parser;
    @BeforeClass
    public static void setUp() {
        //PowerMockito.mockStatic(Log.class);
        replace(method(Log.class, "e", String.class, String.class, Throwable.class)).
                with(method(
                        ConsoleLogger.class, "print", String.class, String.class, Throwable.class));
        replace(method(Log.class, "d", String.class, String.class)).
                with(method(
                        ConsoleLogger.class, "print", String.class, String.class));

    }
    @Before
    public void parse() throws Exception {
        parser = new JsonParser();
        boolean result = parser.parse(new FileInputStream(new File(
                "app/src/test/java/com/sams/test/json/real_Data.txt")));
        assertThat("JsonParserTest ", result, is(true));
    }

    @Test
    public void toJsonString() throws Exception {
        parser.toJsonString();
    }

}