package rp.robotics.gridmap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

import lejos.geom.Point;
import rp.robotics.mapping.IGridMap;
import rp.robotics.mapping.RPLineMap;


public class GridMap implements IGridMap {
	
	private ArrayList<Node<Integer>> map;
	private int XSize;
	private int YSize;
	
	private float xStart;
	private float yStart;
	
	private float cellSize;
	private RPLineMap lineMap;
	
	public GridMap(int xSize, int ySize, float xStart, float yStart, float cellSize, RPLineMap linemap)
	{
		map = new ArrayList<Node<Integer>>();
		
		XSize = xSize;
		YSize = ySize;
		this.xStart = xStart;
		this.yStart = yStart;
		this.cellSize = cellSize;
		this.lineMap = linemap;
		/*
		int [] [] nicksGraph = {//9x6 graph
				{0,0,1,0,0,1}, 
				{0,1,0,0,1,1,0,2}, 
				{0,2,0,3,0,1}, 
				{0,3,0,2,0,4}, 
				{0,4,0,3,0,5}, 
				{0,5,0,6,1,5,0,4}, 
				{0,6,1,6,0,5}, 
				{1,0,0,0,1,1,2,0}, 
				{1,1,1,2,2,1,1,0,0,1}, 
				{1,2,2,2,1,1,1,3}, 
				{1,3,1,2,1,4,2,3}, 
				{1,4,2,4,1,5,1,3}, 
				{1,5,1,4,2,5,1,6,0,5}, 
				{1,6,0,6,1,5,2,6}, 
				{2,0,3,0,2,1,1,0}, 
				{2,1,2,2,1,1,2,0,3,1}, 
				{2,2,1,2,2,1,2,3,3,2}, 
				{2,3,2,2,2,4,3,3,1,3}, 
				{2,4,1,4,2,5,2,3,3,4}, 
				{2,5,2,4,1,5,2,6,3,5}, 
				{2,6,3,6,2,5,1,6}, 
				{3,0,2,0,3,1}, 
				{3,1,3,0,4,1,2,1,3,2}, 
				{3,2,2,2,4,2,3,1}, 
				{3,3,2,3,3,4}, 
				{3,4,2,4,3,3}, 
				{3,5,3,6,2,5,4,5}, 
				{3,6,2,6,3,5}, 
				{4,0}, 
				{4,1,4,2,5,1,3,1}, 
				{4,2,4,1,5,2,3,2}, 
				{4,3}, 
				{4,4}, 
				{4,5,5,5,3,5}, 
				{4,6}, 
				{5,0}, 
				{5,1,4,1,5,2,6,1}, 
				{5,2,4,2,5,1,6,2}, 
				{5,3}, 
				{5,4}, 
				{5,5,4,5,6,5}, 
				{5,6}, 
				{6,0,7,0,6,1}, 
				{6,1,6,0,5,1,6,2,7,1}, 
				{6,2,5,2,6,1,7,2}, 
				{6,3,7,3,6,4}, 
				{6,4,6,3,7,4}, 
				{6,5,5,5,6,6,7,5}, 
				{6,6,7,6,6,5}, 
				{7,0,6,0,7,1,8,0}, 
				{7,1,8,1,7,0,6,1,7,2}, 
				{7,2,7,3,8,2,6,2,7,1}, 
				{7,3,6,3,7,2,7,4,8,3}, 
				{7,4,7,3,8,4,6,4,7,5}, 
				{7,5,8,5,7,6,7,4,6,5}, 
				{7,6,6,6,7,5,8,6}, 
				{8,0,8,1,7,0,9,0}, 
				{8,1,8,2,9,1,7,1,8,0}, 
				{8,2,8,1,7,2,8,3}, 
				{8,3,8,2,7,3,8,4}, 
				{8,4,8,5,8,3,7,4}, 
				{8,5,9,5,8,4,7,5,8,6}, 
				{8,6,8,5,7,6,9,6}, 
				{9,0,9,1,8,0}, 
				{9,1,8,1,9,2,9,0}, 
				{9,2,9,1,9,3}, 
				{9,3,9,2,9,4}, 
				{9,4,9,5,9,3}, 
				{9,5,8,5,9,4,9,6}, 
				{9,6,9,5,8,6} 
				};
		//*
		for (int i = 0; i < nicksGraph.length; i++)
		{
			
			map.add(new Node<Integer>(nicksGraph[i][0], nicksGraph[i][1]));
			
		}
		for (int i = 0; i < nicksGraph.length; i++)
		{
			if (nicksGraph[i].length > 2)
			{
				for (int j = 1; j < (nicksGraph[i].length)/2; j++)
				{
					addConnection(nicksGraph[i][0], nicksGraph[i][1], nicksGraph[i][2*j], nicksGraph[i][2*j+1]);
				}
			}
		}
		*/
		for(int i =0; i< xSize; i++)
		{
			for(int j =0 ; j< ySize; j++)
			{
				if(isValidGridPosition(i, j))
				{
					addToMap(new Node<Integer>(i,j));
				}
				
			}
		}
		
	}
	
	public void addToMap(Node<Integer> item)
	{
		map.add(item);
	}
	
	public Node<Integer> getNode(int x, int y)
	{
		for (int i = 0; i<map.size(); i++)//loop through the map
		{
			if (map.get(i).getX() == x && map.get(i).getY() == y)//check every item
			{
				return map.get(i);
			}
		}
		return map.get(0);
	}
	
	//returns false if connection cannot be added
	public boolean addConnection(int x1, int y1, int x2, int y2)
	{
		if (!onGrid(x1, y1) && !onGrid(x2, y2))
		{
			return false;
		} 
		Node<Integer> nodeA = getNode(x1, y1);
		Node<Integer> nodeB = getNode(x2, y2);
		new Connection(nodeA, nodeB);//automatically assigns references of itself to nodeA and nodeB
		return true;
	}
	
	public ArrayList<Node<Integer>> getMap()
	{
		return map;
	}
	
	public boolean onGrid(int x, int y)
	{
		for (int i = 0; i<map.size(); i++)//loop through the map
		{
			if (map.get(i).getX() == x && map.get(i).getY() == y)//check every item
			{
				return true;//return true if it matches
			}
		}
		return false;//return false if none match
	}
	
	public boolean DepthFirstNodeSearch(int x1, int y1, int x2, int y2)
	{
		if (onGrid(x1, y1)
				 && onGrid(x2, y2))
		{
			ArrayList<Node<Integer>> discovered = new ArrayList<Node<Integer>>();//found nodes
			Stack<Node<Integer>> stack = new Stack<Node<Integer>>();//frontier
			stack.push(getNode(x1,y1));//put starting node in
			Node<Integer> node;//used in each iteration to store top item from stack
			while (!stack.isEmpty())//repeat until no more unchecked nodes can be reached
			{
				node = stack.pop();//get top node
				if (!discovered.contains(node))//make sure it's not been seen before
				{
					if (x2 == node.getX() && y2 == node.getY())//check if it's the target
					{
						return true;//if so, return true and we are done with the search
					}
					discovered.add(node);//else add it to list of discovered nodes
					for (int i = 0; i<node.getConnections().size(); i++)//for every connection it has
					{
						stack.push(node.getConnections().get(i).getNeighbour(node));//add the connected node to the frontier
					}
				}
			}
		}
		return false;//by default - i.e. if any part fails, return false (a node doesn't exist, there isn't a path, ...)
	}
	
	public boolean BreadthFirstNodeSearch(int x1, int y1, int x2, int y2)
	{
		if (onGrid(x1, y1) && onGrid(x2, y2))
		{
			ArrayList<Node<Integer>> discovered = new ArrayList<Node<Integer>>();//found nodes
			ArrayDeque<Node<Integer>> queue = new ArrayDeque<Node<Integer>>();//frontier
			queue.add(getNode(x1, y1));//put starting node in
			discovered.add(queue.peek());
			Node<Integer> target = getNode(x2, y2);//get node we want to find
			Node<Integer> node;//used in each iteration to store top item from queue
			Node<Integer> newNode;//used in each iteration to store each node accessible from current node
			while(!queue.isEmpty())
			{
				node = queue.poll();
				if (target == node)
				{
					return true;
				}
				for (int i = 0; i<node.getConnections().size(); i++)//for every connection it has
				{
					newNode = node.getConnections().get(i).getNeighbour(node);//get the connected node
					if (!discovered.contains(newNode))//if it's not been discovered before
					{
						discovered.add(newNode);//add it to the list of discovered nodes
						queue.add(newNode);//and add it to the queue
					}
				}
			}
			
		}
		return false;
	}

	@Override
	public int getXSize() {
		// TODO Auto-generated method stub
		return XSize;
	}

	@Override
	public int getYSize() {
		// TODO Auto-generated method stub
		return YSize;
	}

	@Override
	public boolean isValidGridPosition(int _x, int _y) {
		return _x >= 0 && _y >= 0 && _x < XSize && _y < YSize && !isObstructed(_x, _y);
	}

	@Override
	public boolean isObstructed(int _x, int _y) {
		// TODO Auto-generated method stub
			
		return !lineMap.inside(new Point(_x*cellSize +xStart, _y*cellSize+yStart));
	}

	@Override
	//won't be used, it's just getNode but for the wrong data type
	public Point getCoordinatesOfGridPosition(int _x, int _y) {
		// TODO Auto-generated method stub
		return new Point(_x, _y);
	}

	public float getXOffset()
	{
		return this.xStart;
	}
	
	public float getYOffset()
	{
		return this.yStart;
	}
	
	@Override
	public boolean isValidTransition(int _x1, int _y1, int _x2, int _y2) {
		return BreadthFirstNodeSearch(_x1, _y1, _x2, _y2);
	}

	@Override
	public float rangeToObstacleFromGridPosition(int _x, int _y, float _heading) {
		int i = 0;
		Node<Integer> node = getNode(_x, _y);
		Node<Integer> neighbour;
		while (true)
		{
			if (_heading == 0)//north
			{
				neighbour = getNode(node.getX(), node.getY()+1);
			}
			else if (_heading == 1)//east
			{
				neighbour = getNode(node.getX()+1, node.getY());
			}
			else if (_heading == 2)//south
			{
				neighbour = getNode(node.getX(), node.getY()-1);
			}
			else //west
			{
				neighbour = getNode(node.getX()-1, node.getY());
			}
			if (!node.hasNeighbour(neighbour))
			{
				break;
			}
			node = neighbour;
			i++;
		}
		i += 0.5;
		return i;
	}
}
