package ch.unibe.iam.scg.minijava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Test;

import ch.unibe.iam.scg.javacc.MiniJavaImpl;
import ch.unibe.iam.scg.javacc.ParseException;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;

public class ExpressionLeftRecursionTests {

	@Test
	public void testExpressionMethodCalls() throws ParseException {
		String str = "a.func1().func2()";
		String result = prettyPrint("Expression", str);
		assertEquals(str, result);
	}

	@Test
	public void testExpressionLength() throws ParseException {
		String str = "a.length.length";
		String result = prettyPrint("Expression", str);
		assertEquals(str, result);
	}

	@Test
	public void testExpressionArrayAccess() throws ParseException {
		String str = "1 + 2[3]";
		String result = prettyPrint("Expression", str);
		assertEquals(str, result);
	}

	@Test
	public void testExpressionOptional7() throws ParseException {
		String str = "a[something].foo().bar().length";
		String feedStr = "a[something]. foo().\n 	bar(     ).length";
		String result = prettyPrint("Expression", feedStr);
		assertEquals(str, result);
	}

	@Test
	public void testExpressionMixed() throws ParseException {
		String str =  "a && (b.foo().bar()) && c.length > true - 4 - 4 - de[2 + 3][6].length";
		String result = prettyPrint("Expression", str);
		assertEquals(str, result);
	}
	
	@Test
	public void testExpressionMixed2() throws ParseException {
		String str =  "a && b.foo().bar() && c.length > true - 4 - 4 - de[2 + 3][6].length";
		String feedStr =  "a \n &&    b.  \n foo().\n\n\n\n\n bar() && c.       length > true - 4 -4-de[2 + 3][6]. length";
		String result = prettyPrint("Expression", feedStr);
		assertEquals(str, result);
	}
	
	@Test
	public void testExpressionMultiArray() throws ParseException {
		String str = "a > B < de[2 + 3][6].length";
		String result = prettyPrint("Expression", str);
		assertEquals(str, result);
	}
	
	@Test
	public void testExpressionMultiArray2() throws ParseException {
		String str = "!(true) && f.boo((d[2][3][4].length + ((3 * (2 * (1 * (1 + 2 + 3)))) + 99))).length";
		String result = prettyPrint("Expression", str);
		assertEquals(str, result);
	}
	
	@Test
	public void testExpressionMultiArray3() throws ParseException {
		String str = "my.method(((!(true)) && f.boo((d[2][3][4].length + ((3 * (2 * (1 * (1 + 2 + 3)))) + 99))).length), (a && B < c && (nm < d)))";
		String result = prettyPrint("Expression", str);
		assertEquals(str, result);
	}
	
	private String prettyPrint(String method, String str) {
		try {
			MiniJava parser = new MiniJavaImpl(getStream(str));
			Node node = (Node) parser.getClass().getMethod(method)
					.invoke(parser);
			PrettyPrinter prettyPrinter = new PrettyPrinter();
			String result = prettyPrinter.prettyPrint(node);
			return result;
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
