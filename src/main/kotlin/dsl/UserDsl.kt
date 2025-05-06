package dsl

import model.CompanyBuilder
import model.TwitterBuilder
import model.User
import model.UserBuilder

@ClientDsl
fun createUser(c: UserBuilderDsl.() -> Unit): User {
    val builder = UserBuilderDsl()
    c(builder)
    return builder.build()
}

@DslMarker
annotation class ClientDsl

@ClientDsl
class CompanyBuilderDsl : CompanyBuilder()

@ClientDsl
class TwitterBuilderDsl : TwitterBuilder()

@ClientDsl
class UserBuilderDsl : UserBuilder()

@ClientDsl
fun UserBuilder.company(t: CompanyBuilderDsl.() -> Unit) {
    company = CompanyBuilderDsl().apply(t).build()
}

@ClientDsl
fun UserBuilder.twitter(t: TwitterBuilderDsl.() -> Unit) {
    twitter = TwitterBuilderDsl().apply(t).build()
}
