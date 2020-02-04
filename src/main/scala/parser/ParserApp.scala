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
  val orgDelimiter = "|"
  val orgSet: Set[Organisation] = Set()

  def getCSVsrc = io.Source.fromFile("/home/benderbej/projects/csv/orgs.csv")

//  def parseFileToSeq = {
//    val buffered = io.Source.fromFile("/home/benderbej/projects/csv/orgs.csv")
//    for (line <- buffered.getLines) {
//      val cols = line.split(",").map(_.trim)
//      val name = s"${cols(0)}"
//      val parent = s"${cols(1)}"
//      val children =
//        println(s"${cols(1)}")
//    }
//    buffered.close
//  }

  def parseFileToSeq = {


    def addChilds(str: String, childs: List[String], curNode: Organisation) = {

      curNode.childList match {
        case Nil =>
        case
      }


    }




    val buffered = getCSVsrc
//    val bufferedWithoutHeader = buffered.
    for (line <- buffered.getLines) {

      val cols: Seq[String] = line.split(",").map(_.trim).toList
      cols match {
        case x :: "" :: tail => x.addToRoot(tail)
        case x :: "" :: "" => x.addToRootNoChilds(tail)
        case x :: y :: "" => y.addChildToRoot(x)
        case x :: y :: tail => x.addToRoot(List(y)); addChilds(x, tail, )
        //case _ exception -error in format
      }


      val name = s"${cols(0)}"
      val parent = s"${cols(1)}"


      val children: Set[Organisation] = {



        println(s"${cols(1)}")
      }
    }
    buffered.close
  }


  def findAndAddChild


  def getAllRootOrganisations = ???

  def getHierarchyByRootOrgName = ???

  def parseLine = ???

  parseFileToSeq

}
