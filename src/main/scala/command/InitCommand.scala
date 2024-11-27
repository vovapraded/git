package command

import repository.GitDirectoryInitializer

class InitCommand extends Command{
  override def execute(): Unit = {
    GitDirectoryInitializer.initGitDirectory()
  }
}
