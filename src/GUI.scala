import scala.swing._
import scala.swing.BorderPanel.Position._
import java.awt.event.ActionListener
import java.awt.Toolkit
import java.awt.Color
import java.awt.geom.Ellipse2D

object GUI {
  
  val buttonDim = 30
  val obsModel = new Obstacle(Vec(0, 0)).model
  val doop = new CheckMenuItem("Check me")
  
  def createMenuBar(): MenuBar = {
    
    val toggleSep = new CheckMenuItem("Seperation")
    val toggleAli = new CheckMenuItem("Cohesion")
    val toggleCoh = new CheckMenuItem("Alignment")
    val toggleObs = new CheckMenuItem("Obstacle avoidance")
    val toggleList = List(toggleSep, toggleAli, toggleCoh, toggleObs)
    
    val radioBoid = new RadioMenuItem("Boid") {name = "Boid"}
    val radioObs = new RadioMenuItem("Obstacle") {name = "Obstacle"}
    val radioTar = new RadioMenuItem("Target") {name = "Target"}
    val radioList = List(radioBoid, radioObs, radioTar)
    
    val menu = new MenuBar {
      contents += new Menu("Components") {
        contents += new MenuItem(Action("Remove all") {
          //TODO: Implement
        })
        contents += new Separator
        val a = radioBoid
        val b = radioObs
        val c = radioTar
        a.selected = true
        val mutex = new ButtonGroup(a,b,c)
        contents ++= mutex.buttons
      }
      
      contents += new Menu("Behaviors") {
        contents += new MenuItem(Action("Apply to all") {
          //TODO: Implement
        })
        contents += new Separator
        contents += toggleAli
        contents += toggleSep
        contents += toggleCoh
        contents += toggleObs
      }
      
      contents += new Menu("Options") {
        //TODO: Implement drawing options etc.
      }
      
      contents += new Menu("About") {
        //TODO: Implement version info etc.
      }
      
      toggleList.foreach(listenTo(_))
      radioList.foreach(listenTo(_))
      
      reactions += {
        case scala.swing.event.ButtonClicked(s) => {
          s.name match {
            case "Boid" => {
              View.addMode = 0
            }
            case "Obstacle" => {
              View.addMode = 1
            }
            case "Target" => {
              View.addMode = 2
            }
            case _ => {
              
            }
          }
        }
        
        
      }
      
    }
    return menu
  }
  
  def createLeftPanel(): GridPanel = {
    val leftPanel = new GridPanel(3, 1) {
      contents += createButton(obsModel)
      contents += createButton(obsModel)
      contents += createButton(obsModel)
    }
    return leftPanel
  }
  
  def createRightPanel(): GridPanel = {
    val leftPanel = new GridPanel(5, 1) {
      contents += createButton(obsModel)
      contents += createButton(obsModel)
      contents += createButton(obsModel)
      contents += createButton(obsModel)
    }
    return leftPanel
  }
  
  def createButton(m: Ellipse2D): Button = {
    val button = new Button {
      
      override def paintComponent(g: Graphics2D) {
        
        val old = g.getTransform()
        
        val h = bounds.getHeight
        val w = bounds.getWidth
        g.translate(location.x, location.y)
        g.setColor(new Color(10, 100, 200))
        g.fill(this.bounds)
        g.translate(location.x + w / 2, location.y + h / 2)
        g.setColor(new Color(255, 255, 255))
        g.fill(obsModel)
        
        g.setTransform(old)
        
      }
      //minimumSize = new Dimension(buttonDim, buttonDim)
      //maximumSize = new Dimension(buttonDim, buttonDim)
      borderPainted = true
      enabled = true
    }
    return button
  }
}