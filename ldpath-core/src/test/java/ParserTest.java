
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import at.newmedialab.ldpath.api.backend.RDFBackend;
import at.newmedialab.ldpath.api.tests.NodeTest;
import at.newmedialab.ldpath.api.transformers.NodeTransformer;
import at.newmedialab.ldpath.model.programs.Program;
import at.newmedialab.ldpath.parser.ParseException;
import at.newmedialab.ldpath.parser.RdfPathParser;


public class ParserTest {

	private static RDFBackend<String> backend;
	private static final String NS_TEST = "http://example.com/";
	private static final String NS_FOO = "http://foo.com/some/path#";
	
	private static final Map<String, String> NAMESPACES;
	static {
		Map<String, String> ns = new HashMap<String, String>();
		ns.put("test", NS_TEST);
		ns.put("foo", NS_FOO);
		NAMESPACES = Collections.unmodifiableMap(ns);
	}
	
	@BeforeClass
	public static void initClass() {
		backend = new EmptyTestingBackend();
	}
	

	@Test
	public void testParseProgram() throws IOException {
		RdfPathParser<String> parser = createParser("program.txt");
		try {
			Program<String> program = parser.parseProgram();
			assertNotNull(program.getField("path"));
			assertNotNull(program.getField("lang_test"));
			assertNotNull(program.getField("type_test"));
			assertNotNull(program.getField("int_s"));
			assertNotNull(program.getField("int_p"));
			assertNotNull(program.getField("inverse"));
			assertNotNull(program.getField("config"));
			assertNotNull(program.getBooster());
			assertNotNull(program.getFilter());
			
		} catch (ParseException e) {
			assertFalse(e.getMessage(), true);
		}
	}
	
	@Test
	public void testParseTest() throws IOException {
		RdfPathParser<String> parser = createParser("test.txt");
		try {
			NodeTest<String> test = parser.parseTest(NAMESPACES);
			assertNotNull(test);
			assertNotNull(test.getPathExpression(backend));
		} catch (ParseException e) {
			assertFalse(e.getMessage(), true);
		}
	}

	@Test
	public void testParsePrefixes() throws IOException {
		RdfPathParser<String> parser = createParser("namespaces.txt");
		try {
			Map<String, String> prefixes = parser.parsePrefixes();
			assertTrue(prefixes.containsKey("test"));
			assertTrue(prefixes.containsKey("foo"));
			assertEquals(NS_TEST, prefixes.get("test"));
			assertEquals(NS_FOO, prefixes.get("foo"));
		} catch (ParseException e) {
			assertFalse(e.getMessage(), true);
		}
	}
	
	
	private RdfPathParser<String> createParser(String input) throws IOException {
		RdfPathParser<String> rdfPathParser = new RdfPathParser<String>(backend,new StringReader(IOUtils.toString(ParserTest.class.getResource(input))));
		rdfPathParser.registerTransformer(NS_TEST + "type", new NodeTransformer<String, String>() {
			@Override
			public String transform(RDFBackend<String> backend, String node)
					throws IllegalArgumentException {
				return node;
			}
		});
		return rdfPathParser;
	}
}
