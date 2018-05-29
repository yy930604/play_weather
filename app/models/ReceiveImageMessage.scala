package models

import scala.xml.NodeSeq

case class ReceiveImageMessage(
                                to_user_name:String,
                                from_user_name:String,
                                create_time:Long,
                                msg_type:String,
                                pic_url:String,
                                media_id:String,
                                msg_id:Long
                              ) extends IBaseReceiveMessage {



}

object ReceiveImageMessage {

  def apply(xml:NodeSeq):ReceiveImageMessage ={

    new ReceiveImageMessage(
      to_user_name = xml.\("ToUserName").text,
      from_user_name = xml.\("FromUserName").text,
      create_time = xml.\("CreateTime").text.toLong,
      msg_type = xml.\("MsgType").text,
      pic_url = xml.\("PicUrl").text,
      media_id = xml.\("MediaId").text,
      msg_id = xml.\("MsgId").text.toLong
    )

  }

}
