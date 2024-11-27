package exceptions

abstract class MyGitException(message:String, cause:Throwable)  extends RuntimeException(message, cause) 
