package ec.edu.monster.service

import android.util.Base64
import ec.edu.monster.model.Movimiento
import ec.edu.monster.model.OperacionCuentaResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.StringReader
import java.security.MessageDigest
import java.util.concurrent.TimeUnit

class SoapService {
    
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()
    
    // URL del servidor SOAP
    // 10.0.2.2 es la IP especial del emulador para acceder a localhost de tu PC
    // Si usas dispositivo físico, cambia a tu IP local (ej: 192.168.1.100)
    private val baseUrl = "http://192.168.100.53:8080/WS_EurekaBank_Server/WSEureka"
    
    companion object {
        private const val NAMESPACE = "http://ws.monster.edu.ec/"
        
        /**
         * Genera hash SHA-256 en Base64 (igual que el servidor)
         */
        private fun generarHash(texto: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-256")
                val hashBytes = md.digest(texto.toByteArray(Charsets.UTF_8))
                Base64.encodeToString(hashBytes, Base64.NO_WRAP)
            } catch (e: Exception) {
                e.printStackTrace()
                texto
            }
        }
    }
    
    /**
     * Validar ingreso de usuario
     */
    suspend fun validarIngreso(usuario: String, password: String): Boolean = withContext(Dispatchers.IO) {
        try {
            println("🌐 Conectando a: $baseUrl")
            println("👤 Usuario: $usuario")
            println("🔑 Password (sin hashear): $password")
            
            // NO hasheamos aquí, el servidor lo hace internamente
            val soapEnvelope = """
                <?xml version="1.0" encoding="UTF-8"?>
                <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
                    <S:Body>
                        <ns2:validarIngreso xmlns:ns2="$NAMESPACE">
                            <usuario>$usuario</usuario>
                            <password>$password</password>
                        </ns2:validarIngreso>
                    </S:Body>
                </S:Envelope>
            """.trimIndent()
            
            println("📤 SOAP Request enviado")
            println("📝 Request XML:\n$soapEnvelope")
            
            val request = Request.Builder()
                .url(baseUrl)
                .post(soapEnvelope.toRequestBody("text/xml; charset=utf-8".toMediaType()))
                .addHeader("SOAPAction", "")
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            
            println("📥 SOAP Response recibido (${response.code})")
            println("Response body: $responseBody")
            
            // Parsear respuesta XML - ahora busca "Exitoso" en vez de true/false
            val resultado = parseValidarIngresoResponse(responseBody)
            println("✅ Resultado parseado: $resultado")
            resultado
        } catch (e: Exception) {
            println("❌ Error en validarIngreso: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    /**
     * Traer movimientos de una cuenta
     */
    suspend fun traerMovimientos(cuenta: String): List<Movimiento> = withContext(Dispatchers.IO) {
        try {
            println("📊 ===== CONSULTANDO MOVIMIENTOS =====")
            println("📋 Cuenta: $cuenta")
            
            val soapEnvelope = """
                <?xml version="1.0" encoding="UTF-8"?>
                <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
                    <S:Body>
                        <ns2:traerMovimientos xmlns:ns2="$NAMESPACE">
                            <cuenta>$cuenta</cuenta>
                        </ns2:traerMovimientos>
                    </S:Body>
                </S:Envelope>
            """.trimIndent()
            
            println("📤 SOAP Request:")
            println(soapEnvelope)
            
            val request = Request.Builder()
                .url(baseUrl)
                .post(soapEnvelope.toRequestBody("text/xml; charset=utf-8".toMediaType()))
                .addHeader("SOAPAction", "")
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            
            println("📥 SOAP Response:")
            println(responseBody)
            
            val movimientos = parseMovimientosResponse(responseBody)
            println("✅ Total movimientos encontrados: ${movimientos.size}")
            println("📊 ===== FIN CONSULTA MOVIMIENTOS =====")
            
            movimientos
        } catch (e: Exception) {
            println("❌ ERROR consultando movimientos: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
    
    /**
     * Registrar depósito
     */
    suspend fun registrarDeposito(cuenta: String, monto: Double): OperacionCuentaResponse = withContext(Dispatchers.IO) {
        try {
            println("🏦 ===== INICIANDO DEPÓSITO =====")
            println("📋 Cuenta: $cuenta")
            println("💵 Monto: $monto")
            
            val soapEnvelope = """
                <?xml version="1.0" encoding="UTF-8"?>
                <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
                    <S:Body>
                        <ns2:regDeposito xmlns:ns2="$NAMESPACE">
                            <cuenta>$cuenta</cuenta>
                            <importe>$monto</importe>
                        </ns2:regDeposito>
                    </S:Body>
                </S:Envelope>
            """.trimIndent()
            
            println("📤 SOAP Request:")
            println(soapEnvelope)
            
            val request = Request.Builder()
                .url(baseUrl)
                .post(soapEnvelope.toRequestBody("text/xml; charset=utf-8".toMediaType()))
                .addHeader("SOAPAction", "")
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            
            println("📥 SOAP Response:")
            println(responseBody)
            println("🏦 ===== FIN DEPÓSITO =====")
            
            parseOperacionResponse(responseBody)
        } catch (e: Exception) {
            println("❌ ERROR en depósito: ${e.message}")
            e.printStackTrace()
            OperacionCuentaResponse(estado = -1, saldo = 0.0)
        }
    }
    
    /**
     * Registrar retiro
     */
    suspend fun registrarRetiro(cuenta: String, monto: Double): OperacionCuentaResponse = withContext(Dispatchers.IO) {
        try {
            println("💸 ===== INICIANDO RETIRO =====")
            println("📋 Cuenta: $cuenta")
            println("💵 Monto: $monto")
            
            val soapEnvelope = """
                <?xml version="1.0" encoding="UTF-8"?>
                <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
                    <S:Body>
                        <ns2:regRetiro xmlns:ns2="$NAMESPACE">
                            <cuenta>$cuenta</cuenta>
                            <importe>$monto</importe>
                        </ns2:regRetiro>
                    </S:Body>
                </S:Envelope>
            """.trimIndent()
            
            println("📤 SOAP Request:")
            println(soapEnvelope)
            
            val request = Request.Builder()
                .url(baseUrl)
                .post(soapEnvelope.toRequestBody("text/xml; charset=utf-8".toMediaType()))
                .addHeader("SOAPAction", "")
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            
            println("📥 SOAP Response:")
            println(responseBody)
            println("💸 ===== FIN RETIRO =====")
            
            parseOperacionResponse(responseBody)
        } catch (e: Exception) {
            println("❌ ERROR en retiro: ${e.message}")
            e.printStackTrace()
            OperacionCuentaResponse(estado = -1, saldo = 0.0)
        }
    }
    
    /**
     * Registrar transferencia
     */
    suspend fun registrarTransferencia(
        cuentaOrigen: String,
        cuentaDestino: String,
        monto: Double
    ): OperacionCuentaResponse = withContext(Dispatchers.IO) {
        try {
            println("💱 ===== INICIANDO TRANSFERENCIA =====")
            println("📋 Cuenta Origen: $cuentaOrigen")
            println("📋 Cuenta Destino: $cuentaDestino")
            println("💵 Monto: $monto")
            
            val soapEnvelope = """
                <?xml version="1.0" encoding="UTF-8"?>
                <S:Envelope xmlns:S="http://schemas.xmlsoap.org/soap/envelope/">
                    <S:Body>
                        <ns2:regTransferencia xmlns:ns2="$NAMESPACE">
                            <cuentaOrigen>$cuentaOrigen</cuentaOrigen>
                            <cuentaDestino>$cuentaDestino</cuentaDestino>
                            <importe>$monto</importe>
                        </ns2:regTransferencia>
                    </S:Body>
                </S:Envelope>
            """.trimIndent()
            
            println("📤 SOAP Request:")
            println(soapEnvelope)
            
            val request = Request.Builder()
                .url(baseUrl)
                .post(soapEnvelope.toRequestBody("text/xml; charset=utf-8".toMediaType()))
                .addHeader("SOAPAction", "")
                .build()
            
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""
            
            println("📥 SOAP Response:")
            println(responseBody)
            println("💱 ===== FIN TRANSFERENCIA =====")
            
            parseOperacionResponse(responseBody)
        } catch (e: Exception) {
            println("❌ ERROR en transferencia: ${e.message}")
            e.printStackTrace()
            OperacionCuentaResponse(estado = -1, saldo = 0.0)
        }
    }
    
    // ==================== PARSERS ====================
    
    private fun parseValidarIngresoResponse(xml: String): Boolean {
        return try {
            println("🔍 Parseando respuesta XML...")
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(xml))
            
            var eventType = parser.eventType
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    println("📌 Tag encontrado: ${parser.name}")
                    if (parser.name == "return") {
                        parser.next()
                        val textoRespuesta = parser.text?.trim()
                        println("📝 Texto de respuesta: '$textoRespuesta'")
                        // El servidor retorna "Exitoso" o "Denegado"
                        val resultado = textoRespuesta?.equals("Exitoso", ignoreCase = true) ?: false
                        println("✅ Resultado final: $resultado")
                        return resultado
                    }
                }
                eventType = parser.next()
            }
            println("⚠️ No se encontró tag 'return', retornando false")
            false
        } catch (e: Exception) {
            println("❌ Error parseando XML: ${e.message}")
            e.printStackTrace()
            false
        }
    }
    
    private fun parseMovimientosResponse(xml: String): List<Movimiento> {
        val movimientos = mutableListOf<Movimiento>()
        try {
            println("📊 Parseando movimientos...")
            println("XML: ${xml.take(500)}")
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(xml))
            
            var eventType = parser.eventType
            var currentMovimiento = Movimiento()
            var currentTag = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        currentTag = parser.name
                        println("📌 Tag: $currentTag")
                        if (currentTag == "return" || currentTag == "movimiento") {
                            currentMovimiento = Movimiento()
                        }
                    }
                    XmlPullParser.TEXT -> {
                        val text = parser.text?.trim() ?: ""
                        if (text.isNotEmpty()) {
                            println("📝 $currentTag = $text")
                            when (currentTag) {
                                "nromov" -> currentMovimiento = currentMovimiento.copy(nroMovimiento = text)
                                "fecha" -> currentMovimiento = currentMovimiento.copy(fecha = text)
                                "tipo" -> currentMovimiento = currentMovimiento.copy(tipo = text)
                                "accion" -> currentMovimiento = currentMovimiento.copy(accion = text)
                                "importe" -> currentMovimiento = currentMovimiento.copy(importe = text.toDoubleOrNull() ?: 0.0)
                            }
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if ((parser.name == "return" || parser.name == "movimiento") && currentMovimiento.nroMovimiento.isNotEmpty()) {
                            println("✅ Movimiento agregado: ${currentMovimiento.nroMovimiento}")
                            movimientos.add(currentMovimiento)
                        }
                    }
                }
                eventType = parser.next()
            }
            println("📊 Total movimientos parseados: ${movimientos.size}")
        } catch (e: Exception) {
            println("❌ Error parseando movimientos: ${e.message}")
            e.printStackTrace()
        }
        return movimientos
    }
    
    private fun parseOperacionResponse(xml: String): OperacionCuentaResponse {
        try {
            println("🔍 Parseando respuesta de operación...")
            println("XML recibido: ${xml.take(1000)}")
            
            val factory = XmlPullParserFactory.newInstance()
            val parser = factory.newPullParser()
            parser.setInput(StringReader(xml))
            
            var eventType = parser.eventType
            var estado = -1
            var saldo = 0.0
            var currentTag = ""
            
            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        currentTag = parser.name
                        println("📌 Tag: $currentTag")
                    }
                    XmlPullParser.TEXT -> {
                        val text = parser.text ?: ""
                        when (currentTag) {
                            "estado" -> {
                                estado = text.toIntOrNull() ?: -1
                                println("📊 Estado: $estado")
                            }
                            "saldo" -> {
                                saldo = text.toDoubleOrNull() ?: 0.0
                                println("💰 Saldo: $saldo")
                            }
                        }
                    }
                }
                eventType = parser.next()
            }
            
            println("✅ Resultado parseado - Estado: $estado, Saldo: $saldo")
            return OperacionCuentaResponse(estado, saldo)
        } catch (e: Exception) {
            println("❌ Error parseando respuesta de operación: ${e.message}")
            e.printStackTrace()
            return OperacionCuentaResponse(-1, 0.0)
        }
    }
}
