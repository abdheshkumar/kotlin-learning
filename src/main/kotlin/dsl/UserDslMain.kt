package dsl

fun main() {
    //Example, without @DslMarker, you can see it is allowing to call company inside the twitter scope.
    // To avoid this problem, we should use @DslMarker
    /*
    val user1 = createUser {
        firstName = "Anton"
        lastName = "Arhipov"
        twitter {
            handle = "@antonarhipov"
            company {
                name = "JetBrains"
                city = "Tallinn"
            }
        }
        company {
            name = "JetBrains"
            city = "Tallinn"
        }
        dob = 24 March 1981
    }
    println("Created user is: $user1")
     */
    val user2 = createUser {
        firstName = "Anton"
        lastName = "Arhipov"
        twitter {
            handle = "@antonarhipov"
            /*company { // throw an error
                name = "JetBrains"
                city = "Tallinn"
            }*/
        }
        company {
            name = "JetBrains"
            city = "Tallinn"
        }
        dob = 24 March 1981
    }
    println("Created user is: $user2")
}