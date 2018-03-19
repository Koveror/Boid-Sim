import java.awt.Graphics2D
import java.awt.geom.{GeneralPath, Path2D}

class Boid(p: Vec) extends SimComponent(p) {
  
  val mass = 1.0
  val maxForce = Vec(2, 2)
  val maxSpeed = Vec(2, 2)
  var orientation = Vec(1, 1)  //TODO: Mutable vectors or var?
  var velocity = Vec(-3, 0)
  
  //Calculates the position of the bounding box first, then creates the ellipse
  val model = {
    val size = 8
    val polyline = new GeneralPath(Path2D.WIND_NON_ZERO, 10)
    polyline.moveTo(-size, -size)
    polyline.lineTo(size, 0)
    polyline.lineTo(-size, size)
    polyline.lineTo(-2.0/3.0 * size, 0)
    polyline.closePath()
    polyline
  }
  
  def act(s: Simulation) {
    pos = pos + velocity
  }
  
  /*Draw this component on the screen by moving it into place and then filling it in. 
   *Finally reset the transform.*/
  def draw(g: Graphics2D) = {
    
    val old = g.getTransform()
    
    g.translate(pos.x, pos.y)
    g.rotate(velocity.angle)
    g.fill(this.model)
    
    g.setTransform(old)
    
  }
  
}