package com.fsmflying.study.quickstart2021.io;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class StartupTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:SS ZZZZ");
        DateTimeFormatter usFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:SS ZZZZ", Locale.US);
        //LocalDateTime localDateTime = LocalDateTime.now();

        ZonedDateTime zonedDateTime = ZonedDateTime.now();
        LocalDateTime localDateTime = LocalDateTime.now();

        System.out.println(formatter.format(zonedDateTime));
        System.out.println(usFormatter.format(zonedDateTime));

        assertTrue( true );
    }
}
