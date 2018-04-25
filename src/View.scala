import scala.swing._
import scala.swing.BorderPanel.Position._
import java.awt.event.ActionListener
import java.awt.RenderingHints
import java.awt.Toolkit
import java.awt.Color
import java.awt.geom.Ellipse2D

/*View class is responsible for drawing the simulation and the GUI on screen.*/
object View extends SimpleSwingApplication {
  
  //Define constants here
  val height = 600
  val width = 800
  val refreshRate = 16  //6ms
  val sim = new Simulation(width, height)
  
  //The addMode variable is controlled by the GUI.
  var addMode = 0
  
  //Main frame contains all the components
  def top = new MainFrame {
    
    title = "Boid-Sim"
    resizable = false
    size = new Dimension(width, height)
    minimumSize   = new Dimension(width, height)
    preferredSize = new Dimension(width, height)
    
    val simSpace = new Panel {
      
      focusable = true
      
      override def paintComponent(g: Graphics2D) {
        //Draw background
        g.setColor(new Color(10, 100, 200))
        g.fillRect(0, 0, width, height)
        //Draw simComponents
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        g.setColor(new Color(255, 255, 255))
        sim.draw(g)
      }
      
    }
    
    //Create the menubar using the GUI object
    val menubar = GUI.createMenuBar()
    
    //Add simSpace and menubar to the window
    contents = new BorderPanel {
      layout(menubar) = North
      layout(simSpace) = Center
    }
    
    //Listen to user input here
    listenTo(simSpace.mouse.clicks)
    listenTo(menubar)
    
    reactions += {
      case scala.swing.event.MousePressed(src, point, mod, _, _) => {
        if(addMode == 0) sim.addBoid(new Boid(Vec(point.x, point.y), Vec(1, 1), Vec(1, 1)))
        if(addMode == 1) sim.addObstacle(new Obstacle(new Vec(point.x, point.y)))
        if(addMode == 2) sim.addTarget(new Target(Vec(point.x, point.y)))
      }
    }
    
    //Listen to action events coming in
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) = {
        sim.step()
        simSpace.repaint()
        Toolkit.getDefaultToolkit().sync()  //Flush the graphics buffer to avoid stuttering when mouse is stationary
      }
    }
    
    //Timer sends an action event to action listener at the rate of refresh rate
    val timer = new javax.swing.Timer(refreshRate, listener)
    timer.start()
  }
}