package parser

abstract class Organisation {

  val name: String
  val childList: List[Organisation] = List()

}

case class RootOrganisation(name: String) extends Organisation {}

case class NotRootOrganisation(name: String, parName: String)
    extends Organisation {}
