import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

class Obstacle(p: Vec) extends SimComponent(p) {
  
  val model = {
    val size = 8
    val ellipse = new Ellipse2D.Double(-size, -size, 2*size, 2*size)
    ellipse
  }
  
  def getVel: Vec = {
    return Vec(0, 0)
  }
  
  def getPos: Vec = {
    return p
  }

  def act(s: Simulation) {
    //Do nothing
  }
  
  def draw(g: Graphics2D) {
    
    val old = g.getTransform()
    
    g.translate(pos.x, pos.y)
    g.fill(this.model)
    
    g.setTransform(old)
    
  }
}