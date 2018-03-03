import scala.swing._

/*This is the view class responsible for drawing the simulation on screen.*/
object View extends SimpleSwingApplication {
  
  //Define constants here
  val height = 400
  val width = 600
  
  //Main frame contains all the other components
  def top = new MainFrame {
    
    title = "Boid-Sim"
    size = new Dimension(width, height)
    
    val canvas = new Panel {
      
      override def paintComponent(g: Graphics2D) {
        
      }
      
    }

  }
}