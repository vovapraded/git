package command

import console.MyConsole
import exceptions.MyGitException
import exceptions.IllegalArgumentException

import scala.util.Try


object CommandExecutor {
  def createAndExecute(command: String, arg: Option[String] = None): Unit = {
    try {
      // Создаём команду на основе входного параметра
      val cmd: Command = command match {
        case "init" => new InitCommand
        case "commit" => new CommitCommand(arg)
        case "rollback" => new RollBackCommand(arg)
        case "push" => new PushCommand(arg)
        case "pull" => new PullCommand(arg)
        case "info" => new InfoCommand
        case _ => throw new IllegalArgumentException(s"Unknown command: $command", null)
      }
      cmd.execute()
    } catch {
      case ex: MyGitException =>
        MyConsole.println(s"${ex.getMessage}")
      case ex: Exception =>
        MyConsole.println(s"Unknown error: ${ex.getMessage}")
        ex.printStackTrace() // Для отладки (опционально)
    }
  }

}
