package services

import java.time.{ZoneOffset, ZonedDateTime}

import daos.IWechatAccessTokenDAO
import javax.inject.Inject
import models.{IBaseSendMessage, ReceiveTextMessage}
import play.api.Configuration
import play.api.libs.ws.WSClient
import models._
import play.api.libs.json.Json
import scalaj.http.Http

import scala.util.Try


trait ITextMesaageService extends IBaseInteractionService[ReceiveTextMessage,IBaseSendMessage]{

  override def receiveAndSend(receiveTextMessage: ReceiveTextMessage):Try[IBaseSendMessage]



}

class TextMessageService @Inject()(
                                     tokenDAO: IWechatAccessTokenDAO,
                                     config:Configuration,
                                     wsClient:WSClient

                                   )extends ITextMesaageService {

  private val logger = play.api.Logger(this.getClass)

  override def receiveAndSend(receiveTextMessage: ReceiveTextMessage): Try[IBaseSendMessage] = {
    logger.debug("text service start")
    val content = new String(receiveTextMessage.content.getBytes("iso-8859-1"), "UTF-8")
        logger.debug(s"content=$content")
    val request = Http(s"http://api.jisuapi.com/weather/query?appkey=8ad51b969545c6c6&city=$content").asString.body
    val json = Json.parse(request)
     json.\("status").get.as[String] match {

       case "0" => {

         val city = json.\("result").\("city").get
         val date = json.\("result").\("date").get
         val week = json.\("result").\("week").get
         val weather = json.\("result").\("weather").get
         val temp = json.\("result").\("temp").get

         Try{
           SendTextMessage(
             to_user_name = receiveTextMessage.from_user_name,
             from_user_name = receiveTextMessage.to_user_name,
             create_time = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond,
             content = s"$city,$date,$week,$weather,$temp"
           )
         }
       }

       case _ => {
         Try{
           SendTextMessage(
             to_user_name = receiveTextMessage.from_user_name,
             from_user_name = receiveTextMessage.to_user_name,
             create_time = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond,
             content = "亲，我是傲娇鬼，我只认识地名哦，啦啦啦啦"
           )
         }
       }
    }
  }
}

//al city = json.\("result").\("city").get
//val date = json.\("result").\("date").get
//val week = json.\("result").\("week").get
//val weather = json.\("result").\("weather").get
//val temp = json.\("result").\("temp").get