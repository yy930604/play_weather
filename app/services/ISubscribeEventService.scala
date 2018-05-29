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
                                       wsClient:WSClient
                                     )extends ISubscribeEventService {

  override def receiveAndSend(receiveSubscribeEvent: ReceiveSubscribeEvent): Try[IBaseSendMessage] ={

    Try{
      SendTextMessage(
        to_user_name = receiveSubscribeEvent.from_user_name,
        from_user_name = receiveSubscribeEvent.to_user_name,
        create_time = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond,
        content = "终于等到你，么么哒，如果你想发图片我会好好珍藏的，笔芯❤，如果下雨 要记得带伞哦，🐱🐱🐱️"
      )


    }


  }

}
