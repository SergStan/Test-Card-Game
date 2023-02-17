package com.example.testcardgameapplication.domain.model

import com.example.testcardgameapplication.domain.Game.Companion.randomInt

class Dealer {

    private lateinit var deck: Deck

    fun openDeck(): Deck {
        val cards: MutableList<Card> = Card.values().map { it } as MutableList<Card>
        deck = Deck(cards)
        return deck
    }

    fun shuffleDeck(deck: Deck): Deck {
        val startIndex = 0
        val endIndex = deck.cards.size - 1
        for (i in 0..MAXIMUM_SHUFFLE_NUMBER) {
            val firstIndex = randomInt(startIndex, endIndex)
            val secondIndex = randomInt(startIndex, endIndex)
            val tempValue = deck.cards[firstIndex]
            deck.cards[firstIndex] = deck.cards[secondIndex]
            deck.cards[secondIndex] = tempValue
        }
        return deck
    }

    fun dealСard(deck: Deck): Card? {
        return if (deck.cards.isNotEmpty()) {
            val lastIndex = deck.cards.size - 1
            val card = deck.cards[lastIndex]
            deck.cards.removeAt(lastIndex)
            card
        } else {
            null
        }
    }

    companion object {
        private const val MAXIMUM_SHUFFLE_NUMBER = 579
    }
}