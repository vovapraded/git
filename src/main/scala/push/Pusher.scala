package push

import constant.Constants
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.integration.sftp.session.SftpSession
import sftp.{SftpConfig, SftpDetails, SftpParser, SftpUtil}

import java.nio.file.{Files, Path, Paths}
import scala.jdk.CollectionConverters.*

object Pusher {
  
  // Функция для создания директории на удаленном сервере, если ее нет
  private def createRemoteDirectoryIfNotExists(session: SftpSession, remoteDir: String): Either[Throwable, Unit] = {
    try {
      if (!session.exists(remoteDir)) {
        println(s"Directory $remoteDir does not exist. Creating...")
        session.mkdir(remoteDir)
        println(s"Directory $remoteDir created successfully.")
      } else {
        println(s"Directory $remoteDir already exists.")
      }
      Right(()) // Успешное создание директории или проверка ее наличия
    } catch {
      case e: Exception =>
        println(s"Error occurred while creating directory: ${e.getMessage}")
        Left(e) // Возвращаем ошибку
    }
  }

  // Функция для загрузки файлов с локальной машины на удаленный сервер
  private def uploadFiles(session: SftpSession, sftpDetails: SftpDetails, localDir: Path): Either[Throwable, Unit] = {
    try {
      val localFiles = Files.list(localDir)
        .iterator()
        .asScala
        .toList // Преобразуем Stream в List, чтобы безопасно работать с элементами

      // Путь для удаленной директории
      val remoteDirPath = Paths.get(sftpDetails.path, Constants.currentDir.getFileName.toString).toString

      // Создаем удаленную директорию, если она не существует
      val dirCreationResult = createRemoteDirectoryIfNotExists(session, remoteDirPath)
      if (dirCreationResult.isLeft){
        dirCreationResult
      }else {
        // Перебор локальных файлов
        localFiles.foreach { localFile =>
          val remoteFilePath = s"$remoteDirPath/${localFile.getFileName}"

          if (!session.exists(remoteFilePath)) {
            println(s"Copying ${localFile.getFileName} to $remoteFilePath...")
            session.write(Files.newInputStream(localFile), remoteFilePath)
            println(s"Copied ${localFile.getFileName} to remote directory.")
          } else {
            println(s"File ${localFile.getFileName} already exists on the remote server, skipping.")
          }
        }
        Right(()) // Успешная загрузка файлов
      }
    } catch {
      case e: Exception =>
        println(s"Error occurred during file upload: ${e.getMessage}")
        Left(e) // Возвращаем ошибку
    }

  }

  // Основная функция для выполнения задачи
  def push(remoteDir: String): Either[Throwable, Unit] = {
    val localDir: Path = Constants.myGitDir // Локальная директория с файлами

    // Получаем SFTP-сессию и детали
    val sftpSessionResult = SftpUtil.getSftpSession(remoteDir)

    // Проверяем результат создания сессии
    sftpSessionResult match {
      case Left(error) =>
        Left(error) // Возвращаем ошибку, если не удалось создать сессию
      case Right((session, sftpDetails)) =>
        // Загружаем файлы, если сессия успешно создана
        uploadFiles(session, sftpDetails, localDir)
    }
  }

}