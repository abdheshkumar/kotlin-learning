interface Fruit {
    fun getColor(): String
}

class Apple: Fruit {
    override fun getColor(): String {
        return "Red"
    }
}

interface Eater<T> {
    fun eat(food: T) {}
}

val fruitEater = object: Eater<Fruit> {
    override fun eat(food: Fruit) {
        println("Eating ${food.getColor()} color fruit")
    }
}

//val appleEater: Eater<Apple> = fruitEater
/*
This basically means it was expecting an eater who could eat apples, but we gave it an eater who could eat fruits. This sounds incorrect. If an eater can eat (all) fruits, and an apple is a fruit, it should also be able to eat an apple as well, right?
 */

interface EaterV<in T> {
    fun eat(food: T) {}
}
//`in` operator in front of T to mark it as contravariant on type T

val fruitEaterV = object: EaterV<Fruit> {
    override fun eat(food: Fruit) {
        println("Eating ${food.getColor()} color fruit")
    }
}

val appleEater: EaterV<Apple> = fruitEaterV

//val fruitEater: Eater<Fruit> = appleEater // won’t compile. Since apple eater can only consume apples and not any other fruit, we cannot assign it to a variable of type Eater<Fruit>.

/*
To summarize, if a class or interface is contravariant on type T (marked as <in T>), it means it expects (or consumes) an object of type T. In the example above, interface Eater<in T> means
 */

/*
If T = Apple, it can only consume apples, so it can’t be assigned to a variable that expects a fruit consumer as not all fruits are apples.
If T = Fruit, it can consume any fruit, so it can be assigned to a variable that expects an apple consumer as all apples are fruits.
 */

