import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

/*SimComponent is an abstract base class for Obstacle, Target and Boid*/
abstract class SimComponent(val pos: Vec) {

  def getPos: Vec
  
  def act(s: Simulation): Unit
  
  def draw(g: Graphics2D): Unit
  
}