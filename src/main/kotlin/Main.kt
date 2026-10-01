import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun report(result: BorrowResult): String = when (result) {
    is BorrowResult.Success -> "OK: '${result.book.title}' due ${result.dueDate}"
    is BorrowResult.LimitReached -> "DENIED: loan limit of ${result.limit} reached"
    BorrowResult.NotAvailable -> "DENIED: book already borrowed"
    is BorrowResult.NotFound -> "ERROR: no book with id ${result.id}"
}

fun main() {
    val libraryName: String = "City Library"
    var operations = 0

    val library = Library()
    listOf(
        Book(1, "Dune", "Frank Herbert", "Sci-Fi", 1965, 12.5),
        Book(2, "Neuromancer", "William Gibson", "Sci-Fi", 1984, 10.0),
        Book(3, "Clean Code", "Robert Martin", "Programming", 2008, 35.0),
        Book(4, "Kotlin in Action", "Dmitry Jemerov", "Programming", 2017, 40.0),
        Book(5, "Emma", "Jane Austen", "Classic", 1815, 8.0),
        Book(6, "Hamlet", "William Shakespeare", "Classic", 1603, 7.5)
    ).forEach(library::addBook)

    val alice = Student("Alice")
    val bob = Teacher("Bob")
    library.addMember(alice)
    library.addMember(bob)

    println("=== $libraryName ===")
    println(library.describe())

    val describables: List<Describable> = library.allMembers() + library
    for (d in describables) println(d.describe())

    println("\n--- Borrowing ---")
    val requests = listOf(alice to 1, alice to 1, bob to 1, alice to 2, alice to 3, alice to 4, bob to 99)
    for ((member, id) in requests) {
        val result = library.borrow(member, id)
        operations++
        println("${member.name} -> book #$id: ${report(result)}")
    }
    println("Operations performed: $operations")

    println("\n--- Collection operations ---")
    val classics = library.search { it.genre == "Classic" }
    println("Classics: ${classics.map { it.title }}")
    println("Books after 2000: ${library.search { it.year > 2000 }.map { "${it.title} (${it.year})" }}")
    println("Total value: ${library.totalValue()}")
    println("Available now: ${library.titlesOfAvailable()}")
    library.booksByGenre().forEach { (genre, list) ->
        println("$genre: ${list.size} book(s), avg price ${"%.2f".format(list.map { it.price }.average())}")
    }
    val authors: Set<String> = library.allBooks().map { it.author.substringAfterLast(' ') }.toSet()
    println("Author surnames (Set): $authors")

    val discounted = library.allBooks().first().copy(price = 9.99)
    println("Copy of first book: $discounted")

    println("\n--- Coroutines ---")
    runBlocking {
        val start = System.currentTimeMillis()

        val tasks = library.allBooks().map { book ->
            async { library.fetchBookRating(book.id) }
        }
        val ratings = tasks.awaitAll()

        val books = library.allBooks()
        for (i in books.indices) {
            println("${books[i].title}: rating ${ratings[i]}")
        }

        val time = System.currentTimeMillis() - start
        println("Fetched 6 ratings concurrently in ~${time}ms (sequentially it would take ~1800ms)")
    }
}
