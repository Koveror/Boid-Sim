import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

class SimComponent(pos: Vec) {
  
  val model= {
    //Calculates the position of the bounding box first, then creates the ellipse
    new Ellipse2D.Double(-8, -8, 16, 16)  //FIXME: Magic numbers
  }
  
  def getPos = pos
  
  def draw(g: Graphics2D) = {
    
    val old = g.getTransform()
    
    g.translate(pos.x, pos.y)
    g.fill(model)
    
    g.setTransform(old)
  }
  
}