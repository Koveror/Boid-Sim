import java.awt.Graphics2D
import scala.swing._
import java.awt.geom.{GeneralPath, Path2D}
import java.awt.geom.Rectangle2D
import scala.collection.mutable.Buffer

/*Boid is a simple vehicle that moves around in the simulation each time the move() method is called.
 *New velocity and new position are calculated when the act() method is called.
 *Based on the behaviors that the boid is following, a steering vector is calculated.
 *This steering vector affects the boids velocity and therefore its new position.*/
class Boid(p: Vec, v: Vec, o: Vec) extends SimComponent(p) {
  
  /*Constants*/
  val mass = 80.0
  val maxForce = 20.0
  val minSpeed = 1.0
  val maxSpeed = 10.0
  val neighborhood = 40.0  //The radius of neighborhood
  val drawSector = false
  
  /*Variables*/
  private var oldPosition = p
  private var newPosition = p
  private var orientation = o
  private var velocity = v
  
  /*Behaviors*/
  //val seek: Behavior = new Seek
  val sep = new Separation
  val coh = new Cohesion
  val ali = new Alignment
  //val obs: Behavior = new ObstacleAvoidance
  val behaviors: Buffer[Behavior] = Buffer(sep, coh, ali)
  
  /*Model is a polyline in the shape of an arrow*/
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
  
  /*Remove all behaviors that are of the same type as b*/
  def removeBehavior(b: Behavior) {
    behaviors -- behaviors.filter(_.getType == b.getType)
  }
  
  /*Add a behavior of this type, if it doesn't exist*/
  def addBehavior(b: Behavior) {
    if(!behaviors.exists(_.getType == b.getType)) {  //TODO: Seek types to different targets???
      behaviors += b
    }
  }
  
  /*Get current velocity*/
  def getVel: Vec = {
    return velocity
  }
  
  /*Get current position of the boid. Use this when calculating behaviors.*/
  def getPos: Vec = {
    return oldPosition
  }
  
  /*Move to the new position calculated by act()*/
  def move() {
    oldPosition = newPosition
  }
  
  /*Calculate new position to be used with move()*/
  def act(s: Simulation) {
    
    val steeringForces = for {
      behavior <- behaviors
    } yield behavior.getSteeringVector(s, this)
    val steeringForce = steeringForces.fold(s.zeroVector)(_ + _)
    val acceleration = steeringForce / mass
    
    velocity = (velocity + acceleration).truncatedWith(maxSpeed)
    velocity = velocity.makeLength(minSpeed)
    
    newPosition = oldPosition + velocity

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
    
    g.setColor(new Color(255, 255, 255))
    
    g.setTransform(old)
    
  }
  
}