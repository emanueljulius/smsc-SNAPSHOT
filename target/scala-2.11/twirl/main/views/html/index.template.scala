
package views.html

import play.twirl.api._
import play.twirl.api.TemplateMagic._

import play.api.templates.PlayMagic._
import models._
import controllers._
import java.lang._
import java.util._
import scala.collection.JavaConversions._
import scala.collection.JavaConverters._
import play.api.i18n._
import play.core.j.PlayMagicForJava._
import play.mvc._
import play.data._
import play.api.data.Field
import play.mvc.Http.Context.Implicit._
import views.html._

/**/
object index extends BaseScalaTemplate[play.twirl.api.HtmlFormat.Appendable,Format[play.twirl.api.HtmlFormat.Appendable]](play.twirl.api.HtmlFormat) with play.twirl.api.Template1[String,play.twirl.api.HtmlFormat.Appendable] {

  /**/
  def apply/*1.2*/(message: String):play.twirl.api.HtmlFormat.Appendable = {
      _display_ {

Seq[Any](format.raw/*1.19*/("""

"""),_display_(/*3.2*/main("Welcome to SMSC")/*3.25*/ {_display_(Seq[Any](format.raw/*3.27*/("""

  """),format.raw/*5.3*/("""<section>
      <p>SMSC</p>
  </section>

""")))}),format.raw/*9.2*/("""
"""))}
  }

  def render(message:String): play.twirl.api.HtmlFormat.Appendable = apply(message)

  def f:((String) => play.twirl.api.HtmlFormat.Appendable) = (message) => apply(message)

  def ref: this.type = this

}
              /*
                  -- GENERATED --
                  DATE: Wed May 15 10:37:00 EAT 2019
                  SOURCE: C:/Users/EMA-GREEN/Documents/Play stuff/smsc/app/views/index.scala.html
                  HASH: 9160df6689d1e85c0a53e1a41f9e1a2158dfbb1c
                  MATRIX: 723->1|828->18|856->21|887->44|926->46|956->50|1028->93
                  LINES: 26->1|29->1|31->3|31->3|31->3|33->5|37->9
                  -- GENERATED --
              */
          