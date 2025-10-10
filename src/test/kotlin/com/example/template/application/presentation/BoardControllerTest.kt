package com.example.template.application.presentation

import com.example.template.FunctionalTestBase
import com.example.template.domain.generateId
import com.example.template.typeRef
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainInOrder
import io.kotest.matchers.comparables.shouldBeGreaterThanOrEqualTo
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus
import org.springframework.web.util.UriComponentsBuilder

class BoardControllerTest : FunctionalTestBase() {
    init {
        context("create") {
            test("생성 요청 값이 주어지면 게시판을 올바르게 생성한다.") {
                // Given
                val request =
                    BoardCreationRequest(
                        title = "테스트 게시글 제목",
                        content = "테스트 게시글 내용",
                    )

                // When
                val actual =
                    client.postForEntity(
                        "/boards",
                        request,
                        BoardResponse::class.java,
                    )

                // Then
                actual.statusCode shouldBe HttpStatus.OK

                assertSoftly(actual.body!!) {
                    it.title shouldBe "테스트 게시글 제목"
                    it.content shouldBe "테스트 게시글 내용"
                }

                val expected = testHelper.getBoard(actual.body!!.id)
                assertSoftly(expected) {
                    it.title shouldBe "테스트 게시글 제목"
                    it.content shouldBe "테스트 게시글 내용"
                }
            }
        }

        context("update") {
            test("수정 데이터가 주어비면 게시판을 올바르게 수정한다.") {
                // Given
                val createdBoard = testHelper.createBoard()
                val request =
                    BoardUpdateRequest(
                        title = "테스트 수정 게시글 제목",
                        content = "테스트 수정 게시글 내용",
                    )

                // When
                val actual =
                    client.putForEntity(
                        "/boards/${createdBoard.id}",
                        request,
                        BoardResponse::class,
                    )

                // Then
                actual.statusCode shouldBe HttpStatus.OK

                assertSoftly(actual.body!!) {
                    it.id shouldBe createdBoard.id
                    it.title shouldBe "테스트 수정 게시글 제목"
                    it.content shouldBe "테스트 수정 게시글 내용"
                }

                val expected = testHelper.getBoard(createdBoard.id)
                assertSoftly(expected) {
                    it.title shouldBe "테스트 수정 게시글 제목"
                    it.content shouldBe "테스트 수정 게시글 내용"
                }
            }

            test("존재하지 않는 ID가 주어지면 오류를 반환한다.") {
                // Given
                val id = generateId()
                val request =
                    BoardUpdateRequest(
                        title = "테스트 수정 게시글 제목",
                        content = "테스트 수정 게시글 내용",
                    )

                // When
                val actual =
                    client.putForEntity(
                        "/boards/$id",
                        request,
                        BoardResponse::class,
                    )

                // Then
                actual.statusCode shouldBe HttpStatus.NOT_FOUND
            }
        }

        context("getById") {
            test("존재하는 게시판 ID가 주어지면 게시판 정보를 반환한다.") {
                // Given
                val createdBoard = testHelper.createBoard("게시판 제목", "게시판 내용")
                val id = createdBoard.id

                // When
                val actual = client.getForEntity("/boards/$id", BoardResponse::class)

                // Then
                actual.statusCode shouldBe HttpStatus.OK
                assertSoftly(actual.body!!) {
                    it.id shouldBe id
                    it.title shouldBe "게시판 제목"
                    it.content shouldBe "게시판 내용"
                }
            }

            test("존재하지 않는 ID가 주어지면 오류를 반환한다.") {
                // Given
                val id = generateId()

                // When
                val actual = client.getForEntity("/boards/$id", BoardResponse::class)

                // Then
                actual.statusCode shouldBe HttpStatus.NOT_FOUND
            }
        }

        context("deleteById") {
            test("게시판 ID가 주어지면 게시판을 삭제한다.") {
                // Given
                val createdBoard = testHelper.createBoard()
                val id = createdBoard.id

                // When
                client.delete("/boards/$id")

                // Then
                shouldThrow<NoSuchElementException> {
                    testHelper.getBoard(id)
                }
            }
        }

        context("getAllByFilter") {
            testHelper.createBoard("게시판 목록 제목1", "게시판 목록 내용1")
            testHelper.createBoard("게시판 목록 제목2", "게시판 목록 내용2")
            testHelper.createBoard("게시판 목록 제목3", "게시판 목록 내용3")
            testHelper.createBoard("게시판 목록 제목4", "게시판 목록 내용4")
            testHelper.createBoard("게시판 목록 제목5", "게시판 목록 내용5")

            test("필터, 페이징 정보 없이 게시판 목록을 조회한다.") {
                // When
                val actual = client.getForEntity("/boards", typeRef<PageResponse<BoardResponse>>())

                // Then
                actual.statusCode shouldBe HttpStatus.OK
                assertSoftly(actual.body!!) {
                    it.content.map { b -> b.title } shouldContainInOrder listOf(
                        "게시판 목록 제목5",
                        "게시판 목록 제목4",
                        "게시판 목록 제목3",
                        "게시판 목록 제목2",
                        "게시판 목록 제목1",
                    )
                    it.totalElements shouldBeGreaterThanOrEqualTo 5
                    it.page shouldBe 0
                    it.size shouldBe 20
                    it.totalPages shouldBeGreaterThanOrEqualTo 1
                }
            }

            test("필터가 주어지면 올바른 게시판 목록을 조회한다.") {
                // Given
                val url = UriComponentsBuilder.newInstance()
                    .path("/boards")
                    .queryParam("title", "게시판 목록")
                    .queryParam("content", "내용3")
                    .build()
                    .toString()

                // When
                val actual = client.getForEntity(url, typeRef<PageResponse<BoardResponse>>())

                // Then
                actual.statusCode shouldBe HttpStatus.OK
                assertSoftly(actual.body!!) {
                    it.content.map { b -> b.title } shouldBe listOf("게시판 목록 제목3")
                    it.totalElements shouldBe 1
                    it.page shouldBe 0
                    it.size shouldBe 20
                    it.totalPages shouldBe 1
                }
            }

            test("필터와 페이징이 주어지면 올바른 게시판 목록을 조회한다.") {
                // Given
                val url = UriComponentsBuilder.newInstance()
                    .path("/boards")
                    .queryParam("title", "게시판 목록")
                    .queryParam("page", "1")
                    .queryParam("size", "2")
                    .build()
                    .toString()

                // When
                val actual = client.getForEntity(url, typeRef<PageResponse<BoardResponse>>())

                // Then
                actual.statusCode shouldBe HttpStatus.OK
                assertSoftly(actual.body!!) {
                    it.content.map { b -> b.title } shouldContainExactly listOf(
                        "게시판 목록 제목3",
                        "게시판 목록 제목2",
                    )
                    it.totalElements shouldBe 5
                    it.page shouldBe 1
                    it.size shouldBe 2
                    it.totalPages shouldBe 3
                }
            }
        }
    }
}
