import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

class Target(p: Vec) extends SimComponent(p) {
  
  val model = {
    val size = 4
    val ellipse = new Ellipse2D.Double(-size, -size, 2*size, 2*size)
    ellipse
  }
  
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