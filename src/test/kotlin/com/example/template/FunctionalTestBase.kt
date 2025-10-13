package com.example.template

import io.kotest.core.extensions.Extension
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.context.annotation.Import
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpMethod.PUT
import org.springframework.http.ResponseEntity
import kotlin.reflect.KClass

@Import(TestConfiguration::class, TestDatabaseConfiguration::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class FunctionalTestBase : FunSpec() {
    override val extensions: List<Extension>
        get() = listOf(SpringExtension())

    @Autowired
    protected lateinit var client: TestRestTemplate

    @Autowired
    protected lateinit var testHelper: TestHelper

    protected fun <T : Any> TestRestTemplate.putForEntity(
        url: String,
        body: Any,
        responseType: KClass<T>,
    ): ResponseEntity<T> = this.exchange(url, PUT, HttpEntity(body), responseType.java)

    protected fun <T : Any> TestRestTemplate.getForEntity(
        url: String,
        responseType: KClass<T>,
    ): ResponseEntity<T> = this.exchange(url, HttpMethod.GET, null, responseType.java)

    protected fun <T : Any> TestRestTemplate.getForEntity(
        url: String,
        responseType: ParameterizedTypeReference<T>,
    ): ResponseEntity<T> = this.exchange(url, HttpMethod.GET, null, responseType)
}
