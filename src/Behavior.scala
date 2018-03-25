

abstract class Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec
}

class Seek extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val target = Vec(200, 200)  //Position of target
    val desiredVel = (target - b.pos).normalize * b.maxSpeed
    val steering = desiredVel - b.velocity
    val steeringForce = steering.truncateWith(b.maxForce)
    return steeringForce
  }
}

class Flee extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val target = Vec(200, 200)  //Position of target
    val desiredVel = (b.pos - target).normalize * b.maxSpeed
    val steering = desiredVel - b.velocity
    val steeringForce = steering.truncateWith(b.maxForce)
    return steeringForce
  }
}