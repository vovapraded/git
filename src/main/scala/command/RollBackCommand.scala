package command

import commit.CommitRollBacker
import commit.commit.CommitDeserializer
import console.MyConsole

class RollBackCommand(override protected val arg: Option[String]) extends Command {
  override def execute(): Unit = {
    arg match {
      case Some(name) =>
        for {
          commit <- CommitDeserializer.deserializeCommit(name)
        } CommitRollBacker.rollBackTo(commit)
      case None =>
        MyConsole.println("Ошибка: имя коммита не предоставлено.")
    }
  }
}


