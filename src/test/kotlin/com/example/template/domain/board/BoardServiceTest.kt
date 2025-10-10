package com.example.template.domain.board

import com.example.template.UnitTestBase
import com.example.template.domain.generateId
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import java.util.Optional

class BoardServiceTest : UnitTestBase() {
    private val boardRepository: BoardRepository = mockk()
    private val boardService = BoardService(boardRepository)

    init {
        context("create") {
            test("생성 데이터가 주어지면 게시판을 올바르게 생성한다.") {
                // Given
                val data = BoardCreationData("title", "content")
                val expectedEntity = Board(data.title, data.content)
                every { boardRepository.save(any()) } returns expectedEntity

                // When
                val actual = boardService.create(data)

                // Then
                actual shouldBe expectedEntity
                verify {
                    val expected =
                        withArg<Board> {
                            it.title shouldBe "title"
                            it.content shouldBe "content"
                        }
                    boardRepository.save(expected)
                }
            }
        }

        context("update") {
            test("수정 데이터가 주어지면 게시판을 올바르게 수정한다.") {
                // Given
                val board = Board("title", "content")
                val data = BoardUpdateData(board.id, "update-title", "update-content")

                every { boardRepository.findById(board.id) } returns Optional.of(board)

                // When
                val actual = boardService.update(data)

                // Then
                actual shouldBe board
                assertSoftly(actual) {
                    it.id shouldBe board.id
                    it.title shouldBe "update-title"
                    it.content shouldBe "update-content"
                }
            }

            test("주어진 ID를 가진 게시판이 존재하지 않으면 오류를 반환한다.") {
                // Given
                val id = generateId()
                val data = BoardUpdateData(id, "title", "content")

                every { boardRepository.findById(id) } returns Optional.empty()

                // Expect
                shouldThrow<NoSuchElementException> {
                    boardService.update(data)
                }
            }
        }

        context("getById") {
            test("ID가 주어지면 게시판을 반환한다.") {
                // Given
                val board = Board("title", "content")

                every { boardRepository.findById(board.id) } returns Optional.of(board)

                // When
                val actual = boardService.getById(board.id)

                // Then
                actual shouldBe board
            }

            test("주어진 ID를 가진 게시판이 존재하지 않으면 오류를 반환한다.") {
                // Given
                val id = generateId()

                every { boardRepository.findById(id) } returns Optional.empty()

                // Expect
                shouldThrow<NoSuchElementException> {
                    boardService.getById(id)
                }
            }
        }

        context("deleteById") {
            test("ID가 주어지면 게시판을 삭제한다.") {
                // Given
                val id = generateId()

                // When
                boardService.deleteById(id)

                // Then
                verify { boardRepository.deleteById(id) }
            }
        }

        context("getAllByFilter") {
            test("필터와 페이징 정보가 주어지면 올바른 게시판 목록을 반환한다.") {
                // Given
                val boards =
                    listOf(
                        Board("title1", "content1"),
                        Board("title2", "content2"),
                        Board("title3", "content3"),
                    )
                val filter = BoardFilter()
                val pageable = Pageable.unpaged()
                val expected = PageImpl(boards)

                every { boardRepository.findAll(filter, pageable) } returns expected

                // When
                val actual = boardService.getAllByFilter(filter, pageable)

                // Then
                actual shouldBe expected
            }
        }
    }
}
