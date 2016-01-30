package ch.unibe.iam.scg.minijava.typesys;


public class ClassScope extends AbstractScope{
	
	/**
	 * identifier of the parentclass
	 */
	public String parentClass;
	public String name;
	
	
	public ClassScope(String className,String parentClass) {
		super();
		this.name=className;
		this.parentClass=parentClass;
	}

	@Override
	@Deprecated
	public ClassScope findClass(String currentClass) {
		return null;
	}


	@Override
	@Deprecated
	public ClassScope addClass(String identifier,String parentClass) throws Exception {
		throw new Exception("No nested classes allowed");
	}

	@Override
	public String getName() {
		return this.name;
	}
	
}
