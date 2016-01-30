package ch.unibe.iam.scg.minijava.bytecodegen;

import java.util.Stack;

import ch.unibe.iam.scg.minijava.ast.syntaxtree.ClassDeclaration;
import ch.unibe.iam.scg.minijava.ast.syntaxtree.MethodDeclaration;
import ch.unibe.iam.scg.minijava.typesys.ClassScope;
import ch.unibe.iam.scg.minijava.typesys.GlobalScope;
import ch.unibe.iam.scg.minijava.typesys.MethodScope;
import ch.unibe.iam.scg.minijava.typesys.Scope;
import ch.unibe.iam.scg.minijava.typesys.SimpleTypeVisitor;
import ch.unibe.iam.scg.minijava.typesys.VarObjectType;
import ch.unibe.iam.scg.minijava.typesys.VarType;

public class ScopeVisitor extends SimpleTypeVisitor {

	protected Stack<Scope> currentScope;
	private Stack<Boolean> lookupAndCheckIdentifiers;

	enum VariableLevel {
		local, instanceVar, globalVar;
	}

	private VariableLevel currentVar;

	public ScopeVisitor(GlobalScope g) {
		this.currentScope = new Stack<Scope>();
		this.currentScope.push(g);
		this.lookupAndCheckIdentifiers = new Stack<Boolean>();
		this.lookupAndCheckIdentifiers.push(false);
	}
	
	public ClassScope getClassScopeFromType(VarType object) {
		//primitive objects
		if (!(object instanceof VarObjectType))
			return null;
		Scope g = this.currentScope.elementAt(0);
		VarObjectType type=(VarObjectType)object;
		return g.findClass(type.className);
	}
	
	public ClassScope getCurrentClassScope() {
		int i=this.currentScope.size()-1;
		Scope s;
		do {
			s=this.currentScope.elementAt(i);
			i--;
			if (i<0) return null;
		} while(!(s instanceof ClassScope));
		return (ClassScope)s;
	}

	public VarType lookUpType(String varName) {
		VarType varNme = null;
		int scopeStackSize = this.currentScope.size();
		scopeStackSize--;
		while (scopeStackSize >= 0) {
			Scope s = this.currentScope.elementAt(scopeStackSize);
			varNme = s.findVariable(varName);
			scopeStackSize--;
			if (varNme != null) {
				if (s instanceof MethodScope)
					this.currentVar=VariableLevel.local;
				else if(s instanceof ClassScope)
					this.currentVar=VariableLevel.instanceVar;
				else
					this.currentVar=VariableLevel.globalVar;
				return varNme;
			}
		}
		return varNme;
	}

	/**
	 * first make a call to lookUpType, otherwise this method doesn't return valid data
	 * @return on which level the variable is defined
	 */
	public VariableLevel lookUpVarLevel() {
		return this.currentVar;
	}

	@Override
	public void visit(ClassDeclaration n) {
		String currentClass = n.f1.f0.tokenImage;
		ClassScope c = this.currentScope.peek().findClass(currentClass);
		this.currentScope.push(c);
		n.f4.accept(this);
		n.f5.accept(this);
		this.currentScope.pop();
	}

	@Override
	public void visit(MethodDeclaration n) {
		String methodName = getIdentifier(n.f2);
		VarType[] params = getParameterTypes(n.f4);

		MethodScope m = this.currentScope.peek().findMethod(methodName, params);
		this.currentScope.push(m);
		n.f4.accept(this);
		n.f7.accept(this);
		n.f8.accept(this);
		n.f10.accept(this);
		this.currentScope.pop();
	}

}