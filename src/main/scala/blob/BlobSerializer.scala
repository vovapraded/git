package blob
import constant.Constants

import java.io.{FileOutputStream, IOException}
import java.nio.file.Files
import java.util.zip.GZIPOutputStream
object BlobSerializer {
    def serialize(hashString: String, content: Array[Byte]): Unit = {
        // Создаем путь для файла с названием hashString
        val filePath = Constants.myGitDir.resolve(hashString)

        // Создаем родительскую директорию, если она не существует
        if (!Files.exists(Constants.myGitDir)) {
          Files.createDirectory(Constants.myGitDir)
        }

        // Открываем поток для записи в сжатый файл (GZIP)
        val fos = new FileOutputStream(filePath.toFile)
        val gzipOS = new GZIPOutputStream(fos)

        // Записываем сжатый контент в файл
        gzipOS.write(content)

        // Закрываем потоки
        gzipOS.close()
        fos.close()
      }
    }  

