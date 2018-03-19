import scala.math._

case class Vec(x: Double, y:Double) {
  
  def length = hypot(x, y)
  
  def angle = atan2(y, x)
  
  def normalize = if(max(x, y) != 0) this / max(x, y) else this
  
  def +(another: Vec) = Vec(this.x + another.x, this.y + another.y)
  
  def *(another: Vec) = Vec(this.x * another.x, this.y * another.y)
  
  def /(scalar: Double) = Vec(this.x / scalar, this.y / scalar)
  
  def *(scalar: Double) = Vec(this.x * scalar, this.y * scalar)
  
  //Truncate the length of this vector with another vectors length, for example maxVelocity.
  def truncateWith(another: Vec): Vec = {
    val newAngle = this.angle
    val newLength = another.length
    val newX = newLength * cos(angle)
    val newY = newLength * sin(angle)
    return Vec(newX, newY)  //TODO: Functional or with side-effects?
  }
  
}