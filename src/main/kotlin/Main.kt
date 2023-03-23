const val CARD_1 = "Maestro"
const val CARD_2 = "MasterCard"
const val CARD_3 = "Visa"
const val CARD_4 = "Mir"
const val CARD_5 = "VK Pay"

const val MIN_AMOUNT_CARD_1 = 300_00
const val MAX_AMOUNT_CARD_1 = 75000_00
const val FIX_COMMISSION_CARD_1 = 20_00
const val COMMISSION_PERCENT_CARD_1 = 0.006
const val FIX_COMMISSION_CARD_3 = 35_00
const val COMMISSION_PERCENT_CARD_3 = 0.0075
const val COMMISSION_PERCENT_CARD_5 = 0

fun main() {
    //переводы Maestro
    result(CARD_1, 0, 100_00)            //перевод < 300руб. -> комиссия 0.6% + 20руб.
    result(CARD_1, 400_00, 100_00)       //от 300 до 75_000 -> комиссии не будет
    result(CARD_1, 100000_00, 50000_00)  //перевод > 75_000 -> комиссия 0.6% + 20 руб.

    //переводы MasterCard
    result(CARD_2, 0, 100_00)
    result(CARD_2, 400_00, 100_00)
    result(CARD_2, 100000_00, 50000_00)

    //переводы Visa
    result(CARD_3, 0, 100_00)
    result(CARD_3, 40000_00, 1000_00)
    result(CARD_3, 100000_00, 5000_00)

    //переводы Mir
    result(CARD_4, 0, 100_00)
    result(CARD_4, 400_00, 1000_00)
    result(CARD_4, 100000_00, 5000_00)

    //переводы VK Pay
    result(CARD_5, 0, 15000_00)
    result(CARD_5, 40000_00, 5000_00)
    result(CARD_5, 100000_00, 15000_00)

}

fun result(card: String, monthlyTransfers: Int = 0, amountTransfer: Int) {
    println()
    println("Карта: $card")
    println("Сумма перевода: ${convertToRubKop(amountTransfer)}")
    println("Сумма транзакций в текущем месяце: ${convertToRubKop(monthlyTransfers)}")
    println("Комиссия: ${convertToRubKop(commission(card, monthlyTransfers, amountTransfer))}")
}

fun convertToRubKop(amount: Int): String {
    return (amount / 100).toString() + " руб. " + amount % 100 + " коп."
}

fun commission(card: String, monthlyTransfers: Int, amountTransfer: Int): Int {
    when (card) {
        CARD_1, CARD_2 -> if (amountTransfer <= 150000_00 && monthlyTransfers <= 600000_00) {
            return if (monthlyTransfers + amountTransfer in MIN_AMOUNT_CARD_1..MAX_AMOUNT_CARD_1) 0
            else ((monthlyTransfers + amountTransfer) * COMMISSION_PERCENT_CARD_1 + FIX_COMMISSION_CARD_1).toInt()
        } else println("Вы превысили лимит по переводам!")


        CARD_3, CARD_4 -> if (amountTransfer <= 150000_00 || monthlyTransfers <= 600000_00) {
            return if (amountTransfer * COMMISSION_PERCENT_CARD_3 > FIX_COMMISSION_CARD_3) {
                (COMMISSION_PERCENT_CARD_3 * amountTransfer).toInt()
            } else FIX_COMMISSION_CARD_3
        } else println("Вы превысили лимит по переводам!")


        CARD_5 -> if (amountTransfer <= 15000_00 && monthlyTransfers <= 40000_00) {
            (amountTransfer + monthlyTransfers) * COMMISSION_PERCENT_CARD_5
        } else println("Вы превысили лимит по переводам!")
    }
    return 0
}