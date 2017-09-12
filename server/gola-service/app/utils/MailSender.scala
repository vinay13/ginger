package utils

import java.io.File

import org.springframework.mail.javamail.{JavaMailSenderImpl, MimeMessageHelper, MimeMessagePreparator}
import javax.mail.internet.MimeMessage

import org.springframework.core.io.FileSystemResource

/**
  * Created by senthil
  */
class MailSender(host:String, port: Int, fromEmail: String, pass: String) {
  def sendMail(from: String, emailTo: Array[String], bcc: Array[String], subject: String, message: String) {
    val mailSender = new JavaMailSenderImpl
    mailSender.setHost(host)
    mailSender.setPort(port)
    val prop = mailSender.getJavaMailProperties
    prop.put("mail.transport.protocol", "smtp")
    prop.put("mail.smtp.auth", "true")
    prop.put("mail.smtp.starttls.enable", "true")
    prop.put("mail.debug", "true")


    mailSender.setUsername(from)
    mailSender.setPassword(pass)
    mailSender.send(new MimeMessagePreparator() {
      @throws[Exception]
      def prepare(mimeMessage: MimeMessage) {
        val messageHelper = new MimeMessageHelper(mimeMessage, "UTF-8")
        messageHelper.setFrom(from, from)
        messageHelper.setTo(emailTo)
        if (bcc != null && bcc.length > 0) messageHelper.setBcc(bcc)
        messageHelper.setSubject(subject)
        messageHelper.setText(message, true)

      }
    })
  }
}
