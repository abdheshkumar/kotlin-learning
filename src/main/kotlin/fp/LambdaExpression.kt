package fp

object LambdaExpression {

    val operation = { a: Int, b: Int -> a + b }

    @JvmStatic
    fun main(args: Array<String>) {
        println(operation(1, 2))
        println(operation.invoke(1, 2))
        findStudentTest()
    }

    interface Combinable<A> {
        fun combine(rh: A): A
    }

    fun <A : Combinable<A>> combine(lh: A, rh: A): A = lh.combine(rh)

    private fun addL(x: () -> Int, y: () -> Int): () -> Int {
        return { println("addL"); x() + y() }
    }

    fun tripleL(x: () -> Int): () -> Int {
        return { println("tripleL"); addL(addL(x, x), x)() }
    }

    private fun divideL(x: () -> Int, y: () -> Int): () -> Int {
        return { println("divideL"); x() / y() }
    }

    fun averageL(x: () -> Int, y: () -> Int): () -> Int {
        return { println("averageL"); divideL(addL(x, y), { 2 })() }
    }

    data class Student(val name: String, val id: Int)

    private fun findSpock(list: List<Student>) {
        list.forEach label@{ // or simply omit and use return@foreach instead
            if (it.name == "Spock") {
                println("Found Spock")
                return@label
            }
        }
        println("Did we find Spock?")
    }

    private fun findSpockWithAnonymousFun(list: List<Student>) {
        list.forEach(fun(student) {
            if (student.name == "Spock") {
                println("Found Spock")
                return
            }
        })
        println("Did we find Spock?")
    }

    private fun findStudentTest() {
        val studentList = listOf(Student("Kirk", 12345), Student("Spock", 54321))
        findSpock(studentList)
        println("End of findStudentTest")
    }

    private inline fun findSpock(list: List<Student>, func: (Student) -> Unit) {
        list.forEach { func(it) }
        println("Did we find Spock?")
    }

    fun findStudentTest1() {
        val studentList = listOf(Student("Kirk", 12345), Student("Spock", 54321))
        findSpock(studentList) {
            if (it.name == "Spock") {
                println("Found Spock")
                return // Compile error if your remove inline from findSpock function
            }
            println("Not Spock")
        }
        println("End of findStudentTest")
    }
}
