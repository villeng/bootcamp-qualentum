package entrenamiento

class Logger {
    private static final String LOG_FILE = "file_operations.log"

    def log(String message) {
        def timestamp = new Date().format('yyyy-MM-dd HH:mm:ss')
        def logMessage = "${timestamp} - ${message}"

        // Imprimir en consola
        println logMessage

        // Escribir en archivo de log
        try {
            new File(LOG_FILE).append("${logMessage}\n")
        } catch (Exception e) {
            println "Error writing to log file: ${e.message}"
        }
    }
}