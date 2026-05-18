package ec.edu.monster.model

data class Movimiento(
    val nroMovimiento: String = "",
    val fecha: String = "",
    val tipo: String = "",
    val accion: String = "",
    val importe: Double = 0.0
)
