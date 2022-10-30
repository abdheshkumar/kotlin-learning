import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PairTest {
    @Test
    fun `test pair extractor`() {
        val pair: Pair<Int, String> = 10 to "Ten"
        val (n, _) = pair
        assertThat(n).isEqualTo(10)
    }
}
