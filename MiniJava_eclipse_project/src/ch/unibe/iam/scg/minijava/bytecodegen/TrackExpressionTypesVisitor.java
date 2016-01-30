package ch.unibe.iam.scg.minijava.bytecodegen;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayLookUp;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Identifier;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ThisExpression;
import ch.unibe.iam.scg.minijava.typesys.ClassScope;
import ch.unibe.iam.scg.minijava.typesys.GlobalScope;
import ch.unibe.iam.scg.minijava.typesys.VarType;

public class TrackExpressionTypesVisitor extends ScopeVisitor {

	public TrackExpressionTypesVisitor(GlobalScope g) {
		super(g);
	}
	
	@Override
	public void visit(ArrayLookUp n) {
		super.visit(n);
		this.currentType=VarType.integer();
	}
	
	@Override
	public void visit(Identifier n) {
		super.visit(n);
		this.currentType=super.lookUpType(n.f0.tokenImage);
	}
	
	@Override
	public void visit(ThisExpression n) {
		super.visit(n);
		ClassScope c=getCurrentClassScope();
		String className=c.name;
		this.currentType=VarType.object(className);
	}

}
