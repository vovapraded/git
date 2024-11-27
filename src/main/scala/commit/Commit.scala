package commit

import java.nio.file.Path

case class Commit(   
 treeForFiles: Map[String, Path] = Map[String,Path](), // храним по хешу название + путь
 listEmptyDirectories: List[Path] = List[Path]() 
)
