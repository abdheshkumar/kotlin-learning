package fp

sealed interface FunList<out T> {
    fun forEach(f: (T) -> Unit) {
        tailrec fun go(list: FunList<T>, f: (T) -> Unit) {
            when (list) {
                is Cons -> {
                    f(list.head)
                    go(list.tail, f)
                }

                is Nil -> Unit // Do nothing
            }
        }
        go(this, f)
    }

    fun <R> fold(init: R, f: (R, T) -> R): R {
        tailrec fun go(list: FunList<T>, init: R, f: (R, T) -> R): R = when
        (list) {
            is Cons -> go(list.tail, f(init, list.head), f)
            is Nil -> init
        }
        return go(this, init, f)
    }

    fun reverse(): FunList<T> = fold(Nil as FunList<T>) { acc, i -> Cons(i, acc) }

    fun <R> foldRight(init: R, f: (T, R) -> R): R {
        return when (this) {
            is Cons -> f(this.head, tail.foldRight(init, f))
            Nil -> init
        }
    }

    fun <R> foldLeft(init: R, f: (R, T) -> R): R {
        return when (this) {
            is Cons -> this.tail.foldLeft(f(init, this.head), f)
            Nil -> init
        }
    }

    fun <R> map(f: (T) -> R): FunList<R> {
        return foldRight(Nil as FunList<R>) { a, acc -> Cons(f(a), acc) }
    }

    fun <R> mapL(f: (T) -> R): FunList<R> {
        return foldLeft(Nil as FunList<R>) { acc, a -> Cons(f(a), acc) }
    }

    // fun add(a: T): FunList<T> = Cons(a, this) // Type parameter T is declared as 'out' but occurs in 'in' position in type T kotlin

    object Nil : FunList<Nothing>

    data class Cons<out T>(val head: T, val tail: FunList<T>) : FunList<T>

    companion object {
        fun <A> of(vararg aa: A): FunList<A> {
            val tail = aa.sliceArray(1 until aa.size)
            return if (aa.isEmpty()) Nil else Cons(aa[0], of(*tail))
        }
    }
}

operator fun <T> FunList<T>.plus(t: T): FunList<T> = FunList.Cons(t, this)

object ListApp {

    @JvmStatic
    fun main(args: Array<String>) {
        val list = FunList.of(1, 2, 3, 4)
        list.forEach { print(it) }
        println()
        println(list)
        println(list + 12)
        println(list.map { it })
        println(list.mapL { it })
    }
}
