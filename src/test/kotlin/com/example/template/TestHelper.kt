package com.example.template

import com.example.template.application.facade.BoardFacade
import com.example.template.domain.board.Board
import com.example.template.domain.board.BoardCreationData
import org.springframework.boot.test.context.TestComponent
import org.springframework.context.ApplicationContext
import java.util.UUID
import kotlin.reflect.KClass

@TestComponent
class TestHelper(
    private val context: ApplicationContext,
) {
    fun getBoard(id: UUID): Board {
        val facade = BoardFacade::class.getBean()
        return facade.getById(id)
    }

    fun createBoard(
        title: String = "title",
        content: String = "content",
    ): Board {
        val facade = BoardFacade::class.getBean()

        val data = BoardCreationData(title, content)
        return facade.create(data)
    }

    private fun <T : Any> KClass<T>.getBean(): T = context.getBean(this.java)
}
