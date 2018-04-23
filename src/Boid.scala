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
  val maxForce = 80.0
  val minSpeed = 1.0  //FIXME: Having minSpeed causes orbiting around target when using Seek
  val maxSpeed = 10.0
  val neighborhood = 40.0  //The radius of neighborhood
  val neighborAngle = Pi / 2
  
  /*Variables*/
  private var oldPosition = p
  private var newPosition = p
  private var orientation = o
  private var velocity = v
  private var steeringForce = v
  private var desiredVel = v
  
  /*Behaviors*/
  val behaviors: Buffer[Behavior] = Buffer()
  
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
    val rect = new Rectangle2D.Double(0, 0, steeringForce.length * 5, 2)
    return rect
  }
  
  def buildVelocity: Rectangle2D = {
    val rect = new Rectangle2D.Double(0, 0, velocity.length * 5, 2)
    return rect
  }
  
  def buildDesired: Rectangle2D = {
    val rect = new Rectangle2D.Double(0, 0, desiredVel.length * 5, 2)
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
    
    val polyline = new GeneralPath(Path2D.WIND_NON_ZERO, 10)
    polyline.moveTo(leftBottom.x, leftBottom.y)
    polyline.lineTo(leftTop.x, leftTop.y)
    polyline.lineTo(rightTop.x, rightTop.y)
    polyline.lineTo(rightBottom.x, rightBottom.y)
    polyline.closePath()
    return polyline
    
  }
  
  def clearBehaviors() {
    behaviors.clear()
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
  
  /*Manually set the position of this boid. Used when looping positions is simulation.*/
  def setPos(p: Vec): Unit = {
    oldPosition = p
  }
  
  /*Set a local copy of the desired velocity vector used in some behaviors.*/
  def setDesired(v: Vec): Unit = {
    desiredVel = v
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
  def move(s: Simulation) {
    if(s.loopPositions) {
      val normPos = Vec(newPosition.x % s.width, newPosition.y % s.height)  //FIXME: Some weird stuff happens sometimes
      oldPosition = normPos
    } else {
      oldPosition = newPosition
    }
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
    
    g.setTransform(old)
    
    if(View.sim.drawVelocity) {
      g.setColor(new Color(0, 255, 0))
      g.translate(oldPosition.x, oldPosition.y)
      g.rotate(velocity.angle)
      g.fill(this.buildVelocity)
    }
    
    g.setTransform(old)
    
    if(View.sim.drawDesired) {
      g.setColor(new Color(0, 0, 255))
      g.translate(oldPosition.x, oldPosition.y)
      g.rotate(desiredVel.angle)
      g.fill(this.buildDesired)
    }
    
    g.setTransform(old)
    
    if(View.sim.drawSteering) {
      g.setColor(new Color(255, 0, 0))
      g.translate(oldPosition.x + 5 * velocity.x, oldPosition.y + 5 * velocity.y)
      g.rotate(steeringForce.angle)
      g.fill(this.buildSteering)
    }
    
    //Sector is drawn in global coordinates to allow for collision detection
    g.setTransform(old)
    g.setColor(new Color(255, 255, 255))
    
    if(View.sim.drawSector) {
      g.setColor(new Color(255, 255, 255, 50))
      g.fill(this.buildSector)
    }
    
    g.setColor(new Color(255, 255, 255))
  }
  
}