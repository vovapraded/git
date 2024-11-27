package commit

import java.nio.file.Path
import java.time.LocalDateTime

case class Commit(
 time: LocalDateTime, // храним временную метку
 treeForFiles: Map[String, Path] = Map[String,Path](), // храним по хешу название + путь
 listEmptyDirectories: List[Path] = List[Path]() 
)
