package com.blockchainapp.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.blockchainapp.R
import com.blockchainapp.dagger.component.DaggerTxPresenterComponent
import com.blockchainapp.dagger.module.DataModule
import com.blockchainapp.data.error.Error
import com.blockchainapp.ui.model.TxUIModel
import com.blockchainapp.ui.transactioinlist.TransactionsAdapter
import javax.inject.Inject

/**
 * Main Activity is the container seen in the app. It just does minimum UI logic and instantiating presenter and calling presenter methods
 * Also implements View methods called by Presenter
 */
class MainActivity : AppCompatActivity(), TxsContract.View {

    //Dagger is used to inject Presenter with the relevant dependencies.
    @Inject
    lateinit var presenter: TxsContract.Presenter

    private lateinit var statusView: TextView
    private lateinit var listView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusView = findViewById(R.id.status_view)
        listView = findViewById(R.id.transactionListView)
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh)
        injectComponent()
        setupActionBar()
        setupSwipeToRefresh()
    }

    private fun setupSwipeToRefresh() {
        swipeRefreshLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            presenter.getTransactions()
        })
    }

    private fun setupActionBar() {
        supportActionBar?.let {
            it.title = getString(R.string.transactionListLabel)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
        presenter.getTransactions()
    }

    override fun onPause() {
        presenter.clear()
        super.onPause()
    }

    override fun onDataLoaded(model: TxUIModel) {
        snackbar?.dismiss()
        swipeRefreshLayout.isRefreshing = false
        listView.visibility = View.VISIBLE
        statusView.text = getString(R.string.fetch_balance).format(model.balance)
        supportActionBar?.subtitle = getString(R.string.number_of_transaction).format(model.allTransactions.size)
        listView.layoutManager = LinearLayoutManager(this)
        listView.adapter = TransactionsAdapter(model.allTransactions)
    }

    override fun onError(error: Error) {
        statusView.text = getString(R.string.fetch_error)
        swipeRefreshLayout.isRefreshing = false
        val rootView: View = findViewById<View>(android.R.id.content)
        snackbar = Snackbar.make(rootView, error.errorData.message, Snackbar.LENGTH_INDEFINITE)
            .setAction(getString(R.string.retry), { _ ->
                presenter.getTransactions()
            })
        snackbar?.show()
        supportActionBar?.subtitle = ""
        listView.visibility = View.GONE
    }

    override fun showFetchingStatusMessage() {
        statusView.text = getString(R.string.fetch_transaction)
        swipeRefreshLayout.isRefreshing = true
        listView.visibility = View.GONE
        supportActionBar?.subtitle = ""
    }

    private fun injectComponent() {
        val presenterComponent = DaggerTxPresenterComponent.builder()
            .dataModule(DataModule(this))
            .build()
        presenterComponent.inject(this)
    }
}
