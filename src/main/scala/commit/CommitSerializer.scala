package commit

import console.MyConsole
import constant.Constants
import io.circe.Encoder
import io.circe.generic.auto.*
import io.circe.syntax.*

import java.nio.file.{Files, Path, StandardOpenOption}

implicit val pathEncoder: Encoder[Path] = Encoder.encodeString.contramap[Path](_.toString)


object CommitSerializer {

  // Метод для сериализации объекта commit в JSON и записи в файл
  def serializeCommit(name:String,commit: Commit): Unit = {

    val commitJson = commit.asJson.toString

    val commitFilePath = Constants.myGitDir.resolve(s"${name}.json")

    try {
      Files.write(commitFilePath, commitJson.getBytes, StandardOpenOption.CREATE_NEW)
      MyConsole.println(s"Коммит сериализован и сохранен в ${commitFilePath}")
    } catch {
      case e: java.nio.file.FileAlreadyExistsException =>
        MyConsole.println(s"Ошибка: файл ${commitFilePath} уже существует!")
      case e: Exception =>
        MyConsole.println(s"Ошибка при сохранении коммита: ${e.getMessage}")
    }
  }
}