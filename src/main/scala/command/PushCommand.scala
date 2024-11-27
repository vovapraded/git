package command

import console.MyConsole
import push.Pusher

class PushCommand(override protected val arg: Option[String]) extends Command{
  override def execute(): Unit = {
    arg match {
      case Some(url) =>
        // If an argument is provided, create the commit
        Pusher.push(url)
      case None =>
        // If no argument is provided, print an error message
        MyConsole.println("Ошибка: имя коммита не предоставлено.")
    }  }
}
