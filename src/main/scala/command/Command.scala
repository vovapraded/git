package command

abstract class Command(protected val arg: Option[String] = None){
    def execute(): Unit 
}
