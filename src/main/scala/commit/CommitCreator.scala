package commit

import blob.{Blob, BlobSerializer}
import console.MyConsole
import constant.Constants
import util.FileUtil
import walker.DirectoryWalker

import java.nio.file.{Files, Path}
import java.security.MessageDigest
import java.time.LocalDateTime

object CommitCreator {
  var treeForFiles: Map[String,Path] = Map[String,Path]() // храним по хешу путь
   var listEmptyDirectories: List[Path] = List[Path]() // храним по хешу название + путь

   def createCommit(name:String):Unit = {
    val exceptions = DirectoryWalker.walkDirectory(Constants.currentDir, makeBlob, addDirectoryInTree)
    for (e <- exceptions ){
      MyConsole.println(e.getMessage)
    }
     val  commit =  Commit( LocalDateTime.now(),Constants.currentDir.getFileName.toString,treeForFiles, listEmptyDirectories )
    CommitSerializer.serializeCommit(name,commit)
  }
  private def makeBlob(path:Path):Unit = {

    val fileContent: Array[Byte] = Files.readAllBytes(path)
    val hashString = FileUtil.receiveHash(fileContent)
    if (!treeForFiles.contains(hashString)) {
      BlobSerializer.serialize(hashString, fileContent)
    }
    treeForFiles = treeForFiles + (hashString -> Constants.currentDir.relativize(path))

  }
    // Создаем и возвращаем Blob
  private def addDirectoryInTree(path:Path):Unit = {
    if ( !Files.list(path).iterator().hasNext) {
      listEmptyDirectories = Constants.currentDir.relativize(path) +: listEmptyDirectories
    }
  }


}
