package models

import scala.xml.NodeSeq

case class ReceiveTextMessage(
                               to_user_name:String,
                               from_user_name:String,
                               create_time:Long,
                               msg_type:String,
                               content:String,
                               msg_id:Long
                             ) extends IBaseReceiveMessage {

}

object ReceiveTextMessage {

  def apply(xml:NodeSeq):ReceiveTextMessage ={

    new ReceiveTextMessage(
      to_user_name = xml.\("ToUserName").text,
      from_user_name = xml.\("FromUserName").text,
      create_time = xml.\("CreateTime").text.toLong,
      msg_type = xml.\("MsgType").text,
      content = xml.\("Content").text,
      msg_id = xml.\("MsgId").text.toLong
    )

  }

}






