package spring.service

import java.util.Base64

import dto.ActivationData
import utils.{EmailTemplateRenderer, MailSender}

/**
  * Created by senthil
  */
class MailService(host: String, port: Int, fromEmail: String, pass: String) {

  val mailSender = new MailSender(host, port, fromEmail, pass)

  def sendActivationEmail(token: String, email: String, verificationBaseUrl: String, template: String, subject: String, password: String): Unit ={

    val toArray = new Array[String](1)
    toArray(0) = email
    val encoder = Base64.getEncoder()
    val verificationUrl = verificationBaseUrl + "/activateUser/" + new String(encoder.encode(email.getBytes)) + "/" + new String(encoder.encode(token.getBytes))
    val activationData = new ActivationData(verificationUrl)
    activationData.setPassword(password)
    val emailTemplateRenderer = new EmailTemplateRenderer().renderTemplate(template, activationData)
    mailSender.sendMail(fromEmail,  toArray, Array[String](), subject, emailTemplateRenderer)
  }

}
