import scala.collection.mutable.Buffer
import java.awt.Graphics2D

class Simulation {
  
  //FIXME: Vars to mutable buffers?
  var newComponents = Buffer[SimComponent]()
  var oldComponents = Buffer[SimComponent]()
  
  def removeComponent(c: SimComponent) {
    newComponents -= c
  }
  
  /*Add a new SimComponent to the simulation*/
  def addComponent(c: SimComponent) {
    newComponents += c
  }
  
  /*Move simulation along by one turn*/
  def step() {
    newComponents = Buffer[SimComponent]()
    oldComponents.foreach(_.act(this))  //FIXME: Nullptr?
    oldComponents = newComponents
  }
  
  /*Draw the simulation state on the given simSpace*/
  def draw(g: Graphics2D) {
    oldComponents.foreach(_.draw(g))
  }
  
}