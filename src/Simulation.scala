import scala.collection.mutable.Buffer
import java.awt.Graphics2D

class Simulation {
  
  val newComponents = Buffer[SimComponent]()
  val oldComponents = Buffer[SimComponent]()
  
  /*Add a new SimComponent to the simulation*/
  def addComponent(c: SimComponent) {
    newComponents += c
  }
  
  /*Move simulation along by one turn*/
  def step() {
    newComponents.foreach(x => x.setPos(x.getPos + Vec(1, 1)))
  }
  
  /*Draw the simulation state on the given canvas*/
  def draw(g: Graphics2D) {
    newComponents.foreach(_.draw(g))  //FIXME: Use old and new buffer
  }
  
}