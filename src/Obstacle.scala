import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

/*Obstacles are static SimComponents that do not move. They can be used to for example create walls.
 *Boids interact with obstacles with the ObstacleAvoidance behavior.*/
class Obstacle(p: Vec) extends SimComponent(p) {
  
  /*Model for obstacle is a big circle*/
  val model = {
    val size = 8
    val ellipse = new Ellipse2D.Double(-size, -size, 2*size, 2*size)
    ellipse
  }
  
  /*Get position*/
  def getPos: Vec = {
    return p
  }
  
  /*Act method can be used if obstacles need to do something each frame*/
  def act(s: Simulation) {
    //Do nothing
  }
  
  /*Draw obstacles model on SimSpace*/
  def draw(g: Graphics2D) {
    
    val old = g.getTransform()
    
    g.translate(pos.x, pos.y)
    g.fill(this.model)
    
    g.setTransform(old)
    
  }
}