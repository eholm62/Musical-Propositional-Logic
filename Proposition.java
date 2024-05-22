import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
	
class Proposition {
		
	private Node<Token> headNode;
	private LinkedList<Statement> orderedStatements;
	private String stringValue;
		
	public Proposition(String propositionString) {
		ArrayList<Token> tokens = tokenize(propositionString);
		this.headNode = makeParseTree(tokens);
		this.orderedStatements = this.makeStatementsOrdered(tokens);
		this.stringValue = stringFromTokens(tokens);
	}	
		
	public String toString() {
		return stringValue;
	}	
		
	public Node<Token> getHeadNode() {
		return headNode;
	}	
		
	public LinkedList<Statement> getStatementsOrdered() {
		return orderedStatements;
	}	
		
	private static Node<Token> makeParseTree(List<Token> tokens) {
		if (tokens.size() == 0) {
			return null;
		} else if (tokens.size() == 1) {
			if (tokens.get(0) instanceof Statement) {
				return new Node<Token>(tokens.get(0));
			} else {
				throw new RuntimeException("Error occured while constructing parseTree");
			}
		}

		
		int parenthesisDepth = 0;
		int indexLowestPrecedence = -1;
		Operator lowestPrecedence = null;
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).equals(Parenthesis.OPEN)) {
				parenthesisDepth++;
				continue;
			} else if (tokens.get(i).equals(Parenthesis.CLOSED)) {
				parenthesisDepth--;
				continue;
			} else if (parenthesisDepth > 0) {
				if (parenthesisDepth < 0) { 
					throw new RuntimeException("Each close parethesis must correspond to an open perenthesis");
				}
				continue;
			} else if (lowestPrecedence == null && tokens.get(i) instanceof Operator) {
				lowestPrecedence = (Operator)tokens.get(i);
				indexLowestPrecedence = i;
			} else if (tokens.get(i) instanceof Operator) { 
				if (tokens.get(i).after((Operator)lowestPrecedence)) {
					lowestPrecedence = (Operator)tokens.get(i);
					indexLowestPrecedence = i;
				}
			}
		}

		if (lowestPrecedence == null) {
			if (tokens.get(tokens.size() - 1).equals(Parenthesis.CLOSED)) {
				if (tokens.get(0).equals(Parenthesis.OPEN)) {
					return makeParseTree(tokens.subList(1, tokens.size() - 1));
				}
			}
			throw new RuntimeException("Each open parenthesis must correspend to a close parenthesis");
		}

		if (lowestPrecedence.equals(Operator.NOT)) {
			if (indexLowestPrecedence < tokens.size() - 1) {
				if (tokens.get(indexLowestPrecedence + 1) instanceof Statement) {
					Node<Token> headNode = new Node<Token>(Operator.NOT);
					headNode.addNextNode(new Node<Token>(tokens.get(indexLowestPrecedence + 1)));
					return headNode;
				} else if (tokens.get(indexLowestPrecedence + 1).equals(Parenthesis.OPEN)) {
					Node<Token> headNode = new Node<Token>(Operator.NOT);
					headNode.addNextNode(makeParseTree(tokens.subList(indexLowestPrecedence + 2, tokens.size() - 1)));
					return headNode;
				} else {
					throw new RuntimeException("The '¬' operator may only precede an atomic statement or an open parenthesis");
				}
			} else {
				throw new RuntimeException("The '¬' operator may only precede an atomic statement or an open parenthesis");
			}
		}

		Node<Token> headNode = new Node<Token>(lowestPrecedence);
		if (indexLowestPrecedence > -1) {
			headNode.addNextNode(makeParseTree(tokens.subList(0, indexLowestPrecedence)));
		} 
		if (indexLowestPrecedence + 1 < tokens.size()) {
			headNode.addNextNode(makeParseTree(tokens.subList(indexLowestPrecedence + 1, tokens.size())));
		}

		return headNode;
	}	
		
	private static void insertStatement(LinkedList<Statement> statements, Statement newStatement) {
		int i = 0;
		for (Statement statement : statements) {
			if (newStatement.equals(statement)) {
				return;
			} else if (newStatement.before(statement)) {
				statements.add(i, newStatement);
				return;
			}
			i++;
		}
		statements.add(newStatement);
	}	
		
	private LinkedList<Statement> makeStatementsOrdered(ArrayList<Token> tokens) {
		LinkedList<Statement> orderedStatements = new LinkedList<Statement>();
		for (Token token : tokens) {
			if (token instanceof Statement) {
				insertStatement(orderedStatements, (Statement)token);
			}
		}
		return orderedStatements;
	}	

	/** takes a string representation of a proposition and converts
	 * it to an ArrayList of tokens */
	private static ArrayList<Token> tokenize(String proposition) throws IllegalArgumentException {
		ArrayList<Token> tokens = new ArrayList<Token>();
		
		for (int i = 0; i < proposition.length(); i++) {
			char c = proposition.charAt(i);
			
			switch (c) {
				case ' ':
					continue;
				case '^':
					tokens.add(Operator.AND);
					continue;
				case 'v':
					tokens.add(Operator.OR);
					continue;
				case '-':
					tokens.add(Operator.NOT);
					continue;
				case '(':
					tokens.add(Parenthesis.OPEN);
					continue;
				case ')':
					tokens.add(Parenthesis.CLOSED);
					continue;
				case '>':
					tokens.add(Operator.CONDITIONAL);
					continue;
				case '<':
					if (i + 1 == proposition.length()) {
						throw new IllegalArgumentException("\"<\" operator is not recognized");
					}
					if (proposition.charAt(i + 1) == '>') {
						tokens.add(Operator.BICONDITIONAL);
						i++;
						continue;
					}
					throw new IllegalArgumentException("\"<\" operator is not recognized");
				default:
					if ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) == -1) {	
						throw new IllegalArgumentException("\"" + c + "\" operator is not recognized");
					} else {
						String statementName = "";
						while ("ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(c) != -1) {
							statementName += c;
							i++;
							if (i == proposition.length()) break;
							c = proposition.charAt(i);
						}
						tokens.add(new Statement(statementName));
						i--;
						continue;
					}
			}
		}
		
		return tokens;
	}	
		
	private static String stringFromTokens(ArrayList<Token> tokens) {
		String string = "";
		
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.equals(Parenthesis.OPEN) || token.equals(Operator.NOT)) {
				string += token;
			} else if (token.equals(Parenthesis.CLOSED)) {
				if (string.length() - 1 >= 0 && string.charAt(string.length() - 1) == ' ') {
					string = string.substring(0, string.length() - 1) + token + " ";
				} else {
					string += token + " ";
				}
			} else {
				string += token + " ";
			}
		}
		
		if (string.charAt(string.length() - 1) == ' ') {
			return string.substring(0, string.length() - 1);
		} else {
			return string;
		}
	}	
		
	public static interface Token {
		String toString();
		boolean equals(Token other);
		boolean before(Token other);
		boolean after(Token other);
	}	
		
	/** represents an atomic statement identified 
	 * by a sequence of capital letters
	 */	
	public static final class Statement implements Token{
		
		private final Letter[] data;
		private final int hashCode;
		
		/** represents a letter */	
		static enum Letter {
			A(0), B(1), C(2), D(3), E(4), F(5), G(6), 
			H(7), I(8), J(9), K(10), L(11), M(12),
			N(13), O(14), P(15), Q(16), R(17), S(18), 
			T(19), U(20), V(21), W(22), X(23), Y(24), Z(25);
		
			private byte value;
			private Letter(int value) {
				this.value = (byte)value;
			}
		
			public boolean before(Letter other) {
				return value < other.value;
			}
		
			public boolean after(Letter other) {
				return value > other.value;
			}
		}
		
		public Statement(String inData) throws IllegalArgumentException {
			this.data = new Letter[inData.length()];
			int hashCode = 0;
			int place = 1;
			
			for (int i = 0; i < inData.length(); i++) {
				switch (inData.charAt(i)) {
					case 'A':
						data[i] = Letter.A;
						hashCode += 0 * place;
						break;
					case 'B':
						data[i] = Letter.B;
						hashCode += 1 * place;
						break;
					case 'C':
						data[i] = Letter.C;
						hashCode += 2 * place;
						break;
					case 'D':
						data[i] = Letter.D;
						hashCode += 3 * place;
						break;
					case 'E':
						data[i] = Letter.E;
						hashCode += 4 * place;
						break;
					case 'F':
						data[i] = Letter.F;
						hashCode += 5 * place;
						break;
					case 'G':
						data[i] = Letter.G;
						hashCode += 6 * place;
						break;
					case 'H':
						data[i] = Letter.H;
						hashCode += 7 * place;
						break;
					case 'I':
						data[i] = Letter.I;
						hashCode += 8 * place;
						break;
					case 'J':
						data[i] = Letter.J;
						hashCode += 9 * place;
						break;
					case 'K':
						data[i] = Letter.K;
						hashCode += 10 * place;
						break;
					case 'L':
						data[i] = Letter.L;
						hashCode += 11 * place;
						break;
					case 'M':
						data[i] = Letter.M;
						hashCode += 12 * place;
						break;
					case 'N':
						data[i] = Letter.N;
						hashCode += 13 * place;
						break;
					case 'O':
						data[i] = Letter.O;
						hashCode += 14 * place;
						break;
					case 'P':
						data[i] = Letter.P;
						hashCode += 15 * place;
						break;
					case 'Q':
						data[i] = Letter.Q;
						hashCode += 16 * place;
						break;
					case 'R':
						data[i] = Letter.R;
						hashCode += 17 * place;
						break;
					case 'S':
						data[i] = Letter.S;
						hashCode += 18 * place;
						break;
					case 'T':
						data[i] = Letter.T;
						hashCode += 19 * place;
						break;
					case 'U':
						data[i] = Letter.U;
						hashCode += 20 * place;
						break;
					case 'V':
						data[i] = Letter.V;
						hashCode += 21 * place;
						break;
					case 'W':
						data[i] = Letter.W;
						hashCode += 22 * place;
						break;
					case 'X':
						data[i] = Letter.X;
						hashCode += 23 * place;
						break;
					case 'Y':
						data[i] = Letter.Y;
						hashCode += 24 * place;
						break;
					case 'Z':
						data[i] = Letter.Z;
						hashCode += 25 * place;
						break;
					default:
						throw new IllegalArgumentException("Must contain only capitals A-Z");
				}
				
				place *= 26;
			}

			this.hashCode = hashCode;
		}
		
		public String toString() {
			String string = "";
			for (Letter letter : data) 
				string += letter;
			return string;
		}
		
		public boolean before(Statement other) {
			int length = data.length;
			if (other.data.length < length) {
				length = other.data.length;
			}
			
			for (int i = 0; i < length; i++) {
				Letter letter = data[i];
				Letter otherLetter = other.data[i];
		
				if (letter.after(otherLetter)) return false;
				if (letter.before(otherLetter)) return true;
			}
		
			return data.length < other.data.length;
		}
		
		public boolean after(Statement other) {
			int length = data.length;
			if (other.data.length < length) {
				length = other.data.length;
			}
			
			for (int i = 0; i < length; i++) {
				Letter letter = data[i];
				Letter otherLetter = other.data[i];
		
				if (letter.before(otherLetter)) return false;
				if (letter.after(otherLetter)) return true;
			}
		
			return data.length > other.data.length;
		}

		public boolean before(Token other) {
			throw new IllegalArgumentException("Argument must be of type Proposition.Statement");
		}

		public boolean after(Token other) {
			throw new IllegalArgumentException("Argument must be of type Proposition.Statement");
		}

		public int hashCode() {
			return hashCode;
		}
		
		public boolean equals(Statement other) {
			return Arrays.equals(data, other.data);
		}

		public boolean equals(Object other) {
			if (other instanceof Statement) {
				return this.equals((Statement)other);
			} else {
				return false;
			}
		}


		/** will only be called if other is 
		 * not also of type Statement 
		 */
		public boolean equals(Token other) {
			return false;	
		}
	}	
		
	public static enum Operator implements Token {
		NOT(0), AND(1), OR(2),
		CONDITIONAL(3), 
		BICONDITIONAL(4);

		private byte value;

		private Operator(int value) {
			this.value = (byte)value;
		}

		public boolean before(Operator other) {
			return value < other.value;
		}

		public boolean after(Operator other) {
			return value > other.value;
		}

		public boolean before(Token other) {
			if (other instanceof Operator) {
				Operator operatorOther = (Operator)other;
				return value < operatorOther.value;
			} else {
				throw new IllegalArgumentException("Argument must be of type Proposition.Operator");
			}
		}

		public boolean after(Token other) {
			if (other instanceof Operator) {
				Operator operatorOther = (Operator)other;
				return value > operatorOther.value;
			} else {
				throw new IllegalArgumentException("Argument must be of type Proposition.Operator");
			}
		}
		
		public String toString() {
			switch (this) {
				case AND:
					return "∧";
				case OR:
					return "∨";
				case NOT:
					return "¬";
				case CONDITIONAL:
					return "→";
				case BICONDITIONAL:
					return "↔";
				default:
					return "";
			}
		}
		
		public boolean equals(Token other) {
			if (!(other instanceof Operator)) return false;
			return this == other;
		}
	}

	private static enum Parenthesis implements Token {
		OPEN,
		CLOSED;
		
		public String toString() {
			switch (this) {
				case OPEN:
					return "(";
				case CLOSED:
					return ")";
				default:
					return "";
			}
		}
		
		public boolean equals(Token other) {
			if (!(other instanceof Parenthesis)) return false;
			return this == other;
		}

		public boolean before(Token other) {
			throw new UnsupportedOperationException("Invalid operation on Parethesis object");
		}

		public boolean after(Token other) {
			throw new UnsupportedOperationException("Invalid operation on Parethesis object");
		}
	}

	public static void main(String[] args) throws Exception {
		Statement statement1 = new Statement(args[0]);
		Statement statement2 = new Statement(args[1]);
		
		System.out.println(statement1);
		System.out.println(statement2);
		System.out.println(statement1.equals(statement2));
		
		System.out.println(Statement.Letter.A == Statement.Letter.A);
	}
}
