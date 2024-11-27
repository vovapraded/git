package util

import console.MyConsole
import exceptions.FileException

import java.io.IOException
import java.nio.file.{Files, Path, Paths, StandardCopyOption}
import java.security.MessageDigest

object FileUtil {
  def receiveHash(fileContent: Array[Byte]): String = {
    val digest = MessageDigest.getInstance("SHA-1")
    val hashBytes = digest.digest(fileContent)
    hashBytes.map("%02x".format(_)).mkString
  }

  def deleteFile(path: Path): Boolean = {
    try {
      if (Files.exists(path)) {
        Files.delete(path)
        true // Файл успешно удалён
      } else {
        MyConsole.println(s"Файл '$path' не существует.")
        false // Файла не существует
      }
    } catch {
      case ex: IOException =>
        MyConsole.println(s"Ошибка при удалении файла '$path': ${ex.getMessage}")
        false // Возвращаем false в случае ошибки
    }
  }

  def moveFileWithDirs(sourcePath: Path, destinationPath: Path): Unit = {
    try {
      // Создаём родительскую директорию для файла назначения, если её нет
      val destinationDir = destinationPath.getParent
      if (destinationDir != null && !Files.exists(destinationDir)) {
        Files.createDirectories(destinationDir)
        MyConsole.println(s"Созданы директории: $destinationDir")
      }

      // Перемещаем файл
      Files.move(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING)
      MyConsole.println(s"Файл перемещён из $sourcePath в $destinationPath")
    } catch {
      case ex: IOException =>
        throw new FileException(s"Ошибка при перемещении файла: ${ex.getMessage}") // Повторно выбрасываем исключение, если нужно
    }
    }

  def deleteDirectoryIfEmpty(dirPath: Path): Boolean = {
    try {
      if (Files.isDirectory(dirPath) && Files.list(dirPath).findFirst().isEmpty) {
        Files.delete(dirPath)
        MyConsole.println(s"Пустая директория удалена: $dirPath")
        true
      } else {
        false
      }
    } catch {
      case ex: IOException =>
        throw new FileException(s"Ошибка при удалении директории: ${ex.getMessage}")
    }
  }
    def createDirectoryRecursively(dirPath: Path): Unit = {
      try {
        Files.createDirectories(dirPath)
        MyConsole.println(s"Директория создана: $dirPath")
      } catch {
        case ex: IOException =>
          throw  new FileException(s"Ошибка при создании директории: ${ex.getMessage}")
      }
    }
}
