import java.awt.Graphics2D
import java.awt.geom.Ellipse2D

abstract class SimComponent(val pos: Vec) {
  
  def getVel: Vec

  def getPos: Vec
  
  def move(): Unit
  
  def act(s: Simulation): Unit
  
  def draw(g: Graphics2D): Unit
  
}