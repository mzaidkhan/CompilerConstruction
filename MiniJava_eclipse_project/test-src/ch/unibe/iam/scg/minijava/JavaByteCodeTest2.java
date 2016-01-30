package ch.unibe.iam.scg.minijava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.InstructionList;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibe.iam.scg.javacc.MiniJavaImpl;
import ch.unibe.iam.scg.javacc.ParseException;
import ch.unibe.iam.scg.minijava.bytecodegen.JavaBytecodeGenerator;
import ch.unibe.iam.scg.minijava.bytecodegen.MiniJavaRuntimeSupport;

/**
 * @author julian these tests show that the operators have no preceedence<br>
 *         right now everything is right associative, so either one of the test
 *         always fails if the order of the operators is not considered. FIXME:
 *         get both tests to run at the same time
 */
public class JavaByteCodeTest2 {
	private MiniJavaRuntimeSupport testSupport;

	@Test
	public void testA1() throws ParseException {
		String str = "(2 * 3) + 4";
		int expected = 10;
		Object result = evaluateExpression(str, MiniJavaTypes.INT);
		assertEquals(expected, result);

	}

	@Test
	public void testA2() throws ParseException {
		String str = "1 + (2 * 3)";
		int expected = 7;
		Object result = evaluateExpression(str, MiniJavaTypes.INT);
		assertEquals(expected, result);
	}

	@Test
	public void testClass() throws ParseException {
		String str = "class Foo {"
				+ "  public int bar(int retval) { int a; while(retval > 0) { retval = retval -1;}  return retval; }"
				+ "  public int baz(int retval) { return retval + 1; }"
				+ "  public boolean bag(int retval) { return retval > 5; }"
				+ "}";

		int expected = 0;
		compile("ClassDeclaration", str);

		Object instance = instantiate("Foo");
		Object result = callMethod(instance, "bar", 10);
		assertEquals(expected, result);

		expected = 11;
		result = callMethod(instance, "baz", 10);
		assertEquals(expected, result);

		result = callMethod(instance, "bag", 10);
		assertEquals(true, result);

		result = callMethod(instance, "bag", 1);
		assertEquals(false, result);
	}

	@Before
	public void setUp() {
		testSupport = new MiniJavaRuntimeSupport();
	}

	@After
	public void tearDown() {

	}

	private Object evaluateExpression(String input, int resultType) {
		JavaBytecodeGenerator jbcg = compile("Expression", input);

		ClassGen cg = jbcg.getClassGen();
		InstructionList il = jbcg.getInstructionList();

		return testSupport.evaluateExpression(cg, il, resultType);
	}

	private Object evaluateMethod(String input, Object... args) {
		JavaBytecodeGenerator jbcg = compile("MethodDeclaration", input);

		ClassGen cg = jbcg.getClassGen();
		Method m = jbcg.getMethod();

		return testSupport.evaluateMethod(cg, m, args);
	}

	private Object callMethod(Object receiver, String name, Object... args) {
		try {
			java.lang.reflect.Method m = receiver.getClass().getMethod(name,
					testSupport.collectArgTypes(args));
			return m.invoke(receiver, args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

	private JavaBytecodeGenerator compile(String rule, String input) {
		JavaBytecodeGenerator jbcg = new JavaBytecodeGenerator();
		Object node = getAst(rule, input);
		jbcg.generate(node);
		return jbcg;
	}

	private Object instantiate(String className) {
		return testSupport.instantiate(className);
	}

	private Object getAst(String method, String str) {
		try {
			MiniJava parser = new MiniJavaImpl(getStream(str));
			Object node = parser.getClass().getMethod(method).invoke(parser);
			return node;
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.toString());
		}
		return null;
	}

	private InputStream getStream(String str)
			throws UnsupportedEncodingException {
		return new ByteArrayInputStream(str.getBytes("UTF-8"));

	}

}
