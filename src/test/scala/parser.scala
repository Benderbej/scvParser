import org.scalatest.{Matchers, WordSpec}
import parser.{Organisation, ParserApp, RootOrganisation}

class ParserAppTest extends WordSpec with Matchers {
  {

    val path = "/home/benderbej/projects/csv/orgs2.csv"
    val parsed = ParserApp.parseFileToSeq(path)

    "ParserAppTest" when {
      "getAllRootOrganisations" should {
        "get list of roots" in {
          ParserApp.getAllRootOrganisations(
            ParserApp.trasformData(parsed, Set()),
            List()
          ) shouldBe (
            List(RootOrganisation("l0", List()), RootOrganisation("n1", List()))
          )
        }
      }
      "getHierarchyByRootOrgName" should {
        "get hierarchy from one node" in {
//        ParserApp.getHierarchyByRootOrgName()
        }
      }
    }
  }

}
//  def getAllRootOrganisations(orgs: Set[Organisation],
//                              res: List[Organisation]): List[Organisation]
//
//  def getHierarchyByRootOrgName(orgs: Set[Organisation],
//                                name: String): Organisation
