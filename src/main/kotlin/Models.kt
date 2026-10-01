import java.time.LocalDate

data class Book(
    val id: Int,
    val title: String,
    val author: String,
    val genre: String,
    val year: Int,
    val price: Double
)

interface Describable {
    fun describe(): String
}

open class Member(val name: String, val maxLoans: Int) : Describable {
    private val _borrowed = mutableListOf<Book>()
    val borrowed: List<Book> get() = _borrowed

    fun canBorrow(): Boolean = _borrowed.size < maxLoans
    fun addLoan(book: Book) { _borrowed.add(book) }
    fun removeLoan(book: Book) { _borrowed.remove(book) }

    open fun finePerDay(): Double = 0.50

    override fun describe(): String =
        "Member $name (limit $maxLoans, fine/day ${"%.2f".format(finePerDay())})"
}

class Student(name: String) : Member(name, maxLoans = 3) {
    override fun finePerDay(): Double = 0.25
    override fun describe(): String = "Student ${super.describe()}"
}

class Teacher(name: String) : Member(name, maxLoans = 6) {
    override fun finePerDay(): Double = 0.10
    override fun describe(): String = "Teacher ${super.describe()}"
}

sealed class BorrowResult {
    data class Success(val book: Book, val dueDate: LocalDate) : BorrowResult()
    data class LimitReached(val limit: Int) : BorrowResult()
    object NotAvailable : BorrowResult()
    data class NotFound(val id: Int) : BorrowResult()
}
