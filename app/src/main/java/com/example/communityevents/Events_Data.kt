package com.example.communityevents

import java.io.ByteArrayInputStream
import java.io.InputStream
import java.io.OutputStream
import java.sql.Blob

data class Events_Data(
    val Event_ID: Int,
    val Event: String,
    val Event_location: String,
    val Event_Time: Int,
    val Image: Blob,
    val category: String = "Local Events"
)

class ByteArrayBlob(val bytes: ByteArray) : Blob {
    override fun length(): Long = bytes.size.toLong()

    override fun getBytes(pos: Long, length: Int): ByteArray {
        val start = (pos - 1).toInt()
        return bytes.copyOfRange(start, start + length)
    }

    override fun getBinaryStream(): InputStream = ByteArrayInputStream(bytes)

    override fun getBinaryStream(pos: Long, length: Long): InputStream =
        ByteArrayInputStream(bytes, (pos - 1).toInt(), length.toInt())

    override fun position(pattern: ByteArray?, start: Long): Long = throw UnsupportedOperationException()
    override fun position(pattern: Blob?, start: Long): Long = throw UnsupportedOperationException()
    override fun setBytes(pos: Long, bytes: ByteArray?): Int = throw UnsupportedOperationException()
    override fun setBytes(pos: Long, bytes: ByteArray?, offset: Int, len: Int): Int = throw UnsupportedOperationException()
    override fun setBinaryStream(pos: Long): OutputStream = throw UnsupportedOperationException()
    override fun truncate(len: Long) {}
    override fun free() {}
}