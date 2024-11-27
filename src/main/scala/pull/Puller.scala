package pull

import constant.Constants
import exceptions.UnexpectedException
import org.apache.commons.vfs2.{Selectors, VFS}
import util.VFSUtil

import java.io.File
import java.nio.file.{Files, Path}

object Puller {
  def pull(sftpUrl: String): Unit = {
    val localDir = Constants.myGitDir
    try {
      // Создаем менеджер файловой системы
      val manager = VFS.getManager
      // Получаем ссылку на удаленную директорию
      val remoteDir = VFSUtil.resolveFileWithOptions( sftpUrl, manager)

      // Работаем с файлами в удаленной директории
      val remoteFiles = remoteDir.getChildren

      // Копируем файлы, которых нет в локальной директории
      remoteFiles.foreach { remoteFile =>
        val localFile =  Path.of(localDir.toString, remoteFile.getName.getBaseName.toString)

        // Проверяем, существует ли файл локально
        if (!Files.exists(localFile)) {
          manager.resolveFile("file://" + localFile.toString).copyFrom(remoteFile, Selectors.SELECT_SELF )
          println(s"Copied ${remoteFile.getName.getBaseName} to local directory")
        } else {
          println(s"File ${remoteFile.getName.getBaseName} already exists locally, skipping.")
        }
      }
    } catch {
      case e: Exception => throw new UnexpectedException(e.getMessage, e)
    }
  }

}
