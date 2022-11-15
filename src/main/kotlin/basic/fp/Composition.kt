package basic.fp

/** Define the type of function from A to B */
typealias Fun<A, B> = (A) -> B

/** A Predicate is a function returning a Boolean */
typealias Predicate<A> = Fun<A, Boolean>
