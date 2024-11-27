package push

import constant.Constants
import exceptions.UnexpectedException
import org.apache.commons.vfs2.impl.StandardFileSystemManager
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder
import org.apache.commons.vfs2.{FileSystemManager, FileSystemOptions, Selectors, VFS}
import util.VFSUtil.resolveFileWithOptions

import java.io.File
import java.nio.file.{FileSystemException, Files, Path}

object Pusher {
  def push(sftpUrl:String): Unit = {
    val localDir = Constants.myGitDir
    val manager =  VFS.getManager
    try {
      // Создаем менеджер файловой системы
      val remoteDir= resolveFileWithOptions(sftpUrl,manager )
      // Получаем ссылку на удаленную директорию


      // Работаем с файлами в локальной директории
      val localFiles = Files.list(localDir)

      // Копируем файлы, которых нет на удаленной стороне
      localFiles.forEach { localFile =>
        val remoteFile = remoteDir.resolveFile(localFile.getFileName.toString)

        // Проверяем, существует ли файл на удалённой стороне
        if (!remoteFile.exists()) {
          remoteFile.copyFrom(manager.resolveFile("file://" + localFile), Selectors.SELECT_SELF)
          println(s"Copied ${localFile.getFileName.toString} to remote directory")
        } else {
          println(s"File ${localFile.getFileName.toString} already exists on the remote server, skipping.")
        }
      }
    }catch
       {
    case e: FileSystemException => throw new UnexpectedException(e.getMessage, e)
  }
}

// Создаем менеджер файловой системы с настройками

}
