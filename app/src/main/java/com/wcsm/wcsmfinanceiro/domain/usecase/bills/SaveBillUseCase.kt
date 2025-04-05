package com.wcsm.wcsmfinanceiro.domain.usecase.bills

import com.wcsm.wcsmfinanceiro.data.local.entity.Bill
import com.wcsm.wcsmfinanceiro.data.local.model.BillType
import com.wcsm.wcsmfinanceiro.domain.model.Response
import com.wcsm.wcsmfinanceiro.domain.repository.BillsRepository
import com.wcsm.wcsmfinanceiro.domain.repository.WalletRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject

class SaveBillUseCase @Inject constructor(
    private val billsRepository: BillsRepository,
    private val walletRepository: WalletRepository
) {
    suspend operator fun invoke(newBill: Bill) : Flow<Response<Long>> {
        if(newBill.value > 9999999.99) {
            return flow {
                emit(Response.Error("Valor muito alto (max. R$9.999.999,99)."))
            }
        }

        val bill = newBill.copy(billId = generateUniqueId())

        if(newBill.paid) {
            val walletWithCards = walletRepository.getWalletWithCardById(newBill.walletId)

            val isWalletUpdateValid = canWalletBalanceBeUpdated(
                isIncomeBill = newBill.billType == BillType.INCOME,
                actualBalance = walletWithCards.wallet.balance,
                amount = newBill.value
            )

            if(!isWalletUpdateValid.first) {
                return flow {
                    emit(Response.Error(isWalletUpdateValid.second))
                }
            }

            val formatedBillValue = if(bill.billType == BillType.INCOME) {
                bill.value
            } else {
                -bill.value
            }

            val newBillValue = Pair(bill.billId, formatedBillValue)
            val walletBills = walletWithCards.wallet.walletBills
            val indexToUpdate = walletBills.indexOfFirst { it.first == newBillValue.first }
            val updatedWalletBills = walletBills.toMutableList().apply {
                if(indexToUpdate != -1) {
                    this[indexToUpdate] = newBillValue
                } else {
                    add(newBillValue)
                }
            }
            val updatedWallet = walletWithCards.wallet.copy(
                walletBills = updatedWalletBills
            )

            walletRepository.updateWallet(wallet = updatedWallet).collect {}
        }

        return billsRepository.saveBill(bill)
    }

    private fun generateUniqueId(): String {
        return UUID.randomUUID().toString()
    }

    private fun canWalletBalanceBeUpdated(
        isIncomeBill: Boolean,
        actualBalance: Double,
        amount: Double
    ): Pair<Boolean, String> {
        if(isIncomeBill && actualBalance + amount > 9999999.99) {
            return Pair(false, "Não foi possível salvar esta conta, saldo ultrapassa limite.")
        }

        if(!isIncomeBill && actualBalance - amount < 0) {
            Pair(false, "Não foi possível salvar esta conta, saldo insuficiente.")
        }

        return Pair(true, "")
    }
}