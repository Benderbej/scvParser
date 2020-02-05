package parser

trait Usable {

  def getAllRootOrganisations(orgs: Set[Organisation],
                              res: List[Organisation]): List[Organisation]

  def getHierarchyByRootOrgName(orgs: Set[Organisation],
                                name: String): Organisation

}
