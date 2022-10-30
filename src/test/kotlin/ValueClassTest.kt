import basic.fp.ValueClass
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class ValueClassTest {
    @Test
    fun `test invoke`() {
        val result = ValueClass.NonZeroIntSmart(0)
        assertThat(result).isNull()
    }
}
