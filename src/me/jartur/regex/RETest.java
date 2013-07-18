package me.jartur.regex;

import junit.framework.Assert;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: jt
 * Date: 7/18/13
 * Time: 11:58 PM
 */
public class RETest {
    @Test
    public void testRegexes() {
        RegexTests[] tests = new RegexTests[] {
                new RegexTests("a", new String[]{"a"}, new String[]{"b"}),
                new RegexTests("a?b", new String[]{"ab", "b"}, new String[]{"c", "a"}),
                new RegexTests("a*b", new String[]{"ab", "b", "aab", "aaaaaaab"}, new String[]{"c", "a", "aaaaaaaaaaaa"}),
                new RegexTests("a+b", new String[]{"ab", "aaaab", "aab"}, new String[]{"c", "b", "baaa"}),
                new RegexTests("a|b", new String[]{"a", "b"}, new String[]{"c"}),
                new RegexTests("a.b", new String[]{"aXb", "a+b", "a†b"}, new String[]{"ab", "c"}),
                new RegexTests("(a.+)|b", new String[]{"ax", "aß", "b", "a nice test string", "ab"}, new String[]{"zba", "z"}),
                new RegexTests("[fg]\\([xyz](,[xyz])*\\)=[xyz]([\\*-/+][xyz])*", new String[]{"f(x)=x",
                "g(y,x)=x+y", "f(x,y,z)=x*y+z/x*y"}, new String[]{"hello", "z(f)=f^2", "h(x)=x", "f(x,y)=++y"}),
                new RegexTests(".*(a*b+c?[xyz]+((cat)+|dog)s)|wow", new String[]{"anyabcxyzcatcatcats", "anybxdogs", "wow"},
                        new String[]{"acxcatwow", "bxcatdogs", "anyabcxyzdogdogs"})
        };

        for (RegexTests test : tests) {
            test.run();
        }
    }

    private static class RegexTests {
        private final String regexString;
        private final String[] shouldMatch;
        private final String[] shouldNotMatch;
        private final RE regex;

        private RegexTests(String regex, String[] shouldMatch, String[] shouldNotMatch) {
            this.regexString = regex;
            this.shouldMatch = shouldMatch;
            this.shouldNotMatch = shouldNotMatch;
            this.regex = RE.compile(regex);
        }

        public void run() {
            for (String s : shouldMatch) {
                Assert.assertTrue(String.format("Regex %s should match %s", regexString, s), regex.match(s));
            }

            for (String s : shouldNotMatch) {
                Assert.assertFalse(String.format("Regex %s should not match %s", regexString, s), regex.match(s));
            }
        }
    }
}
