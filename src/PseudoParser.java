import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;

public class PseudoParser {
	ArrayList <PseudoLexer.Token> tokens;
	ArrayList <PseudoLexer.Token> symbols = new ArrayList<>();
	ArrayList <PseudoLexer.Token> tempTokenList;
	ArrayList <Tupla> tuplas = new ArrayList<>();
	PseudoLexer.Token tempToken;
	Tupla tupla;
	SymbolTable table;
	Symbol symbol;
	JFrame frame;
	GUI gui;
	Canvas canvas;
	int index = 0;
	int lineCode = 1;
	ArrayList <Integer> tempLineCode = new ArrayList<>();
	int arraySize = 0;
	int i = 0;
	ArrayList <Integer> repeat = new ArrayList<>();
	
	public PseudoParser(GUI gui, String input) {
		this.gui = gui;
		canvas = gui.getCanvas();
		this.frame = gui.getFrame();
		PseudoLexer lexer = new PseudoLexer();
		table = new SymbolTable();
		tokens = lexer.lex(input);
		arraySize = tokens.size();
	}
	public void printTokens() {
		for(int x=0; x<tokens.size(); x++) {
			System.out.println(tokens.get(x).type.name() + " " + tokens.get(x).data);
		}
	}
	private void match(String name) {
		//System.out.println(tokens.size());
		if(tokens.size() > 3) {
			if(tokens.get(index).type.name().equals(name)) {
				switch(tokens.get(index).type.name()) {
					case "INICIOPROGRAMA": break;
					case "FINPROGRAMA": break;
					case "FINCICLO": table.define(new VariableSymbol(tokens.get(index).data, new BuiltInTypeSymbol("cadena"), true)); break;
					case "CICLO": table.define(new VariableSymbol(tokens.get(index).data, new BuiltInTypeSymbol("cadena"), true)); break;
					case "UP":
					case "DOWN":
					case "LEFT":
					case "RIGHT":
					case "ARRIBA":
					case "ABAJO": 
					case "IZQUIERDA":
					case "DERECHA": table.define(new VariableSymbol(tokens.get(index).data, new BuiltInTypeSymbol("cadena"), true)); break;
					case "PASOS": table.define(new VariableSymbol(tokens.get(index).data, new BuiltInTypeSymbol("entero"), Integer.parseInt(tokens.get(index).data), true)); break;				
					default: JOptionPane.showMessageDialog(this.gui.getFrame(), "Instrucción invalida!", "ERROR", JOptionPane.WARNING_MESSAGE); throw new Error("NO MATCH");
				}
				index++;
				return;
			} else {
				gui.setResetEnabled(true);
				if(index == 0) {
					JOptionPane.showMessageDialog(this.gui.getFrame(), "Instrucción requerida: INICIO-PROGRAMA", "ERROR", JOptionPane.WARNING_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(this.gui.getFrame(), "Instrucción invalida!", "ERROR", JOptionPane.WARNING_MESSAGE);	
				}
				throw new Error("NO MATCH");
			}
		} else {
			gui.setResetEnabled(true);
			JOptionPane.showMessageDialog(this.gui.getFrame(), "INSUFICIENTES COMANDOS", "ERROR", JOptionPane.WARNING_MESSAGE);
			throw new Error("TOO FEW COMMANDS");
		}
	}
	public void programa() {
		match("INICIOPROGRAMA");
		enunciados();
		match("FINPROGRAMA");
		//printTokens();
		printTuplas();
	}
	public void interpretar() {
		i = 0;
		arraySize = -1;
		System.out.println("\nEJECUTANDO PROGRAMA...");
		final Timer t = new Timer(100, null);
		t.setInitialDelay(0);
		t.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(canvas.getTimerRunning() == false) {
					if(canvas.getCanMove() && canvas.getMetas() > 0 && i < tuplas.size() && canvas.getPlayerX() >= 0 && canvas.getPlayerX() < 9 && canvas.getPlayerY() >= 0 && canvas.getPlayerY() < 9 && canvas.getGridValue(canvas.getPlayerX(), canvas.getPlayerY()) != 3){
						canvas.setTimerRunning(true);
						switch(tuplas.get(i).getSymbol()) {
							case "ciclo": repeat.add(tuplas.get(i).getSteps()); arraySize += 1; canvas.setTimerRunning(false); System.out.println("NEW CICLO: " + repeat); break;
							case "fin-ciclo":	if(repeat.get(arraySize) > 1) {
													repeat.set(arraySize, repeat.get(arraySize)-1);
													System.out.println("DECREASED: " + repeat);
													i = tuplas.get(i).getSteps()-1; 
												} else {
													repeat.remove(arraySize);
													arraySize -= 1;
													System.out.println("REMOVED: " + repeat);
												} 
												canvas.setTimerRunning(false); break;
							default: canvas.nextMove(tuplas.get(i).getSymbol(), tuplas.get(i).getSteps());
						}
					} else {
						t.stop();
						System.out.println("FIN DE PROGRAMA");
						if(canvas.getMetas() > 0) {
							canvas.loser();
						}
					}
					//System.out.println("REPEAT: "+ repeat + " LINECODE: "+ i);
					i++;
				}
			}
		});
		t.setRepeats(true);
		t.start();
	}
	public void printTuplas() {
		for(int i=0; i<tuplas.size(); i++) {
			table = tuplas.get(i).getSymbols();
			System.out.println("("+tuplas.get(i).getLineCode()+", "+tuplas.get(i).getSymbol()+", "+tuplas.get(i).getSteps()+")");
		}
	}
	private void enunciados() {
		enunciado();
		if(tokens.size() > 4) {
			if(index < (tokens.size())) {
				if(tokens.get(index).type.name().equals("FINPROGRAMA")){
					return;
				} else {
					enunciados();
				}
			} else {
				gui.setResetEnabled(true);
				JOptionPane.showMessageDialog(this.gui.getFrame(), "Instrucción requerida: FIN-PROGRAMA", "ERROR", JOptionPane.WARNING_MESSAGE);	
				throw new Error("FIN-PROGRAMA MISSING");
			}
		}
	}
	private void enunciado() {
		if(index > 0 && tokens.size() > 3) {
			if(!tokens.get(index).type.name().equals("ERROR")) {
				switch(tokens.get(index).type.name()){
					case "CICLO": ciclo(); break;
					case "FINCICLO": finCiclo(); break;
					default: mover(); 
				}
				return;
			} else {
				JOptionPane.showMessageDialog(this.gui.getFrame(), "Enunciado invalido!", "ERROR", JOptionPane.WARNING_MESSAGE);
				gui.setResetEnabled(true);
				throw new Error("enunciado invalido");
			}
		} else {
			gui.setResetEnabled(true);
			JOptionPane.showMessageDialog(this.gui.getFrame(), "INSUFICIENTES COMANDOS", "ERROR", JOptionPane.WARNING_MESSAGE);
		}
	}
	private void ciclo() {
		table = new SymbolTable();
		match("CICLO");
		match("PASOS");
		tempLineCode.add(lineCode);
		tupla = new Tupla(lineCode, table.getNname(2), table.getValue()); 
		tuplas.add(tupla);
		lineCode++;
	}
	private void finCiclo() {
		table = new SymbolTable();
		match("FINCICLO");
		if(tempLineCode.size() > 0) {
			tupla = new Tupla(lineCode, table.getNname(1), tempLineCode.get(tempLineCode.size()-1));
			tempLineCode.remove(tempLineCode.size()-1);
		} else {
			gui.setResetEnabled(true);
			JOptionPane.showMessageDialog(this.gui.getFrame(), "Instrucción requerida: CICLO", "ERROR", JOptionPane.WARNING_MESSAGE);
			throw new Error("FALTA DE COMANDO CICLO");
		}
		tuplas.add(tupla);
		lineCode++;
	}
	private void mover() {
		switch(tokens.get(index).type.name()) {
			case "ARRIBA": arriba(); break;
			case "UP": moveUp(); break;
			case "ABAJO": abajo(); break;
			case "DOWN": moveDown(); break;
			case "IZQUIERDA": izquierda(); break;
			case "LEFT": moveLeft(); break;
			case "DERECHA": derecha(); break;
			case "RIGHT": moveRight(); break;
			default: gui.setResetEnabled(true); JOptionPane.showMessageDialog(this.gui.getFrame(), "Comando invalido!", "ERROR", JOptionPane.WARNING_MESSAGE); throw new Error("comando invalido");
		}
	}
	private void moveUp() {
		table = new SymbolTable();
		match("UP");
		match("PASOS");
		tupla = new Tupla(lineCode, table.getNname(2), table.getValue()); 
		tuplas.add(tupla);
		lineCode++;
	}
	private void arriba() {
		table = new SymbolTable();
		match("ARRIBA");
		match("PASOS");
		tupla = new Tupla(lineCode, table.getNname(2), table.getValue()); 
		tuplas.add(tupla);
		lineCode++;
	}
	private void moveDown() {
		table = new SymbolTable();
		match("DOWN"); 
		match("PASOS");
		tupla = new Tupla(lineCode, table.getNname(2), table.getValue()); 
		tuplas.add(tupla);
		lineCode++;
	}
	private void abajo() {
		table = new SymbolTable();
		match("ABAJO"); 
		match("PASOS");
		tupla = new Tupla(lineCode, table.getNname(2), table.getValue()); 
		tuplas.add(tupla);
		lineCode++;
	}
	private void moveLeft() {
		table = new SymbolTable();
		match("LEFT");
		match("PASOS");
		tupla = new Tupla(lineCode, table.getNname(2), table.getValue()); 
		tuplas.add(tupla);
		lineCode++;
	}
	private void izquierda() {
		table = new SymbolTable();
		match("IZQUIERDA");
		match("PASOS");
		tupla = new Tupla(lineCode, table.getNname(2), table.getValue()); 
		tuplas.add(tupla);
		lineCode++;
	}
	private void moveRight() {
		table = new SymbolTable();
		match("RIGHT");
		match("PASOS");
		tupla = new Tupla(lineCode, table.getNname(2), table.getValue()); 
		tuplas.add(tupla);
		lineCode++;
	}
	private void derecha() {
		table = new SymbolTable();
		match("DERECHA");
		match("PASOS");
		tupla = new Tupla(lineCode, table.getNname(2), table.getValue()); 
		tuplas.add(tupla);
		lineCode++;
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