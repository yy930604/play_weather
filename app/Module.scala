import com.google.inject.AbstractModule
import java.time.Clock

import daos.{IImageDAO, IWechatAccessTokenDAO, ImageDAO, WechatAccessTokenDAO}
import services._

/**
 * This class is a Guice module that tells Guice how to bind several
 * different types. This Guice module is created when the Play
 * application starts.

 * Play will automatically use any class called `Module` that is in
 * the root package. You can create modules in other locations by
 * adding `play.modules.enabled` settings to the `application.conf`
 * configuration file.
 */
class Module extends AbstractModule {

  override def configure() = {
    // Use the system clock as the default implementation of Clock
    bind(classOf[Clock]).toInstance(Clock.systemDefaultZone)
    // Ask Guice to create an instance of ApplicationTimer when the
    // application starts.
    // Set AtomicCounter as the implementation for Counter.
    bind(classOf[IWechatAccessTokenDAO]).to(classOf[WechatAccessTokenDAO])


    bind(classOf[ITextMesaageService]).to(classOf[TextMessageService])
    bind(classOf[IImageMessageService]).to(classOf[ImageMessageService])
    bind(classOf[ISubscribeEventService]).to(classOf[SubscribeEventService])
    bind(classOf[IImageDAO]).to(classOf[ImageDAO])
//    bind(classOf[IClickEventService]).to(classOf[ClickEventService])



  }

}
