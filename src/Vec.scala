import scala.math._

class Vec(val x: Double, val y:Double) {
  
  def length: Double = hypot(x, y)
  
  def angle: Double = atan2(y, x)
  
  def normalized: Vec = if(this.length != 0) this / abs(this.length) else this
  
  def +(another: Vec): Vec = new Vec(this.x + another.x, this.y + another.y)
  
  def -(another: Vec): Vec = new Vec(this.x - another.x, this.y - another.y)
  
  def *(another: Vec): Vec = new Vec(this.x * another.x, this.y * another.y)
  
  def /(scalar: Double): Vec = new Vec(this.x / scalar, this.y / scalar)
  
  def *(scalar: Double): Vec =  new Vec(this.x * scalar, this.y * scalar)
  
  //Give a new vector truncated with length of scalar. If scalar is larger than current lenght, do nothing.
  def truncatedWith(scalar: Double): Vec = {
    val newAngle = this.angle
    val newLength = min(scalar, this.length)
    val newX = newLength * cos(newAngle)
    val newY = newLength * sin(newAngle)
    return new Vec(newX, newY)
  }

}

