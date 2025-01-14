package pull

import console.MyConsole
import constant.Constants
import exceptions.UnexpectedException
import org.springframework.integration.sftp.session.SftpSession
import sftp.{SftpDetails, SftpUtil}
import util.FileUtil

import java.nio.file.{Files, Path, Paths, StandardCopyOption, StandardOpenOption}

object Puller {
  val localDir: Path = Constants.myGitDir

  // Функция для скачивания файлов с удаленного SFTP-сервера с использованием Spring Integration
  private def downloadFiles(session: SftpSession, details: SftpDetails, path: Path): Unit = {
    try {
      val remoteDirPath = Paths.get(details.path, Constants.currentDir.getFileName.toString)
      // Получаем список файлов в удаленной директории
      val remoteFiles = session.list(remoteDirPath.toString)

      // Копируем файлы, которых нет в локальной директории
      remoteFiles.foreach { remoteFile =>
        val fileName = remoteFile.getFilename

        // Пропускаем скрытые файлы (начинаются с точки)
        if (!fileName.startsWith(".")) {
          val localFile = path.resolve(fileName)

          // Проверяем, существует ли файл локально
          if (!Files.exists(localFile)) {
            // Загружаем файл, если он не существует
            println(s"Copying $fileName to local directory...")
            session.read(remoteDirPath.resolve(fileName).toString, Files.newOutputStream(localFile, StandardOpenOption.CREATE, StandardOpenOption.WRITE))
            println(s"Copied $fileName to local directory")
          } else {
            println(s"File $fileName already exists locally, skipping.")
          }
        } else {
          println(s"Skipping hidden file: $fileName")
        }
      }
    } catch {
      case e: Exception => throw new UnexpectedException(e.getMessage, e)
    }
  }
  def pull(sftpUrl: String): Unit = {
    FileUtil.createDirectory(Constants.myGitDir) match {
      case Right(_) =>
        MyConsole.println(s"Директория .myGit успешно создана")
      // Дальше делаем что-то с коммитом
      case Left(ignored) =>
    }
    // Получаем SFTP-сессию и детали
    val sftpSessionResult = SftpUtil.getSftpSession(sftpUrl)

    // Проверяем результат создания сессии
    sftpSessionResult match {
      case Left(error) =>
        println(s"Error: ${error.getMessage}")
      case Right((session, sftpDetails)) =>
        // Загружаем файлы, если сессия успешно создана
        downloadFiles(session, sftpDetails, localDir)
    }
  }
}