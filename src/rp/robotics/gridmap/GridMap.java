package rp.robotics.gridmap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Stack;

import lejos.geom.Point;
import lejos.robotics.navigation.Pose;
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
		for(int i=0; i< xSize;i++)
		{
			for(int j=0;j<ySize;j++)
			{
				if(isValidGridPosition(i,j))
				{	
					addConnection(i,j,i,j+1);
					addConnection(i,j,i+1,j);
					
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
		float heading;
		if(y1-y2 != 0)
		{
			heading = (float) (90.0*(y2-y1));
		}else{
			if(x1-x2 == -1)
			{
				heading = (float)0.0;
			}else
			{
				heading = (float) 180.0;
			}
		}
		Pose pose = new Pose(x1*cellSize +xStart, y1*cellSize + xStart, heading );
		float range = lineMap.range(pose);
		if (!onGrid(x1, y1) || !onGrid(x2, y2))
		{
			return false;
		} else if( lineMap.range(pose) < cellSize && lineMap.range(pose) >-1 ) {
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
		Pose pose = new Pose(_x*cellSize+xStart ,_y*cellSize+yStart, _heading);
		return lineMap.range(pose);
		/*int i = 0;
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
		*/
	}
}
