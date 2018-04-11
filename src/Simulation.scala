import scala.collection.mutable.Buffer
import java.awt.Graphics2D

class Simulation(val width: Int, val height: Int) {
  
  val Components = Buffer[SimComponent]()
  
  //Initialize simulation
  val target = new Target(Vec(100, 300))
  Components += target
  Components += new Obstacle(Vec(100, 100))
  Components += new Obstacle(Vec(150, 250))
  Components += new Obstacle(Vec(400, 50))
  
  def removeComponent(c: SimComponent) {
    Components -= c
  }
  
  /*Add a new SimComponent to the simulation*/
  def addComponent(c: SimComponent) {
    Components += c
  }
  
  /*Move simulation along by one turn*/
  def step() {
    Components.filter(x => (x.pos.x >= 0 && x.pos.x <= width) && (x.pos.y >= 0 && x.pos.y <= height))
              .foreach(_.act(this))  //FIXME: Nullptr?
  }
  
  /*Draw the simulation state on the given simSpace*/
  def draw(g: Graphics2D) {
    Components.foreach(_.draw(g))
  }
  
}