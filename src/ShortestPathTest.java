


import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 *test of shortest path algorithm<br>
 *uses Grid;  displays matrix of shortest path lengths
 *@author Roger Glassey
 */
public class ShortestPathTest extends JFrame implements ActionListener  

{ 
   JTextArea area = new JTextArea(9,12);//where the list values go
   JButton setButton = new JButton("set Destination ");
   JButton blockButton = new JButton("block");
   JButton unblockButton = new JButton("unblock");
   JTextField xField = new JTextField("0",3);
   JTextField yField = new JTextField("0",3);
   JTextField info = new JTextField(15);
   int gridX =  5;
   int gridY = 7;
   Grid grid = new Grid(gridX,gridY);
   String msg;
   /**
    * constructor builds the GUI
    */
   public ShortestPathTest()
   {
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      setTitle("Shortest Path Test");
      setSize(500,600);
      System.out.println(grid.nodes.length+" "+grid.nodes[0].length);
      buildGui();
   }
   public void buildGui()
   {
  // buttons and text input fields
      JPanel topPanel = new JPanel();
      topPanel.add(xField);
      topPanel.add(yField);
      setButton.addActionListener(this);
      topPanel.add(setButton);
      blockButton.addActionListener(this);
      topPanel.add(blockButton);
      topPanel.add(unblockButton);
      unblockButton.addActionListener(this);
      topPanel.add(new JLabel("x"));
      topPanel.add(xField);
      topPanel.add(new JLabel("y"));
      topPanel.add(yField);
      add(topPanel,BorderLayout.NORTH);
      JPanel centerPanel = new JPanel();

      centerPanel .add(info);
      centerPanel .add(area);
      add(centerPanel ,BorderLayout.CENTER);
      info.setText("maxX= "+(gridX-1)+"  maxY= "+(gridY-1));
//      add(primary);

   }
   /**
    *processes button events
    */
   public void actionPerformed (ActionEvent e)
   {
      // rad x and y from text fields
      int x = Integer.parseInt(xField.getText());
      int y =Integer.parseInt(yField.getText());
      if(e.getSource()==setButton)
      {
         msg=("setDest ");
         grid.setDestination(x,y);
      }
      if(e.getSource()==blockButton)
      {
         msg="block";
         grid.nodes[x][y].blocked();
      }
      if(e.getSource()==unblockButton)
      {
         msg="unblock";
         grid.nodes[x][y].unblocked();
      }
      grid.recalc();
      System.out.println("reacalc");
      updateTextArea();
   }
   /**
    *called by actionPerformed(); builds output grid
    **/
   public void updateTextArea()
   {
      info.setText(msg);
      area.setText(" ");
      String output;
      for(int y = gridY-1;y>=0;y--)// one line at a time, top down
      {
         output = "\n ";// new line
         for(int x = 0;x<gridX;x++)
         {
            Node n = grid.nodes[x][y];
            if(n.isBlocked()) output +="   "+"*";
            else
            {
               int dist = n.getDistance();
               if(dist<10)
                  output += "   "+dist ;
               else
                  output += " "+dist;
            }
         }
         area.append(output);
      }
      area.repaint();
   }

   public static void main(String [] args)
   {
      ShortestPathTest test = new ShortestPathTest();
      test.setVisible(true);
   }
}
