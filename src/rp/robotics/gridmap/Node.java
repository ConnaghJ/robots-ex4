package rp.robotics.gridmap;
import java.util.ArrayList;

/** 
 * A node used to store data on a 2-dimensional grid. Can be connected to other nodes and be assigned a predecessor.
 * 
 * @author Dom
 *
 * @param <A> The type of element held in x and y, and that other nodes must have in order to be assigned as this node's predecessor.
 */
public class Node<A> {
	private A x;
	private A y;
	private Node<A> previous;
	private ArrayList<Connection> connections;
	
	public String toString()
	{
		String output = "(" + getX() + "," + getY() + ")";
		return output;
	}
	
	public Node(A x, A y)
	{
		this.x = x;
		this.y = y;
		connections = new ArrayList<Connection>();
	}
	
	/** 
	 * A way to access the x value of the node
	 * 
	 * @return x value of the node
	 */
	public A getX()
	{
		return this.x;
	}
	
	/** 
	 * A way to access the y value of the node
	 * 
	 * @return y value of the node
	 */
	public A getY()
	{
		return this.y;
	}
	
	/** 
	 * Sets the predecessor value of the node; useful in search methods
	 * 
	 * @param newVal the new predecessor of the node - can be itself
	 */
	public void setPrevious(Node<A> newVal)
	{
		previous = newVal;
	}
	
	/**
	 * getter method for the predecessor value
	 * @return the predecessor value
	 */
	public Node<A> getPrevious()
	{
		try
		{
			return previous;
		}
		catch (NullPointerException e)
		{
			return this;
		}
		
	}
	
	/**
	 * adds connections that contain this node
	 * @param con the new connection
	 */
	public void addConnection(Connection con)
	{
		connections.add(con);
	}
	
	public boolean hasNeighbour(Node<Integer> neighbour)
	{
		for (int i = 0; i < connections.size(); i++)
		{
			if (connections.get(i).getNeighbour(neighbour) == neighbour)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * getter method for the list of connections
	 * @return the list of connections
	 */
	public ArrayList<Connection> getConnections()
	{
		return connections;
	}
}
