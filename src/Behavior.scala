

abstract class Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec
}

class Seek extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val desiredVel = (s.target.pos - b.pos).normalize * b.maxSpeed
    val steering = desiredVel - b.velocity
    val steeringForce = steering.truncateWith(b.maxForce)
    return steeringForce
  }
}

class Flee extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val desiredVel = (b.pos - s.target.pos).normalize * b.maxSpeed
    val steering = desiredVel - b.velocity
    val steeringForce = steering.truncateWith(b.maxForce)
    return steeringForce
  }
}