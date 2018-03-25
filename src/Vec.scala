import scala.math._

case class Vec(x: Double, y:Double) {
  
  def length = hypot(x, y)
  
  def angle = atan2(y, x)
  
  def normalize = if(this.length != 0) this / abs(this.length) else this
  
  def +(another: Vec) = Vec(this.x + another.x, this.y + another.y)
  
  def -(another: Vec) = Vec(this.x - another.x, this.y - another.y)
  
  def *(another: Vec) = Vec(this.x * another.x, this.y * another.y)
  
  def /(scalar: Double) = Vec(this.x / scalar, this.y / scalar)
  
  def *(scalar: Double) = Vec(this.x * scalar, this.y * scalar)
  
  //Truncate vector length with a scalar. If scalar is larger than current lenght, do nothing.
  def truncateWith(scalar: Double): Vec = {
    val newAngle = this.angle
    val newLength = min(scalar, this.length)
    val newX = newLength * cos(newAngle)
    val newY = newLength * sin(newAngle)
    return Vec(newX, newY)
  }
  
}