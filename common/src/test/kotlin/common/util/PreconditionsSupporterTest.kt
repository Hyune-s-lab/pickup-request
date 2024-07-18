package common.util

import com.hyunec.pickuprequest.common.util.PreconditionsSupporter
import com.hyunec.pickuprequest.common.util.PreconditionsSupporter.require
import common.TestDefaultSupport
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.RepeatedTest

class PreconditionsSupporterTest : TestDefaultSupport() {

    @RepeatedTest(10)
    fun `require() - 메시지를 넣지 않으면 메시지가 비어 있다`() {
        val exception = shouldThrow<ArrayStoreException> {
            require<ArrayStoreException>(false)
        }

        exception.shouldBeInstanceOf<ArrayStoreException>()
        exception.message shouldBe null
    }

    @RepeatedTest(10)
    fun `require() - 메시지를 넣으면 메시지가 포함된다`() {
        val exception = shouldThrow<ArrayStoreException> {
            require<ArrayStoreException>(false) { "Test message" }
        }

        exception.shouldBeInstanceOf<ArrayStoreException>()
        exception shouldHaveMessage "Test message"
    }

    @RepeatedTest(10)
    fun `NotHavingEmptyConstructorException 을 대상으로 메세지 없이 require() 를 하면 IllegalArgumentException 이 발생한다`() {
        val exception = shouldThrow<IllegalArgumentException> {
            require<NotHavingEmptyConstructorException>(false)
        }

        exception.shouldBeInstanceOf<IllegalArgumentException>()
        exception.message shouldBe PreconditionsSupporter.NO_SUITABLE_CONSTRUCTOR_MESSAGE
    }

    @RepeatedTest(10)
    fun `NotHavingMessageConstructorException 을 대상으로 메세지를 넣고 require() 를 하면 IllegalArgumentException 이 발생한다`() {
        val exception = shouldThrow<IllegalArgumentException> {
            require<NotHavingMessageConstructorException>(false) { "Test message" }
        }

        exception.shouldBeInstanceOf<IllegalArgumentException>()
        exception.message shouldBe PreconditionsSupporter.NO_SUITABLE_CONSTRUCTOR_MESSAGE
    }

    @RepeatedTest(10)
    fun `check() - 메시지를 넣지 않으면 메시지가 비어 있다`() {
        val exception = shouldThrow<ArrayStoreException> {
            PreconditionsSupporter.check<ArrayStoreException>(false)
        }

        exception.shouldBeInstanceOf<ArrayStoreException>()
        exception.message shouldBe null
    }

    @RepeatedTest(10)
    fun `check() - 메시지를 넣으면 메시지가 포함된다`() {
        val exception = shouldThrow<ArrayStoreException> {
            PreconditionsSupporter.check<ArrayStoreException>(false) { "Test message" }
        }

        exception.shouldBeInstanceOf<ArrayStoreException>()
        exception shouldHaveMessage "Test message"
    }

    @RepeatedTest(10)
    fun `NotHavingEmptyConstructorException 을 대상으로 메세지 없이 check() 를 하면 IllegalArgumentException 이 발생한다`() {
        val exception = shouldThrow<IllegalArgumentException> {
            PreconditionsSupporter.check<NotHavingEmptyConstructorException>(false)
        }

        exception.shouldBeInstanceOf<IllegalArgumentException>()
        exception.message shouldBe PreconditionsSupporter.NO_SUITABLE_CONSTRUCTOR_MESSAGE
    }

    @RepeatedTest(10)
    fun `NotHavingMessageConstructorException 을 대상으로 메세지를 넣고 check() 를 하면 IllegalArgumentException 이 발생한다`() {
        val exception = shouldThrow<IllegalArgumentException> {
            PreconditionsSupporter.check<NotHavingMessageConstructorException>(false) { "Test message" }
        }

        exception.shouldBeInstanceOf<IllegalArgumentException>()
        exception.message shouldBe PreconditionsSupporter.NO_SUITABLE_CONSTRUCTOR_MESSAGE
    }

    class NotHavingEmptyConstructorException(message: String) : RuntimeException(message)
    class NotHavingMessageConstructorException : RuntimeException()
}
