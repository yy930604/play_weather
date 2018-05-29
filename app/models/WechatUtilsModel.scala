package models

// https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140183

case class WechatAccessToken(
                              access_token:String,
                              expires_in:Int
                            )

case class WechatErrorMessage(
                               errcode:Int,
                               errmsg:String
                             )