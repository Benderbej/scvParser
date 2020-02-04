package parser

trait Usable {

  def getAllRootOrganisations: List[Organisation]

  def getHierarchyByRootOrgName

}
