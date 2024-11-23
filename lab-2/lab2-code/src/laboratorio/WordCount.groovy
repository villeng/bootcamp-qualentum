package laboratorio

import groovy.transform.Field
import java.util.logging.*

@Field int OCCURRENCES = 5
@Field String INPUT_DIR = "src/laboratorio.input"
@Field String OUTPUT_DIR = "src/laboratorio.output"
@Field String TEXT_URL = "https://gist.githubusercontent.com/jsdario/6d6c69398cb0c73111e49f1218960f79/raw/8d4fc4548d437e2a7203a5aeeace5477f598827d/el_quijote.txt"
@Field logger = Logger.getLogger("WordCount")
logger.setLevel(Level.ALL)
Logger.getLogger("org.codehaus.groovy").setLevel(Level.SEVERE)  // Root logger
def consoleHandler = new ConsoleHandler()
logger.addHandler(consoleHandler)

List<String> getMostFrequent(String text, int occurrences = OCURRENCES) {
    logger.info("Analysing occurrences...")
    Map<String, Integer> wordFreqMap = text
            .tokenize()
            .collect { it.toLowerCase() }
            .groupBy { it }
            .collectEntries { _, items -> [items[0] as String, items.size()]
            }


    wordFreqMap.entrySet().stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .filter { !WordsToFilter.isWordToFilter(it.key) }
            .limit(occurrences)
            .map(Map.Entry::getKey) as List
}


void procesarDirectorio(String inputPath) {
    def inputDir = new File(inputPath)

    inputDir.eachFile { file ->
        try {
            def content = file.text.trim()
            def fileContent

            // Intenta como URL, si falla asume ruta local
            try {
                fileContent = new URL(content).text
            } catch (e) {
                def path = content.trim()
                fileContent = new File(path.normalize().toString()).text
            }

            def outputFile = new File("${OUTPUT_DIR}/${file.name.replaceFirst(/\.[^.]+$/, '')}_most_frequent.txt")
            outputFile.text = getMostFrequent(fileContent).join("\n")
            logger.info("${file.name} processed.")
        } catch (e) {
            logger.warning("An exception has ocurred during file processing: ${e}")
        }
    }
}

OCURRENCES = args[0] as Integer
INPUT_DIR = args[1]

logger.info("Processing files from directory ${INPUT_DIR}")
procesarDirectorio(INPUT_DIR)
