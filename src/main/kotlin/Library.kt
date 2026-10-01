import kotlinx.coroutines.delay
import java.time.LocalDate

class Library : Describable {
    private val books = mutableMapOf<Int, Book>()          // Map: id -> book
    private val available = mutableSetOf<Int>()            // Set: ids of books on the shelf
    private val members = mutableListOf<Member>()          // List

    fun addBook(book: Book) {
        books[book.id] = book
        available.add(book.id)
    }

    fun addMember(member: Member) { members.add(member) }

    fun borrow(member: Member, bookId: Int, today: LocalDate = LocalDate.now()): BorrowResult {
        val book = books[bookId] ?: return BorrowResult.NotFound(bookId)
        return when {
            bookId !in available -> BorrowResult.NotAvailable
            !member.canBorrow() -> BorrowResult.LimitReached(member.maxLoans)
            else -> {
                available.remove(bookId)
                member.addLoan(book)
                BorrowResult.Success(book, today.plusDays(14))
            }
        }
    }

    fun giveBack(member: Member, book: Book) {
        member.removeLoan(book)
        available.add(book.id)
    }

    // Suspend function simulating a slow remote call (e.g. a database / network)
    suspend fun fetchBookRating(bookId: Int): Double {
        delay(300)
        return 3.0 + (bookId % 20) / 10.0
    }

    // Higher-order function: filter by any predicate
    fun search(predicate: (Book) -> Boolean): List<Book> = books.values.filter(predicate)

    // Collection operations
    fun booksByGenre(): Map<String, List<Book>> = books.values.groupBy { it.genre }
    fun titlesOfAvailable(): List<String> = available.mapNotNull { books[it]?.title }.sorted()
    fun totalValue(): Double = books.values.map { it.price }.reduce { a, b -> a + b }
    fun allBooks(): List<Book> = books.values.toList()
    fun allMembers(): List<Member> = members

    override fun describe(): String =
        "Library: ${books.size} books, ${available.size} available, ${members.size} members"
}
