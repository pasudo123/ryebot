@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package com.github.ryebot.util

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.core.io.ClassPathResource
import sun.security.util.DerInputStream
import java.nio.file.Files
import java.security.KeyFactory
import java.security.spec.RSAPrivateCrtKeySpec
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Base64
import java.util.Date

/**
 * - https://magnus-k-karlsson.blogspot.com/2018/05/how-to-read-pem-pkcs1-or-pkcs8-encoded.html
 * - https://dev.to/h3xstream/how-to-solve-symbol-is-declared-in-module-x-which-does-not-export-package-y-303g
 */
object Jwt {

    private val pemFile = ClassPathResource("privatekey/ryebot999.2022-12-17.private-key.pem").file

    fun build(): String {
        val privatePem = String(Files.readAllBytes(pemFile.toPath()))
            .replace("-----BEGIN RSA PRIVATE KEY-----", "")
            .replace("-----END RSA PRIVATE KEY-----", "")
            .replace(System.lineSeparator(), "")
            .trim()

        val bytes = Base64.getDecoder().decode(privatePem)

        val derReader = DerInputStream(bytes)
        val sequence = derReader.getSequence(0)

        val modulus = sequence[1].bigInteger
        val publicExp = sequence[2].bigInteger
        val privateExp = sequence[3].bigInteger
        val prime1 = sequence[4].bigInteger
        val prime2 = sequence[5].bigInteger
        val exp1 = sequence[6].bigInteger
        val exp2 = sequence[7].bigInteger
        val crtCoef = sequence[8].bigInteger

        val keySpec = RSAPrivateCrtKeySpec(modulus, publicExp, privateExp, prime1, prime2, exp1, exp2, crtCoef)
        val keyFactory = KeyFactory.getInstance("RSA")
        val privateKey = keyFactory.generatePrivate(keySpec)

        return Jwts.builder()
            .setIssuer("188959")
            .setIssuedAt(Date.from(LocalDateTime.now().minusSeconds(60).atZone(ZoneId.of("Asia/Seoul")).toInstant()))
            .setExpiration(Date.from(LocalDateTime.now().plusSeconds(600).atZone(ZoneId.of("Asia/Seoul")).toInstant()))
            .signWith(privateKey, SignatureAlgorithm.RS256)
            .compact()
    }
}
