package repository
import console.MyConsole

import java.nio.file.{Files, Path, Paths, StandardCopyOption}
import java.nio.file.attribute.PosixFilePermissions


object GitDirectoryCreator {
  def createGitDirectory(myGitDir:Path): Either[String, Unit] = {
    // Создаем путь к директории .git
    if (!Files.exists(myGitDir)) {
      Files.createDirectory(myGitDir)
      Right(())
    } else {
      Left(s"Директория уже была инициализированна")
    }
  }
}