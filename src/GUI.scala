import scala.swing._
import scala.swing.BorderPanel.Position._
import java.awt.event.ActionListener
import java.awt.Toolkit
import java.awt.Color
import java.awt.geom.Ellipse2D

/*GUI object is used to create a graphical user interface for the View.
 *The graphical user interface controls the simulation options based on user input.*/
object GUI {
  
  def createMenuBar(): MenuBar = {
    
    //Behavior buttons
    val toggleSep = new CheckMenuItem("Separation")
    val toggleCoh = new CheckMenuItem("Cohesion")
    val toggleAli = new CheckMenuItem("Alignment")
    val toggleObs = new CheckMenuItem("Obstacle avoidance")
    val toggleTar = new CheckMenuItem("Target seeking")
    val behaviorList = List(toggleSep, toggleAli, toggleCoh, toggleObs, toggleTar)
    
    //Simcomponent buttons
    val radioBoid = new RadioMenuItem("Boid")
    val radioObs = new RadioMenuItem("Obstacle")
    val radioTar = new RadioMenuItem("Target")
    val componentList = List(radioBoid, radioObs, radioTar)
    
    //Options buttons
    val drawSector = new CheckMenuItem("Draw sectors")
    val drawVector = new CheckMenuItem("Draw vectors")
    val loopPositions = new CheckMenuItem("Loop positions")
    val optionList = List(drawSector, drawVector, loopPositions)
    
    val menu = new MenuBar {
      
      contents += new Menu("Components") {
        contents += new MenuItem(Action("Remove all") {
          View.sim.clear()
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
          View.sim.useDefaultForAll()
        })
        contents += new Separator
        contents ++= behaviorList
      }
      
      contents += new Menu("Options") {
        contents ++= optionList
      }
      
      contents += new Menu("About") {
        //TODO: Improve
        contents += new MenuItem("Boid-Sim v1.0")
      }
      
      behaviorList.foreach(listenTo(_))
      componentList.foreach(listenTo(_))
      optionList.foreach(listenTo(_))
      
      reactions += {
        
        /*Component buttons*/
        case scala.swing.event.ButtonClicked(`radioBoid`) => {
          View.addMode = 0
        }
        case scala.swing.event.ButtonClicked(`radioObs`) => {
          View.addMode = 1
        }
        case scala.swing.event.ButtonClicked(`radioTar`) => {
          View.addMode = 2
        }
        
        /*Behavior buttons*/
        case scala.swing.event.ButtonClicked(`toggleSep`) => {
          if(toggleSep.selected) {
            //It is now selected, so add to all
            View.sim.addDefaultBehavior(new Separation)
          } else {
            View.sim.removeDefaultBehavior(new Separation)
          }
        }
        
        case scala.swing.event.ButtonClicked(`toggleAli`) => {
          if(toggleAli.selected) {
            //It is now selected, so add to all
            View.sim.addDefaultBehavior(new Alignment)
          } else {
            View.sim.removeDefaultBehavior(new Alignment)
          }
        }
        
        case scala.swing.event.ButtonClicked(`toggleCoh`) => {
          if(toggleCoh.selected) {
            //It is now selected, so add to all
            View.sim.addDefaultBehavior(new Cohesion)
          } else {
            View.sim.removeDefaultBehavior(new Cohesion)
          }
        }
        
        case scala.swing.event.ButtonClicked(`toggleObs`) => {
          if(toggleObs.selected) {
            //It is now selected, so add to all
            View.sim.addDefaultBehavior(new ObstacleAvoidance)
          } else {
            View.sim.removeDefaultBehavior(new ObstacleAvoidance)
          }
        }
        
        case scala.swing.event.ButtonClicked(`toggleTar`) => {
          if(toggleTar.selected) {
            //It is now selected, so add to all
            View.sim.addDefaultBehavior(new TargetSeeking)
          } else {
            View.sim.removeDefaultBehavior(new TargetSeeking)
          }
        }
        
        /*Option buttons*/
        case scala.swing.event.ButtonClicked(`drawSector`) => {
          if(drawSector.selected) {
            //It is now selected, so add to all
            View.sim.drawSector = true
          } else {
            View.sim.drawSector = false
          }
        }
        case scala.swing.event.ButtonClicked(`loopPositions`) => {
          if(loopPositions.selected) {
            //It is now selected, so add to all
            View.sim.loopPositions = true
          } else {
            View.sim.loopPositions = false
          }
        }
        case scala.swing.event.ButtonClicked(`drawVector`) => {
          if(drawVector.selected) {
            //It is now selected, so add to all
            View.sim.drawVelocity = true
            View.sim.drawSteering = true
            View.sim.drawDesired = true
          } else {
            View.sim.drawVelocity = false
            View.sim.drawSteering = false
            View.sim.drawDesired = false
          }
        }
        
      }
      
    }
    return menu
  }
}