package saving

class SaverRegistry {
    private val savers = mutableMapOf<String, DataSaver>()

    fun register(saver: DataSaver) { savers[saver.formatName.lowercase()] = saver }

    fun get(name: String): DataSaver? = savers[name.lowercase()]

    fun availableFormats(): Set<String> = savers.keys

    companion object {
        fun default(): SaverRegistry = SaverRegistry().apply {
            register(JsonSaver())
            register(XmlSaver())
            register(TextReportSaver())
        }
    }
}