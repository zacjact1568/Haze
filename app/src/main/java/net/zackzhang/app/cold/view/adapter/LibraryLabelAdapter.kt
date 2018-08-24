package net.zackzhang.app.cold.view.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_label_library.*
import net.zackzhang.app.cold.R
import net.zackzhang.app.cold.util.SystemUtil

class LibraryLabelAdapter(private val activity: Activity) : RecyclerView.Adapter<LibraryLabelAdapter.ItemViewHolder>() {

    private val libraries = arrayOf(
            Pair("HeWeather", "https://www.heweather.com"),
            Pair("Android Support Library", "https://developer.android.com/topic/libraries/support-library/"),
            Pair("EventBus", "http://greenrobot.org/eventbus/"),
            Pair("Gson", "https://github.com/google/gson"),
            Pair("OkHttp", "http://square.github.io/okhttp/"),
            Pair("Retrofit", "http://square.github.io/retrofit/"),
            Pair("RxJava", "https://github.com/ReactiveX/RxJava"),
            Pair("InkPageIndicator", "https://github.com/DavidPacioianu/InkPageIndicator"),
            Pair("Kotlin", "http://kotlinlang.org"),
            Pair("AMap Location Service", "https://lbs.amap.com/api/android-location-sdk/"),
            Pair("Stetho", "http://facebook.github.io/stetho/"),
            Pair("Android Architecture Components", "https://developer.android.com/topic/libraries/architecture/"),
            Pair("FlexboxLayout", "https://github.com/google/flexbox-layout"),
            Pair("Material Design Icons", "https://material.io/tools/icons/"),
            Pair("Font Awesome", "https://fontawesome.com")
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_label_library, parent, false))

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val (name, link) = libraries[position]

        holder.vLibraryText.text = name

        holder.itemView.setOnClickListener { SystemUtil.openLink(link, activity) }
    }

    override fun getItemCount() = libraries.size

    class ItemViewHolder(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer
}
