package command

import commit.CommitCreator
import console.MyConsole

class CommitCommand(override protected val arg: Option[String]) extends Command {
  override def execute(): Unit = {
    arg match {
      case Some(name) =>
        // If an argument is provided, create the commit
        CommitCreator.createCommit(name)
      case None =>
        // If no argument is provided, print an error message
        MyConsole.println("Ошибка: имя коммита не предоставлено.")
    }
  }
}
