package com.jkane.comicviewer.application.utils

import com.jkane.comicviewer.BuildConfig
import java.security.MessageDigest

/**
 * Network hash required for the Marvel API
 * From https://developer.marvel.com/documentation/authorization
 *
 * @return hash - a md5 digest of the ts parameter, your private key
 * and your public key (e.g. md5(ts+privateKey+publicKey)
 */
fun generateNetworkHash(timeStamp: String) = generateMD5Hash(
    timeStamp.plus(BuildConfig.MARVEL_API_PRIVATE_KEY).plus(BuildConfig.MARVEL_API_PUBLIC_KEY)
)

/**
 * Generating an MD5 Hash
 * From https://stackoverflow.com/questions/4846484/md5-hashing-in-android
 *
 * Takes an input string, converts to a byte array, generates an MD5 hash
 * and then converts back to a Hex string.
 *
 * @return Hex string of an MD5 hash
 */
private fun generateMD5Hash(input: String) =
    MessageDigest
        .getInstance("MD5")
        .digest(input.toByteArray())
        .joinToString("") { String.format("%02x", it) }  // bytes to hex