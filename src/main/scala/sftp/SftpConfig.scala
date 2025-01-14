package sftp

import org.springframework.context.annotation.{Bean, Configuration}
import org.springframework.core.io.FileSystemResource
import org.springframework.integration.sftp.session.DefaultSftpSessionFactory

@Configuration
class SftpConfig {
  // Фабричный метод, принимающий параметры
  def sftpSessionFactory(details: SftpDetails): DefaultSftpSessionFactory = {
    val factory = new DefaultSftpSessionFactory()
    factory.setHost(details.host)
    factory.setPort(details.port)
    factory.setUser(details.user)
    factory.setPassword(details.password)
    factory.setAllowUnknownKeys(true)
    factory
  }
}