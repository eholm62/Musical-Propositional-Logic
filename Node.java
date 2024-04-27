import java.util.ArrayList;

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
