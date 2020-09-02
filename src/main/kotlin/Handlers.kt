import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import java.awt.TextArea
import java.nio.charset.Charset
import java.text.SimpleDateFormat
import java.util.*

const val MESSAGE_TO_RETURN = "Type=DUST&CurrentTime=20190806154125&ReceiveTime=20190806154119^20190806154119&WorstIndex=0^1&Data=pm1.0;ug/m3;7;좋음,pm2.5;ug/m3;7;좋음,pm10;ug/m3;7;보통^pm1.0;ug/m3;7;좋음,pm2.5;ug/m3;7;좋음,pm10;ug/m3;7;보통&Temperature=10,°C^11,°C&Humidity=70,%^75,%&Return=OK"

class FineDustEchoServerHandler(private val responseMessage: String, taConsole: TextArea) : ChannelInboundHandlerAdapter() {
    var taConsole = TextArea()
    val simpleDateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")

    init {
        this.taConsole = taConsole
    }

    override fun channelRead(ctx: ChannelHandlerContext?, msg: Any?) {
        val message = (msg as ByteBuf).toString(Charset.forName("euc-kr"))
        taConsole.append("${simpleDateFormat.format(Date())} receive << $message\n")
//        println("receive << $message")

        val strWithLength = "${padTo8(this.encodeToEUCKRByteArray(responseMessage).size)}$responseMessage"
        taConsole.append("${simpleDateFormat.format(Date())} response >> $strWithLength\n")
//        println("response >> $strWithLength")
        val buffToSend = Unpooled.buffer()
        buffToSend.writeBytes(this.encodeToEUCKRByteArray(strWithLength))
        ctx?.write(buffToSend)
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        ctx?.flush()
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        cause?.printStackTrace()
        ctx?.close()
    }

    private fun encodeToEUCKRByteArray(str: String): ByteArray {
        return str.toByteArray(Charset.forName("euc-kr"))
    }

    private fun padTo8(num: Int): String {
        var resultStr = num.toString()
        while (resultStr.length < 8) {
            resultStr = "0$resultStr"
        }
        return resultStr
    }
}
