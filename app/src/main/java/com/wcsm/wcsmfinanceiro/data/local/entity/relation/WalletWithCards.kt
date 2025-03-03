package com.wcsm.wcsmfinanceiro.data.local.entity.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.wcsm.wcsmfinanceiro.data.local.entity.Wallet
import com.wcsm.wcsmfinanceiro.data.local.entity.WalletCard

data class WalletWithCards(
    @Embedded
    val wallet: Wallet,
    @Relation(
        parentColumn = "wallet_id",
        entityColumn = "wallet_id",
        entity = WalletCard::class,
    )
    val walletCards: List<WalletCard>
)
