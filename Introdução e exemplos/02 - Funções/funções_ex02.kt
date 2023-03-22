/**
 * Funções com parâmetros [vararg].
*/
fun main() {

    fun printAll(vararg messages: String) {                            // 1
        for (m in messages) println(m)
    }
    printAll("Hello", "Olá", "Salut", "Hola", "你好")                  // 2
    
    fun printAllWithPrefix(vararg messages: String, prefix: String) {  // 3
        for (m in messages) println(prefix + m)
    }
    printAllWithPrefix(
            "Hello", "Olá", "Salut", "Hola", "你好",
            prefix = "Greeting: "                                      // 4
    )

    fun log(vararg entries: String) {
        printAll(*entries)                                             // 5
    }

}