package ch.unibe.iam.scg.minijava.typesys;

public class GlobalScope extends AbstractScope {

	public GlobalScope() {
		super();
	}

	@Override
	@Deprecated
	public void addVariable(String identifier, VarType type) throws Exception {
		throw new Exception("cannot add variables in the global scope");
	}

	@Override
	@Deprecated
	public VarType findVariable(String identifier) {
		return null;
	}

	// Note: commented because method declaration itself is tested without
	// surrounding classes
	// @Override
	// @Deprecated
	// public MethodScope findMethod(String identifier, VarType[] params) {
	// return null;
	// }
	//
	// @Override
	// @Deprecated
	// public MethodScope addMethod(String identifier, VarType returntype,
	// VarType[] params) throws Exception {
	// throw new RuntimeException("It's not allowed to use global methods");
	// }

	@Override
	public String getName() {
		return "--***SUPERGLOBAL***--";
	}
}
