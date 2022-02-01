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

    @Test
    fun `un usuario que no tiene mensajes obtiene una lista vacia`() {
        // given
        val user = User("user without messages", "Full Name")
        val retrieveUserMessages = RetrieveUserMessages(emptyList())
        val userMessages = emptyList<UserMessage>()

        // when
        val retrievedUserMessages = retrieveUserMessages.execute(user)

        // then
        Assertions.assertEquals(userMessages, retrievedUserMessages)
    }


    @Test
    fun `un usuario puede listar sus mensajes`() {
        // given
        val user = User("nickName", "Full Name")
        val userMessages = listOf(UserMessage(user, "message 1"), UserMessage(user, "message 2"))
        val retrieveUserMessages = RetrieveUserMessages(userMessages)

        // when
        val retrievedUserMessages = retrieveUserMessages.execute(user)

        // then
        Assertions.assertEquals(userMessages, retrievedUserMessages)
    }

}

class RetrieveUserMessages(private val messages: List<UserMessage>) {

    fun execute(searchUser: User): List<UserMessage> {
        return messages.filter { it.user == searchUser }
    }

}

class PublishMessage {

    fun post(user: User, message: String): UserMessage {
        return UserMessage(user, message)
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

data class UserMessage(val user: User, val message: String)

data class User(val nickName: String, val fullName: String)

class RegisteredUserException : RuntimeException()