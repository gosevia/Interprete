public class Tupla { 
  public final int line;
  public final int steps;
  public int trueOutcome, falseOutcome; 
  public final String symbol; 
  public SymbolTable table;
  
  public Tupla(int line, String symbol, int steps) { 
    this.line = line; 
    this.symbol = symbol;
    this.steps = steps;
    this.table = null;
    this.trueOutcome = -1;
    this.falseOutcome = -1;
  }
  public String getLineCode() {
	  return Integer.toString(line);
  }
  public String getSymbol() {
	  return this.symbol;
  }
  public int getSteps() {
	  return this.steps;
  }
  public SymbolTable getSymbols() {
	  return this.table;
  }
  public int getSymbolTableSize(){
	  return this.table.getTableSize();
  }
  public String getTrueOutcome() {
	  return Integer.toString(trueOutcome);
  }
  public String getFalseOutcome() {
	  return Integer.toString(falseOutcome);
  }
  public int getTrueOutcomeValue() {
	  return this.trueOutcome;
  }
  public int getFalseOutcomeValue() {
	  return this.falseOutcome;
  }
  public void setLineNumber(int num) {
	  this.falseOutcome = num;
  }
  public void setLineNumbers(int num) {
	  this.trueOutcome = num;
	  this.falseOutcome = num;
  }
} 