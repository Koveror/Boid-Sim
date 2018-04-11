import scala.math._

class Vec(var x: Double, var y:Double) {
  
  /**No side effects**/
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
  
  /**Side effects**/
  def normalize: Unit = {
    if(this.length != 0) {
      this /= abs(this.length)
    }
  }
  
  def +=(another: Vec): Unit = {
    x = x + another.x
    y = y + another.y
  }
  
  def -=(another: Vec): Unit = {
    x = x - another.x
    y = y - another.y
  }
  
  def *=(another: Vec): Unit = {
    x = x * another.x
    y = y * another.y
  }
  
  def /=(another: Vec): Unit = {
    x = x / another.x
    y = y / another.y
  }
  
  def *=(scalar: Double): Unit = {
    x = x * scalar
    y = y * scalar
  }
  
  def /=(scalar: Double): Unit = {
    x = x / scalar
    y = y / scalar
  }
  
  //Truncate this vectors length with a scalar. If scalar is larger than current lenght, do nothing.
  def truncateWith(scalar: Double): Unit = {
    val newLength = min(scalar, this.length)
    x = newLength * cos(this.angle)
    y = newLength * sin(this.angle)
  }
  
  
}

