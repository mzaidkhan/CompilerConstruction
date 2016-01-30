package ch.unibe.iam.scg.minijava.optimizer;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import ch.unibe.iam.scg.javacc.MiniJavaImpl;
import ch.unibe.iam.scg.minijava.MiniJava;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeToken;
import ch.unibe.iam.scg.minijava.ast.visitor.DepthFirstVisitor;

/**
 * creates the most easiest nodes possible for constant values<br>
 * Note: following exactly the MiniJavaInitialRules since creation of Nodes
 * sometimes allows Node arguments, great care has to be taken, otherwise the
 * other visitors might get into troubles
 * 
 * @author j
 * 
 */
public class ConstantGenerator {

	public Node generateConstantLiterals(String method, Object value) {
		//negative numbers cause problems for our parser
		if (value instanceof Integer) {
			if ((Integer)value < 0)
				return generateConstantNegativeInteger(method,(Integer)value);
		}
		try {
			String v = String.valueOf(value);
			MiniJava parser = new MiniJavaImpl(getStream(v));
			Node node = (Node) parser.getClass().getMethod(method)
					.invoke(parser);
			return node;
		} catch (Throwable t) {
			t.printStackTrace();
			fail(t.toString());
		}
		return null;
	}
	
	
	private Node generateConstantNegativeInteger(String method, final Integer value) {
		Node n = generateConstantLiterals(method, 0);
		n.accept(new DepthFirstVisitor() {
			@Override
			public void visit(IntegerLiteral n) {
				n.f0 = new NodeToken(String.valueOf(value));
			}
		});
		return n;
	}


	private InputStream getStream(String str)
			throws UnsupportedEncodingException {
		return new ByteArrayInputStream(str.getBytes("UTF-8"));

	}

}
