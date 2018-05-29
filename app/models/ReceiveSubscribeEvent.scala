package models

import scala.xml.NodeSeq

case class ReceiveSubscribeEvent(
                                  to_user_name:String,
                                  from_user_name:String,
                                  create_time:Long,      //当用户订阅我们的公眾号，playserver会接收到的参数
                                  msg_type:String,
                                  event:String,
                                  event_key:Option[String]=None,  //如果没被定义，这个就為none
                                  //?
                                  ticket:Option[String]=None
                                ) extends IBaseReceiveEvent



object ReceiveSubscribeEvent {

  def apply(xml:NodeSeq):ReceiveSubscribeEvent ={
    new ReceiveSubscribeEvent(
      to_user_name = xml.\("ToUserName").text,
      from_user_name = xml.\("FromUserName").text,
      create_time = xml.\("CreateTime").text.toLong,
      msg_type = xml.\("MsgType").text,
      event = xml.\("Event").text,
      event_key = xml.\("EventKey").headOption.map[String]{node => node.text},
      ticket = xml.\("Ticket").headOption.map{node => node.text}
    )
  }

}
