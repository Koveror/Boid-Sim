import scala.swing._
import scala.swing.BorderPanel.Position._
import java.awt.event.ActionListener
import java.awt.Toolkit
import java.awt.Color
import java.awt.geom.Ellipse2D

/*View class is responsible for drawing the simulation on screen.*/
object View extends SimpleSwingApplication {
  
  //Define constants here
  val buttonDim = 30
  val simSpaceHeight = 400
  val simSpaceWidth = 600
  val height = simSpaceHeight
  val width = buttonDim + simSpaceWidth + buttonDim
  val refreshRate = 32  //6ms
  val sim = new Simulation(width, height)
  
  //Define variables here
  var addMode = 0
  
  //Main frame contains all the other components
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
        g.setColor(new Color(255, 255, 255))
        sim.draw(g)
      }
      
    }
    
    val label = new Label {
      text = "I'm a big label!"
      font = new Font("Ariel", java.awt.Font.ITALIC, 24)
    }
    
    val leftPanel = GUI.createLeftPanel()
    
    val rightPanel = GUI.createRightPanel()
    
    //Add components to the window
    contents = new BorderPanel {
      layout(simSpace) = Center
      layout(leftPanel) = East
      layout(rightPanel) = West
    }
    
    //Listen to user input here
    listenTo(simSpace.mouse.clicks)
    listenTo(simSpace.keys)
    
    reactions += {
      case scala.swing.event.MousePressed(src, point, mod, _, _) => {
        if(addMode == 0) sim.addBoid(new Boid(new Vec(point.x, point.y), new Vec(1, 1), new Vec(1, 1)))
        if(addMode == 1) sim.addObstacle(new Obstacle(new Vec(point.x, point.y)))
      }
      
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
        if(key == 'o') {
          addMode = 1
        }
        if(key == 'b') {
          addMode = 0
        }
        if(key == 't') {
          sim.addBehavior(new Seek(sim.targets.last))
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