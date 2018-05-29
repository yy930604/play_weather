package services

import scala.util.Try

//trait的范型可以定义两个，一个给方法参数，另一个给方法回传值
trait IBaseInteractionService[R,+S] {

  def receiveAndSend(o:R):Try[S]

}
