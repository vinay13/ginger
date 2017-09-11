package dao

import java.security.spec.KeySpec
import java.security.{MessageDigest, NoSuchAlgorithmException, SecureRandom}
import java.util.{Base64, Date}
import javax.crypto.spec.{IvParameterSpec, PBEKeySpec, SecretKeySpec}
import javax.crypto._
import java.util.Base64.getEncoder
import java.lang.Math.abs
import javax.crypto.Cipher.ENCRYPT_MODE
import java.security.Security.addProvider


import com.nimbusds.jose.crypto.{MACSigner, MACVerifier}
import com.nimbusds.jose._
import com.nimbusds.jwt.{JWTClaimsSet, SignedJWT}
import org.slf4j.LoggerFactory.getLogger
import com.nimbusds.jose.JWEAlgorithm._
import com.nimbusds.jose.JWSHeader.Builder
import model.{SubjectClaim, UserSecurity}
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.mindrot.jbcrypt.BCrypt

/**
  * Created by senthil
  */
object JWT {

  private var message_string_key_hash: String = null
  //TODO: Make it as a Char Array and as a method instead of keeping it as string.
  private var messageKeyspec: KeySpec = null

  private val algo = "PBEWITHSHA256AND256BITAES-CBC-BC"
  private val MESSAGE_STRING_KEY: String = "Ginger , MobiGraph Solution India Pvt limited"
  private val HMAC_ALGO_FOR_TOPIC_GENERATION = "HMac-SHA1"
  private val HMAC_ALGO_FOR_CRYPT = "HMac-SHA384"

  private val ic = 150
  private val message_ic = 10

  private val logger = getLogger(JWT.getClass)

  addProvider(new BouncyCastleProvider())



  def generateJWT(emailId: String, partnerId: String, expiresAt: Date, userSecurity: UserSecurity, jtiSeed: Long = System.currentTimeMillis()): String = {
    import com.nimbusds.jose.JWSAlgorithm._
    import com.nimbusds.jose.EncryptionMethod._
    try {
      val userSecret = decrypt(userSecurity.getSecKey, userSecurity.getSecId)
      logger.info("User Secret is {}", userSecret)
      val signer = new MACSigner(userSecret.getBytes())
      val builder = new JWTClaimsSet.Builder()

      val jwtId = encryptMessage(String.valueOf(jtiSeed))
      val jwtClaimsSet = builder.subject(emailId).claim("partnerId", partnerId).claim("emailId", emailId).jwtID(jwtId).expirationTime(expiresAt).build()

      val header = new Builder(HS256).build()
      val signedJWT = new SignedJWT(header, jwtClaimsSet)
      signedJWT.sign(signer)

      signedJWT.serialize
    }
    catch {
      case e: Exception => {
        logger.error("Error while generating jwToken ", e)
        throw new RuntimeException(e)
      }
    }
  }

  def getSubject(jwtToken: String) = {
    val parse = SignedJWT.parse(jwtToken)
    val jwtClaimsSet = parse.getJWTClaimsSet
    val jWTID = jwtClaimsSet.getJWTID
    new SubjectClaim(jwtClaimsSet.getClaim("emailId").toString, jwtClaimsSet.getClaim("partnerId").toString, jwtClaimsSet.getExpirationTime)
  }

  def verifyToken(token: String, subject: SubjectClaim, userSecurity: UserSecurity): Boolean = {
    logger.info("Verifying {} ", subject)
    try {
      val secret = decrypt(userSecurity.getSecKey, userSecurity.getSecId)
      val verifier = new MACVerifier(secret)
      val before = new Date().before(subject.expiry)
      val verify = SignedJWT.parse(token).verify(verifier)
      logger.info("Verifying {} {}", verify, before)
      //logger.debug("subject verification result: {} isExpiredToken: {} , subject:{}", verify, !before, subject)
      return verify && before
    }
    catch {
      case e: Exception => {
        logger.error("Error while jwToken verification", e)
      }
    }
    false
  }

  def encryptMessage(toEncryptStr: String) = {
    if (toEncryptStr == null) null
    encrypt(toEncryptStr, generateMessageKeySpec)
  }

  private def getIV() = {
    val iv = Array(0x4d.toByte, 0x6f.toByte, 0x62.toByte, 0x69.toByte, 0x47.toByte, 0x72.toByte, 0x61.toByte, 0x70.toByte, 0x68.toByte, 0x20.toByte, 0x49.toByte, 0x6e.toByte, 0x64.toByte, 0x69.toByte, 0x61.toByte, 0x20.toByte)
    iv
  }

  private def generateKey() = {
    // "MobiGraph India Inc EncDecUtils";
    Array[Byte](0x4d.toByte, 0x6f.toByte, 0x62.toByte, 0x69.toByte, 0x47.toByte, 0x72.toByte, 0x61.toByte, 0x70.toByte, 0x68.toByte, 0x20.toByte, 0x49.toByte, 0x6e.toByte, 0x64.toByte, 0x69.toByte, 0x61.toByte, 0x20.toByte, 0x49.toByte, 0x6e.toByte, 0x63.toByte, 0x20.toByte, 0x45.toByte, 0x6e.toByte, 0x63.toByte, 0x72.toByte, 0x79.toByte, 0x70.toByte, 0x74.toByte, 0x69.toByte, 0x6f.toByte, 0x6e.toByte, 0x55.toByte, 0x74.toByte, 0x69.toByte, 0x6c.toByte, 0x73.toByte)
  }

  def decrypt(toDecryptStr: String, pin: String) = {
    import java.util.Base64.getDecoder
    import javax.crypto.Cipher.DECRYPT_MODE
    val keyspec = new PBEKeySpec((new String(generateKey)).toCharArray(), pin.getBytes(), ic);

    try{
      val instance = SecretKeyFactory.getInstance(algo);
      val secretKey = instance.generateSecret(keyspec);
      val cipher = Cipher.getInstance(algo);
      cipher.init(DECRYPT_MODE, secretKey, new IvParameterSpec(getIV()));
      val decryptedTxt = cipher.doFinal(Base64.getDecoder().decode(toDecryptStr));
      new String(decryptedTxt);
    }catch {
      case e: Exception => {
        logger.error("Error while jwToken verification", e)
        throw new RuntimeException(e)
      }
    }
  }


  def encrypt(toEncryptStr: Array[Byte], pin: String): Array[Byte] = {


    val keyspec = new PBEKeySpec(
      (new String(generateKey)).toCharArray(), pin.getBytes(), ic);
    try {
      val instance = SecretKeyFactory.getInstance(algo);
      val secretKey = instance
        .generateSecret(keyspec);
      val cipher = Cipher.getInstance(algo);
      cipher.init(ENCRYPT_MODE, secretKey, new IvParameterSpec(
        getIV()));
      cipher.doFinal(toEncryptStr);
    } catch {
      case e: Exception => {
        logger.error("Error while encryption.", e)
        throw new RuntimeException(e)
      }
    }

  }


  private def encrypt(toEncryptStr: String, keyspec: KeySpec) = {

    import java.util.Base64.getEncoder
    import javax.crypto.Cipher.ENCRYPT_MODE

    val toEncryptBytes = toEncryptStr.getBytes


    try {
      val secretKey = SecretKeyFactory.getInstance(algo).generateSecret(keyspec)
      val encoded = secretKey.getEncoded
      val cipher = Cipher.getInstance(algo)
      cipher.init(ENCRYPT_MODE, secretKey)
      var encryptedBytes = cipher.doFinal(toEncryptBytes)
      val iv = cipher.getIV
      new String(getEncoder.encode(encryptedBytes))
    }
    catch {
      case e: Any => {
        logger.error("Error while encryption.", e)
        throw new RuntimeException(e)
      }
    }

  }

  private def generateMessageKeySpec = {
    if (messageKeyspec == null) {
      if (message_string_key_hash == null) generateMessageSHA1Hash()
      messageKeyspec = getKeySpec(message_string_key_hash, message_ic)
    }
    messageKeyspec
  }

  private def getKeySpec(salt: String, ic_count: Int) = {
    val bytes = salt.getBytes
    new PBEKeySpec(new String(xPressoKey).toCharArray, bytes, ic_count)
  }

  @throws[NoSuchAlgorithmException]
  def makeSHA1Hash(input: String): String = {
    val md = MessageDigest.getInstance("MD5")
    md.reset
    val buffer = input.getBytes
    md.update(buffer)
    val digest = md.digest
    var hexStr = ""
    var i = 0
    while (i < digest.length) {
      {
        hexStr += Integer.toString((digest(i) & 0xff) + 0x100, 16).substring(1)
      }
      {
        i += 1;
        i - 1
      }
    }
    hexStr
  }

  private def generateMessageSHA1Hash() {
    try
      message_string_key_hash = makeSHA1Hash(MESSAGE_STRING_KEY)

    catch {
      case e: Exception => {
        logger.error("Error while getting message string hash.", e)
      }
    }
  }

  private def xPressoKey() = Array[Byte](0x22.toByte, 0x45.toByte, 0x67.toByte, 0x6f.toByte, 0x20.toByte, 0x2d.toByte,
    0x20.toByte, 0x45.toByte, 0x6d.toByte, 0x6f.toByte, 0x74.toByte, 0x69.toByte,
    0x6f.toByte, 0x6e.toByte, 0x20.toByte, 0x6f.toByte, 0x6e.toByte, 0x20.toByte,
    0x74.toByte, 0x68.toByte, 0x65.toByte, 0x20.toByte, 0x47.toByte, 0x6f.toByte,
    0x20.toByte, 0x2c.toByte, 0x20.toByte, 0x4d.toByte, 0x6f.toByte, 0x62.toByte,
    0x69.toByte, 0x47.toByte, 0x72.toByte, 0x61.toByte, 0x70.toByte, 0x68.toByte,
    0x20.toByte, 0x53.toByte, 0x6f.toByte, 0x6c.toByte, 0x75.toByte, 0x74.toByte,
    0x69.toByte, 0x6f.toByte, 0x6e.toByte, 0x20.toByte, 0x49.toByte, 0x6e.toByte,
    0x64.toByte, 0x69.toByte, 0x61.toByte, 0x20.toByte, 0x49.toByte, 0x6e.toByte,
    0x63.toByte, 0x20.toByte, 0x45.toByte, 0x6e.toByte, 0x63.toByte, 0x72.toByte,
    0x79.toByte, 0x70.toByte, 0x74.toByte, 0x69.toByte, 0x6f.toByte, 0x6e.toByte,
    0x55.toByte, 0x74.toByte, 0x69.toByte, 0x6c.toByte, 0x73.toByte, 0x22.toByte, 0x3b.toByte)

  private def getHKeyT() = Array[Byte](0x51.toByte, 0x75.toByte, 0x47.toByte, 0x6f.toByte,
    0x20.toByte, 0x41.toByte, 0x70.toByte, 0x50.toByte, 0x20.toByte, 0x52.toByte, 0x6f.toByte,
    0x43.toByte, 0x4b.toByte, 0x73.toByte, 0x20.toByte, 0x21.toByte, 0x40.toByte, 0x23.toByte,
    0x24.toByte, 0x31.toByte, 0x39.toByte, 0x32.toByte, 0x30.toByte, 0x33.toByte, 0x34.toByte,
    0x38.toByte, 0x35.toByte, 0x36.toByte, 0x31.toByte, 0x32.toByte)

  private def getHKey1() = Array[Byte](0x68.toByte, 0x20.toByte, 0x49.toByte, 0x6e.toByte, 0x64.toByte,
    0x69.toByte, 0x61.toByte, 0x20.toByte, 0x49.toByte, 0x6e.toByte, 0x69.toByte, 0x74.toByte,
    0x69.toByte, 0x61.toByte, 0x6c.toByte, 0x69.toByte, 0x6e.toByte, 0x64.toByte, 0x69.toByte,
    0x61.toByte, 0x20.toByte, 0x49.toByte, 0x6e.toByte, 0x63.toByte, 0x20.toByte, 0x45.toByte,
    0x6e.toByte, 0x63.toByte, 0x72.toByte, 0x79.toByte, 0x70.toByte, 0x74.toByte)

  private val secureRandom = new SecureRandom()

  def generateHMAC(hmacForBytes: Array[Byte]): String = computeHMAC(hmacForBytes, getHKey1, HMAC_ALGO_FOR_CRYPT)

  def generateUserKeys(userId: String) = {
    val appKey = generateHMACForString(userId + abs(secureRandom.nextLong())).replaceAll("\\+", "").replaceAll("/", "").replaceAll("=", "");
    (appKey, generateHMAC(userId.getBytes()))
  }

  def generateHMACForString(deviceIdentifier: String): String = {
    if (deviceIdentifier == null || deviceIdentifier.isEmpty())
      return null;
    computeHMAC(deviceIdentifier.getBytes(), getHKeyT, HMAC_ALGO_FOR_TOPIC_GENERATION);
  }

  def computeHMAC(hmacForBytes: Array[Byte], hmacBytes: Array[Byte], hmacAlgo1: String): String = {
    val hmacKey = new SecretKeySpec(hmacBytes, hmacAlgo1);
    try {
      val mac = Mac.getInstance(hmacAlgo1);
      mac.init(hmacKey);
      val hmac = mac.doFinal(hmacForBytes);
      new String(getEncoder().encode(hmac))
    } catch {
      case e: Exception => {
        logger.error("Error while getting message string hash.", e)
        throw new RuntimeException(e)
      }
    }


  }
}


