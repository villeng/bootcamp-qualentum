package entrenamiento

class FileManager {
    private Logger logger
    
    FileManager() {
        this.logger = new Logger()
    }
    
    def processFile(String fileName) {
        logger.log("Starting to process file: ${fileName}")
        
        try {
            // Leer el archivo
            def file = new File(fileName)
            if (!file.exists()) {
                logger.log("Error: File ${fileName} does not exist")
                return false
            }
            
            logger.log("Reading file content")
            def lines = file.readLines()
            
            // Transformar el contenido usando programación funcional
            logger.log("Transforming file content")
            def transformedLines = lines
                .collect { line -> 
                    // Añadir timestamp y convertir a mayúsculas
                    "[${new Date().format('yyyy-MM-dd HH:mm:ss')}] ${line.toUpperCase()}"
                }
                .findAll { line -> 
                    // Filtrar líneas vacías
                    line.trim()
                }
            
            // Crear el nombre del archivo de salida en el directorio actual
            def outputFileName = "copy_${new File(fileName).getName()}"
            def outputFile = new File(outputFileName)
            
            // Escribir el contenido transformado
            logger.log("Writing transformed content to: ${outputFileName}")
            outputFile.write("") // Crear el archivo si no existe
            transformedLines.each { line ->
                outputFile.append(line + "\n")
            }
            
            logger.log("File processing completed successfully")
            return true
            
        } catch (Exception e) {
            logger.log("Error processing file: ${e.message}")
            e.printStackTrace() // Esto nos ayudará a ver más detalles del error
            return false
        }
    }
    
    static void main(String[] args) {
        if (args.length == 0) {
            println "Error: Please provide a file name as an argument"
            System.exit(1)
        }
        
        def fileManager = new FileManager()
        if (!fileManager.processFile(args[0])) {
            System.exit(1)
        }
    }
}