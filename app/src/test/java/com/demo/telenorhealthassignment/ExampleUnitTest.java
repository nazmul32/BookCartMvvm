package com.demo.telenorhealthassignment;

import com.demo.telenorhealthassignment.util.Helper;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(Helper.isValidEmail("name@email.com"));
    }

    @Test
    public void emailValidator_IncorrectEmailSimple_ReturnsFalse() {
        assertFalse(Helper.isValidEmail("name.email.com"));
    }

    @Test
    public void passwordValidator_CorrectPasswordSimple_ReturnsTrue() {
        assertTrue(Helper.isValidEmail("1234567"));
    }

    @Test
    public void passwordValidator_IncorrectPasswordSimple_ReturnsFalse() {
        assertFalse(Helper.isValidEmail("1234"));
    }

    @Test
    public void passwordSimilarityValidator_BothCorrectPasswordSimple_ReturnsTrue() {
        assertTrue(Helper.isBothPasswordEqual("1234567", "1234567"));
    }

    @Test
    public void passwordSimilarityValidator_BothIncorrectPasswordSimple_ReturnsFalse() {
        assertFalse(Helper.isBothPasswordEqual("1234567", "123456789"));
    }

    @Test
    public void nameValidator_CorrectNameSimple_ReturnsTrue() {
        assertTrue(Helper.isValidName("nazmul"));
    }

    @Test
    public void nameValidator_IncorrectNameSimple_ReturnsFalse() {
        assertFalse(Helper.isValidName(""));
    }

    @Test
    public void nameValidator_IncorrectNameSimple_ReturnsFalse_NULL() {
        assertFalse(Helper.isValidName(null));
    }
}