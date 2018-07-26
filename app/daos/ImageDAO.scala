package daos


import javax.inject.Inject
import models.Images
import play.api.Configuration
import play.api.i18n.{Lang, Langs, MessagesApi}

import scala.util.{Failure, Success, Try}
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}
import play.filters.cors.CORSConfig.Origins.All

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

trait IImageDAO extends IBaseDAO[Images]{

  def insert(images: Images):Try[Images]

  def find():Option[List[Images]]


}

class ImageDAO@Inject()(
                         conf: Configuration,
                         messagesApi: MessagesApi,
                         langs:Langs

                       ) extends IImageDAO {

  implicit val lang: Lang = langs.availables.head


  private val mongoClient:MongoClient = MongoClient(conf.get[String]("tongues.mongodb.uri"))

  val codecRegistry = fromRegistries(fromProviders(classOf[Images]), DEFAULT_CODEC_REGISTRY )

  private val mongoDatabase : MongoDatabase = mongoClient.getDatabase(conf.get[String]("tongues.mongodb.db.name")).withCodecRegistry(codecRegistry)

  private val tongues : MongoCollection[Images] = mongoDatabase.getCollection(conf.get[String]("tongues.tongue_images.mongodb.collection.name"))


  def insert(tongueForSave:Images):Try[Images] = {


    Try{

      Await.result(tongues.insertOne(

        tongueForSave

      ).head,5 seconds)

    }  match {
      case Success(e) => Success(tongueForSave)
      case Failure(e) => Failure(e)
    }
  }

  def find():Option[List[Images]] = ???

}


