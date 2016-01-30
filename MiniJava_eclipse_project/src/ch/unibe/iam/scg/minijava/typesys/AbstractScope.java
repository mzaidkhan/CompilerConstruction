package ch.unibe.iam.scg.minijava.typesys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public abstract class AbstractScope implements Scope {

	/**
	 * maps the
	 */
	public HashMap<String, List<MethodScope>> methods;
	/**
	 * maps identifier of variables to their types
	 */
	public HashMap<String, VarType> variables;
	private HashMap<String, ClassScope> classes;

	/**
	 * identifier of the parentclass
	 */

	public AbstractScope() {
		methods = new HashMap<String, List<MethodScope>>();
		variables = new HashMap<String, VarType>();
		classes = new HashMap<String, ClassScope>();
	}

	@Override
	public VarType findVariable(String identifier) {
		return this.variables.get(identifier);
	}

	@Override
	public void addVariable(String identifier, VarType type) throws Exception {
		if (findVariable(identifier) != null)
			throw new Exception("Variable is already defined: " + identifier);
		this.variables.put(identifier, type);
	}

	@Override
	public ClassScope findClass(String identifier) {
		return this.classes.get(identifier);
	}

	@Override
	public ClassScope addClass(String identifier, String parentClass)
			throws Exception {
		if (findClass(identifier) != null)
			throw new Exception("Class is already defined: " + identifier);
		ClassScope c = new ClassScope(identifier, parentClass);
		this.classes.put(identifier, c);
		return c;
	}

	@Override
	public MethodScope findMethod(String identifier, VarType[] params) {
		List<MethodScope> l = this.methods.get(identifier);
		if (l == null)
			return null;
		for (MethodScope m : l) {
			if (m.hasSameParameter(params))
				return m;
		}
		return null;
	}

	@Override
	public MethodScope addMethod(String identifier, VarType returntype,
			VarType[] params, String[] paramNames) throws Exception {
		if (params == null)
			params = new VarType[0];
		if (findMethod(identifier, params) != null)
			throw new Exception(
					"Method is already defined in this class with the same head: "
							+ identifier);
		MethodScope m = new MethodScope(identifier, returntype, params,
				paramNames);
		putMethod(identifier, m);
		return m;
	}

	private void putMethod(String identifier, MethodScope m) {
		List<MethodScope> l = this.methods.get(identifier);
		if (l == null) {
			l = new ArrayList<MethodScope>();
			this.methods.put(identifier, l);
		}
		l.add(m);
	}

	public String toString() {
		return toString(0);
	}

	protected String toStringSimple() {
		return "";
	}

	public String toString(int ind) {
		String i = ind(ind);
		StringBuffer s = new StringBuffer(i + this.getName() + " ("
				+ this.getClass().getSimpleName() + ")");
		s.append(" contains " + this.classes.size() + " classes, "
				+ this.methods.size() + " methods, " + this.variables.size()
				+ " variables\n");
		String simple = toStringSimple();
		if (!simple.equals(""))
			s.append(i + simple);
		if (this.classes.size() > 0) {
			s.append(i + "Classes: \n");
			for (Entry<String, ClassScope> e : this.classes.entrySet()) {
				s.append(e.getValue().toString(ind + 1));
			}
		}
		if (this.methods.size() > 0) {
			s.append(i + "Methods: \n");
			for (Entry<String, List<MethodScope>> e : this.methods.entrySet()) {
				for (MethodScope m : e.getValue())
					s.append(m.toString(ind + 1)+"\n");
			}
		}
		if (this.variables.size() > 0) {
			s.append(i + "Variables: \n");
			s.append(i + "\t");
			for (Entry<String, VarType> e : this.variables.entrySet()) {
				 s.append(e.getValue()+", ");
			}
			s.setLength(s.length()-2);
		}
		return s.toString();
	}

	private String ind(int ind) {
		StringBuffer s = new StringBuffer();
		while (ind > 0) {
			s.append("\t");
			ind--;
		}
		return s.toString();
	}

}
