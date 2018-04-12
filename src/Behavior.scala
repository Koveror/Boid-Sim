import java.awt.Graphics2D
import scala.math._

abstract class Behavior {
  
  /*Each behavior gives a unique steering force that dictates which way the behavior wants to take the boid.
   * Steering forces are added to the current velocity of a boid*/
  def getSteeringVector(s: Simulation, b: Boid): Vec
  
  /*Each behavior has a unique integer value, which makes comparing easy*/
  def getType: Int
}

class Seek(target: SimComponent) extends Behavior {
  
  /*Seek behavior makes boids home in on the target*/
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val desiredVel = (target.getPos - b.getPos).normalized * b.maxSpeed
    val steering = desiredVel - b.getVel
    val steeringForce = steering.truncatedWith(b.maxForce)
    return steeringForce
  }
  
  def getType = 1
}

class Flee(target: SimComponent) extends Behavior {
  
  /*Flee behavior makes boids flee from the target*/
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val desiredVel = (b.getPos - target.getPos).normalized * b.maxSpeed
    val steering = desiredVel - b.getVel
    val steeringForce = steering.truncatedWith(b.maxForce)
    return steeringForce
  }
  
  def getType = 2
}

class Cohesion extends Behavior {
  
  /*Cohesion makes boids seek towards the center of their group*/
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
  
  def getType = 3
}

class Separation extends Behavior {
  
  /*Separation behavior makes boid keep distance to nearby boids.*/
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    
    val scalar = 120  //Make the behavior more intense with a coefficient
    val allBoids = s.boids
    val otherBoids = allBoids - b
    val nearbyBoids = otherBoids.filter(x => (x.getPos - b.getPos).length < b.neighborhood)
    
    val steeringVectors = for {
      boid <- nearbyBoids
      val offsetVector = (b.getPos - boid.getPos).normalized
      val weight = 1.0 * scalar / max(0.00000001,(b.getPos - boid.getPos).length)
      val steeringVector = offsetVector * weight
    } yield steeringVector
    
    val sum = steeringVectors.fold(s.zeroVector)(_ + _).truncatedWith(b.maxForce)
    return sum
  }
  
  def getType = 4
}


class Alignment extends Behavior {
  
  /*Aligment makes boid face the same direction as the rest of the group*/
  def getSteeringVector(s: Simulation, b: Boid): Vec = {
    val allBoids = s.boids
    val otherBoids = allBoids - b
    val nearbyBoids = otherBoids.filter(x => (x.getPos - b.getPos).length < b.neighborhood)
    
    if(nearbyBoids.isEmpty) {
      return s.zeroVector
    } else {
      val averageVel = nearbyBoids.map(x => x.getVel).fold(s.zeroVector)(_ + _) / nearbyBoids.size
      val steeringVector = (averageVel - b.getVel).truncatedWith(b.maxForce)
      return steeringVector
    }
    
  }
  
  def getType = 5
}

/*TODO: Checking for obstacles in front*/
class ObstacleAvoidance extends Behavior {
  
  /*Obstacle avoidance makes boid flee from obstacles in front, avoiding them*/
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
  
  def getType = 6
}