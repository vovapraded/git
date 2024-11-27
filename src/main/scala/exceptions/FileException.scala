package exceptions

class FileException(message: String, cause: Throwable = null) extends MyGitException(message,cause) {
}