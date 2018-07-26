package daos

import models.WechatAccessToken


import scala.util.{Try,Success,Failure}


trait IWechatAccessTokenDAO extends IBaseDAO[WechatAccessToken]{

  def refreshTokenSync:Try[Unit]

  def getTokenSync:Option[String]


}

import javax.inject.Inject
import play.api.libs.ws.WSClient
import play.api.Configuration
import play.api.libs.json.Json

import play.api.cache.SyncCacheApi
import play.api.cache.AsyncCacheApi
import scala.concurrent.duration._
import scala.concurrent.Await

import scala.concurrent.ExecutionContext.Implicits.global

class WechatAccessTokenDAO @Inject()(
                                      ws:WSClient,
                                      config:Configuration,
                                      cacheSyncApi:SyncCacheApi,
                                      cacheAsyncApi:AsyncCacheApi
                                    )extends IWechatAccessTokenDAO {

  private val wechatAppId= config.get[String]("wechat.appid")
  private val wechatAppSecret= config.get[String]("wechat.appsecret")
  private val logger = play.api.Logger(this.getClass)
  private implicit val wechatAccessTokenJsonFormat =   Json.format[WechatAccessToken]

  override def refreshTokenSync: Try[Unit] = {

    val responseFuture = ws.url("https://api.weixin.qq.com/cgi-bin/token").withQueryStringParameters(
      ("grant_type","client_credential"),
      ("appid",wechatAppId),
      ("secret",wechatAppSecret)
    ).get()

    val setTokenIntoCacheFuture = responseFuture.map{ response =>
      Json.fromJson[WechatAccessToken](Json.parse(response.body))(wechatAccessTokenJsonFormat).fold(
        errorInstance => {
          logger.info(errorInstance.toString())
          logger.error("Wechat異常或網路異常")
          Failure[Unit](new Exception("Wechat異常或網路異常"))
        },
        tokenInstance =>{
          cacheSyncApi.set("wechat_access_token",tokenInstance.access_token,7000 seconds)
          Success[Unit](println(""))
        }
      )
    }

    Await.result(setTokenIntoCacheFuture, 3 second)

  }

  override def getTokenSync: Option[String] = {

    cacheSyncApi.get[String]("wechat_access_token").fold(
      ifEmpty = {
        refreshTokenSync.fold(
          failure => None,
          success => getTokenSync
        )
      }
    ){ token =>
      Option(token)
    }
  }


}

