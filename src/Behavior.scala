import java.awt.Graphics2D
import scala.math._

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

class Flee(target: SimComponent) extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val desiredVel = (b.pos - target.pos).normalize * b.maxSpeed
    val steering = desiredVel - b.velocity
    val steeringForce = steering.truncateWith(b.maxForce)
    return steeringForce
  }
}

class Separation extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val otherBoids = s.oldComponents.filter(x => x.isInstanceOf[Boid])
    val nearbyBoids = otherBoids.filter(x => (x.pos - b.pos).length < b.neighborhood)
    
    val steeringVectors = for {
      boid <- nearbyBoids
      val offsetVector = (b.pos - boid.pos).normalize
      val weight = 1.0 / max(0.001,(b.pos - boid.pos).length) //FIXME: DivideByZero?
      val steeringVector = offsetVector * weight
    } yield steeringVector
    
    val sum = steeringVectors.fold(Vec(0, 0))(_ + _).truncateWith(b.maxForce)
    return sum
  }
}

/*TODO: Checking for obstacles in front*/
class ObstacleAvoidance extends Behavior {
  
  /*Calculate a sum vector for fleeing from all the obstacles in sight*/
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val components = s.oldComponents.clone()
    val obstacles = components.filter(x => x.isInstanceOf[Obstacle])
    //FIXME: Sector is not in place when checking
    val obstaclesInSight = obstacles.filter(x => b.sector.contains(x.pos.x, x.pos.y))
    //println("Size of obstacles: " + obstaclesInSight.size)
    //Create a new flee behavior for each obstacle and get vectors
    val steeringVectors = for {
      obs <- obstaclesInSight
      val flee = new Flee(obs)
    } yield flee.getSteeringVector(s, b)
    
    //Sum the vectors together, truncate with maxForce and then return it
    val sum = steeringVectors.fold(Vec(0, 0))(_ + _).truncateWith(b.maxForce)
    return sum
  }
}