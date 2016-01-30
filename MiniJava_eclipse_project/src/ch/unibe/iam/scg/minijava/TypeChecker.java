/**
 * 
 */
package ch.unibe.iam.scg.minijava;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.typesys.BuildUpTypesVisitor;
import ch.unibe.iam.scg.minijava.typesys.CheckTypesVisitor;
import ch.unibe.iam.scg.minijava.typesys.ErrorReporting;
import ch.unibe.iam.scg.minijava.typesys.GlobalScope;

/**
 * 
 * @author Julian Schelker, Zaid Khan
 * 
 */
public class TypeChecker {

	public boolean silentMode;

	public TypeChecker() {
		this.silentMode = true;
	}

	public boolean check(Object node) {
		ErrorReporting e = new ErrorReporting();
		GlobalScope g = new GlobalScope();
		Node n = (Node) node;

		n.accept(new BuildUpTypesVisitor(g, e));
		n.accept(new CheckTypesVisitor(g, e));

		if (!this.silentMode)
			e.reportTo(System.err);

		return !e.hasFoundErrors();
	}

}
