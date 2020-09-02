import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import java.awt.TextArea

const val ECHO_SERVER_PORT = 29520

class FineDustEchoServer(responseMessage: String, taConsole: TextArea): Thread() {
    var responseMessage = ""
    var taConsole = TextArea()

    init {
        this.responseMessage = responseMessage
        this.taConsole = taConsole
    }

    override fun run() {
        startFineDustEchoServer(responseMessage, taConsole)
    }
}

fun startFineDustEchoServer(responseMessage: String, taConsole: TextArea) {
    val parentGroup = NioEventLoopGroup(1)
    val childGroup = NioEventLoopGroup()
    try {
        val serverBootstrap = ServerBootstrap();
        serverBootstrap
            .group(parentGroup, childGroup)
            .channel(NioServerSocketChannel::class.java)
            .option(ChannelOption.SO_BACKLOG, 100)
            .handler(LoggingHandler(LogLevel.DEBUG))
            .childHandler(object : ChannelInitializer<SocketChannel>() {
                override fun initChannel(socketChannel: SocketChannel?) {
                    val pipeline = socketChannel?.pipeline()
                    pipeline?.addLast(FineDustEchoServerHandler(responseMessage, taConsole))
                }
            })

        val channelFuture = serverBootstrap.bind(ECHO_SERVER_PORT).sync()

        channelFuture.channel().closeFuture().sync()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        parentGroup.shutdownGracefully()
        childGroup.shutdownGracefully()
    }
}
