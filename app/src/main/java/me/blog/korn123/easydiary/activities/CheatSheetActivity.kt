package me.blog.korn123.easydiary.activities

import android.app.*
import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import me.blog.korn123.easydiary.R
import me.blog.korn123.easydiary.adapters.CheatSheetAdapter
import me.blog.korn123.easydiary.databinding.ActivityCheatSheetBinding
import me.blog.korn123.easydiary.extensions.*
import me.blog.korn123.easydiary.helper.*
import java.util.*

open class CheatSheetActivity : EasyDiaryActivity() {

    /***************************************************************************************************
     *   global properties
     *
     ***************************************************************************************************/
    private lateinit var mBinding: ActivityCheatSheetBinding
    private var mCheatSheetList = arrayListOf<CheatSheetAdapter.CheatSheet>()


    /***************************************************************************************************
     *   override functions
     *
     ***************************************************************************************************/
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_cheat_sheet)
        mBinding.lifecycleOwner = this

        setSupportActionBar(mBinding.toolbar)
        supportActionBar?.run {
            title = "Cheat Sheet"
            setDisplayHomeAsUpEnabled(true)
        }

        setupCheatSheet()
    }


    /***************************************************************************************************
     *   test functions
     *
     ***************************************************************************************************/
    private fun setupCheatSheet() {
        mCheatSheetList.run {
            add(CheatSheetAdapter.CheatSheet("Package kotlin", "Explanation of kotlin basic functions", "https://raw.githubusercontent.com/hanjoongcho/CheatSheet/master/kotlin/kotlin.md"))
            add(CheatSheetAdapter.CheatSheet("Package kotlin.collections", "Explanation of kotlin collection functions", "https://raw.githubusercontent.com/hanjoongcho/CheatSheet/master/kotlin/kotlin.collections.md"))
            add(CheatSheetAdapter.CheatSheet("Cheat Sheet", "This page is a collection of useful link information such as open source projects and development related guides.", "https://raw.githubusercontent.com/hanjoongcho/CheatSheet/master/README.md"))
            add(CheatSheetAdapter.CheatSheet("Spring Annotation", "Describes annotations mainly used in Spring Framework", "https://raw.githubusercontent.com/hanjoongcho/CheatSheet/master/annotations/spring.md"))

            add(CheatSheetAdapter.CheatSheet("데이터베이스 표준화", "국가상수도데이터베이스표준화지침(20210101 개정)", "https://raw.githubusercontent.com/hanjoongcho/CheatSheet/master/design/database/standardization.md"))

            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Lambda Expression",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/lambda/LambdaExpressionTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Default Methods",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/defaultmethod/DefaultMethodTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Functions",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/functions/FunctionFunctionalInterfaceTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Stream Count",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/streams/StreamWithCountTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Stream with Filter",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/streams/StreamWithFilterTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Stream with Map",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/streams/StreamWithMapTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Stream with Sorted",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/streams/StreamWithSortedTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Stream with Match",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/streams/StreamWithMatchTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Stream Reduce",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/streams/StreamReduceTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Stream Consumer",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/consumer/ConsumerFunctionalInterfaceTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Predicate",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/predicate/PredicateFunctionalInterfaceTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Comparator",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/comparator/ComparatorFunctionalInterfaceTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "Java 8 - Suppliers",
                    "",
                    "https://raw.githubusercontent.com/hanjoongcho/java8-guides-tutorials/master/src/test/java/suppliers/SupplierFunctionalInterfaceTest.java", true
            ))
            add(CheatSheetAdapter.CheatSheet(
                    "ES6",
                    "var, let, const",
                    "https://gist.githubusercontent.com/hanjoongcho/983fe388a669f1da9df13cf64f63c5f3/raw/d1587f1da1d7ead1ba695e50094dbf52daaf6e1e/var-let-const.md", false
            ))
        }

        mBinding.recyclerCheatSheet.apply {
            layoutManager = LinearLayoutManager(this@CheatSheetActivity, LinearLayoutManager.VERTICAL, false)
//            addItemDecoration(SettingsScheduleFragment.SpacesItemDecoration(resources.getDimensionPixelSize(R.dimen.card_layout_padding)))
            adapter =  CheatSheetAdapter(
                    this@CheatSheetActivity,
                    mCheatSheetList,
                    AdapterView.OnItemClickListener { _, _, position, _ ->
                        val item = mCheatSheetList[position]
                        TransitionHelper.startActivityWithTransition(this@CheatSheetActivity, Intent(this@CheatSheetActivity, MarkDownViewActivity::class.java).apply {
                            putExtra(MarkDownViewActivity.OPEN_URL_INFO, item.url)
                            putExtra(MarkDownViewActivity.OPEN_URL_DESCRIPTION, item.title)
                            putExtra(MarkDownViewActivity.FORCE_APPEND_CODE_BLOCK, item.forceAppendCodeBlock)
                        })
                    }
            )
        }
    }
}






