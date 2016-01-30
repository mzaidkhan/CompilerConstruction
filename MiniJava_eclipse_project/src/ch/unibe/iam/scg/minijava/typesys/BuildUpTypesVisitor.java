package ch.unibe.iam.scg.minijava.typesys;

import java.util.Stack;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.ClassDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FormalParameter;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MainClass;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MainFunction;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.VarDeclaration;

public class BuildUpTypesVisitor extends SimpleTypeVisitor {

	protected Stack<Scope> currentScope;
	private ErrorReporting error;

	public BuildUpTypesVisitor(GlobalScope g, ErrorReporting e) {
		this.currentScope = new Stack<Scope>();
		this.currentScope.push(g);
		this.error = e;
	}

	@Override
	public void visit(MainClass n) {
		try {
			this.currentScope.push(this.currentScope.peek().addClass(
					getIdentifier(n.f1), null));
			super.visit(n);
			this.currentScope.pop();
		} catch (Exception e) {
			error.addError(e.getMessage());
		}
	}

	@Override
	public void visit(MainFunction n) {
		try {
			MethodScope m = this.currentScope.peek().addMethod(n.f3.tokenImage,
					getType(n.f2), new VarType[] { getType(n.f5) },new String[]{n.f6.f0.tokenImage});
			this.currentScope.push(m);
			super.visit(n);
			this.currentScope.pop();
		} catch (Exception e) {
			error.addError(e.getMessage());
		}
	}

	@Override
	public void visit(ClassDeclaration n) {
		try {
			String parentClass = null;
			if (n.f2.present()) {
				parentClass = getIdentifier(n.f2);
			}

			this.currentScope.push(this.currentScope.peek().addClass(
					getIdentifier(n.f1), parentClass));
			super.visit(n);
			this.currentScope.pop();
		} catch (Exception e) {
			error.addError(e.getMessage());
		}
	}

	@Override
	public void visit(VarDeclaration n) {
		try {
			this.currentScope.peek().addVariable(getIdentifier(n.f1),
					getType(n.f0));
			super.visit(n);
		} catch (Exception e) {
			error.addError(e.getMessage());
		}
	}
	
	@Override
	public void visit(FormalParameter n) {
		try {
			this.currentScope.peek().addVariable(getIdentifier(n.f1),
					getType(n.f0));
			super.visit(n);
		} catch (Exception e) {
			error.addError(e.getMessage());
		}
	}

	@Override
	public void visit(MethodDeclaration n) {
		try {
			MethodScope m = this.currentScope.peek()
					.addMethod(getIdentifier(n.f2), getType(n.f1),
							null,null);
			this.currentScope.push(m);
			m.setParameters(getParameterTypes(n.f4), getParameterNames(n.f4));
			n.f7.accept(this);
			n.f8.accept(this);
			n.f10.accept(this);
			this.currentScope.pop();
		} catch (Exception e) {
			error.addError(e.getMessage());
		}
	}
}