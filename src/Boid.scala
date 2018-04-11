import java.awt.Graphics2D
import scala.swing._
import java.awt.geom.{GeneralPath, Path2D}
import java.awt.geom.Rectangle2D

class Boid(p: Vec, v: Vec, o: Vec) extends SimComponent(p) {
  
  val mass = 20.0
  val maxForce = 20.0
  val maxSpeed = 10.0
  val neighborhood = 40.0  //The radius of neighborhood
  val zeroVector = new Vec(0, 0)
  
  private var oldPosition = p
  private var newPosition = p
  private var orientation = o
  private var velocity = v
  
  //val seek: Behavior = new Seek
  val sep = new Separation
  val coh = new Cohesion
  val ali = new Alignment
  //val obs: Behavior = new ObstacleAvoidance
  val behaviors = Array(sep, coh, ali)
  
  val drawSector = false
  
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
  
  def getVel: Vec = {
    return velocity
  }
  
  def getPos: Vec = {
    return oldPosition
  }
  
  def act(s: Simulation) {
    
    val steeringForces = for {
      behavior <- behaviors
    } yield behavior.getSteeringVector(s, this)
    val steeringForce = steeringForces.fold(zeroVector)(_ + _)
    //println("SF: " + steeringForce)
    //println("Is it: " + javax.swing.SwingUtilities.isEventDispatchThread)
    val acceleration = steeringForce / mass
    velocity = (velocity + acceleration).truncatedWith(maxSpeed)
    newPosition = oldPosition + velocity
    
    oldPosition = newPosition

  }
  
  /*Draw this component on the screen by moving it into place and then filling it in. 
   *Finally reset the transform.*/
  def draw(g: Graphics2D) = {
    
    val old = g.getTransform()
    
    g.setColor(new Color(255, 255, 255))
    g.translate(oldPosition.x, oldPosition.y)
    g.rotate(velocity.angle)
    g.fill(this.model)
    
    if(drawSector) {
      g.setColor(new Color(255, 255, 255, 50))
      g.fill(this.sector)
    }
    
    g.setTransform(old)
    
  }
  
}