import scala.swing._
import java.awt.event.ActionListener

/*This is the view class responsible for drawing the simulation on screen.*/
object View extends SimpleSwingApplication {
  
  //Define constants here
  val height = 400
  val width = 600
  val refreshRate = 6  //6ms
  
  //Define variables here
  var sim = new Simulation
  
  //Main frame contains all the other components
  def top = new MainFrame {
    
    title = "Boid-Sim"
    resizable = false
    size = new Dimension(width, height)
    minimumSize   = new Dimension(width, height)
    preferredSize = new Dimension(width, height)
    maximumSize   = new Dimension(width, height)
    
    val simSpace = new Panel {
      
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
    
    reactions += {
      case scala.swing.event.MousePressed(src, point, _, _, _) => sim.addComponent(new Boid(Vec(point.x, point.y), Vec(8, 8), Vec(1, 1)))
    }
    
    val listener = new ActionListener() {
      def actionPerformed(e: java.awt.event.ActionEvent) = {
        sim.step()
        simSpace.repaint()
      }
    }
    
    val timer = new javax.swing.Timer(refreshRate, listener)
    timer.start()
  }
}