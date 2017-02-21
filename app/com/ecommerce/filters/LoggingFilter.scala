package com.ecommerce.filters

import play.api.Logger
import play.api.mvc._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.{JsNull,Json,JsString,JsValue}
import scala.concurrent.Future
import play.api.libs.iteratee._
import play.mvc.Http.RequestBody

class LoggingFilter extends EssentialFilter {
  var requestData : String = null
	def apply(nextFilter: EssentialAction) = new EssentialAction {
		def apply(requestHeader: RequestHeader): Iteratee[Array[Byte], Result] = {
      
      var startTime = System.currentTimeMillis;
      var nolog = requestHeader.headers.get("no-log")
      if(requestHeader.contentType.contains("application/json") 
          || requestHeader.contentType.contains("application/x-www-form-urlencoded")) {
        logRequest(nextFilter: EssentialAction, requestHeader: RequestHeader).map { result =>
            
              val endTime = System.currentTimeMillis
              val requestTime = endTime - startTime
              val bytesToString: Enumeratee[ Array[Byte], String ] = Enumeratee.map[Array[Byte]]{ bytes => new String(bytes) } 
              val consume: Iteratee[String,String] = Iteratee.consume[String]()
              val resultBody : Future[String] = result.body |>>> bytesToString &>> consume
              var requestJson : JsValue = Json.obj();
                        
              if(requestData != null && requestData != "") {
                requestJson = Json.parse(requestData); 
              }
              
              resultBody.map {
                body =>
                  val responseData = Json.parse(body)
                  val apiLogsJson: JsValue = Json.obj(
                    "start_time" -> startTime,
                    "end_time" -> endTime,
                    "time_taken" -> requestTime,
                    "request_params" -> Json.obj(
                        "method" -> requestHeader.method,
                        "content_type" -> requestHeader.contentType,
                        "host" -> requestHeader.host,
                        "uri" -> requestHeader.uri,
                        "ip" -> requestHeader.remoteAddress,
                        "session_token" -> requestHeader.headers.get("token"),
                        "headers" -> Json.toJson(requestHeader.headers.toMap),
                        "request_data" -> requestJson
                    ),
                    "response_params" -> Json.obj(
                        "status_code" -> result.header.status,
                        "response_data" -> responseData
                    )
                  )
                          
                  if (nolog == null || nolog == "" || nolog == None) {
                    Logger.info(apiLogsJson.toString())
                  }
                }
              
              result.withHeaders("Request-Time" -> requestTime.toString);
          }
      }
      
      else if(requestHeader.contentType.contains("multipart/form-data")) {
        nextFilter(requestHeader).map { result =>
            
              val endTime = System.currentTimeMillis
              val requestTime = endTime - startTime
              val bytesToString: Enumeratee[ Array[Byte], String ] = Enumeratee.map[Array[Byte]]{ bytes => new String(bytes) } 
              val consume: Iteratee[String,String] = Iteratee.consume[String]()
              val resultBody : Future[String] = result.body |>>> bytesToString &>> consume
              
              resultBody.map {
                body =>
                  val responseData = Json.parse(body)
                  val apiLogsJson: JsValue = Json.obj(
                    "start_time" -> startTime,
                    "end_time" -> endTime,
                    "time_taken" -> requestTime,
                    "request_params" -> Json.obj(
                        "method" -> requestHeader.method,
                        "content_type" -> requestHeader.contentType,
                        "host" -> requestHeader.host,
                        "uri" -> requestHeader.uri,
                        "ip" -> requestHeader.remoteAddress,
                        "session_token" -> requestHeader.headers.get("token"),
                        "headers" -> Json.toJson(requestHeader.headers.toMap)
                    ),
                    "response_params" -> Json.obj(
                        "status_code" -> result.header.status,
                        "response_data" -> responseData
                    )
                  )
                  
                  if (nolog == null || nolog == "" || nolog == None) {
                    Logger.info(apiLogsJson.toString())
                  } 
                }
              
              result.withHeaders("Request-Time" -> requestTime.toString);
          }
      }
      
      else {
        nextFilter(requestHeader).map { result =>
              val endTime = System.currentTimeMillis
              val requestTime = endTime - startTime
     
              result.withHeaders("Request-Time" -> requestTime.toString);
         }
      }
		}
	}
  
  def logRequest(nextA: EssentialAction, request: RequestHeader): Iteratee[Array[Byte], Result] = {

    def step(body: Array[Byte], nextI: Iteratee[Array[Byte], Result])(i: Input[Array[Byte]]):
      Iteratee[Array[Byte], Result] = i match {
        case Input.EOF =>
          val requestBody = new String(body, "utf-8")
          requestData = requestBody;
          Iteratee.flatten(nextI.feed(Input.El(requestBody.getBytes)))
        case Input.Empty =>
          Cont[Array[Byte], Result](step(body, nextI) _)
        case Input.El(e) =>
          val curBody = Array.concat(body, e)
          Cont[Array[Byte], Result](step(curBody, nextI) _)
      }

    val nextIteratee: Iteratee[Array[Byte], Result] = nextA(request)
    Cont[Array[Byte], Result](i => step(Array(), nextIteratee)(i))
  };
}