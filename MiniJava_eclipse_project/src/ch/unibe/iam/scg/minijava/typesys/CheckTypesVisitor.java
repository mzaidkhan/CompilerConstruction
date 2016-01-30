package ch.unibe.iam.scg.minijava.typesys;

import java.util.Stack;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.AllocationExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAllocationExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayAssignmentStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ArrayLookUp;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.BracketExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ClassDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Expression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.FalseLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Identifier;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IfStatement;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.IntegerLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.LengthLookUp;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodCall;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MoreParameterExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Node;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeChoice;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeSequence;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NodeToken;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.NotExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ParamList;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Sum;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.Term;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.ThisExpression;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.TrueLiteral;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.WhileStatement;

public class CheckTypesVisitor extends SimpleTypeVisitor {

	protected Stack<Scope> currentScope;
	protected ErrorReporting error;
	private VarType exprCurType;
	private Stack<Boolean> lookupAndCheckIdentifiers;
	private Scope global;

	public CheckTypesVisitor(Scope g, ErrorReporting e) {
		this.currentScope = new Stack<Scope>();
		this.currentScope.push(g);
		this.global = g;
		this.error = e;
		this.exprCurType = null;
		this.lookupAndCheckIdentifiers = new Stack<Boolean>();
		this.lookupAndCheckIdentifiers.push(false);
	}

	@Override
	public void visit(ArrayAssignmentStatement n) {
		n.f0.accept(this);
		expectUnaryType(n, null, VarType.intArray());
		n.f2.accept(this);
		expectUnaryType(n, null, VarType.integer());
		n.f5.accept(this);
		expectUnaryType(n, null, VarType.integer());
	}

	@Override
	public void visit(ParamList n) {
		if (n.f0.present()) {
			NodeSequence seq = (NodeSequence) n.f0.node;
			seq.elementAt(0).accept(this);
			this.parameters.add(exprCurType);
			seq.elementAt(1).accept(this);
		}
	}

	@Override
	public void visit(MoreParameterExpression n) {
		super.visit(n);
		this.parameters.add(exprCurType);
	}

	@Override
	public void visit(WhileStatement n) {
		// while (expression)
		n.f2.accept(this);
		expectUnaryType(n, null, VarType.bool());
		// do statemenet
		n.f4.accept(this);
	}

	@Override
	public void visit(IfStatement n) {
		// expression
		n.f2.accept(this);
		expectUnaryType(n, null, VarType.bool());
		// then
		n.f4.accept(this);
		// else
		n.f6.accept(this);
	}

	@Override
	public void visit(ClassDeclaration n) {
		String currentClass = n.f1.f0.tokenImage;
		ClassScope c = this.currentScope.peek().findClass(currentClass);
		this.currentScope.push(c);
		super.visit(n);
		if (this.currentScope.pop() != c)
			this.error.addError("Scopes of classes not matching - E1");
	}

	@Override
	public void visit(MethodDeclaration n) {
		VarType returnType = getType(n.f1);
		String methodName = getIdentifier(n.f2);
		VarType[] params = getParameterTypes(n.f4);

		MethodScope m = this.currentScope.peek().findMethod(methodName, params);
		this.currentScope.push(m);
		// statemenets
		n.f8.accept(this);

		// return expression
		n.f10.accept(this);
		if (!returnType.equals(exprCurType))
			this.error.addError("Return expression (" + exprCurType
					+ ") doesn't match the expected return type (" + returnType
					+ ") in: \n" + n);

		if (this.currentScope.pop() != m)
			this.error.addError("Scopes of methods not matching - E2");
	}

	public void expectUnaryType(Node n, VarType become, VarType... t) {
		if (!arrContains(exprCurType, t)) {
			StringBuffer expected = new StringBuffer();
			if (t.length > 1) {
				expected.append("one of \"");
				for (int i = 0; i < t.length; i++) {
					expected.append(t[0].toString());
					if (i < t.length - 1)
						expected.append(", ");
				}
			} else
				expected.append("\"" + t[0]);
			this.error.addError("Invalid Type \"" + this.exprCurType
					+ "\" exptected " + expected + "\" in:\n" + n);

		}
		this.exprCurType = become;
	}

	protected boolean arrContains(VarType lookfor, VarType... t) {
		for (VarType element : t)
			if (element.equals(lookfor))
				return true;
		return false;
	}

	@Override
	public void visit(Expression n) {
		this.lookupAndCheckIdentifiers.push(true);
		n.f0.accept(this);
		if (n.f1.present()) {
			NodeSequence l = (NodeSequence) n.f1.node;
			NodeChoice c = (NodeChoice) l.nodes.get(0);
			NodeToken operation = (NodeToken) c.choice;
			if (operation.tokenImage.equals("&&")) {
				expectUnaryType(n.f0, VarType.bool(), VarType.bool());
				n.f1.accept(this);
				expectUnaryType(n.f1, VarType.bool(), VarType.bool());
			} else if (operation.tokenImage.equals("<")
					|| operation.tokenImage.equals(">")) {
				expectUnaryType(n.f0, VarType.bool(), VarType.integer());
				n.f1.accept(this);
				expectUnaryType(n.f1, VarType.bool(), VarType.integer());
			}
		}
		this.lookupAndCheckIdentifiers.pop();
	}

	@Override
	public void visit(Sum n) {
		n.f0.accept(this);
		if (n.f1.present()) {
			expectUnaryType(n.f0, VarType.integer(), VarType.integer());
			n.f1.accept(this);
			expectUnaryType(n.f1, VarType.integer(), VarType.integer());
		}
	}

	@Override
	public void visit(Term n) {
		n.f0.accept(this);
		if (n.f1.present()) {
			expectUnaryType(n.f0, VarType.integer(), VarType.integer());
			n.f1.accept(this);
			expectUnaryType(n.f1, VarType.integer(), VarType.integer());
		}
	}

	@Override
	public void visit(MethodCall n) {
		if (!(this.exprCurType instanceof VarObjectType)) {
			this.error.addError("Cannot call a method onto a non-object in: "
					+ n);
			return;
		}
		VarObjectType obj = (VarObjectType) this.exprCurType;
		String className = obj.className;
		ClassScope c = null;
		int size = this.currentScope.size();
		for (int i = size - 1; i >= 0; i--) {
			Scope s = this.currentScope.get(i);
			c = s.findClass(className);
			if (c != null)
				break;
		}
		if (c == null) {
			this.error.addError("No class type found for this variable");
			return;
		}

		MethodScope m = null;
		while (c != null && m == null) {
			m = c.findMethod(getIdentifier(n.f1), getParameterTypes(n.f3));
			c = this.global.findClass(c.parentClass);
		}
		if (m == null) {
			this.error.addError("method not found in: " + n);
			return;
		}
		this.exprCurType = m.returntype;

	}

	@Override
	public void visit(LengthLookUp n) {
		super.visit(n);
		expectUnaryType(n, VarType.integer(), VarType.intArray());
	}

	@Override
	public void visit(ArrayLookUp n) {
		expectUnaryType(n, null, VarType.intArray());
		super.visit(n);
		expectUnaryType(n, VarType.integer(), VarType.integer());
	}

	@Override
	public void visit(IntegerLiteral n) {
		super.visit(n);
		this.exprCurType = VarType.integer();
	}

	@Override
	public void visit(TrueLiteral n) {
		super.visit(n);
		this.exprCurType = VarType.bool();
	}

	@Override
	public void visit(FalseLiteral n) {
		super.visit(n);
		this.exprCurType = VarType.bool();
	}

	@Override
	public void visit(Identifier n) {
		super.visit(n);
		if (this.lookupAndCheckIdentifiers.peek()) {
			String name = getIdentifier(n);
			int size = this.currentScope.size();
			VarType v = null;
			for (int i = size - 1; i >= 0; i--) {
				Scope s = this.currentScope.get(i);
				v = s.findVariable(name);
				if (v != null)
					break;

				// also search inside superclasses
				if (s instanceof ClassScope) {
					ClassScope c = (ClassScope) s;
					while (c.parentClass != null && v == null) {
						c = this.global.findClass(c.parentClass);
						v = c.findVariable(name);
					}
					if (v != null)
						break;
				}
			}

			if (v == null)
				this.error.addError("Undeclared variable: " + n);
			this.exprCurType = v;
		}
	}

	@Override
	public void visit(ThisExpression n) {
		super.visit(n);
		String className = null;
		int size = this.currentScope.size();
		for (int i = size - 1; i >= 0; i--) {
			Scope s = this.currentScope.get(i);
			if (s instanceof ClassScope) {
				className = s.getName();
				break;
			}
		}
		this.exprCurType = VarType.object(className);
	}

	@Override
	public void visit(ArrayAllocationExpression n) {
		this.exprCurType = VarType.integer();
		super.visit(n);
		n.f3.accept(this);
		expectUnaryType(n, VarType.intArray(), VarType.integer());
	}

	@Override
	public void visit(AllocationExpression n) {
		VarType t = VarType.object(getIdentifier(n.f1));
		this.exprCurType = t;
	}

	@Override
	public void visit(NotExpression n) {
		super.visit(n);
		expectUnaryType(n, VarType.bool(), VarType.bool());
	}

	@Override
	public void visit(BracketExpression n) {
		super.visit(n);
	}

	public VarType getCurrentType() {
		return this.exprCurType;
	}

}