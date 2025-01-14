package sftp

import java.net.URI
import java.net.URISyntaxException

object SftpParser {
  // Метод для парсинга SFTP URL с учетом домашней папки
  def parseSftpUrl(sftpUrl: String): Option[SftpDetails] = {
    try {
      val uri = new URI(sftpUrl)

      // Проверяем, что схема - это "sftp"
      if (uri.getScheme != "sftp") {
        println(s"Invalid URL scheme: ${uri.getScheme}")
        return None
      }

      // Извлекаем user, password, host и port
      val userInfo = Option(uri.getUserInfo).getOrElse("").split(":")

      val user = if (userInfo.nonEmpty) userInfo(0) else ""
      val password = if (userInfo.length > 1) userInfo(1) else ""

      val host = uri.getHost
      val port = if (uri.getPort != -1) uri.getPort else 22 // по умолчанию 22, если порт не указан

      // Путь, который идет после host:port
      val path = Option(uri.getPath).getOrElse("")


      Some(SftpDetails(user, password, host, port, path))
    } catch {
      case e: URISyntaxException =>
        println(s"Invalid SFTP URL: ${e.getMessage}")
        None
    }
  }
}