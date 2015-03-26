package rp.robotics.gridmap;
import java.util.ArrayList;

/**
 * Stores two objects of type Node<Integer> that are directly connected and have different x and/or y values
 * @author Dom
 * @param A	The first node in the connection - cannot be the same as the second
 * @param B	The second node in the connection - cannot be the same as the first
 */
public class Connection {
	private Node<Integer> nodeA;
	private Node<Integer> nodeB;
	
	public Connection(Node<Integer> A, Node<Integer> B)
	{
		if (A.getX() != B.getX() || A.getY() != B.getY())
		{
			nodeA = A;
			A.addConnection(this);
			nodeB = B;
			B.addConnection(this);
		}
	}
	
	/** 
	 * Gets a list of nodes that are in the connection
	 * 
	 * @return list of nodes that are connected (will always be a pair)
	 */
	public ArrayList<Node<Integer>> getNodes()
	{
		ArrayList<Node<Integer>> temp = new ArrayList<Node<Integer>>();
		temp.add(nodeA);
		temp.add(nodeB);
		return temp;
	}
	
	/** 
	 * Given that one node is known, this method can be used to find the other
	 *  
	 * @param node1 The known node
	 * @return the unknown node (the node in the connection that is not node1)
	 */
	public Node<Integer> getNeighbour(Node<Integer> node1)
	{
		if (node1 == nodeA)
		{
			return nodeB;
		}
		return nodeA;
	}
}
