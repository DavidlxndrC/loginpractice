package co.edu.unicauca.saberpro.adapter;

/**
 * Servicio externo (la clase "Adaptee"): simula una plataforma de terceros
 * cuyo formato NO coincide con el del banco. Entrega las preguntas como un
 * texto JSON con sus propios nombres de campo (tipo, titulo, alternativas,
 * clave, explicacion...), por lo que no se puede usar directamente.
 */
public class ExternalQuestionService {

    public String fetchQuestionsJson() {
        return """
                [
                  {
                    "codigo": "EXT-001",
                    "tipo": "SELECCION_MULTIPLE",
                    "titulo": "Patrón Singleton",
                    "enunciado": "¿Cuál es el propósito del patrón Singleton?",
                    "alternativas": [
                      "Crear familias de objetos relacionados",
                      "Garantizar una única instancia de una clase",
                      "Adaptar interfaces incompatibles",
                      "Notificar cambios a los observadores"
                    ],
                    "clave": "B",
                    "explicacion": "Singleton restringe la instanciación a un único objeto con acceso global.",
                    "competencia": "Diseño de software",
                    "nivel": "Básico",
                    "categoria": "Patrones creacionales"
                  },
                  {
                    "codigo": "EXT-002",
                    "tipo": "VERDADERO_FALSO",
                    "titulo": "Patrón Facade",
                    "enunciado": "El patrón Facade ofrece una interfaz simplificada a un subsistema complejo.",
                    "alternativas": ["Verdadero", "Falso"],
                    "clave": "A",
                    "explicacion": "Facade oculta la complejidad del subsistema detrás de una interfaz única.",
                    "competencia": "Diseño de software",
                    "nivel": "Básico",
                    "categoria": "Patrones estructurales"
                  },
                  {
                    "codigo": "EXT-003",
                    "tipo": "CASO_ESTUDIO",
                    "titulo": "Reglas de descuento",
                    "contexto": "Una tienda en línea aplica descuentos distintos según el tipo de cliente y cada semana aparecen nuevas reglas. El código actual usa una cadena de if/else que crece sin control.",
                    "enunciado": "¿Qué patrón de comportamiento permite encapsular cada regla y cambiarla en tiempo de ejecución?",
                    "alternativas": ["Strategy", "Singleton", "Facade", "Builder"],
                    "clave": "A",
                    "explicacion": "Strategy encapsula algoritmos intercambiables detrás de una interfaz común.",
                    "competencia": "Diseño de software",
                    "nivel": "Alto",
                    "categoria": "Patrones de comportamiento"
                  },
                  {
                    "codigo": "EXT-004",
                    "tipo": "SELECCION_MULTIPLE",
                    "titulo": "Entrada inválida de ejemplo",
                    "enunciado": "Esta entrada trae solo 3 alternativas, así que el adaptador debe rechazarla.",
                    "alternativas": ["Uno", "Dos", "Tres"],
                    "clave": "A",
                    "explicacion": "",
                    "competencia": "",
                    "nivel": "",
                    "categoria": ""
                  }
                ]
                """;
    }
}
