package exceptions

class UnexpectedException(message: String, cause: Throwable = null) extends MyGitException(message, cause) {
  // Конструктор с только сообщением
  def this(message: String) = this(message, null)

  // Конструктор с только причиной
  def this(cause: Throwable) = this( null, cause)

  def this() = this(null, null)
}