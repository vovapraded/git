package command

import command.Command
import console.MyConsole
import constant.Constants

import scala.jdk.CollectionConverters.*
import java.nio.file.{Files, Path}
import io.circe.*
import io.circe.parser.*
import io.circe.generic.auto.*

import java.nio.file.{Files, Path}
import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, ZoneOffset, ZonedDateTime}
import java.util.{Comparator, Locale}

class InfoCommand extends Command {
  val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy, HH:mm:ss")

  override def execute(): Unit = {
    // Создаём объект папки по пути
    val dir = Constants.myGitDir

    // Получаем все файлы в папке
    val files = Files.list(dir)

    // Фильтруем только JSON файлы
    val jsonFiles = files.filter(file => file.getFileName.toString.endsWith(".json")).toList.asScala

    // Преобразуем файлы в список пар (имя файла, время)
    val filesWithTimes = jsonFiles.flatMap(parseTime)

    if (filesWithTimes.nonEmpty) {
      // Сортируем по времени в убывающем порядке
      val sortedFiles = filesWithTimes.sortWith((a, b) => a._2.compareTo(b._2) > 0)
      MyConsole.println("Список файлов с временем (по убыванию):")
      sortedFiles.foreach { case (file, time) =>
        val timeFormatted = formatter.format(time) // Форматируем LocalDateTime в ISO 8601 строку
        MyConsole.println(s"${file.getFileName.toString} $timeFormatted")
      }
    } else {
      MyConsole.println("JSON файлы не найдены или не содержат времени.")
    }
  }

  // Преобразуем JSON файл в пару (файл, время)
  def parseTime(file: Path): Option[(Path, LocalDateTime)] = {
    try {
      val jsonStr = Files.readString(file) // Читаем содержимое файла
      val json = parse(jsonStr).getOrElse(Json.Null) // Парсим содержимое в JSON

      // Извлекаем поле time
      json.hcursor.downField("time").as[LocalDateTime].toOption.map { time =>
        (file, time) // Возвращаем пару (файл, время)
      }
    } catch {
      case _: Exception => None // Возвращаем None в случае ошибки парсинга
    }
  }
}