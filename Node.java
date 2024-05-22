import java.util.ArrayList;

/** Node that stores data of 
 * type T and points to any
 * number of other ordered
 * nodes, in a specific order.
 * Whichever node is added first
 * is the first node, and so on.
 */
class Node<T> {
	private final T data;
	private ArrayList<Node<T>> nextNodes;

	public Node(T data) {
		this.data = data;
		this.nextNodes = new ArrayList<Node<T>>();
	}
	
	public Node<T> getNextNode(int i) {
		return nextNodes.get(i);
	}

	public T getData() {
		return data;
	}

	public void addNextNode(Node<T> nextNode) {
		nextNodes.add(nextNode);
	}
}
