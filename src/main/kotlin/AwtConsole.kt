import java.awt.*
import java.awt.event.WindowEvent
import java.awt.event.WindowListener
import kotlin.system.exitProcess

class AwtConsoleManager {
    var serverNow: FineDustEchoServer? = null

    fun startConsole() {
        val mainFrame = MainFrame()
        val mainPanel = Panel()

        val tfResponse = TextField("Type=DUST&CurrentTime=20190806154125&ReceiveTime=20190806154119^20190806154119&WorstIndex=0^1&Data=pm1.0;ug/m3;7;좋음,pm2.5;ug/m3;7;좋음,pm10;ug/m3;7;보통^pm1.0;ug/m3;7;좋음,pm2.5;ug/m3;7;좋음,pm10;ug/m3;7;보통&Temperature=10,°C^11,°C&Humidity=70,%^75,%&uvi=2,좋음^5,보통&Return=OK")

        val taConsole = TextArea()

        val btnExit = Button("server exit")
        btnExit.setSize(200, 100)
        btnExit.addActionListener{
            exitProcess(0)
        }

        val btnStart = Button("server start")
        btnStart.addActionListener {
            try {
                serverNow = FineDustEchoServer(tfResponse.text.toString(), taConsole)
                serverNow!!.start()
                btnStart.isEnabled = false
            } catch (e: Exception) {
                taConsole.append("${e.message}\n")
                e.printStackTrace()
            } finally {
            }
        }

        mainPanel.layout = BorderLayout()
        mainPanel.add("North", tfResponse)
        mainPanel.add("South", btnExit)
        mainPanel.add("West", btnStart)
        mainPanel.add("Center", taConsole)


        mainFrame.add(mainPanel)

        mainFrame.addWindowListener(mainFrame)
        mainFrame.title = "미세먼지 에코서버"
        mainFrame.setSize(1200, 200)
        mainFrame.isVisible = true
    }


}

class MainFrame: Frame(), WindowListener {
    override fun windowOpened(e: WindowEvent?) {
    }

    override fun windowClosing(e: WindowEvent?) {
        exitProcess(0)
    }

    override fun windowClosed(e: WindowEvent?) {
    }

    override fun windowIconified(e: WindowEvent?) {
    }

    override fun windowDeiconified(e: WindowEvent?) {
    }

    override fun windowActivated(e: WindowEvent?) {
    }

    override fun windowDeactivated(e: WindowEvent?) {
    }
}
