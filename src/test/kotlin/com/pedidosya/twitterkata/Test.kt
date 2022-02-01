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
        // given
        val publishMessage = PublishMessage()

        val user = User("nickName", "fullName")
        val message = "hi :D"

        // when
        val userMessage: UserMessage = publishMessage.post(user, message)

        // then
        assertEquals(message, userMessage.message)
        assertEquals(user, userMessage.user)
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

    fun post(user: User, message: String): UserMessage {
        return UserMessage(user, message)
    }
}

data class UserMessage(val user: User, val message: String)


data class User(val nickName: String, val fullName: String)

class RegisteredUserException : RuntimeException()