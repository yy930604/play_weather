package controllers

import javax.inject._

//import models.TongueUser
import play.api.mvc._

import scala.xml.NodeSeq
import services._
import play.api.Configuration
@Singleton
class WechatController @Inject()(
                                  cc: ControllerComponents,
                                  textService:ITextMesaageService,
                                  imageService:IImageMessageService,
                                  subscribeEventService:ISubscribeEventService,
                                  config:Configuration
                                ) extends AbstractController(cc) {

  private val logger = play.api.Logger(this.getClass)


  def validServer(signature:String,timestamp:String,nonce:String,echostr:String) =Action{

    val selfToken = config.get[String]("wechat.token")
    val combineString = Array(selfToken,timestamp,nonce).sorted.mkString
    println(combineString)
    val md = java.security.MessageDigest.getInstance("SHA-1")

    val ha =md.digest(combineString.getBytes("UTF-8")).map("%02x".format(_)).mkString
    Ok(echostr)
  }

  def receiveMessageAndEventExample = Action(parse.xml){ implicit request =>


    request.body.\("MsgType").text match{

      case "text" =>{
        logger.debug("recieve a text message")
        val textMessageInstance  = models.ReceiveTextMessage(request.body)
        val sendMessageInstance = textService.receiveAndSend(textMessageInstance)
        logger.debug(s"text_接收到的消息 = $sendMessageInstance")
        sendMessageInstance.fold(
          error => Ok(""),
          sendMessage => {
            Ok(sendMessage.toXml)
          }
        )
      }

      case "image" =>{
        logger.debug("recieve a image message")
        val imageMessageInstance = models.ReceiveImageMessage(request.body)
        imageService.receiveAndSend(imageMessageInstance).fold(
          error => Ok(""),
          sendMessage => Ok(sendMessage.toXml)
        )
      }

      case "event" =>{

        request.body.\("Event").text match {

          case "subscribe" =>{
            logger.debug("recieve a event message")
            val subscribeEventInstance = models.ReceiveSubscribeEvent(request.body)
            println(subscribeEventInstance)  //在playserver中檢視用戶傳來的元素
            subscribeEventService.receiveAndSend(subscribeEventInstance).fold(
              error => Ok("亲，订阅失败，请在关注一次。"),
              sendMessage => {
                val test = sendMessage.toXml
                //                logger.debug(s"event_接收到的消息回覆 = $test")
                Ok(sendMessage.toXml)
              }
            )
          }

          case _ => {
            logger.debug("recieve a strange message")
            Ok("亲，有什么事吗？")
          }

        }
      }
    }
  }





}
