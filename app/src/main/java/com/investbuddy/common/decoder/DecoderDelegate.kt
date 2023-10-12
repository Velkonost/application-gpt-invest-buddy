package com.investbuddy.common.decoder


import android.util.Base64
import com.investbuddy.BuildConfig
import java.security.MessageDigest
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec



class DecoderDelegate {

    private fun createCipher(mode: Int, ivBytes: ByteArray): Cipher {
        val c = Cipher.getInstance("AES/CBC/PKCS7Padding")
        val sk = SecretKeySpec(BuildConfig.AES.getSha256(), "AES")
        val iv = IvParameterSpec(ivBytes)
        c.init(Cipher.DECRYPT_MODE, sk, iv)
        return c
    }

    fun decrypt(data: String): ByteArray {
        var decoded = data

        var ans = ""
        for (c in decoded) {
            ans += backShift(c, 7)
        }
        decoded = ans

        ans = ""
        for (c in decoded) {
            ans += backShift(c, 17)
        }
        decoded = ans

        ans = ""
        for (c in decoded) {
            ans += backShift(c, 3)
        }
        decoded = ans


        var bytes = Base64.decode(decoded, Base64.DEFAULT)

        val ivBytes = bytes.take(16).toByteArray()
        val rawDataBytes = bytes.drop(16).toByteArray()
        val cipher = createCipher(Cipher.DECRYPT_MODE, ivBytes)
        return cipher.doFinal(rawDataBytes)
    }

    private fun backShift(char: Char, n: Int): Char {
        val s = " !\"#\$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_abcdefghijklmnopqrstuvwxyz{|}~"
        val index = s.indexOf(char)
        val shiftedIndex = (index - n + s.length) % s.length
        return s[shiftedIndex]
    }


    private fun String.getSha256(): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256").also { it.reset() }
        return digest.digest(this.toByteArray())
    }

}