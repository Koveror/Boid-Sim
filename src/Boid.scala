import java.awt.Graphics2D
import scala.swing._
import java.awt.geom.{GeneralPath, Path2D}
import java.awt.geom.Rectangle2D
import scala.collection.mutable.Buffer
import scala.math._

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
  val drawSector = true
  val drawSteering = true
  
  /*Variables*/
  private var oldPosition = p
  private var newPosition = p
  private var orientation = o
  private var velocity = v
  private var steeringForce = v
  
  /*Behaviors*/
  //val seek: Behavior = new Seek
  val sep = new Separation
  val coh = new Cohesion
  val ali = new Alignment
  val obs = new ObstacleAvoidance
  val behaviors: Buffer[Behavior] = Buffer(sep, coh, ali, obs)
  
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
  
  def buildSteering: Rectangle2D = {
    val rect = new Rectangle2D.Double(0, 0, 2, steeringForce.length * 20)
    return rect
  }
  
  /*Boid can see SimComponents within it's rectangular view.*/
  def buildSector: GeneralPath =  {
    
    val forward = getVel.normalized
    val front = getVel * 80  //Build dynamically based on speed
    val left = forward.rotate(Pi / 2) * 20
    val right = forward.rotate(-Pi / 2) * 20
    val leftBottom = getPos + left
    val rightBottom = getPos + right
    val leftTop = getPos + left + front
    val rightTop = getPos + right + front
    
    //println("LB: " + leftBottom + ", LT: " + leftTop + ", RT: " + rightTop + ", RB: " + rightBottom)
    
    val polyline = new GeneralPath(Path2D.WIND_NON_ZERO, 10)
    polyline.moveTo(leftBottom.x, leftBottom.y)
    polyline.lineTo(leftTop.x, leftTop.y)
    polyline.lineTo(rightTop.x, rightTop.y)
    polyline.lineTo(rightBottom.x, rightBottom.y)
    polyline.closePath()
    return polyline
    
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
    
    steeringForce = steeringForces.fold(s.zeroVector)(_ + _)
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
    
    if(drawSteering) {
      g.setColor(new Color(255, 0, 0))
      g.rotate(steeringForce.angle)
      g.fill(this.buildSteering)
    }
    
    //Sector is drawn in global coordinates to allow for collision detection
    g.setTransform(old)
    g.setColor(new Color(255, 255, 255))
    
    if(drawSector) {
      g.setColor(new Color(255, 255, 255, 50))
      g.fill(this.buildSector)
    }
    
    g.setColor(new Color(255, 255, 255))
  }
  
}