package assignment3;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;

//import scoreannotation.Score; // Comment out this line if not using grading script
//import testutils.NoExitSecurityManager;

/**
 * This is the sample test cases for students
 *
 */
public class SampleTest {
    private static Set<String> dict;
    private static ByteArrayOutputStream outContent;

    private static final int SHORT_TIMEOUT = 300; // ms
    private static final int SEARCH_TIMEOUT = 30000; // ms

    private SecurityManager initialSecurityManager;

    // @Rule // Comment this rule and the next line out when debugging to remove timeouts
    // public Timeout globalTimeout = new Timeout(SEARCH_TIMEOUT);

    @Before // this method is run before each test
    public void setUp() {
        Main.initialize();
        dict = Main.makeDictionary();
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        initialSecurityManager = System.getSecurityManager();
		//System.setSecurityManager(new NoExitSecurityManager());
    }

    @After
	public void cleanup() {
		System.setSecurityManager(initialSecurityManager);
	}

    private boolean verifyLadder(ArrayList<String> ladder, String start, String end) {
        String prev = null;
        if (ladder == null)
            return true;
        for (String word : ladder) {
            if (!dict.contains(word.toUpperCase()) && !dict.contains(word.toLowerCase())) {
                return false;
            }
            if (prev != null && !differByOne(prev, word))
                return false;
            prev = word;
        }
        return ladder.size() > 0
                && ladder.get(0).toLowerCase().equals(start)
                && ladder.get(ladder.size() - 1).toLowerCase().equals(end);

    }

    private static boolean differByOne(String s1, String s2) {
        if (s1.length() != s2.length())
            return false;

        int diff = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i) && diff++ > 1) {
                return false;
            }
        }

        return true;
    }
    //Test for parse method
	@Test
	// @Score(1)  // Comment out this line if not using grading script
	public void testParse() {
		String input = "hello world";
		Scanner scan = new Scanner(input);
		ArrayList<String> expected = new ArrayList<>();
		expected.add("hello");
		expected.add("world");
		ArrayList<String> res = Main.parse(scan);
		assertEquals(expected.get(0), res.get(0).toLowerCase());
		assertEquals(expected.get(1), res.get(1).toLowerCase());
	}

    // @Ignore
	@Test(timeout = SHORT_TIMEOUT)
	//@Score(1) // Comment out this line if not using grading script
	public void testParseQuit() {
		String quit = "/quit";
		Scanner scan = new Scanner(quit);
        assertNull(Main.parse(scan));
	}
    /**
     * Has Word Ladder
     **/
	@Test(timeout = SHORT_TIMEOUT)
	//@Score(1)
    public void testBFS1() {
        ArrayList<String> res = Main.getWordLadderBFS("hello", "cells");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(verifyLadder(res, "hello", "cells"));
        assertFalse(res == null || res.size() == 0 || res.size() == 2);
        assertTrue(res.size() < 6);
    }

    @Test
	//@Score(1)
    public void testDFS1() {
        ArrayList<String> res = Main.getWordLadderDFS("hello", "cells");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(verifyLadder(res, "hello", "cells"));
        assertFalse(res == null || res.size() == 0 || res.size() == 2);
    }

    /**
     * No Word Ladder
     **/
    @Test
	//@Score(1)
    public void testBFS2() {
        ArrayList<String> res = Main.getWordLadderBFS("aldol", "drawl");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(res == null || res.size() == 0 || res.size() == 2);
    }

    @Test
	//@Score(1)
    public void testDFS2() {
        ArrayList<String> res = Main.getWordLadderDFS("aldol", "drawl");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(res == null || res.size() == 0 || res.size() == 2);
    }

    @Test
	//@Score(1)
    public void testPrintLadder() {
        ArrayList<String> res = Main.getWordLadderBFS("twixt", "hakus");
        outContent.reset();
        Main.printLadder(res);
        String str = outContent.toString().replace("\n", "").replace(".", "").trim();
        assertEquals("no word ladder can be found between twixt and hakus", str);
    }

    @Test
    //@Score(1)
    public void testValidLadder() {
        ArrayList<String> testValid = new ArrayList<>();
        testValid.clear();
        testValid.add("start");
        testValid.add("smart");
        assert(Main.validLadder(testValid));
        testValid.add("hello");
        testValid.add("world");
        assert(! Main.validLadder(testValid));
    }

    @Test
    //@Score(1)
    public void testStackOverflow() {
        ArrayList<String> bfs = Main.getWordLadderDFS("iller", "nylon");
        ArrayList<String> dfs = Main.getWordLadderDFS("iller", "nylon");
        if (bfs != null) {
            HashSet<String> set = new HashSet<String>(bfs);
            assertEquals(set.size(), bfs.size());
        }
        if (dfs != null) {
            HashSet<String> set = new HashSet<String>(dfs);
            assertEquals(set.size(), dfs.size());
        }
        assertTrue(bfs == null || bfs.size() == 0 || bfs.size() == 2);
        assertTrue(dfs == null || dfs.size() == 0 || dfs.size() == 2);
    }

    @Test
    //@Score(1)
    public void testNoLadder() {
        ArrayList<String> res = Main.getWordLadderDFS("iller", "nylon");
        if (res != null) {
            HashSet<String> set = new HashSet<String>(res);
            assertEquals(set.size(), res.size());
        }
        assertTrue(res == null || res.size() == 0 || res.size() == 2);
    }

}
