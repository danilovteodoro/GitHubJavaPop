package dev.danilovteodoro.javapop.util

import android.content.SearchRecentSuggestionsProvider

class AppSuggestionProvider : SearchRecentSuggestionsProvider() {

    init {
        setupSuggestions(AUTHORITY, MODE)
    }

    companion object {
        const val AUTHORITY = "ev.danilovteodoro.javapop.AppSuggestionProvider"
        const val MODE:Int = SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES
    }
}