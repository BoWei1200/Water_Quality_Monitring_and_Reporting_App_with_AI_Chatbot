package com.example.water_quality_monitring_and_reporting_app_with_ai_chatbot;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(JUnit4.class)

public class UnitTestingTest {
    @Test
    public void validation(){
        String fName = "", lName = "";
        String result = new UnitTesting().nameValidation(fName, lName);

        assertThat(result, true);
    }
}
