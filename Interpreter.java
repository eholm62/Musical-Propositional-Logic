import java.util.HashMap;
import java.util.Arrays;
import java.util.List;

// add better comments and error messages
class Interpreter {

	private static boolean conditional(boolean p, boolean q) {
		return !p || q;
	}

	private static boolean biconditional(boolean p, boolean q) {
		return conditional(p, q) && conditional(q, p);
	}

	public static boolean eval(Proposition proposition, HashMap<Proposition.Statement, Boolean> statementData) {
		return eval(proposition.getHeadNode(), statementData);
	}

	public static boolean eval(Node<Proposition.Token> headNode, HashMap<Proposition.Statement, Boolean> statementData) {
		if (headNode.getData() instanceof Proposition.Statement) {
			Boolean result = statementData.get((Proposition.Statement)(headNode.getData()));
			if (result == null) {
				throw new IllegalArgumentException("Statement data does not contain all the needed information");
			} else {
				return result;
			}
		}

		if (headNode.getData() instanceof Proposition.Operator) {
			if (headNode.getData().equals(Proposition.Operator.NOT)) {
				return !eval(headNode.getNextNode(0), statementData);
			} else if(headNode.getData().equals(Proposition.Operator.AND)) {
				return eval(headNode.getNextNode(0), statementData) && eval(headNode.getNextNode(1), statementData);
			} else if (headNode.getData().equals(Proposition.Operator.OR)) {
				return eval(headNode.getNextNode(0), statementData) || eval(headNode.getNextNode(1), statementData);
			} else if (headNode.getData().equals(Proposition.Operator.CONDITIONAL)) {
				return conditional(eval(headNode.getNextNode(0), statementData), eval(headNode.getNextNode(1), statementData));
			} else if (headNode.getData().equals(Proposition.Operator.BICONDITIONAL)) {
				return biconditional(eval(headNode.getNextNode(0), statementData), eval(headNode.getNextNode(1), statementData));
			}
		}

		throw new IllegalArgumentException("Nodes may only contain data of types Proposition.Operator or Proposition.Statement");
	}

	/** Takes in a boolean array and mutates it 
	 * such that it now represents the number one
	 * greater than the number it previously
	 * represented in binary */
	private static void increment(boolean[] data) {
		for (int i = data.length - 1; i > -1; i--) {
			if (data[i] == false) {
				data[i] = true;
				break;
			} else {
				data[i] = false;
			}
		}
	}

	public static boolean[] makeTruthTable(Proposition proposition) {
		List<Proposition.Statement> orderedStatements = proposition.getStatementsOrdered();
		boolean[] input = new boolean[orderedStatements.size()];
		boolean[] outputs = new boolean[1 << orderedStatements.size()];

		for (int i = 0; i < outputs.length; i++) {
			HashMap<Proposition.Statement, Boolean> statementData = new HashMap<Proposition.Statement, Boolean>();
			int j = 0;
			for (Proposition.Statement statement : orderedStatements) {
				statementData.put(statement, input[j]);
				j++;
			}
			outputs[i] = eval(proposition, statementData);
			increment(input);
		}

		return outputs;
	}
}
