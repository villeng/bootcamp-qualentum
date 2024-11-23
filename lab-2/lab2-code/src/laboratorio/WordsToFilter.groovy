package laboratorio

enum WordsToFilter {
    // Prepositions
    A, ANTE, BAJO, CABE, CON, CONTRA, DE, DESDE,
    DURANTE, EN, ENTRE, HACIA, HASTA, MEDIANTE,
    PARA, POR, SEGÚN, SIN, SO, SOBRE, TRAS,

    // Articles
    EL, LA, LOS, LAS, UN, UNA, UNOS, UNAS, ESTE, ESE, AQUEL,
    ESTA, ESA, AQUELLA, ESTOS, ESOS, AQUELLOS, ALGÚN, NINGÚN,
    ALGUNO, NINGUNO, TODO, TODA, TODOS, TODAS, NADA,
    DEL, AL,

    // Conjunctions
    Y, E, O, SI, NO, TAL, QUE, PERO, MAS, SINO, AUNQUE, PORQUE, QUÉ, YA,
    ASÍ,PUES, MIENTRAS, CUANDO, COMO, NI, TAN, MÁS,

    // Pronombres
    YO, ME, MI, MIO, CONMIGO, TU, TE, TI, CONTIGO, ÉL, LO, LE, SE, SÍ,
    CONSIGO, ELLA, SU, SUYO, ELLOS, LES, VOSOTROS, NOS, USTED, USTEDES,
    NUESTRO, VUESTRO, NUESTRA, VUESTRA, SUS,

    //OTROS
    ES, HA, HE, HAN, HEMOS, HABÉIS, HAS, ERA, ASI,

    static boolean isWordToFilter(String palabra) {
        try {
            valueOf(palabra.toUpperCase())
            return true
        } catch (e) {
            return false
        }
    }
}