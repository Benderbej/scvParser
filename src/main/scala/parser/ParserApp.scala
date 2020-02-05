package parser

/**
  * Написать без сторонних библиотек парсер файла csv(scala only), организовать хранение в памяти и реализовать следующие методы:
  *  - получить список всех корневых организаций
  *  - получить иерархическую структуру для организации по корневому имени
  *
  * В файле формата csv хранятся данные о структуре организаций (иерархическая структура).
  * name - имя подразделения, уникальное.
  * parent - ссылка на родительское подразделение (name).
  * children - дочерние подразделения (name), разделены символом “|”.
  * пример csv файла:
  *
  * name,parent,children
  * l0,,l1|l2|l3
  * l1,l0,
  * l2,l0,l4|l5
  * l3,l0,
  * l4,l2,
  * l5,l2,
  * n1,,n2|n3
  * n2,n1,
  * n3,n1,
  */
object ParserApp extends App with Usable {

  val delimiter = ","
  val orgDelimiter = "\\|"
  val orgSet: Set[Organisation] = Set()

  val src = getCSVsrc(args.head)

//  def getCSVsrc = io.Source.fromFile("/home/benderbej/projects/csv/orgs2.csv")
  def getCSVsrc(path: String) = io.Source.fromFile(path)

  args.head
  def parseFileToSeq = {

    val buffered = src
    var list: List[List[String]] = List()
    for (line <- buffered.getLines) {
      val cols: Seq[String] = line.split(delimiter).map(_.trim).toList
      cols match {
        case x :: y :: Nil => list = list.appended(x :: y :: Nil)
        case x :: y :: z :: Nil =>
          val chList: List[String] = z.split(orgDelimiter).toList //TODO tolist remove set toset
          list = list.appended(chList.prepended(y).prepended(x))
        case _ => throw new IllegalArgumentException("invalid format")
      }
    }
    buffered.close
    list.tail
  }

  def trasformData(src: List[List[String]],
                   res: Set[Organisation]): Set[Organisation] = {

    def getSetFromChildsList(childs: List[String],
                             parName: String): Set[Organisation] = {
      val r = for {
        c <- childs
      } yield NotRootOrganisation(c, parName)
      r.toSet //TODO remove
    }

    src match {
      case Nil => res
      case head :: tail => {
        head match {
          case name :: "" :: childs => {
            trasformData(
              tail,
              res
                .concat(getSetFromChildsList(childs, name).toSet)
                .concat(Set(RootOrganisation(name)))
            )
          }
          case name :: parent :: Nil =>
            trasformData(
              tail,
              res.concat(Set(NotRootOrganisation(name, parent)))
            )
          case name :: parent :: childs =>
            trasformData(
              tail,
              res
                .concat(Set(NotRootOrganisation(name, parent)))
                .concat(getSetFromChildsList(childs, name))
            )
          case name :: "" :: Nil =>
            trasformData(tail, res.concat(Set(RootOrganisation(name))))
        }
      }
    }
  }

  def getAllRootOrganisations(orgs: Set[Organisation],
                              res: List[Organisation]): List[Organisation] = {

    if (orgs.nonEmpty) {
      orgs.head match {
        case org: RootOrganisation =>
          getAllRootOrganisations(orgs.tail, res.prepended(org))
        case _: NotRootOrganisation =>
          getAllRootOrganisations(orgs.tail, res)
      }
    } else {
      res
    }
  }

  def getHierarchyByRootOrgName(s: Set[Organisation], startOrg: String) = {

    def addChilds(orgSet: Set[Organisation],
                  parOrganisation: Organisation): Organisation = {
      if (orgSet.nonEmpty) {
        orgSet.head match {
          case org: NotRootOrganisation =>
            if (org.parName == parOrganisation.name) {
              parOrganisation match {
                case pOrg: NotRootOrganisation => {
                  addChilds(
                    orgSet.tail,
                    NotRootOrganisation(
                      pOrg.name,
                      pOrg.parName,
                      pOrg.childList.appended(addChilds(s, org))
                    )
                  )
                }
                case pOrg: RootOrganisation => {
                  addChilds(
                    orgSet.tail,
                    RootOrganisation(
                      pOrg.name,
                      pOrg.childList.appended(addChilds(s, org))
                    )
                  )
                }
              }
            } else {
              addChilds(orgSet.tail, parOrganisation)
            }
          case _: RootOrganisation => addChilds(orgSet.tail, parOrganisation)
        }
      } else {
        parOrganisation
      }

    }
    addChilds(s, RootOrganisation(startOrg))
  }

  val data = trasformData(parseFileToSeq, Set());

  println("SET OF ORGS(NAME, ROOT INFO ONLY)")
  println(data)
  println()

  println("SET OF ROOT ORGS")
  println(getAllRootOrganisations(data, List()))
  println()

  println("HIERARCHY FROM ONE")
  val h = getHierarchyByRootOrgName(data, "l0")
  println(h)

}
//RootOrganisation(l0,List(
//  NotRootOrganisation(l1,l0,List()),
//  NotRootOrganisation(l2,l0,List(
//    NotRootOrganisation(l4,l2,List()),
//    NotRootOrganisation(l5,l2,List()))),
//  NotRootOrganisation(l3,l0,List())))
