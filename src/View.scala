import scala.swing._
import java.awt.event.ActionListener
import java.awt.Toolkit

/*This is the view class responsible for drawing the simulation on screen.*/
object View extends SimpleSwingApplication {
  
  //Define constants here
  val height = 400
  val width = 600
  val refreshRate = 32  //6ms
  
  //Define variables here
  val sim = new Simulation(width, height)
  
  //Main frame contains all the other components
  def top = new MainFrame {
    
    title = "Boid-Sim"
    resizable = false
    size = new Dimension(width, height)
    minimumSize   = new Dimension(width, height)
    preferredSize = new Dimension(width, height)
    maximumSize   = new Dimension(width, height)
    
    val simSpace = new Panel {
      
      focusable = true
      
      override def paintComponent(g: Graphics2D) {
        //Draw background
        g.setColor(new Color(10, 100, 200))
        g.fillRect(0, 0, width, height)
        //Draw simComponents
        g.setColor(new Color(255, 255, 255))
        sim.draw(g)
      }
      
    }
    
    //Add components to the window
    contents = simSpace
    
    //Listen to user input here
    listenTo(simSpace.mouse.clicks)
    listenTo(simSpace.keys)
    
    reactions += {
      case scala.swing.event.MousePressed(src, point, _, _, _) => sim.addBoid(new Boid(new Vec(point.x, point.y), new Vec(1, 1), new Vec(1, 1)))
      case scala.swing.event.KeyTyped(src, key, _, _) => {
        if(key == 'c') {
          sim.addBehavior(new Cohesion)
        }
        if(key == 's') {
          sim.addBehavior(new Separation)
        }
        if(key == 'a') {
          sim.addBehavior(new Alignment)
        }
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