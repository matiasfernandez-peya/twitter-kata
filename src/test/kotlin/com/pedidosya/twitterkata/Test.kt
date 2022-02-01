package com.pedidosya.twitterkata

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Test {

    @Test
    fun test() {
        Assertions.assertNotNull("complete me")
    }

    @Test
    fun `un usuario puede registrarse usando nickname y nombre completo`() {
        //GHERKIN BDD
        // setup - given
        val nickname = "Alecito"
        val fullName = "Rigoberto Perez"
        val register = RegisterUser()

        // EXECUTE - when
        val user = register.execute(nickname, fullName)

        // TEST - then
        assertEquals(nickname, user.nickName)
        assertEquals(fullName, user.fullName)
    }

    @Test
    fun `un usuario no puede registrarse dos veces`() {
        val register = RegisterUser()

        val nickname = "Alecito"
        val fullName = "Rigoberto Perez"

        register.execute(nickname, fullName)

        // EXECUTE - when
        Assertions.assertThrows(RegisteredUserException::class.java) { register.execute(nickname, fullName) }
    }

    @Test
    fun `un usuario puede publicar mensajes a su timeline personal`() {
        val user = User("nickName", "fullName")
        val timeline = Timeline(user)

        val publishMessage = PublishMessage()

        val message = "hi :D"

        publishMessage.post(user, message)

        assert(publishMessage.messages.isNotEmpty())
    }
}

class RegisterUser {

    private val users: MutableList<User> = mutableListOf()

    fun execute(nickName: String, fullName: String): User {
        val user = User(nickName, fullName)

        if (users.contains(user)) {
            throw RegisteredUserException()
        }
        users.add(user)

        return user
    }
}

class PublishMessage {
    val messages: MutableList<String> = mutableListOf()

    fun post(user: User, message :String) {
        messages.add(message)
    }
}

data class Timeline(val user: User)

data class User(val nickName: String, val fullName: String)

class RegisteredUserException : RuntimeException()