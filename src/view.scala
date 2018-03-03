import scala.swing._

object View extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "test"
    size = new Dimension(400, 400)
  }
}