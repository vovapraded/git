package commit
package commit
 
import console.MyConsole
import constant.Constants
import io.circe.{Decoder, parser}
import io.circe.generic.auto.*

import java.nio.file.{Files, Path}

import io.circe.{Decoder, HCursor}
import java.nio.file.{Path, Paths}



implicit val pathDecoder: Decoder[Path] = new Decoder[Path] {
    final def apply(c: HCursor): Decoder.Result[Path] =
      c.as[String].map(s => Paths.get(s)) // Преобразует строку в Path
  }

implicit val mapStringToPathDecoder: Decoder[Map[String, Path]] = Decoder.decodeMap[String, Path]


object CommitDeserializer {

  // Метод для десериализации JSON из файла в объект Commit
  def deserializeCommit(name: String): Option[Commit] = {
    val commitFilePath = Constants.myGitDir.resolve(s"${name}.json")

    // Проверяем, существует ли файл
    if (!Files.exists(commitFilePath)) {
      MyConsole.println(s"Ошибка: файл ${commitFilePath} не найден!")
      return None
    }

    try {
      // Читаем содержимое файла
      val commitJson = Files.readString(commitFilePath)

      // Парсим JSON в объект Commit
      parser.decode[Commit](commitJson) match {
        case Right(commit) =>
          MyConsole.println(s"Коммит успешно десериализован из ${commitFilePath}")
          Some(commit)
        case Left(error) =>
          MyConsole.println(s"Ошибка при десериализации коммита: ${error.getMessage}")
          None
      }
    } catch {
      case e: Exception =>
        MyConsole.println(s"Ошибка при чтении файла ${commitFilePath}: ${e.getMessage}")
        None
    }
  }
}