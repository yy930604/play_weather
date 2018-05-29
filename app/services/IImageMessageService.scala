


package services

import java.time.{ZoneOffset, ZonedDateTime}
import javax.inject.Inject


import models.{IBaseSendMessage, ReceiveImageMessage, SendTextMessage}
import play.api.cache.SyncCacheApi

import scala.util.Try

trait IImageMessageService extends IBaseInteractionService[ReceiveImageMessage,IBaseSendMessage]{

  override def receiveAndSend(receiveImageMessage: ReceiveImageMessage):Try[IBaseSendMessage]


}



class ImageMessageService @Inject()(
                                     cacheSyncApi:SyncCacheApi
                                   )  extends IImageMessageService {

  private val logger = play.api.Logger(this.getClass)

  override def receiveAndSend(receiveImageMessage: ReceiveImageMessage): Try[SendTextMessage] = {

    cacheSyncApi.set(receiveImageMessage.from_user_name,receiveImageMessage.media_id)
    val getCache = cacheSyncApi.get[String](receiveImageMessage.from_user_name)

    println(getCache)

    logger.debug(s"mediaid=$getCache")

    Try{
      SendTextMessage(
        to_user_name = receiveImageMessage.from_user_name,
        from_user_name = receiveImageMessage.to_user_name,
        create_time = ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond,
        content = "我会好好收藏的，❤️"
      )
    }
  }
}



