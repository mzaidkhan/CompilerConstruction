package ch.unibe.iam.scg.minijava.typesys;

import java.util.HashMap;

public class MethodScope extends AbstractScope {

	public VarType returntype;
	private VarType[] params;
	private String name;
	private String[] paramNames;
	private HashMap<String, VarType> paramMap;
	

	public MethodScope(String methodName, VarType returntype,
			VarType[] params, String[] paramNames) {
		super();
		this.name = methodName;
		this.returntype = returntype;
		this.params = params;
		this.paramNames=paramNames;
		initHashMapOfVariables();
	}
	
	protected void setParameters(VarType[] params,String[] paramNames) {
		this.params = params;
		this.paramNames=paramNames;
		initHashMapOfVariables();
	}
	
	private void initHashMapOfVariables() {
		this.paramMap=new HashMap<String,VarType>();
		for(int i=0;i<params.length;i++)
			this.paramMap.put(paramNames[i], params[i]);
	}

	@Override
	public VarType findVariable(String identifier) {
		if (this.paramMap.containsKey(identifier))
			return paramMap.get(identifier);
		return super.findVariable(identifier);
	}

	@Override
	@Deprecated
	public ClassScope findClass(String currentClass) {
		return null;
	}

	@Override
	@Deprecated
	public ClassScope addClass(String identifier, String parentClass)
			throws Exception {
		throw new Exception("No anonymous classes inside methods allowed");
	}

	@Override
	@Deprecated
	public MethodScope findMethod(String identifier, VarType[] params) {
		return null;
	}

	@Override
	@Deprecated
	public MethodScope addMethod(String identifier, VarType returntype,
			VarType[] params,String[] paramNames) throws Exception {
		throw new Exception("No nested methods allowed");
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	protected String toStringSimple() {
		String r="returns: "+returntype+", arguments: "+params.length+" (";
		for(int i=0;i<params.length;i++){
			r+=paramNames[i]+":"+params[i]+", ";
		}
		if (params.length>0)
			r=r.substring(0,r.length()-2);
		return r+=")\n";
	}

	public boolean hasSameParameter(VarType[] params2) {
		if (this.params.length!=params2.length)
			return false;
		for(int i=0;i<this.params.length;i++) {
			if (!this.params[i].equals(params2[i]))
				return false;
		}
		return true;
	}


}
