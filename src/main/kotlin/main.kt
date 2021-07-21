import kotlinx.browser.document
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

external val module: Module

external interface Module {
    val hot: Hot?
}

external interface Hot {
    val data: dynamic

    fun accept()
    fun accept(dependency: String, callback: () -> Unit)
    fun accept(dependencies: Array<String>, callback: (updated: Array<String>) -> Unit)

    fun dispose(callback: (data: dynamic) -> Unit)
}

abstract class Application {

    open fun start() {}

    open fun start(state: Map<String, Any>) {
        start()
    }

    open fun dispose(): Map<String, Any> {
        return mapOf()
    }
}

fun startApplication(builder: () -> Application, hot: Hot? = null) {

    fun start(state: dynamic): Application {
        val application = builder()
        @Suppress("UnsafeCastFromDynamic")
        application.start(state?.appState ?: emptyMap())
        return application
    }

    var application: Application? = null

    val state: dynamic = hot?.let {
        it.accept()

        it.dispose { data ->
            data.appState = application?.dispose()
            application = null
        }

        it.data
    }

    if (document.body != null) {
        application = start(state)
    } else {
        application = null
        document.addEventListener("DOMContentLoaded", { application = start(state) })
    }
}

class App : Application() {
    override fun start() {
        console.log("starting")
        GlobalScope.launch {
            console.log("launch")
            delay(1000)
            console.log("ending")
        }
    }
}

fun main() {
    startApplication(::App, module.hot)
}
