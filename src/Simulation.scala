import scala.collection.mutable.Buffer
import java.awt.Graphics2D

class Simulation {
  
  val newComponents = Buffer[SimComponent]()
  
  def addComponent(c: SimComponent) {
    newComponents += c
  }
  
  def step() {
    //Do nothing for now
  }
  
  def draw(g: Graphics2D) {
    newComponents.foreach(_.draw(g))  //FIXME: Use old and new buffer
  }
  
}