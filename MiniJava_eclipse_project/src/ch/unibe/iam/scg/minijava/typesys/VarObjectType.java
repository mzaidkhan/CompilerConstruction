package ch.unibe.iam.scg.minijava.typesys;

public class VarObjectType extends VarType {

	public String className;

	public VarObjectType(String className) {
		super(VarType.SimpleType.OBJECT);
		this.className=className;
	}
	
	@Override
	public String toString() {
		return this.s+"("+this.className+")";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VarObjectType))
			return false;
		VarObjectType t=(VarObjectType)obj;
		return this.className.equals(t.className);
	}
	
}
