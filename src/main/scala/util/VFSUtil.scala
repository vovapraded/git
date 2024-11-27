package util

import org.apache.commons.vfs2.provider.sftp.{IdentityInfo, SftpFileSystemConfigBuilder}
import org.apache.commons.vfs2.{FileObject, FileSystemManager, FileSystemOptions, VFS}

import java.io.File
import java.net.URI
import java.nio.file.Path

object VFSUtil {
  def resolveFileWithOptions(sftpUrl: String, manager: FileSystemManager): FileObject = {
    val opts = new FileSystemOptions

    // Настроим SFTP, отключив строгую проверку хостов
    val config = SftpFileSystemConfigBuilder.getInstance
    val privateKeyFile = Path.of(System.getProperty("user.home"), ".ssh", "mygit").toFile
    val passphrase = null

    // Настраиваем SSH-ключ
    val identityInfo = new IdentityInfo(privateKeyFile, passphrase)
    config.setIdentityInfo(opts, identityInfo)
    config.setStrictHostKeyChecking(opts, "no")  // Отключаем проверку хоста (по желанию)
    config.setUserDirIsRoot(opts, false)
    // Передаем параметры для подключения через SFTP с каждым вызовом
    manager.resolveFile(sftpUrl, opts)
  }
}
