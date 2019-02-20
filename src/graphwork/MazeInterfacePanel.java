package graphwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


@SuppressWarnings("serial")
class MazeInterfacePanel extends JFrame 
{

	
	private final JSplitPane splitPane;
    private final JPanel rightPanel; 
    private JButton button_refresh;
    private JButton button_bfs;
    private JButton button_dfs;
    private JButton button_as;
	
	public MazeInterfacePanel() {
		
		splitPane = new JSplitPane();
        rightPanel = new JPanel();   
        
        getContentPane().setLayout(new GridLayout());
        getContentPane().add(splitPane); 
        
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);  // we want it to split the window horizontally               
        splitPane.setRightComponent(rightPanel);
        
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        
        button_refresh = new JButton("Generate new maze");
        
        button_refresh.setSize(400, 175);
        rightPanel.add(button_refresh);
        
        button_bfs = new JButton("Breadth First Search");
        rightPanel.add(button_bfs);
        button_bfs.setSize(400, 175);
        
        button_dfs = new JButton("depth First Search");
        rightPanel.add(button_dfs);
        button_dfs.setSize(400, 175);
        
        button_as = new JButton("A*");
        rightPanel.add(button_as);
        button_as.setSize(400, 175);
        
        splitPane.setResizeWeight(.9d);
	}

	public static void main(String[] args) 
	{
	
		MazeInterfacePanel frame = new MazeInterfacePanel();
		
		
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.pack();
	    frame.setSize(1500, 1000);
	    Maze labyrinth = new Maze(120);
	    frame.getContentPane().add(labyrinth);
	    frame.setVisible(true);
	    
	    frame.splitPane.setLeftComponent(labyrinth);
	    
	    frame.button_refresh.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		System.out.println("refresh pressed");
        		labyrinth.generateNewMaze();
        		labyrinth.repaint();
        	}
        });
	    
	    frame.button_bfs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		System.out.println("bfs pressed");
        		labyrinth.map.mazeGraph.bfs(labyrinth.map.start, labyrinth.map.maze);
        		labyrinth.repaint();
        	}
        });
	    
	    frame.button_dfs.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		System.out.println("dfs pressed");
        		labyrinth.map.mazeGraph.dfs(labyrinth.map.start, labyrinth.map.maze);
        		labyrinth.repaint();
        	}
        });
	    
	    frame.button_as.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent arg0) {
        		System.out.println("A* pressed");
        	}
        });
	    
	}
}
	
	
@SuppressWarnings("serial")
class Maze extends JPanel 
{
	
	MazeGenerator map;
	int d = 50;
	
	public Maze()
	{
		map = new MazeGenerator(d,d);
	}
	
	public Maze(int d)
	{
		this.d = d;
		map = new MazeGenerator(d,d);
	}
	
	public void generateNewMaze()
	{
		map = new MazeGenerator(this.d, this.d);
	}
	
	protected void paintComponent(Graphics g) 
	{
		
	
	    int rectSize = 900/map.maze.length;
	    int scaleSize = 900/map.maze.length;
	    for (int i = 0; i < map.maze.length; i++) 
	    {
	        for (int j = 0; j < map.maze[i].length; j++) 
	        {
	            int factori = i * scaleSize;
	            int factorj = j * scaleSize;
	            
	            switch (map.maze[i][j]) 
	            {
	            	//wall
		            case 0: 
		            {
		                g.setColor(Color.black);
		                g.fillRect(factori, factorj, rectSize, rectSize);
		
		            }
		                break;
		            //path
		            case 1: 
		            {
		                g.setColor(Color.gray);
		                g.fillRect(factori, factorj, rectSize, rectSize);
		
		            }
		                break;
		            //start
		            case 2: 
		            {
		                g.setColor(Color.green);
		                g.fillRect(factori, factorj, rectSize, rectSize);
		
		            }
		                break;
		            //finish
		            case 3: 
		            {
		                g.setColor(Color.blue);
		                g.fillRect(factori, factorj, rectSize, rectSize);
		
		            }
		                break;
		            //bfs
		            case 4: 
		            {
		                g.setColor(Color.orange);
		                g.fillRect(factori, factorj, rectSize, rectSize);
		
		            }
		                break;
		            //dfs
		            case 5: 
		            {
		                g.setColor(Color.red);
		                g.fillRect(factori, factorj, rectSize, rectSize);
		
		            }
		                break;
		            //A*
		            case 6: 
		            {
		                g.setColor(Color.PINK);
		                g.fillRect(factori, factorj, rectSize, rectSize);
		
		            }
		                break;
		            }
	        }
	    }
	}
}