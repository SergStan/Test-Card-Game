package com.example.testcardgameapplication.domain.model

class Player(
    val name: String,
    val startDeck: Deck = Deck(),
    val resultDeck: Deck = Deck()
) {

    val hasCard: Boolean
        get() = startDeck.cards.size > 0

    fun getCardFromTop(): Card {
        val playerTopCardIndex = startDeck.cards.size - 1
        return startDeck.cards[playerTopCardIndex]
    }

    fun removeCardFromTop() {
        val playerTopCardIndex = startDeck.cards.size - 1
        startDeck.cards.removeAt(playerTopCardIndex)
    }

    fun addTrick(trick: Pair<Card, Card>) {
        resultDeck.cards.add(trick.first)
        resultDeck.cards.add(trick.second)
    }
}