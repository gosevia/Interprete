import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PseudoLexer {
	public enum TokenType {
		INICIOPROGRAMA("inicio-programa"),
		FINPROGRAMA("fin-programa"),
		CICLO("ciclo"),
		FINCICLO("fin-ciclo"),
		PASOS("[1-9]+"),
		UP("up"),
		DOWN("down"),
		LEFT("left"),
		RIGHT("right"),
		ARRIBA("arriba"),
		ABAJO("abajo"),
		IZQUIERDA("izquierda"),
		DERECHA("derecha"),
		ESPACIOS("[ \t\f\r\n]+"),
		ERROR(".+");
		
		public final String pattern;
		
		private TokenType(String pattern) {
			this.pattern = pattern;
		}
	}
	public class Token {
		public TokenType type;
		public String data;
		public Token(TokenType type, String data) {
			this.type = type;
			this.data = data;
		}
		@Override
		public String toString() {
			return String.format("(%s \"%s\")", type.name(), data);
		}
	}
	public ArrayList<Token> lex(String input) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		StringBuffer tokenPatternsBuffer = new StringBuffer();
		for(TokenType tokenType : TokenType.values())
			tokenPatternsBuffer.append(String.format("|(?<%s>%s)", tokenType.name(), tokenType.pattern));
		
		Pattern tokenPatterns = Pattern.compile(new String(tokenPatternsBuffer.substring(1)));
		Matcher matcher = tokenPatterns.matcher(input);
		
		while(matcher.find()) {
			for(TokenType t: TokenType.values()) {
				if(matcher.group(TokenType.ESPACIOS.name()) != null) continue;
				else if(matcher.group(t.name()) != null) {
					tokens.add(new Token(t, matcher.group(t.name())));
					break;
				}
			}
		}
		return tokens;
	}
}
