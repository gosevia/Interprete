public class VariableSymbol extends Symbol {
	public VariableSymbol(String name, Type type) {
		super(name, type);
	}
	public VariableSymbol(String name, Type type, boolean toPrint) {
		super(name, type, toPrint);
	}
	public VariableSymbol(String name, Type type, int value, boolean toPrint) {
		super(name, type, value, toPrint);
	}
}
