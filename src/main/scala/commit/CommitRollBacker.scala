package commit

import blob.BlobUnpacker
import console.MyConsole
import constant.Constants
import exceptions.FileException
import util.FileUtil
import walker.DirectoryWalker

import java.io.IOException
import java.nio.file.{Files, Path}
import scala.collection.SeqView

object CommitRollBacker {
  private var hashToPath: Map[String, Path] =  Map[String, Path]()
  private var directories: Set[Path] = Set[Path]();

  def rollBackTo(commit: Commit): Unit = {
    clearVariables()
    DirectoryWalker.walkDirectory(Constants.currentDir, memorizeFile, memorizeDirectory)
    //обрабатываем имеющиеся файлы
    hashToPath.foreach { case (hashString, filePath) =>
       if (commit.treeForFiles.contains(hashString)) {
         if (!filePath.equals(commit.treeForFiles(hashString))){
            FileUtil.moveFileWithDirs(filePath, commit.treeForFiles(hashString))
         }
       } else {
        if (!FileUtil.deleteFile(filePath)) {
          throw new FileException(s"Ошибка удаления файла: $filePath")
        }
       }
    }
    //обрабатываем остальные
    commit.treeForFiles.foreach { case (hashString, filePath) =>
      // Проверяем, существует ли файл с этим хешем в репозитории
      if  (!hashToPath.contains(hashString)){
        val result = BlobUnpacker.unpackBlob(hashString, filePath)
        // Если файл был успешно разархивирован, выводим сообщение
        if (result) {
          MyConsole.println(s"Файл для хеша $hashString успешно восстановлен по пути $filePath")
        } else {
          // Если произошла ошибка, выводим сообщение
          MyConsole.println(s"Ошибка при восстановлении файла по пути $filePath")
        }
      }
    }
    commit.listEmptyDirectories.foreach { dirPath =>
      try {
        Files.createDirectories(dirPath) // Создаёт директорию, если её ещё нет
        MyConsole.println(s"Директория создана: $dirPath")
      } catch {
        case e: Exception =>
          MyConsole.println(s"Ошибка при создании директории $dirPath: ${e.getMessage}")
      }
    }
    //удаляем пустые папки
    directories.foreach(dirPath =>
        FileUtil.deleteDirectoryIfEmpty(dirPath)
    )
    commit.listEmptyDirectories.foreach(
      dirPath =>
        FileUtil.createDirectoryRecursively(dirPath)
    )
  }

  private def clearVariables(): Unit = {
    directories = Set[Path]();
    hashToPath = Map[String, Path]()
  }

  private def memorizeFile(path: Path): Unit = {
    hashToPath = hashToPath + (FileUtil.receiveHash(Files.readAllBytes(path))-> path )
    }
  private def memorizeDirectory(path: Path):Unit = {
    directories = directories + path
  }
}
