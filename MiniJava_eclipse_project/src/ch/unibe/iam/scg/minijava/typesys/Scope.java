package ch.unibe.iam.scg.minijava.typesys;


public interface Scope {
	public VarType findVariable(String identifier);
	public void addVariable(String identifier,VarType type) throws Exception;
	public ClassScope findClass(String identifier);
	public ClassScope addClass(String identifier,String parentClass) throws Exception;
	
	public MethodScope findMethod(String identifier, VarType[] params);
	public MethodScope addMethod(String identifier,VarType returntype,VarType[] params,String[] paramNames) throws Exception;
	
	/**
	 * @return the name of the class or method depending what object this Scope is
	 */
	public String getName();
	
}
