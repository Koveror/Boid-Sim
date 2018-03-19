import scala.math._

case class Vec(x: Double, y:Double) {
  
  def length = hypot(x, y)
  
  def angle = atan(y / x)
  
  def +(another: Vec) = Vec(this.x + another.x, this.y + another.y)
  
  def *(another: Vec) = Vec(this.x * another.x, this.y * another.y)
  
  //Truncate the length of this vector with another vectors length, for example maxVelocity.
  def truncateWith(another: Vec): Vec = {
    val newAngle = this.angle
    val newLength = another.length
    val newX = newLength * cos(angle)
    val newY = newLength * sin(angle)
    return Vec(newX, newY)  //TODO: Functional or with side-effects?
  }
  
}