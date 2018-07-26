


package services

import java.nio.file.Paths
import java.time.{ZoneOffset, ZonedDateTime}
import java.util.Base64

import daos.{ImageDAO, WechatAccessTokenDAO}
import javax.inject.Inject
import models.{IBaseSendMessage, Images, ReceiveImageMessage, SendTextMessage}
import play.api.cache.SyncCacheApi
import play.api.libs.ws.WSClient

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.util.Try
import scala.concurrent.ExecutionContext.Implicits.global

trait IImageMessageService extends IBaseInteractionService[ReceiveImageMessage,IBaseSendMessage]{

  override def receiveAndSend(receiveImageMessage: ReceiveImageMessage):Try[IBaseSendMessage]


}



class ImageMessageService @Inject()(
                                     imageDAO: ImageDAO,
                                     ws:WSClient,
                                     cacheSyncApi:SyncCacheApi,
                                     wechatAccessToken:WechatAccessTokenDAO
                                   )  extends IImageMessageService {

  private val logger = play.api.Logger(this.getClass)

  override def receiveAndSend(receiveImageMessage: ReceiveImageMessage): Try[SendTextMessage] = {

    cacheSyncApi.set(receiveImageMessage.from_user_name,receiveImageMessage.media_id)
    val getCache = cacheSyncApi.get[String](receiveImageMessage.from_user_name)

    logger.debug(s"mediaid=$getCache")


    val wechatToken = wechatAccessToken.getTokenSync.get


    val media_response = ws.url("https://api.weixin.qq.com/cgi-bin/media/get").withQueryStringParameters(

      ("access_token", wechatToken),
      ("media_id",receiveImageMessage.media_id)

    ).get()

    val media_base64 = media_response.map( media_response =>

      Base64.getEncoder.encodeToString(media_response.body[Array[Byte]])

    )

    val base64 = Await.result(media_base64,5 seconds)

    val pic_user_id = receiveImageMessage.from_user_name


//    println(pic_user_id)

//    println(base64)


    val tongueForSave = Images.apply(

      pic_user_id = pic_user_id,

      pic_base64 = base64

    )

    imageDAO.insert(tongueForSave)


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



