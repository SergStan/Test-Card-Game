package com.example.testcardgameapplication.presentation.history

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testcardgameapplication.R
import com.example.testcardgameapplication.databinding.ActivityHistoryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class HistoryActivity : AppCompatActivity() {

    private val historyViewModel: HistoryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val historyAdapter = HistoryAdapter(layoutInflater)

        with(binding.historyRv) {
            layoutManager = LinearLayoutManager(this@HistoryActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this@HistoryActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            adapter = historyAdapter
        }

        historyViewModel.result.observe(this) { state ->
            with(binding) {
                when (state) {
                    is HistoryState.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }
                    is HistoryState.Content -> {
                        progressBar.visibility = View.GONE
                        historyAdapter.submitList(state.history)
                    }
                    is HistoryState.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(
                            this@HistoryActivity,
                            state.throwable.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e("TAAG", "Exception loading data", state.throwable)
                    }
                }
            }
        }
        historyViewModel.loadHistory()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.history_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_clear_history -> historyViewModel.clearHistory()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, HistoryActivity::class.java)
        }
    }
}