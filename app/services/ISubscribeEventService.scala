package services

import java.time.{ZoneOffset, ZonedDateTime}

import daos.IWechatAccessTokenDAO
import javax.inject.Inject
import models._
import play.api.Configuration
import play.api.libs.ws.WSClient

import scala.util.Try


trait ISubscribeEventService extends IBaseInteractionService[ReceiveSubscribeEvent,IBaseSendMessage]{

  override def receiveAndSend(receiveSubscribeEvent: ReceiveSubscribeEvent): Try[IBaseSendMessage]

}

class SubscribeEventService @Inject()(
                                       tokenDAO: IWechatAccessTokenDAO,
                                       config:Configuration,
                                       wsClient:WSClient,
                                     )extends ISubscribeEventService {

   private val logger =play.api.Logger(this.getClass)

  override def receiveAndSend(receiveSubscribeEvent: ReceiveSubscribeEvent): Try[IBaseSendMessage] ={

    logger.debug(s"k=======${receiveSubscribeEvent.to_user_name}")

    Try{
      SendTextMessage(
        to_user_name = receiveSubscribeEvent.from_user_name,
        from_user_name = receiveSubscribeEvent.to_user_name,
        create_time = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond,
        content = "终于等到你，么么哒，如果你想发图片我会好好珍藏的，笔芯❤，发送地方我会返回天气哦，如果下雨 要记得带伞哦，🐱🐱🐱️"
      )
    }
  }

}
