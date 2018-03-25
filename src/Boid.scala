import java.awt.Graphics2D
import scala.swing._
import java.awt.geom.{GeneralPath, Path2D}
import java.awt.geom.Rectangle2D

class Boid(p: Vec, v: Vec, o: Vec) extends SimComponent(p) {
  
  val mass = 20.0
  val maxForce = 20.0
  val maxSpeed = 20.0
  val orientation = o  //TODO: Mutable vectors or var?
  val velocity = v
  val behavior = new Seek
  val drawSector = true
  
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
  
  /*Boid can see SimComponents within it's rectangular view.*/
  val sector = {
    val width = 24
    val length = 128
    val rectangle = new Rectangle2D.Double(0, -width / 2, length, width)
    rectangle
  }
  
  def act(s: Simulation) {
    
    val steeringForce = behavior.getSteeringVector(s, this)
    
    val accerelation = steeringForce / mass
    val newVelocity = (velocity + accerelation).truncateWith(maxSpeed)
    
    val newPos = pos + newVelocity
    val newComponent = new Boid(newPos, newVelocity, orientation)
    
    s.addComponent(newComponent)
  }
  
  /*Draw this component on the screen by moving it into place and then filling it in. 
   *Finally reset the transform.*/
  def draw(g: Graphics2D) = {
    
    val old = g.getTransform()
    
    g.setColor(new Color(255, 255, 255))
    g.translate(pos.x, pos.y)
    g.rotate(velocity.angle)
    g.fill(this.model)
    
    if(drawSector) {
      g.setColor(new Color(255, 255, 255, 50))
      g.fill(this.sector)
    }
    
    g.setTransform(old)
    
  }
  
}