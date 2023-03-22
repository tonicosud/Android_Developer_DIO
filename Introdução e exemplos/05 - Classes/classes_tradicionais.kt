/**
 * Classes Tradicionais.
*/

class Customer                                  // 1

class Contact(val id: Int, var email: String)   // 2

fun main() {

    val customer = Customer()                   // 3

    val contact = Contact(1, "mary@gmail.com")  // 4

    println(contact.id)                         // 5
    println(contact.email)
    contact.email = "jane@gmail.com"            // 6
    println(contact.email)
}