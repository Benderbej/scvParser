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
          val chList: List[String] = z.split(orgDelimiter).toList //TODO tolist remove set toset
          list = list.appended(chList.prepended(y).prepended(x))
        case _ => throw new IllegalArgumentException("invalid format")
      }
    }
    buffered.close
    list
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

  def getAllRootOrganisations(orgs: Set[Organisation]) = {
      (for (o <- orgs)
        yield
          o match {
            case org: RootOrganisation => org;
            case _                     =>
          }).toList
  }

  def getHierarchyByRootOrgName(orgs: Set[Organisation], name: String) = {

    for (o <- orgs; if o.name == name; if) yield o

  }




  def constrHier(s: Set[Organisation]) = {



    def addChilds(curParName: String, organisation: Organisation): Organisation ={
      val l: List[Organisation] = List()
      for (
          o <- s
      ) o match {
        case NotRootOrganisation(name, parent) => if(parent == curParName){
          organisation.childList.appended(addChilds(name, NotRootOrganisation(name, parent)))
        }
      }
    }







    addChilds()



  }





  def getSublist(node: Organisation) = {









    node match {

       case org: NotRootOrganisation => {

         if(org.parName == parname){

         }



       }




    }




  }

//
//  def parseLine = ???

  println(parseFileToSeq)

  println(trasformData(parseFileToSeq, Set()))

  println(getAllRootOrganisations(trasformData(parseFileToSeq, Set())))
  //lazy vals

}
