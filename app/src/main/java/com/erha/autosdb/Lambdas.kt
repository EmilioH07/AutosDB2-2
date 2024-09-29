package com.erha.autosdb

fun main() {
    operaNumeros(52, 26)
    operaNumeros(30, 18)

    val miLambdaSuma: (Int, Int) -> Unit = { x, y -> println("La suma de $x + $y es ${x + y}") }

    operaNumeros(50, 20,
        { x, y ->
            println("@string")
        }, {
            println("Hola $it")
        })

    operaNumeros(10, 5,
        { x, y ->
            println("La multiplicación de $x * $y es ${x * y}")
        }, { name ->
            println("Hello $name")
        })
}

fun operaNumeros(num1: Int, num2: Int) {
    println("La suma de $num1 + $num2 es: ${num1 + num2}")
}

fun operaNumeros(num1: Int, num2: Int, operacion: (Int, Int) -> Unit, miLambda2: (String) -> Unit) {
    operacion(num1, num2)
    miLambda2("Amaury")
}