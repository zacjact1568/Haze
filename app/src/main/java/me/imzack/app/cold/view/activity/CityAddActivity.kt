package me.imzack.app.cold.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_city_add.*
import kotlinx.android.synthetic.main.content_city_add.*
import me.imzack.app.cold.R
import me.imzack.app.cold.presenter.CityAddPresenter
import me.imzack.app.cold.view.adapter.CitySearchResultAdapter
import me.imzack.app.cold.view.contract.CityAddViewContract

class CityAddActivity : BaseActivity(), CityAddViewContract {

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, CityAddActivity::class.java))
        }
    }

    private val cityAddPresenter = CityAddPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        cityAddPresenter.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        cityAddPresenter.detach()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> exit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showInitialView(citySearchResultAdapter: CitySearchResultAdapter) {
        setContentView(R.layout.activity_city_add)

        setSupportActionBar(vToolbar)
        setupActionBar()

        vCitySearchEditor.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                cityAddPresenter.notifySearchTextChanged(s.toString())
            }
        })

        vCitySearchList.adapter = citySearchResultAdapter
        vCitySearchList.emptyView = vEmptyText
        vCitySearchList.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ -> cityAddPresenter.notifyCityListItemClicked(position) }

        // 初始隐藏没搜索到城市的提示（必须在 setEmptyView 后设置才有效）
        vEmptyText.visibility = View.GONE
    }

    override fun onSearchTextEmptied() {
        vEmptyText.visibility = View.GONE
    }
}
