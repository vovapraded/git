package exceptions

class IllegalArgumentException(message: String, cause:Throwable = null) extends MyGitException(message, cause)
