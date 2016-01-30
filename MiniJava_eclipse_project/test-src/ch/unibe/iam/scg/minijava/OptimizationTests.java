package ch.unibe.iam.scg.minijava;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.ClassGen;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ch.unibe.iam.scg.javacc.MiniJavaImpl;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.bytecodegen.JavaBytecodeGenerator;
import ch.unibe.iam.scg.minijava.bytecodegen.MiniJavaRuntimeSupport;
import ch.unibe.iam.scg.minijava.optimizer.ConstantDetectorVisitor;
import ch.unibe.iam.scg.minijava.optimizer.ConstantGenerator;
import ch.unibe.iam.scg.minijava.optimizer.EvaluationVisitor;

public class OptimizationTests {
	private EvaluationVisitor eval;
	private ConstantGenerator cons;
	private ConstantDetectorVisitor opti;
	private PrettyPrinter p;

	@Test
	public void testEvaluationSimpleExpression() {
		int[] somevalues = new int[] { 0, 1, 5, 100 };
		for (int value : somevalues) {
			Node n = cons.generateConstantLiterals("Expression", value);
			eval.evaluate("Expression", n);
			assertEquals(value, eval.getValue());
		}
	}

	@Test
	public void testEvaluationSimpleTerm() {
		int[] somevalues = new int[] { 0, 1, 5, 100 };
		for (int value : somevalues) {
			Node n = cons.generateConstantLiterals("Term", value);
			eval.evaluate("Term", n);
			assertEquals(value, eval.getValue());
		}
	}

	@Test
	public void testEvaluationSimpleFactor() {
		int[] somevalues = new int[] { 0, 1, 5, 100 };
		for (int value : somevalues) {
			Node n = cons.generateConstantLiterals("Factor", value);
			eval.evaluate("Factor", n);
			assertEquals(value, eval.getValue());
		}
	}

	@Test
	public void testMultiplication() {
		Node n = getAst("Expression", "2*3");
		eval.evaluate("Expression", n);
		assertEquals(6, eval.getValue());
	}

	@Test
	public void testSum() {
		Node n = getAst("Expression", "2+3");
		eval.evaluate("Expression", n);
		assertEquals(5, eval.getValue());
	}

	@Test
	public void testReplacementSum() {
		Node before = getAst("Expression", "2+3");
		Node n = this.opti.optimizeExpression(before);
		assertEquals("5", p.prettyPrint(n));
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testReplacementMultiplication() {
		Node n = getAst("Expression", "2*3");
		n = this.opti.optimizeExpression(n);
		assertEquals("6", p.prettyPrint(n));
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testReplacementBrackets1() {
		Node n = getAst("Expression", "2*(3+1)");
		n = this.opti.optimizeExpression(n);
		assertEquals("8", p.prettyPrint(n));
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testReplacementBrackets2() {
		Node n = getAst("Expression", "(2*(3))+1");
		n = this.opti.optimizeExpression(n);
		assertEquals("7", p.prettyPrint(n));
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testWhile() {
		Node n = getAst("Statement", "while ((6-4) > 3) {}");
		this.opti.optimize(n);
		assertEquals("", cleanString(p.prettyPrint(n)));
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testIf() {
		Node n = getAst("Statement",
				"if(true) {a=b;}else{System.out.println(1);}");
		this.opti.optimize(n);
		String output = cleanString(p.prettyPrint(n));
		assertEquals("a = b;", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testElse() {
		Node n = getAst("Statement",
				"if(false) {a=b;}else{System.out.println(1);}");
		this.opti.optimize(n);
		String output = cleanString(p.prettyPrint(n));
		assertEquals("System.out.println(1);", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testWhile2() {
		Node n = getAst("Statement", "while(false) {a=b;}");
		this.opti.optimize(n);
		String output = cleanString(p.prettyPrint(n));
		assertEquals("", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testWhile3() {
		Node n = getAst("MethodDeclaration", 
				"public int a(){" + 
				"	int a;"	+ 
				"	int b;" + "	a=1;b=1;" + "	" +
				"while ((b-a) < 1) {"
				+ "		b = a + 1;" + "	}" 
				+ "	return b;" + "}");
		this.opti.optimize(n);
		String output = cleanString(p.prettyPrint(n));
		assertEquals("public int a() {\n    int a;\n    int b;\n\n    "
				+ "a = 1;\n    b = 1;\n    while ((b - 1) < 1) {\n        "
				+ "b = 2;\n}\n    return b;\n}", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testWhile4() {
		Node n = getAst("MethodDeclaration", 
				"public int a(){" + 
				"	int a;" +
				"	int b;" + 
				"	a=1;b=a;" + 
				"	while ((b-a) < 1) {" +
				"		b = a + 1;" + "	}" + 
				"	return b;" + "}");
		this.opti.optimize(n);
		String output = cleanString(p.prettyPrint(n));
		assertEquals("public int a() {\n    int a;\n    int b;\n\n    "
				+ "a = 1;\n    b = 1;\n    while ((b - 1) < 1) {\n        "
				+ "b = 2;\n}\n    return b;\n}", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testWhile5() {
		Node n = getAst("MethodDeclaration", 
				"public int a(){" + 
				"	int a;"	+ 
				"	int b;" + 
				"	a=1;b=a;" + 
				"	while ((b-a) < 1) {" + 
				"		a = a + 1;" + "	}" + 
				"	return a;" + "}");
		this.opti.optimize(n);
		String output = cleanString(p.prettyPrint(n));
		assertEquals("public int a() {\n    int a;\n    int b;\n\n    "
				+ "a = 1;\n    b = 1;\n    while ((1 - a) < 1) {\n        "
				+ "a = a + 1;\n}\n    return a;\n}", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testWhile6() {
		Node n = getAst("MethodDeclaration", 
				"public int a(){" + 
				"	int a;"	+ 
				"	int b;" + 
				"	a=1;b=a;" + 
				"	while ((b-a) < 1) {" +
				"		a = a + 1;" + 
				"       b = b + 1;" + 
				"	}" + 
				"	return a;"
				+ "}");
		this.opti.optimize(n);
		String output = cleanString(p.prettyPrint(n));
		assertEquals("public int a() {\n    int a;\n    int b;\n\n    "
				+ "a = 1;\n    b = 1;\n    while ((b - a) < 1) {\n        "
				+ "a = a + 1;\n        b = b + 1;\n}\n    return a;\n}", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testIfElse() {
		Node n = getAst("MethodDeclaration",
				"public int a(){" + 
				"	int a;" + 
				"	int b;" + 
				"	a=1; b=a;" + 
				"	if ((b-a) < 1) {" +
				"		a = a + 1;" + 
				"       b = b + 1;" + 
				"	}else {" + 
				"	 a = a + b; }" + 
				"	return a;" + "}");
		this.opti.optimize(n);
		String output = cleanString(p.prettyPrint(n));
		assertEquals("public int a() {\n    return 2;\n}", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}

	@Test
	public void testIfElse2() {
		Node n = getAst("MethodDeclaration", 
				"public int a(){" + 
				"	int a;" +
				"	int b;" + 
				"	a=1;b=2;" + 
				"	if ((b-a) < 1) {" + 
				"		a = a + 1;" + 
				"       b = b + 1;" + 
				"	}else {" + 
				"	 a = a + b; }" + 
				"	return a;" + "}");
		this.opti.optimize(n);
		String output = cleanString(p.prettyPrint(n));
		assertEquals("public int a() {\n    return 3;\n}", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}
	
	@Test
	public void testConstExprSysout() {
		Node n = getAst("MethodDeclaration", 
				"public int a(){" + 
				"	int a;" +
				"	int b;" + 
				"	a=1;b=2;" + 
				"	if ((b-a) < 1) {" +
				"		a = a + 1;" + 
				"       b = b + 1;" + 
				"	}else {" +
				"	 a = a + b; System.out.println(1);}" + 
				"	return a;" + 
				"}");
		this.opti.optimize(n);
		String output = cleanString2(p.prettyPrint(n));
		
		output=output.replaceAll("  ", " ");
		output=output.replaceAll("\n", "");
		assertEquals("public int a() { int a; int b; a = 1; b = 2; { " +
				"{ a = 3; System.out.println(1);} } return 3;}", output);
		assertTrue(opti.getOptimizedValue() > 0);
	}
	
	public String cleanString2(String s) {
		String temp=s.replaceAll("  ", " ").replaceAll("\n", "");
		while (temp.contains("  ")) {
			temp=temp.replaceAll("  ", " ");
		}
		return temp;
	}
	

	@Test
	public void testShiftL() {
		String m = "public int a(int a){ "
				+ "a = a * 4; return a; }";

		int result=(Integer) evaluateMethod(m,5);
		assertEquals(20,result);

	}
	
	
	private Object evaluateMethod(String input, Object ... args)
    {
        JavaBytecodeGenerator jbcg = compile("MethodDeclaration", input);
        
        ClassGen cg = jbcg.getClassGen();
        Method m = jbcg.getMethod();
       MiniJavaRuntimeSupport testSupport = new MiniJavaRuntimeSupport();
        return testSupport.evaluateMethod(cg, m, args);
    }
	
	private JavaBytecodeGenerator compile(String rule, String input)
    {
        JavaBytecodeGenerator jbcg = new JavaBytecodeGenerator();
        Object node = getAst(rule, input);
        jbcg.generate(node);
        return jbcg;
    }   

	public String cleanString(String s) {
		s = s.trim();
		while (s.startsWith("{") && s.endsWith("}")) {
			s = s.substring(1, s.length() - 2);
			s = s.trim();
		}
		return s;
	}

	@Before
	public void setUp() {
		this.eval = new EvaluationVisitor();
		this.cons = new ConstantGenerator();
		this.opti = new ConstantDetectorVisitor();
		this.p = new PrettyPrinter();
	}

	@After
	public void tearDown() {

	}

	private Node getAst(String method, String str) {
		try {
			MiniJava parser = new MiniJavaImpl(getStream(str));
			Node node = (Node) parser.getClass().getMethod(method)
					.invoke(parser);
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
