package models

import scala.xml.NodeSeq

trait IBaseSendMessage {
  def toXml:NodeSeq

}
