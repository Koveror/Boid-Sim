import scala.swing._
import scala.swing.BorderPanel.Position._
import java.awt.event.ActionListener
import java.awt.Toolkit
import java.awt.Color
import java.awt.geom.Ellipse2D

object GUI {
  
  val buttonDim = 30
  val obsModel = new Obstacle(Vec(0, 0)).model
  
  def createLeftPanel(): GridPanel = {
    val leftPanel = new GridPanel(3, 1) {
      contents += createButton(obsModel)
      contents += createButton(obsModel)
      contents += createButton(obsModel)
    }
    return leftPanel
  }
  
  def createRightPanel(): GridPanel = {
    val leftPanel = new GridPanel(3, 1) {
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
        
        val h = this.bounds.getHeight
        val w = this.bounds.getWidth
        g.translate(this.location.x, this.location.y)
        g.setColor(new Color(10, 100, 200))
        g.fill(this.bounds)
        g.translate(this.location.x + w / 2, this.location.y + h / 2)
        g.setColor(new Color(255, 255, 255))
        g.fill(obsModel)
        
        g.setTransform(old)
        
      }
      minimumSize = new Dimension(buttonDim, buttonDim)
      maximumSize = new Dimension(buttonDim, buttonDim)
      borderPainted = true
      enabled = true
    }
    return button
  }
}