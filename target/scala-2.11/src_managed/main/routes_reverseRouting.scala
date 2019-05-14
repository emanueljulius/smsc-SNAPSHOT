// @SOURCE:C:/Users/EMA-GREEN/Documents/Play stuff/smsc/conf/routes
// @HASH:3483d35a1b7b66396fb0cbc684a39e8a50240db4
// @DATE:Tue May 14 12:50:28 EAT 2019

import Routes.{prefix => _prefix, defaultPrefix => _defaultPrefix}
import play.core._
import play.core.Router._
import play.core.Router.HandlerInvokerFactory._
import play.core.j._

import play.api.mvc._
import _root_.controllers.Assets.Asset
import _root_.play.libs.F

import Router.queryString


// @LINE:11
// @LINE:8
// @LINE:6
package controllers {

// @LINE:11
class ReverseAssets {


// @LINE:11
def at(file:String): Call = {
   implicit val _rrc = new ReverseRouteContext(Map(("path", "/public")))
   Call("GET", _prefix + { _defaultPrefix } + "assets/" + implicitly[PathBindable[String]].unbind("file", file))
}
                        

}
                          

// @LINE:8
// @LINE:6
class ReverseApplication {


// @LINE:6
def index(): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix)
}
                        

// @LINE:8
def callBack(msisdn:String, message:String): Call = {
   import ReverseRouteContext.empty
   Call("GET", _prefix + { _defaultPrefix } + "agri-smsc/callback/" + implicitly[PathBindable[String]].unbind("msisdn", dynamicString(msisdn)) + "/" + implicitly[PathBindable[String]].unbind("message", dynamicString(message)))
}
                        

}
                          
}
                  


// @LINE:11
// @LINE:8
// @LINE:6
package controllers.javascript {
import ReverseRouteContext.empty

// @LINE:11
class ReverseAssets {


// @LINE:11
def at : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Assets.at",
   """
      function(file) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "assets/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("file", file)})
      }
   """
)
                        

}
              

// @LINE:8
// @LINE:6
class ReverseApplication {


// @LINE:6
def index : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.index",
   """
      function() {
      return _wA({method:"GET", url:"""" + _prefix + """"})
      }
   """
)
                        

// @LINE:8
def callBack : JavascriptReverseRoute = JavascriptReverseRoute(
   "controllers.Application.callBack",
   """
      function(msisdn,message) {
      return _wA({method:"GET", url:"""" + _prefix + { _defaultPrefix } + """" + "agri-smsc/callback/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("msisdn", encodeURIComponent(msisdn)) + "/" + (""" + implicitly[PathBindable[String]].javascriptUnbind + """)("message", encodeURIComponent(message))})
      }
   """
)
                        

}
              
}
        


// @LINE:11
// @LINE:8
// @LINE:6
package controllers.ref {


// @LINE:11
class ReverseAssets {


// @LINE:11
def at(path:String, file:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Assets.at(path, file), HandlerDef(this.getClass.getClassLoader, "", "controllers.Assets", "at", Seq(classOf[String], classOf[String]), "GET", """ Map static resources from the /public folder to the /assets URL path""", _prefix + """assets/$file<.+>""")
)
                      

}
                          

// @LINE:8
// @LINE:6
class ReverseApplication {


// @LINE:6
def index(): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.index(), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "index", Seq(), "GET", """ Home page""", _prefix + """""")
)
                      

// @LINE:8
def callBack(msisdn:String, message:String): play.api.mvc.HandlerRef[_] = new play.api.mvc.HandlerRef(
   controllers.Application.callBack(msisdn, message), HandlerDef(this.getClass.getClassLoader, "", "controllers.Application", "callBack", Seq(classOf[String], classOf[String]), "GET", """""", _prefix + """agri-smsc/callback/$msisdn<[^/]+>/$message<[^/]+>""")
)
                      

}
                          
}
        
    