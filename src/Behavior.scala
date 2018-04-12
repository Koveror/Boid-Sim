import java.awt.Graphics2D
import scala.math._

abstract class Behavior {
  /*Each behavior gives a unique steering force that dictates which way the behavior wants to take the boid.
   * Steering forces are added to the current velocity of a boid*/
  def getSteeringVector(s: Simulation, b: Boid): Vec
}

class Seek(target: SimComponent) extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val desiredVel = (target.getPos - b.getPos).normalized * b.maxSpeed
    val steering = desiredVel - b.getVel
    val steeringForce = steering.truncatedWith(b.maxForce)
    return steeringForce
  }
}

class Flee(target: SimComponent) extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val desiredVel = (b.getPos - target.getPos).normalized * b.maxSpeed
    val steering = desiredVel - b.getVel
    val steeringForce = steering.truncatedWith(b.maxForce)
    return steeringForce
  }
}


//FIXME: Quite intense
class Cohesion extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val allBoids = s.boids
    val otherBoids = allBoids - b
    val nearbyBoids = otherBoids.filter(x => (x.getPos - b.getPos).length < b.neighborhood)
    if(nearbyBoids.isEmpty) {
      return s.zeroVector
    } else {
      val positions = nearbyBoids.map(_.getPos)
      val averagePos = positions.fold(s.zeroVector)(_ + _) / positions.size  //FIXME: Average position with 0,0 included?
      val seek = new Seek(new Target(averagePos))
      return seek.getSteeringVector(s, b)
    }
  }
}

class Separation extends Behavior {
  /*Separation behavior keeps distance to nearby boids.*/
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val allBoids = s.boids
    val otherBoids = allBoids - b
    val nearbyBoids = otherBoids.filter(x => (x.getPos - b.getPos).length < b.neighborhood)
    
    val steeringVectors = for {
      boid <- nearbyBoids
      val offsetVector = (b.getPos - boid.getPos).normalized
      val weight = 1.0 / max(0.00000001,(b.getPos - boid.getPos).length)
      val steeringVector = offsetVector * weight
    } yield steeringVector
    
    val sum = steeringVectors.fold(s.zeroVector)(_ + _).truncatedWith(b.maxForce)
    return sum
  }
}


class Alignment extends Behavior {
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val allBoids = s.boids
    val otherBoids = allBoids - b
    val nearbyBoids = otherBoids.filter(x => (x.getPos - b.getPos).length < b.neighborhood)
    
    if(nearbyBoids.isEmpty) {
      return s.zeroVector
    } else {
      val averageVel = nearbyBoids.map(x => x.getVel).fold(s.zeroVector)(_ + _) / nearbyBoids.size
      val steeringVector = (b.getVel - averageVel).truncatedWith(b.maxForce)
      return steeringVector
    }
    
  }
}

/*TODO: Checking for obstacles in front*/
class ObstacleAvoidance extends Behavior {
  
  /*Calculate a sum vector for fleeing from all the obstacles in sight*/
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val obstacles = s.obstacles
    //FIXME: Sector is not in place when checking
    val obstaclesInSight = obstacles.filter(x => b.sector.contains(x.getPos.x, x.getPos.y))
    //println("Size of obstacles: " + obstaclesInSight.size)
    //Create a new flee behavior for each obstacle and get vectors
    val steeringVectors = for {
      obs <- obstaclesInSight
      val flee = new Flee(obs)
    } yield flee.getSteeringVector(s, b)
    
    //Sum the vectors together, truncate with maxForce and then return it
    val sum = steeringVectors.fold(s.zeroVector)(_ + _).truncatedWith(b.maxForce)
    return sum
  }
}