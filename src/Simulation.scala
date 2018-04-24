import scala.collection.mutable.Buffer
import java.awt.Graphics2D

/*Simulation is moved along with the step() method.
 *The method draw() is used to draw the current state of the simulation.*/
class Simulation(val width: Int, val height: Int) {
  
  /*Components*/
  val boids = Buffer[Boid]()
  val obstacles = Buffer[Obstacle]()
  val targets = Buffer[Target]()
  
  /*Constants*/
  val zeroVector = new Vec(0, 0)
  
  /*Default behaviors*/
  val defaultBeh = Buffer[Behavior]()
  
  /*Options*/
  var drawSector = false
  var drawSteering = false
  var drawVelocity = false
  var drawDesired = false
  var loopPositions = false
  
  /*Remove all components*/
  def clear() {
    boids.clear()
    obstacles.clear()
    targets.clear()
  }
  
  /*Add target to targets*/
  def addTarget(t: Target) {
    targets.clear()
    targets += t
  }
  
  /*Add behavior to list of default behaviors. New boids are created with these behaviors*/
  def addDefaultBehavior(b: Behavior) {
    if(!defaultBeh.exists(b.getType == _.getType)) {
      defaultBeh += b 
    }
  }
  
  /*Add behavior to list of default behaviors. New boids are created with these behaviors*/
  def removeDefaultBehavior(b: Behavior) {
    defaultBeh --= defaultBeh.filter(_.getType == b.getType)
  }
  
  /*Use default set with all*/
  def useDefaultForAll() {
    boids.foreach(_.clearBehaviors())
    for(beh <- defaultBeh) {
      boids.foreach(_.addBehavior(beh))
    }
  }
  
  /*Add obstacle to obstacles*/
  def addObstacle(o: Obstacle) {
    obstacles += o
  }
  
  /*Removes the boid b from the simulation*/
  def removeBoid(b: Boid) {
    boids -= b
  }
  
  /*Add a new boid b to the simulation with all default behaviors*/
  def addBoid(b: Boid) {
    defaultBeh.foreach(b.addBehavior(_))
    boids += b
  }
  
  /*Move simulation along by one turn*/
  def step() {
    if(!loopPositions) {
      boids --= boids.filterNot(x => (x.getPos.x >= 0 && x.getPos.x <= width) && (x.getPos.y >= 0 && x.getPos.y <= height))
    }
    boids.foreach(_.act(this))  //FIXME: Nullptr?
    boids.foreach(_.move(this))
    //println(defaultBeh)
  }
  
  /*Draw the simulation state on the given SimSpace*/
  def draw(g: Graphics2D) {
    boids.foreach(_.draw(g))
    obstacles.foreach(_.draw(g))
    targets.foreach(_.draw(g))
  }
  
}