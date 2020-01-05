public class Symbol{
	String name;
	Type type;
	int value;
	boolean toPrint = false;
	boolean cadena = false;
	boolean arithmetic = false;
	boolean relacional = false;
	
	public Symbol(String name) {
		this.name = name;
		this.value = 0;
	}
	public Symbol(String name, boolean toPrint) {
		this.name = name;
		this.toPrint = toPrint;
		if(name.compareTo("+") == 0 || name.compareTo("-") == 0 || name.compareTo("*") == 0 || name.compareTo("/") == 0) {
			this.arithmetic = true;
			this.value = 0;
		}
		if(name.compareTo("<") == 0 || name.compareTo(">") == 0 || name.compareTo("==") == 0 || name.compareTo("<=") == 0 || name.compareTo(">=") == 0 || name.compareTo("!=") == 0) {
			this.relacional = true;
			this.value = 0;
		}
		if(isInteger(this.name)) {
			this.value = Integer.parseInt(name);
		}
	}
	public Symbol(String name, boolean toPrint, boolean cadena) {
		this(name);
		this.type = null;
		this.toPrint = toPrint;
		this.cadena = cadena;
		this.value = 0;
	}
	public Symbol(String name, Type type, int value, boolean toPrint) {
		this.name = name;
		this.type = type;
		this.value = value;
		this.toPrint = toPrint;
	}
	public Symbol(String name, Type type) {
		this(name);
		this.type = type;
		this.value = 0;
	}
	public Symbol(String name, Type type, boolean toPrint) {
		this(name);
		this.type = type;
		this.toPrint = toPrint;
		this.value = 0;
	}
	public Symbol(String name, Type type, boolean toPrint, boolean cadena) {
		this(name);
		this.type = type;
		this.toPrint = toPrint;
		this.cadena = cadena;
		this.value = 0;
	}
	public String getName() {
		return name;
	}
	public boolean getToPrint() {
		return this.toPrint;
	}
	public String toString() {
		if(type != null) {
			return '<'+getName()+":"+type+'>';
		}
		return getName();
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getValue() {
		return this.value;
	}
	public boolean isArithmetic() {
		return this.arithmetic;
	}
	public boolean isRelacional() {
		return this.relacional;
	}
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}
}