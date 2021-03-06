package me.blog.korn123.easydiary.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.aafactory.commons.utils.DateUtils
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_diary_main.toolbar
import kotlinx.android.synthetic.main.partial_daily_symbol_s.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.blog.korn123.commons.utils.FlavorUtils
import me.blog.korn123.easydiary.R
import me.blog.korn123.easydiary.adapters.DailySymbolAdapter
import me.blog.korn123.easydiary.extensions.config
import me.blog.korn123.easydiary.extensions.getLayoutLayoutInflater
import me.blog.korn123.easydiary.fragments.*
import me.blog.korn123.easydiary.helper.REQUEST_CODE_UPDATE_DAILY_SYMBOL_FILTER
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by CHO HANJOONG on 2017-03-16.
 */

class DashboardActivity : EasyDiaryActivity() {

    /***************************************************************************************************
     *   global properties
     *
     ***************************************************************************************************/
    private lateinit var mDailySymbolAdapter: DailySymbolAdapter
    private var mDailySymbolList: ArrayList<DailySymbolAdapter.DailySymbol> = arrayListOf()

    /***************************************************************************************************
     *   override functions
     *
     ***************************************************************************************************/
    public override fun onCreate(savedInstanceState: Bundle?) {
        // FIXME: Fixed a background thread processing error inside fragment when rotating the screen
        super.onCreate(null)

        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(toolbar)
        supportActionBar?.run {
            title = "Dashboard"
            setDisplayHomeAsUpEnabled(true)
        }

        supportFragmentManager.beginTransaction().run {
            replace(R.id.summary, DashBoardSummaryFragment())
            commit()
        }

        supportFragmentManager.beginTransaction().run {
            replace(R.id.lifetime, DashBoardRankFragment().apply {
                val args = Bundle()
                args.putString(DashBoardRankFragment.MODE_FLAG, DashBoardRankFragment.MODE_LIFETIME)
                arguments = args
            })
            commit()
        }

        supportFragmentManager.beginTransaction().run {
            replace(R.id.lastMonth, DashBoardRankFragment().apply {
                val args = Bundle()
                args.putString(DashBoardRankFragment.MODE_FLAG, DashBoardRankFragment.MODE_LAST_MONTH)
                arguments = args
            })
            commit()
        }

        supportFragmentManager.beginTransaction().run {
            replace(R.id.lastWeek, DashBoardRankFragment().apply {
                val args = Bundle()
                args.putString(DashBoardRankFragment.MODE_FLAG, DashBoardRankFragment.MODE_LAST_WEEK)
                arguments = args
            })
            commit()
        }

        val chartTitle = getString(R.string.statistics_creation_time)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.statistics1, BarChartFragment().apply {
                val args = Bundle()
                args.putString(BarChartFragment.CHART_TITLE, chartTitle)
                arguments = args
            })
            commit()
        }

        val symbolAllTitle = getString(R.string.statistics_symbol_all)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.statistics2, BarChartFragmentT2().apply {
                val args = Bundle()
                args.putString(BarChartFragment.CHART_TITLE, symbolAllTitle)
                arguments = args
            })
            commit()
        }

        val symbolTopTenTitle = getString(R.string.statistics_symbol_top_ten)
        supportFragmentManager.beginTransaction().run {
            replace(R.id.statistics3, HorizontalBarChartFragment().apply {
                val args = Bundle()
                args.putString(BarChartFragment.CHART_TITLE, symbolTopTenTitle)
                arguments = args
            })
            commit()
        }

        supportFragmentManager.executePendingTransactions()
        initializeDailySymbol()

        editSymbolFilter.setOnClickListener {
            startActivityForResult(Intent(this, SymbolFilterPickerActivity::class.java), REQUEST_CODE_UPDATE_DAILY_SYMBOL_FILTER)
//            TransitionHelper.startActivityWithTransition(this, Intent(this, SymbolFilterPickerActivity::class.java))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        when (resultCode == Activity.RESULT_OK/* && intent != null*/) {
            true -> {
                when (requestCode) {
                    REQUEST_CODE_UPDATE_DAILY_SYMBOL_FILTER -> {
                        updateDailyCard()
                    }
                }
            }
            false -> {}
        }
    }


    /***************************************************************************************************
     *   etc functions
     *
     ***************************************************************************************************/
    private fun initializeDailySymbol() {
        val dayOfMonth = SimpleDateFormat("dd", Locale.getDefault())
        val dateFormat = SimpleDateFormat(DateUtils.DATE_PATTERN_DASH, Locale.getDefault())
        val cal = Calendar.getInstance()
        cal.time = Date()
        month.text = cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())

        mDailySymbolAdapter = DailySymbolAdapter(
                this,
                mDailySymbolList
        )
        dailyCardRecyclerView?.apply {
//            layoutManager = androidx.recyclerview.widget.GridLayoutManager(this@DashboardActivity, 1)
            layoutManager = LinearLayoutManager(this@DashboardActivity, LinearLayoutManager.HORIZONTAL, false)
//            addItemDecoration(SettingsScheduleFragment.SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.card_layout_padding)))
            adapter = mDailySymbolAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    month.text = mDailySymbolList[(dailyCardRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()].date
                }
            })
        }

        GlobalScope.launch { // launch a new coroutine and keep a reference to its Job
            for (num in 1..365) {
                mDailySymbolList.add(DailySymbolAdapter.DailySymbol(dateFormat.format(cal.time), cal.get(Calendar.DAY_OF_WEEK), cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())!!, dayOfMonth.format(cal.time), cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())!!))
                cal.add(Calendar.DATE, -1)
            }
            runOnUiThread {
                updateDailyCard()
            }
        }
    }

    private fun updateDailyCard() {
        month.visibility = View.GONE
        dailyCardRecyclerView.visibility = View.GONE
        dailyCardProgressBar.visibility = View.VISIBLE
        selectedSymbolFlexBox.removeAllViews()

        GlobalScope.launch {
            config.selectedSymbols.split(",").map { sequence ->
                val symbolCard = getLayoutLayoutInflater().inflate(R.layout.partial_daily_symbol_s, null)
                runOnUiThread {
                    FlavorUtils.initWeatherView(this@DashboardActivity, symbolCard.dailySymbol, sequence.toInt())
                    selectedSymbolFlexBox.addView(symbolCard)
                }
            }
            runOnUiThread {
                mDailySymbolAdapter.notifyDataSetChanged()
                month.visibility = View.VISIBLE
                dailyCardRecyclerView.visibility = View.VISIBLE
                dailyCardProgressBar.visibility = View.GONE
            }
        }
    }
}
