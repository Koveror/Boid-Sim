import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

class Target(p: Vec) extends SimComponent(p) {
  
  val v = new Vec(0, 0)
  
  val model = {
    val size = 4
    val ellipse = new Ellipse2D.Double(-size, -size, 2*size, 2*size)
    ellipse
  }
  
  def move() {
    
  }
  
  def getVel: Vec = {
    return v
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