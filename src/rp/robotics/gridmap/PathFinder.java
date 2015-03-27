package rp.robotics.gridmap;
import java.util.ArrayList;
import java.util.Stack;
import java.util.ArrayDeque;


public class PathFinder {
	private GridMap map;
	
	public PathFinder (GridMap map)
	{
		this.map = map;
	}
	
	public ArrayDeque<Node<Integer>> DepthFirstSearch (int x1, int y1, int x2, int y2)
	{
		if (map.onGrid(x1, y1) && map.onGrid(x2, y2))
		{
			ArrayList<Node<Integer>> discovered = new ArrayList<Node<Integer>>();//found nodes
			Stack<Node<Integer>> stack = new Stack<Node<Integer>>();//frontier
			stack.push(map.getNode(x1,y1));//put starting node in
			stack.peek().setPrevious(stack.peek());//sets up a cyclical reference where the first value is its own precursor
			Node<Integer> node;//used in each iteration to store top item from stack
			while (!stack.isEmpty())//repeat until no more unchecked nodes can be reached
			{
				node = stack.pop();//get top node
				if (!discovered.contains(node))//make sure it's not been seen before
				{
					if (x2 == node.getX() && y2 == node.getY())//check if it's the target
					{
						//System.out.println("Debug: found target node in DFS");
						ArrayDeque<Node<Integer>> path = new ArrayDeque<Node<Integer>>();//path from (x1,y1) to (x2,y2) will go here
						path.add(node);//put current node onto bottom of queue
						while (true)//repeatedly
						{
							if (path.getLast().getX() == x1 && path.getLast().getY() == y1)//check if it's the starting node
							{
								break;//if so, exit loop
							}
							//System.out.println("Not found starting node yet, currently on node " + path.peekLast());
							//System.out.println("Currently experiencing minor technical difficulties: " + path.peekLast().getPrevious());
							path.addLast(path.getLast().getPrevious());//put previous node onto bottom of queue
						}
						//System.out.println("Debug: found first node from last node in DFS: " + path.getLast());
						return path;
					}
					discovered.add(node);//else add it to list of discovered nodes
					for (int i = 0; i<node.getConnections().size(); i++)//for every connection it has
					{
						stack.push(node.getConnections().get(i).getNeighbour(node));//add the connected node to the frontier
						if (!discovered.contains(node.getConnections().get(i).getNeighbour(node)))
						{
							node.getConnections().get(i).getNeighbour(node).setPrevious(node);//add a backwards pointer
						}
					}
				}
			}
		}
		return new ArrayDeque<Node<Integer>>();
	}
	
	public ArrayDeque<Node<Integer>> BreadthFirstSearch (int x1, int y1, int x2, int y2)
	{
		if (map.onGrid(x1, y1) && map.onGrid(x2, y2))
		{
			ArrayList<Node<Integer>> discovered = new ArrayList<Node<Integer>>();//found nodes
			ArrayDeque<Node<Integer>> queue = new ArrayDeque<Node<Integer>>();//frontier
			queue.add(map.getNode(x1,y1));//put starting node in
			queue.peek().setPrevious(queue.peek());//sets up a cyclical reference where the first value is its own precursor
			discovered.add(queue.peek());
			Node<Integer> node;//used in each iteration to store top item from stack
			while (!queue.isEmpty())//repeat until no more unchecked nodes can be reached
			{
				node = queue.poll();//get top node
				if (x2 == node.getX() && y2 == node.getY())//check if it's the target
				{
					//System.out.println("Debug: found target node in DFS");
					ArrayDeque<Node<Integer>> path = new ArrayDeque<Node<Integer>>();//path from (x1,y1) to (x2,y2) will go here
					path.add(node);//put current node onto bottom of queue
					while (true)//repeatedly
					{
						if (path.getLast().getX() == x1 && path.getLast().getY() == y1)//check if it's the starting node
						{
							break;//if so, exit loop
						}
						//System.out.println("Not found starting node yet, currently on node " + path.peekLast());
						//System.out.println("Currently experiencing minor technical difficulties: " + path.peekLast().getPrevious());
						path.addLast(path.getLast().getPrevious());//put previous node onto bottom of queue
					}
					//System.out.println("Debug: found first node from last node in DFS: " + path.getLast());
					return path;
				}
				for (int i = 0; i<node.getConnections().size(); i++)//for every connection it has
				{
					if (!discovered.contains(node.getConnections().get(i).getNeighbour(node)))
					{
						discovered.add(node.getConnections().get(i).getNeighbour(node));//add new node to discovered
						node.getConnections().get(i).getNeighbour(node).setPrevious(node);//add a backwards pointer
						queue.add(node.getConnections().get(i).getNeighbour(node));//add the connected node to the frontier
					}
				}
			}
		}
		return new ArrayDeque<Node<Integer>>();
	}
	
	
}
