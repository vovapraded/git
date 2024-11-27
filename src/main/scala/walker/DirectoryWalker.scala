package walker

import constant.Constants
import exceptions.{FileBypassException, UnexpectedException}

import java.io.IOException
import java.nio.file.*
import java.nio.file.attribute.BasicFileAttributes
import scala.util.Try

object DirectoryWalker {

  def walkDirectory(
                     startPath: Path,
                     functionWithFile: Path => Unit,
                     functionWithDirectory: Path => Unit
                   ): List[Exception] = {
     var exceptions: List[FileBypassException] = List.empty
    // Создаем файловый обходчик, который будет рекурсивно обходить все файлы и директории
     val visitor = new SimpleFileVisitor[Path]() {

      // Для каждого файла вызываем функцию functionWithFile
      override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {

        if (!file.startsWith(Constants.myGitDir)) {

          Try(if (file.toFile.isFile) functionWithFile(file)).recover {
            case e: Exception => exceptions =
              new FileBypassException(s"Error processing file $file: ${e.getMessage}", e) +: exceptions
          }
        }

        FileVisitResult.CONTINUE
      }

       override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
         if (!dir.startsWith(Constants.myGitDir) && !dir.equals(Constants.currentDir)) {
           Try(functionWithDirectory(dir)).recover {
             case e: Exception =>
               exceptions =
                 new FileBypassException(s"Error processing directory $dir: ${e.getMessage}", e) +: exceptions
           }
         }
         FileVisitResult.CONTINUE
       }


      // Если не удаётся прочитать файл, просто сообщаем об ошибке и продолжаем
      override def visitFileFailed(file: Path, exc: IOException): FileVisitResult = {
        exceptions = new FileBypassException(s"Failed to visit file: $file", exc) +: exceptions
        FileVisitResult.CONTINUE
      }
    }
    // Пытаемся пройти по директории с использованием обходчика
     Try(Files.walkFileTree(startPath, visitor)).recover {
      case e: IOException => throw new UnexpectedException("Error: Unexpected error",e)
    }
    exceptions
  }


}