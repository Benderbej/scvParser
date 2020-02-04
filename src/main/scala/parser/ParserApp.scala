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
object ParserApp extends App {

  val delimiter = ","
  val orgDelimiter = "\\|"
  val orgSet: Set[Organisation] = Set()

  def getCSVsrc = io.Source.fromFile("/home/benderbej/projects/csv/orgs.csv")

  def parseFileToSeq = {

    val buffered = getCSVsrc
    var list: List[List[String]] = List()
//    val bufferedWithoutHeader = buffered.
    for (line <- buffered.getLines) {
      val cols: Seq[String] = line.split(delimiter).map(_.trim).toList
      cols match {
        case x :: y :: Nil => list = list.appended(x :: y :: Nil)
        case x :: y :: z :: Nil =>
          val chList: List[String] = z.split(orgDelimiter).toList
          list = list.appended(chList.prepended(y).prepended(x))
        case _ => throw new IllegalArgumentException("invalid format")
      }
    }
    buffered.close
    list
  }

  def trasformData(src: List[List[String]], res: Set[Organisation]): Set[
    Organisation
  ] = { //we know that this root exists and look for it to add new node

//    def addOrganisationToSet(orgSet: Set[Organisation], parOrgName: String, chOrgName: String): Set[Organisation] = {
////      if(orgSet.)
//      for {
//        org <- orgSet
//
//        if org.name != parOrgName
//
//      } println(orgName) //yield s
//      s match {
//        case
//        case
//      }
//    }

    def getSetFromChildsList(childs: List[String],
                             parName: String): Set[Organisation] = {
      val r = for {
        c <- childs
      } yield NotRootOrganisation(c, parName)
      r.toSet
    }

    src match {

      case Nil => res
      case head :: tail => {

        head match {
          case name :: "" :: childs => { //root with childs
//            val orgs = for {
//              c <- childs
//            } yield NotRootOrganisation(c, name)
//            val orgs = getSetFromChildsList(childs, name)
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
            ) //node without childs
          case name :: parent :: childs =>
            trasformData(
              tail,
              res
                .concat(Set(NotRootOrganisation(name, parent)))
                .concat(getSetFromChildsList(childs, name))
            ) //node with childs
          case name :: "" :: Nil =>
            trasformData(tail, res.concat(Set(RootOrganisation(name)))) //root without childs

        }
//        trasformData(tail, res)
      }

    }

  }

//  def
//
//
//  def findAndAddChild = ???
//
//  def getAllRootOrganisations = ???
//
//  def getHierarchyByRootOrgName = ???
//
//  def parseLine = ???

  println(parseFileToSeq)

  println(trasformData(parseFileToSeq, Set()))

}
//cols match {
//  case x :: "" :: tail => x.addToRoot(tail)
//  case x :: "" :: "" => x.addToRootNoChilds(tail)
//  case x :: y :: "" => y.addChildToRoot(x)
//  case x :: y :: tail => x.addToRoot(List(y)); addChilds(x, tail, )
//  //case _ exception -error in format
//}
