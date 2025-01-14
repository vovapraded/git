package sftp

import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.integration.sftp.session.SftpSession

object SftpUtil {
   def getSftpSession(sftpUrl: String): Either[Throwable, (SftpSession, SftpDetails)] = {
    val context = new AnnotationConfigApplicationContext(classOf[SftpConfig])
    try {
      // Получаем SFTP-детали из парсера
      val sftpDetails = SftpParser.parseSftpUrl(sftpUrl).getOrElse {
        val errorMessage = "Invalid SFTP URL"
        println(errorMessage)
        Left(new IllegalArgumentException(errorMessage)) // Возвращаем ошибку
      }
      sftpDetails match
        case sftpDetails: SftpDetails => {
          val sftpConfig = context.getBean(classOf[SftpConfig])
          val sftpFactory = sftpConfig.sftpSessionFactory(sftpDetails) // Передаем SftpDetails
          val session = sftpFactory.getSession

          // Возвращаем как кортеж: SftpSession и SftpDetails
          Right((session, sftpDetails))
        }
        case error:Left[Throwable,(SftpSession, SftpDetails)] => error

    } finally {
      context.close() // Закрываем контекст
    }
  }

}
