package graphwork;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Arrays;
 
/*
 * recursive backtracking algorithm
 * shamelessly borrowed from the ruby at
 * http://weblog.jamisbuck.org/2010/12/27/maze-generation-recursive-backtracking
 */
public class MazeGenerator {
	private class Cell{
		public int x;
		public int y;
		public Cell(int x, int y)
		{
			this.x = x;
			this.y = y;
		}
		
		public boolean isEqual(Cell other)
		{
			return this.x == other.x && this.y == other.y;
		}
		
		public Cell() {}
		
		public String toString()
		{
			return "x: " + x + " y: " + y;
		}
	}
	
	
	public final int x;
	public final int y;
	public final int[][] maze;
	public Cell start;
	public Cell end;
	public Graph mazeGraph;
 
	public MazeGenerator(int x, int y) {
		if(x != y)
		{
			y = x;
		}
		this.x = x;
		this.y = y;
		this.start = new Cell();
		this.end = new Cell();
		maze = new int[this.x][this.y];
		generateMaze();
		addStart();
		addEnd();
		mazeGraph = new Graph(this.maze);
		
	}
	
	public void addEnd()
	{
		for(int i = maze.length - 1; i >= 0 ; i--)
		{
			if(maze[maze.length-1][i] == 1)
			{
				end.x = maze.length-1;
				end.y = i;
				maze[end.x][end.y]= 3; 
				return;
			}
		}
	}
	
	public void addStart()
	{
		for(int i = 0; i < maze.length; i++)
		{
			if(maze[0][i] == 1)
			{
				start.x = 0;
				start.y = i;
				maze[start.x][start.y] = 2;
				return;
			}
		}
	}
	
	public boolean canChange(int x, int y)
	{
		int count = 0;
		//check up
		if(x-1 >= 0  && maze[x-1][y] == 1)
		{
			count++;
		}
		//check down
		if(x+1 < maze.length  && maze[x+1][y] == 1)
		{
			count++;
		}
		//check left	
		if(y-1 >= 0 && maze[x][y-1] == 1)
		{
			count++;
		}
		//check right
		if(y+1 < maze.length && maze[x][y+1] == 1)
		{
			count++;
		}
		
		return count <= 1;
	}
 
	/*
	 * 0 - wall
	 * 1 - walkable
	 * 2 - start
	 * 3 - end
	 */
	private void generateMaze() 
	{
		//grid full of walls
		boolean[][] visited = new boolean[this.x][this.y];
		for(int i = 0; i < maze.length; i++)
		{
			for(int j = 0; j < maze[i].length; j++)
			{
				maze[i][j] = 0;
				visited[i][j] = false;
			}
		}
		
		//pick a cell, mark as part of maze, add cell to wall list
		
		Cell start = new Cell(new Random().nextInt(maze.length), new Random().nextInt(maze.length));
		List<Cell> l1 = new ArrayList<Cell>(); 
		l1.add(start);
		
		//while list is not empty
		while(!l1.isEmpty())
		{
			int randIndex = new Random().nextInt(l1.size());
			Cell cur = l1.get(randIndex);
			
			/*
			while(visited[cur.x][cur.y])
			{
				l1.remove(randIndex);
				randIndex = new Random().nextInt(l1.size());
				cur = l1.get(randIndex);
			}
			*/
			
			visited[cur.x][cur.y] = true;
			l1.remove(randIndex);
			
			if(canChange(cur.x, cur.y))
			{				
				//System.out.println("currently at cell " + cur.toString());
				int salt = new Random().nextInt(101);
				//System.out.println("Salt value = " + salt);
				//System.out.println("0-40 Vertical 41-80 Horizontal 81-100 Both");
				
				//vertical
				if(salt <= 40 || salt >= 81) 
				{
					//check up
					if(cur.x - 1 >= 0 && canChange(cur.x-1, cur.y))
					{
						if(cur.x + 1 > maze.length-1 || visited[cur.x + 1][cur.y] == false)
						{
							//add pathways
							maze[cur.x - 1][cur.y] = 1;
							maze[cur.x][cur.y] = 1;
							//add neighbors
							//left
							if(cur.y - 1 >= 0 && visited[cur.x-1][cur.y-1] == false)
							{
								l1.add(new Cell(cur.x-1, cur.y-1));
							}
							//right
							if(cur.y + 1 < maze.length && visited[cur.x-1][cur.y+1]==false)
							{
								l1.add(new Cell(cur.x-1, cur.y+1));
							}
							//up
							if(cur.x - 2 >= 0 && visited[cur.x-2][cur.y]==false)
							{
								l1.add(new Cell(cur.x-2, cur.y));
							}
						}
					}
					//check down
					if(cur.x + 1 < maze.length && canChange(cur.x+1, cur.y))
					{
						if(cur.x - 1 < 0 || visited[cur.x - 1][cur.y]== false )
						{
							//add pathways
							maze[cur.x + 1][cur.y]= 1;
							maze[cur.x][cur.y]= 1;
							//add neighbors
							//left
							if(cur.y - 1 >= 0 && visited[cur.x+1][cur.y-1]==false)
							{
								l1.add(new Cell(cur.x+1, cur.y-1));
							}
							//right
							if(cur.y + 1 < maze.length && visited[cur.x+1][cur.y+1]==false)
							{
								l1.add(new Cell(cur.x+1, cur.y+1));
							}
							//down
							if(cur.x + 2 < maze.length && visited[cur.x+2][cur.y]==false)
							{
								l1.add(new Cell(cur.x+2, cur.y));
							}
						}
					}
				}
				
				//horizontal
				if(salt >= 41)
				{
					//check left
					if(cur.y - 1 >= 0 && canChange(cur.x, cur.y-1))
					{
						if(cur.y + 1 > maze.length-1 || visited[cur.x][cur.y + 1]==false)
						{
							//add pathways
							maze[cur.x][cur.y - 1] = 1;
							maze[cur.x][cur.y]= 1;
							//add neighbors
							//left
							if(cur.y - 2 >= 0 && visited[cur.x][cur.y-2]==false)
							{
								l1.add(new Cell(cur.x, cur.y-2));
							}
							//up
							if(cur.x - 1 >= 0 && visited[cur.x-1][cur.y-1]==false)
							{
								l1.add(new Cell(cur.x-1, cur.y-1));
							}
							//down
							if(cur.x + 1 < maze.length && visited[cur.x+1][cur.y-1]==false)
							{
								l1.add(new Cell(cur.x+1, cur.y-1));
							}
						}
					}
					//check right
					if(cur.y + 1 < maze.length-1 && canChange(cur.x, cur.y+1))
					{
						if(cur.y - 1 < 0 || visited[cur.x][cur.y - 1]==false)
						{
							//add pathways
							maze[cur.x][cur.y + 1] = 1;
							maze[cur.x][cur.y]= 1;
							//add neighbors
							//up
							if(cur.x - 1 >= 0 && visited[cur.x-1][cur.y+1]==false)
							{
								l1.add(new Cell(cur.x-1, cur.y+1));
							}
							//down
							if(cur.x + 1 < maze.length && visited[cur.x+1][cur.y+1]==false)
							{
								l1.add(new Cell(cur.x+1, cur.y+1));
							}
							//right
							if(cur.y + 2 < maze.length && visited[cur.x][cur.y+2]==false)
							{
								l1.add(new Cell(cur.x, cur.y+2));
							}
						}
					}
					
				}
					
			}
		}
		
	}
	
	class Graph
	{
		List<List<Cell>> g;
		
		public Graph(int[][] arr)
		{
			g = new ArrayList<List<Cell>>();
			
			//get all valid walkable tiles
			for(int i = 0; i < arr.length; i++)
			{
				for(int j = 0; j < arr[i].length; j++)
				{
					if(arr[i][j] != 0)
					{
						List<Cell> temp = new ArrayList<Cell>();
						temp.add(new Cell(i,j));
						g.add(temp);
					}
				}
			}
			
			//populate adjacency list
			for(int i = 0; i < g.size(); i++)
			{
				Cell c = g.get(i).get(0);
				//check up
				if(c.x - 1 >= 0 && arr[c.x-1][c.y] != 0)
				{
					Cell adjCell = new Cell(c.x-1, c.y);
					g.get(i).add(adjCell);
				}
				//check right
				if(c.y + 1 < arr.length && arr[c.x][c.y+1] != 0)
				{
					Cell adjCell = new Cell(c.x, c.y+1);
					g.get(i).add(adjCell);
				}
				//check left
				if(c.y - 1 >= 0 && arr[c.x][c.y-1] != 0)
				{
					Cell adjCell = new Cell(c.x, c.y-1);
					g.get(i).add(adjCell);
				}
				//check down
				if(c.x + 1 < arr.length && arr[c.x+1][c.y]!= 0 )
				{
					Cell adjCell = new Cell(c.x+1, c.y);
					g.get(i).add(adjCell);
				}
			}
		}
		
		
		public void bfs(Cell start, int[][] arrRef)
		{
			// Mark all the vertices as not visited(By default 
	        // set as false) 
	        
			//boolean visited[] = new boolean[g.size()]; 
	        HashMap<Cell, String> visited = new HashMap();
	  
	        // Create a queue for BFS 
	        LinkedList<Cell> queue = new LinkedList<Cell>(); 
	  
	        // Mark the current node as visited and enqueue it 
	        //visited[i]=true; 
	        visited.put(start, "visited");
	        
	        queue.add(start); 
	        
	        
	        while (queue.size() != 0) 
	        { 
	        	
	            // Dequeue a vertex from queue and print it 
	            start = queue.poll();
	            
	            //System.out.println("BFS at node: " + start);
	            
	            //if its the finish we're done
	            if(arrRef[start.x][start.y]==3 )
	            {
	            	return;
	            }
	            //if its a path, mark for bfs value
	            if(arrRef[start.x][start.y]>0 && arrRef[start.x][start.y]!= 2 )
	            {
	            	arrRef[start.x][start.y] = 4;
	            }
	            
	            // Get all adjacent vertices of the dequeued vertex s 
	            // If a adjacent has not been visited, then mark it 
	            // visited and enqueue it 
	            int i = 0;
	            while(i < g.size() && !start.isEqual(g.get(i).get(0)))
	            {
	            	i++;
	            }
	            
	            List<Cell> adj = new ArrayList<Cell>(g.get(i));
	            adj.remove(0);
	            while (!adj.isEmpty()) 
	            { 
	            	Cell key = adj.get(0);
	            	adj.remove(0);
	                if (visited.get(key) == null) 
	                { 
	                    visited.put(key, "visited");
	                    queue.add(key); 
	                } 
	            } 
	        } 
		}
		
		public void dfs(Cell start, int[][] arrRef)
		{
			// Mark all the vertices as not visited(By default 
	        // set as false) 
	        
			//boolean visited[] = new boolean[g.size()]; 
	        HashMap<Cell, String> visited = new HashMap();
	  
	        // Create a queue for BFS 
	        Stack<Cell> stack = new Stack<Cell>(); 
	  
	        // Mark the current node as visited and enqueue it 
	        //visited[i]=true; 
	        visited.put(start, "visited");
	        
	        stack.push(start); 
	        
	        
	        while (stack.size() != 0) 
	        { 
	        	
	            // Dequeue a vertex from queue and print it 
	            start = stack.pop();
	            
	            //System.out.println("BFS at node: " + start);
	            
	            //if its the finish we're done
	            if(arrRef[start.x][start.y]==3 )
	            {
	            	return;
	            }
	            //if its a path, mark for bfs value
	            if(arrRef[start.x][start.y] > 0 && arrRef[start.x][start.y]!= 2)
	            {
	            	arrRef[start.x][start.y] = 5;
	            }
	            
	            // Get all adjacent vertices of the dequeued vertex s 
	            // If a adjacent has not been visited, then mark it 
	            // visited and enqueue it 
	            int i = 0;
	            while(i < g.size() && !start.isEqual(g.get(i).get(0)))
	            {
	            	i++;
	            }
	            
	            List<Cell> adj = new ArrayList<Cell>(g.get(i));
	            adj.remove(0);
	            while (!adj.isEmpty()) 
	            { 
	            	Cell key = adj.get(0);
	            	adj.remove(0);
	                if (visited.get(key) == null) 
	                { 
	                    visited.put(key, "visited");
	                    stack.push(key); 
	                } 
	            } 
	        } 
		}
		
		public void as()
		{
			
		}
		
	}
	
	
 
}