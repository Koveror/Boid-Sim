

case class Vec(x: Double, y:Double) {
  
  def +(another: Vec) = {
    Vec(this.x + another.x, this.y + another.y)
  }
  
  def *(another: Vec) = {
    Vec(this.x * another.x, this.y + another.y)
  }
  
}