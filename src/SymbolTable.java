import java.util.*;
public class SymbolTable implements Scope {
	Map<String, Symbol> symbols = new LinkedHashMap<String, Symbol>();
	Symbol s;
	
	public SymbolTable() {
		initTypeSystem();
	}
	protected void initTypeSystem() {
		define(new BuiltInTypeSymbol("entero"));
		define(new BuiltInTypeSymbol("cadena"));
	}
	public String getScopeName() {
		return "global";
	}
	public Scope getEnclosingScope() {
		return null;
	}
	public void define(Symbol sym) {
		symbols.put(sym.name, sym);
	}
	public Symbol resolve(String name) {
		return symbols.get(name);
	}
	public String toString() {
		return getScopeName()+":"+symbols;
	}
	public int getTableSize() {
		return symbols.size();
	}
	public ArrayList<String> printValues() {
		ArrayList<String> list = new ArrayList<>();
		for(String key : symbols.keySet()) {
			if(symbols.get(key).getToPrint()) {
				list.add(symbols.get(key).getName());
			}
		}
		return list;
	}
	public String getCadena() {
		s = new Symbol("new");
		for(String key : symbols.keySet()) {
            s = symbols.get(key);
            if(s.cadena) {
            	return s.getName();
            }
        }
		return null;
	}
	public boolean isCadena() {
		boolean c = false;
		s = new Symbol("new");
		for(String key : symbols.keySet()) {
            s = symbols.get(key);
            if(s.cadena) {
            	c = true;
            } else {
            	c = false;
            }
        }
		return c;
	}
	public String getArithmetic() {
		s = new Symbol("new");
		for(String key : symbols.keySet()) {
            s = symbols.get(key);
            if(s.isArithmetic()) {
            	return s.getName();
            }
        }
		return null;
	}
	public String getRelacional() {
		s = new Symbol("new");
		for(String key : symbols.keySet()) {
            s = symbols.get(key);
            if(s.isRelacional()) {
            	return s.getName();
            }
        }
		return "";
	}
	public void setValue(int value) {
		s = new Symbol("new");
		for(String key : symbols.keySet()) {
            s = symbols.get(key);
        }
		s.setValue(value);
	}
	public int getNvalue(int n) {
		ArrayList<Integer> values = new ArrayList<>();
		s = new Symbol("new");
		for(String key : symbols.keySet()) {
            values.add(symbols.get(key).value);
        }
		return values.get(values.size()-n);
	}
	public String getNname(int n) {
		ArrayList<String> values = new ArrayList<>();
		s = new Symbol("new");
		for(String key : symbols.keySet()) {
            values.add(symbols.get(key).getName());
        }
		return values.get(values.size()-n);
	}
	public void setNvalue(int n, int value) {
		ArrayList<Symbol> s = new ArrayList<>();
		for(String key : symbols.keySet()) {
            s.add(symbols.get(key));
        }
		s.get(s.size()-n).setValue(value);;
	}
	// Obtener el último valor de SymbolTable
	public int getValue() {
		s = new Symbol("new");
		for(String key : symbols.keySet()) {
            s = symbols.get(key);
        }
		return s.getValue();
	}
	public String getLastName() {
		s = new Symbol("new");
		for(String key : symbols.keySet()) {
            s = symbols.get(key);
        }
		return s.getName();
	}
}
