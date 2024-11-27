import command.CommandExecutor
import console.MyConsole

import scala.util.CommandLineParser

object AppRunner {
 
  @main
  def main(args: String*): Unit = {
        if (args.length > 1) {
          CommandExecutor.createAndExecute(args(0), Option(args(1)))
        } else if (args.length == 1) {
          CommandExecutor.createAndExecute(args(0))
        } else {
          MyConsole.println("Ошибка: Команда не задана")
        }
      
  }
}