import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

class SimComponent(var pos: Vec) {
  
  val model= {
    //Calculates the position of the bounding box first, then creates the ellipse
    new Ellipse2D.Double(-8, -8, 16, 16)  //FIXME: Magic numbers
  }
  
  def getPos = pos
  
  def setPos(p: Vec) {
    pos = p
  }
  
  /*Draw this component on the screen by moving it into place and then filling it in.
   *Finally reset the transform.*/
  def draw(g: Graphics2D) = {
    
    val old = g.getTransform()
    
    g.translate(pos.x, pos.y)
    g.fill(model)
    
    g.setTransform(old)
    
  }
  
}