package basic

import Apple
import Fruit

interface Seller<T> {
    fun sell(): T
}

val appleSeller: Seller<Apple> = object : Seller<Apple> {
    override fun sell(): Apple {
        return Apple()
    }
}

//val fruitSeller: Seller<Fruit> = appleSeller // It throws an error: Required Fruit. Found Apple

/*
This, again, sounds a bit incorrect. I have an apple seller who sells apples. I should be able to assign it to a fruit seller since apples are also fruits. Basically, an apple seller sells apples, and all apples are fruits, so it should be okay to consider it as a fruit seller as well. To fix this, let us change the Seller interface as below:
 */

interface SellerV<out T> {
    fun sell(): T
}

val appleSellerV: SellerV<Apple> = object : SellerV<Apple> {
    override fun sell(): Apple {
        return Apple()
    }
}


val fruitSeller: SellerV<Fruit> = appleSellerV

//val appleSellerVV: SellerV<Apple> = fruitSeller

/*
The above statement won’t compile. Since a fruit seller can sell any fruit and not just apples, we cannot assign it to a variable of type apple seller.
 */

/*
To summarise, if a class or interface is covariant on type T (marked as <out T>), it basically means it emits (or produces) an object of type T. It becomes compatible with any consumer that can consume items of type T. In the example above, interface Seller<out T>

If T = Apple, it produces an Apple, so it CAN be assigned to a variable that expects a fruit producer.
If T = Fruit, it produces a fruit, so it can't be assigned to a variable that expects an apple producer, as not all fruits are apples.
 */