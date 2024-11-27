package command

import console.MyConsole
import pull.Puller

class PullCommand(override protected val arg: Option[String]) extends Command{
  override def execute(): Unit = {
    arg match {
      case Some(url) =>
        // If an argument is provided, create the commit
        Puller.pull(url)
      case None =>
        // If no argument is provided, print an error message
        MyConsole.println("Ошибка: имя коммита не предоставлено.")
    }  }
}
