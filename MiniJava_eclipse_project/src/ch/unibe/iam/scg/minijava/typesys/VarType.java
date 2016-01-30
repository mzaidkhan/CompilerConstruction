package ch.unibe.iam.scg.minijava.typesys;

public class VarType {

	public static VarType bool() {
		return new VarType(SimpleType.BOOLEAN);
	}

	public static VarType integer() {
		return new VarType(SimpleType.INTEGER);
	}

	public static VarType intArray() {
		return new VarType(SimpleType.INTEGER_ARRAY);
	}

	public static VarType voidT() {
		return new VarType(SimpleType.VOID);
	}

	public static VarType stringArray() {
		return new VarType(SimpleType.STRING_ARRAY);
	}

	public static VarType object(String className) {
		return new VarObjectType(className);
	}

	public SimpleType s;

	/**
	 * don't call this through your code, use the static methods to create type
	 * objects
	 * 
	 * @param s
	 */
	protected VarType(SimpleType s) {
		if (s == SimpleType.OBJECT
				&& this.getClass().getSimpleName().equals("Type"))
			throw new RuntimeException(
					"You should create an ObjectType instance instead of a Type instance.");
		this.s = s;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof VarType))
			return false;
		VarType t = (VarType) obj;
		return this.s == t.s;
	}

	@Override
	public String toString() {
		return this.s.toString();
	}

	public enum SimpleType {
		INTEGER("int"), BOOLEAN("boolean"), INTEGER_ARRAY("int[]"), STRING_ARRAY(
				"String[]"), VOID("void"), OBJECT("Object");

		private String name;

		SimpleType(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

}
