package blob
import console.MyConsole
import constant.Constants

import java.io.{FileInputStream, FileOutputStream, IOException}
import java.nio.file.{Files, Path}
import java.util.zip.GZIPInputStream

object BlobUnpacker {

  def unpackBlob(hashString: String, targetPath: Path): Boolean = {
    // Формируем путь к сжатому файлу по хешу
    val filePath = Constants.myGitDir.resolve(hashString)

    // Проверяем существует ли файл
    if (Files.exists(filePath)) {
      var fis: FileInputStream = null
      var gzipIS: GZIPInputStream = null
      var fos: FileOutputStream = null
      try {
        // Создаем путь к целевому файлу (распакованный файл)
        if (targetPath.getParent != null) {
          Files.createDirectories(targetPath.getParent)
        }
        // Открываем потоки для чтения и записи
        val fis = new FileInputStream(filePath.toFile)
        val gzipIS = new GZIPInputStream(fis)
        val fos = new FileOutputStream(targetPath.toFile)

        // Буфер для чтения данных, размер буфера 1024 байта
        val buffer = new Array[Byte](1024)  // Опреддля чтения
        var bytesRead = gzipIS.read(buffer) // Считываем данные из сжатого потока

        // Читаем и распаковываем данные из GZIP
        while (bytesRead != -1) {
          fos.write(buffer, 0, bytesRead)  // Записываем в файл
          bytesRead = gzipIS.read(buffer)  // Чтение следующего блока данных
        }

        // Закрываем потоки
        gzipIS.close()
        fis.close()
        fos.close()

        MyConsole.println(s"Файл успешно распакован в: $targetPath")
        true
      }  catch {
      case e: IOException =>
        MyConsole.println(s"Ошибка при распаковке файла: ${e.getMessage}")
        false
    } finally {
      // Закрытие потоков, если они были открыты
      if (fis != null) fis.close()
      if (gzipIS != null) gzipIS.close()
      if (fos != null) fos.close()
    }
  } else {
    MyConsole.println(s"Файл для хеша $hashString не найден.")
    false
  }
  }
}