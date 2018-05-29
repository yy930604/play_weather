package models

import scala.xml.NodeSeq

case class SendTextMessage(
                            to_user_name:String,
                            from_user_name:String,
                            create_time:Long,
                            msg_type:String = "text",
                            content:String
                          ) extends IBaseSendMessage {
  override def toXml: NodeSeq = {
    <xml>
      <ToUserName>{to_user_name}</ToUserName>
      <FromUserName>{from_user_name}</FromUserName>
      <CreateTime>{create_time}</CreateTime>
      <MsgType>{msg_type}</MsgType>
      <Content>{content}</Content>
    </xml>
  }
}
