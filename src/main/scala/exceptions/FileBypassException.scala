package exceptions

// Класс исключения, который наследуется от RuntimeException
class FileBypassException(message: String, cause: Throwable = null) extends RuntimeException(message,cause)  {
   }