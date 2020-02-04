package parser

abstract class Organisation {

  val name: String
  val childList: List[Organisation]

}

case class RootOrganisation(name: String,
                            override val childList: List[Organisation] = List())
    extends Organisation {
  def apply(name: String): RootOrganisation = new RootOrganisation(name)
}

case class NotRootOrganisation(name: String,
                               parName: String,
                               override val childList: List[Organisation] =
                                 List())
    extends Organisation {
  def apply(name: String, parName: String): NotRootOrganisation =
    new NotRootOrganisation(name, parName)
}
