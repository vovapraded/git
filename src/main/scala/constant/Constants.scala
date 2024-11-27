package constant

import java.nio.file.{Path, Paths}

object Constants {
  val currentDir: Path = Paths.get(".").toAbsolutePath.normalize() // Получаем текущую рабочую директорию
  val myGitDir: Path = currentDir.resolve(".myGit").normalize()
}
