const val CARD_1 = "Maestro"
const val CARD_2 = "MasterCard"
const val CARD_3 = "Visa"
const val CARD_4 = "Mir"
const val CARD_5 = "VK Pay"

const val MIN_AMOUNT_CARD_1 = 300_00
const val MAX_AMOUNT_CARD_1 = 75_000_00
const val FIX_COMMISSION_CARD_1 = 20_00
const val COMMISSION_PERCENT_CARD_1 = 0.006
const val FIX_COMMISSION_CARD_3 = 35_00
const val COMMISSION_PERCENT_CARD_3 = 0.0075

fun main() {
    //переводы Maestro
    result(CARD_1, 0, 100_00)                //перевод < 300руб. -> комиссия 0.6% + 20руб.
    result(CARD_1, 400_00, 100_00)           //от 300 до 75_000 -> комиссии не будет
    result(CARD_1, 100_000_00, 50_000_00)    //перевод > 75_000 -> комиссия 0.6% + 20 руб.

    //переводы MasterCard
    result(CARD_2, 0, 100_00)
    result(CARD_2, 400_00, 151_000_00)        //превышение лимита по переводам в день
    result(CARD_2, 700_000_00, 250_000_00)    //превышение лимита по переводам в месяц

    //переводы Visa
    result(CARD_3, 0, 100_00)
    result(CARD_3, 40_000_00, 1_000_00)
    result(CARD_3, 100_000_00, 5_000_00)

    //переводы Mir
    result(CARD_4, 0, 100_00)
    result(CARD_4, 400_00, 1_000_00)
    result(CARD_4, 100_000_00, 5_000_00)

    //не указываю карту - по умолчанию используется система VK Pay
    result(monthlyTransfers = 0, amountTransfer = 15_000_00)
    result(monthlyTransfers = 40_000_00, amountTransfer = 16_000_00)        //превышение лимита по переводу за один раз
    result(monthlyTransfers = 100_000_00, amountTransfer = 15_000_00)       //превышение лимита по переводам в месяц

}

fun result(card: String = CARD_5, monthlyTransfers: Int, amountTransfer: Int) {
    println()
    println("Карта: $card")
    println("Сумма перевода: ${convertToRubKop(amountTransfer)}")
    println("Сумма транзакций в текущем месяце: ${convertToRubKop(monthlyTransfers)}")
    println("Комиссия: ${convertToRubKop(commission(card, monthlyTransfers, amountTransfer))}")
    println(limTransferPrint(card, monthlyTransfers, amountTransfer))
}

//Функция для руб. коп.
fun convertToRubKop(amount: Int): String {
    return (amount / 100).toString() + " руб. " + amount % 100 + " коп."
}

//Функция считающая комиссию
fun commission(card: String = CARD_5, monthlyTransfers: Int = 0, amountTransfer: Int): Int {
    when (card) {
        CARD_1, CARD_2 -> return if (monthlyTransfers + amountTransfer in MIN_AMOUNT_CARD_1..MAX_AMOUNT_CARD_1) 0
        else ((monthlyTransfers + amountTransfer) * COMMISSION_PERCENT_CARD_1 + FIX_COMMISSION_CARD_1).toInt()

        CARD_3, CARD_4 ->
            return if (amountTransfer * COMMISSION_PERCENT_CARD_3 > FIX_COMMISSION_CARD_3) {
                (COMMISSION_PERCENT_CARD_3 * amountTransfer).toInt()
            } else FIX_COMMISSION_CARD_3
    }
    return 0
}

//Функция для лимитов за переводы
fun limTransferPrint(card: String, monthlyTransfers: Int, amountTransfer: Int): String {
    when (card) {
        CARD_1, CARD_2, CARD_3, CARD_4 -> if (monthlyTransfers > 600_000_00) {
            return "Вы превысили лимит по переводам в этом месяце!"
        } else if (amountTransfer > 150_000_00) {
            return "Вы превысили лимит по переводам в день!"
        }

        CARD_5 -> if (monthlyTransfers > 40_000_00) {
            return "Вы превысили лимит по переводам в этом месяце!"
        } else if (amountTransfer > 15_000_00) {
            return "Вы превысили лимит по переводам за один раз!"
        }
    }
    return ""
}