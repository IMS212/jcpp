package org.anarres.cpp;

import static org.junit.jupiter.api.Assertions.*;

import java.io.*;

import org.anarres.cpp.test_util.CppReader;
import org.junit.jupiter.api.Test;
import org.slf4j.*;

import com.google.common.io.CharStreams;

/**
 * https://github.com/shevek/jcpp/issues/25
 *
 * @author shevek
 */
public class TokenPastingWhitespaceTest {

	private static final Logger LOG = LoggerFactory.getLogger(TokenPastingWhitespaceTest.class);

	@Test
	public void testWhitespacePasting() throws IOException {
		Preprocessor pp = new Preprocessor();
		pp.addInput(new StringLexerSource(
				"#define ONE(arg) one_##arg\n"
						+ "#define TWO(arg) ONE(two_##arg)\n"
						+ "\n"
						+ "TWO(good)\n"
						+ "TWO(     /* evil newline */\n"
						+ "    bad)\n"
						+ "\n"
						+ "ONE(good)\n"
						+ "ONE(     /* evil newline */\n"
						+ "    bad)\n",
				true));
		Reader r = new CppReader(pp);
		String text = CharStreams.toString(r).trim();
		LOG.info("Output is:\n" + text);
		assertEquals("one_two_good\n"
				+ "one_two_bad\n"
				+ "\n"
				+ "one_good\n"
				+ "one_bad", text);
	}
}
