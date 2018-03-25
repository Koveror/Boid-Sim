import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

class Obstacle(p: Vec) extends SimComponent(p) {
  
  val model = {
    val size = 8
    val ellipse = new Ellipse2D.Double(-size, -size, 2*size, 2*size)
    ellipse
  }
  
  /*Keep component in simulation by adding it to newComponents*/
  def act(s: Simulation) {
    s.addComponent(this)
  }
  
  def draw(g: Graphics2D) {
    
    val old = g.getTransform()
    
    g.translate(pos.x, pos.y)
    g.fill(this.model)
    
    g.setTransform(old)
    
  }
}