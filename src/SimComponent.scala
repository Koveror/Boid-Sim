import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

abstract class SimComponent(var pos: Vec) {
  
  def getPos = pos

  def act(s: Simulation): Unit
  
  def draw(g: Graphics2D): Unit
  
}