import java.util.HashMap;
import java.util.LinkedList;
import java.util.Arrays;

class Test {
	public static void main(String[] args) {
		Proposition proposition = new Proposition(args[0]);
		HashMap<Proposition.Statement, Boolean> statementData = new HashMap<Proposition.Statement, Boolean>();

		int i = 1;
		LinkedList<Proposition.Statement> orderedStatements = proposition.getStatementsOrdered();
		for (Proposition.Statement statement : orderedStatements) {
			statementData.put(statement, Boolean.parseBoolean(args[i]));
			i++;
		}

		System.out.println("(" + proposition + ") for " + statementData);
		System.out.println(Interpreter.eval(proposition, statementData));

		System.out.println("\nFull Truth Table:");
		System.out.println(Arrays.toString(Interpreter.makeTruthTable(proposition)));
	}
}
