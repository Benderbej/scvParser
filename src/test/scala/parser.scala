import org.scalatest.{Matchers, WordSpec}
import parser.{NotRootOrganisation, ParserApp, RootOrganisation}

class ParserAppTest extends WordSpec with Matchers {
  {

    val srcList = List(
      List("l0", "", "l1", "l2", "l3"),
      List("l1", "l0"),
      List("l2", "l0", "l4", "l5"),
      List("l3", "l0"),
      List("l4", "l2"),
      List("l5", "l2"),
      List("n1", "", "n2", "n3"),
      List("n2", "n1"),
      List("n3", "n1")
    )

    "ParserAppTest" when {
      "getAllRootOrganisations" should {
        "get list of roots" in {
          ParserApp.getAllRootOrganisations(
            ParserApp.trasformData(srcList, Set()),
            List()
          ) shouldBe (
            List(RootOrganisation("l0", List()), RootOrganisation("n1", List()))
          )
        }
      }
      "getHierarchyByRootOrgName" should {
        "get hierarchy from one node" in {
          ParserApp.getHierarchyByRootOrgName(
            ParserApp.trasformData(srcList, Set()),
            "l0"
          ) shouldBe (
            RootOrganisation(
              "l0",
              List(
                NotRootOrganisation("l1", "l0", List()),
                NotRootOrganisation(
                  "l2",
                  "l0",
                  List(
                    NotRootOrganisation("l4", "l2", List()),
                    NotRootOrganisation("l5", "l2", List())
                  )
                ),
                NotRootOrganisation("l3", "l0", List())
              )
            )
          )
        }
      }
    }
  }
}
