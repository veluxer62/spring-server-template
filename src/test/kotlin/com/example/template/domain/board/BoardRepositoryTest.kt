package com.example.template.domain.board

import com.example.template.RepositoryTestBase
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.data.domain.Pageable

class BoardRepositoryTest : RepositoryTestBase() {
    @Autowired private lateinit var boardRepository: BoardRepository

    init {
        context("findAllByFilter") {
            test("빈 필터가 주어지면 모든 게시판 목록을 조회한다.") {
                // Given
                val board1 = Board("title1", "content1")
                val board2 = Board("title2", "content2")
                val board3 = Board("title3", "content3")

                entityManager.persist(board1)
                entityManager.persist(board2)
                entityManager.persist(board3)

                val filter = BoardFilter()

                // When
                val actual = boardRepository.findAll(filter)

                // Then
                actual shouldContainExactlyInAnyOrder listOf(board1, board2, board3)
            }

            test("제목 필터가 주어지면 검색어가 포함된 제목을 가진 게시판 목록을 조회한다.") {
                // Given
                val board1 = Board("title1", "content1")
                val board2 = Board("제목 테스트1 검색", "content2")
                val board3 = Board("제목 테스트2 검색", "content3")
                val board4 = Board("title4", "content3")

                entityManager.persist(board1)
                entityManager.persist(board2)
                entityManager.persist(board3)
                entityManager.persist(board4)

                val filter = BoardFilter(title = "테스트")

                // When
                val actual = boardRepository.findAll(filter)

                // Then
                actual shouldContainExactlyInAnyOrder listOf(board2, board3)
            }

            test("내용 필터가 주어지면 검색어가 포함된 내용을 가진 게시판 목록을 조회한다.") {
                // Given
                val board1 = Board("title1", "content1")
                val board2 = Board("title2", "게시판 내용1 검색")
                val board3 = Board("title3", "게시판 내용2 검색")
                val board4 = Board("title4", "content3")

                entityManager.persist(board1)
                entityManager.persist(board2)
                entityManager.persist(board3)
                entityManager.persist(board4)

                val filter = BoardFilter(content = "내용1")

                // When
                val actual = boardRepository.findAll(filter)

                // Then
                actual shouldContainExactlyInAnyOrder listOf(board2)
            }
        }
    }
}
