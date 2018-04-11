import scala.collection.mutable.Buffer
import java.awt.Graphics2D

class Simulation(val width: Int, val height: Int) {
  
  val components = Buffer[SimComponent]()
  
  //Initialize simulation
  val target = new Target(Vec(100, 300))
  components += target
  components += new Obstacle(Vec(100, 100))
  components += new Obstacle(Vec(150, 250))
  components += new Obstacle(Vec(400, 50))
  
  def removeComponent(c: SimComponent) {
    components -= c
  }
  
  /*Add a new SimComponent to the simulation*/
  def addComponent(c: SimComponent) {
    components += c
  }
  
  /*Move simulation along by one turn*/
  def step() {
    components --= components.filterNot(x => (x.getPos.x >= 0 && x.getPos.x <= width) && (x.getPos.y >= 0 && x.getPos.y <= height))
    components.foreach(_.act(this))  //FIXME: Nullptr?
  }
  
  /*Draw the simulation state on the given simSpace*/
  def draw(g: Graphics2D) {
    components.foreach(_.draw(g))
  }
  
}