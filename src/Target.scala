import java.awt.Graphics2D
import java.awt.geom.Ellipse2D
import java.awt.geom.Rectangle2D

/*Targets are static SimComponents that do not move. They can be used as targets for Seek and Flee behaviors. */
class Target(p: Vec) extends SimComponent(p) {
  
  /*Model for targets is a small rectangle*/
  val model = {
    val size = 4
    val rect = new Rectangle2D.Double(-size, -size, 2*size, 2*size)
    rect
  }
  
  /*Get position*/
  def getPos: Vec = {
    return p
  }
  
  /*Act method can be used if targets need to do something each frame*/
  def act(s: Simulation) {
    //Do nothing
  }
  
  /*Draw targets model on SimSpace*/
  def draw(g: Graphics2D) {
    
    val old = g.getTransform()
    
    g.translate(pos.x, pos.y)
    g.fill(this.model)
    
    g.setTransform(old)
    
  }
  
}