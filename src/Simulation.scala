import scala.collection.mutable.Buffer
import java.awt.Graphics2D

class Simulation(val width: Int, val height: Int) {
  
  val boids = Buffer[Boid]()
  val obstacles = Buffer[Obstacle]()
  val targets = Buffer[Target]()
  val zeroVector = new Vec(0, 0)
  
  //Initialize simulation
  val target = new Target(new Vec(100, 300))
  targets += target
  obstacles += new Obstacle(new Vec(100, 100))
  obstacles += new Obstacle(new Vec(150, 250))
  obstacles += new Obstacle(new Vec(400, 50))
  
  def addBehavior(b: Behavior) {
    boids.foreach(_.addBehavior(b))
  }
  
  def removeBoid(b: Boid) {
    boids -= b
  }
  
  /*Add a new SimComponent to the simulation*/
  def addBoid(b: Boid) {
    boids += b
  }
  
  /*Move simulation along by one turn*/
  def step() {
    boids --= boids.filterNot(x => (x.getPos.x >= 0 && x.getPos.x <= width) && (x.getPos.y >= 0 && x.getPos.y <= height))
    boids.foreach(_.act(this))  //FIXME: Nullptr?
    boids.foreach(_.move())
  }
  
  /*Draw the simulation state on the given simSpace*/
  def draw(g: Graphics2D) {
    boids.foreach(_.draw(g))
    obstacles.foreach(_.draw(g))
    targets.foreach(_.draw(g))
  }
  
}