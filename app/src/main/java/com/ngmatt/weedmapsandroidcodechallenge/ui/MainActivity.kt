package com.ngmatt.weedmapsandroidcodechallenge.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ngmatt.weedmapsandroidcodechallenge.R
import com.ngmatt.weedmapsandroidcodechallenge.database.AppDatabase
import com.ngmatt.weedmapsandroidcodechallenge.database.BusinessEntity
import com.ngmatt.weedmapsandroidcodechallenge.network.api.NetworkingHelper
import com.ngmatt.weedmapsandroidcodechallenge.network.model.BusinessResponse
import com.ngmatt.weedmapsandroidcodechallenge.network.repository.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Created by Matt Ng on 9/14/20
 */
class MainActivity: AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var isLoadingNext = false
    private var recyclerViewState: Parcelable? = null
    private var resultOffset = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = MainViewModel(Repository(NetworkingHelper.getService()), application)
        checkPermission()
        loadUI()
    }

    private fun loadUI() {
        val searchTextView = findViewById<AutoCompleteTextView>(R.id.main_activity_autoTextView)
        searchTextView.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(textView: TextView?, action: Int, event: KeyEvent?): Boolean {
                if (action == EditorInfo.IME_ACTION_SEARCH) {
                    getLocation(object: OnLocationFoundListener{
                        override fun onFound(lat: Double, lng: Double) {
                            hideKeyboard(searchTextView)
                            resultOffset = 0
                            search(textView?.text.toString(), lat, lng)
                        }
                    })
                    return true
                }
                return false
            }
        })
    }

    private fun search(term: String, lat: Double?, lng: Double?) {
        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getDatabase(this@MainActivity).businessDao().getCachedResults(term).collect {
                if (it.isEmpty()) {
                    fetchResults(term, lat, lng)
                } else {
                   withContext(Dispatchers.Main) {
                       updateUI(term, it, lat, lng)
                   }
                }
            }
        }
    }

    private fun fetchResults(term: String, lat: Double?, lng: Double?) {
        viewModel.getBusinesses(term, lat, lng, resultOffset, object: Repository.OnTaskCompleteListener{
            override fun onSuccess(response: Any?) {
                //do nothing, database handles now
                isLoadingNext = false
            }

            override fun onFailure(t: Throwable) {
                showError(t.localizedMessage)
                t.printStackTrace()
            }

            override fun onError(response: Any?, code: Int?) {
                showError(getString(R.string.http_error_message) + " " + code)
            }
        })
    }

    private fun getLocation(listener: OnLocationFoundListener) {
        try{
            val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10f) { location ->
                listener.onFound(location.latitude, location.longitude)
            }
        } catch(exception: SecurityException) {
            Log.e(MainActivity::class.java.name, "Location Permission Not found")
        }
    }

    private fun updateUI(term: String, results: List<BusinessEntity>, lat: Double?, lng: Double?) {
        resultOffset += results.size
        val resultsTextView = findViewById<TextView>(R.id.main_activity_header_textView)
        resultsTextView.text = getString(R.string.main_activity_header_text) + " $term"
        val recyclerView = findViewById<RecyclerView>(R.id.main_activity_recyclerView)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = BusinessAdapter(results.distinctBy { it.businessId })
        recyclerView.layoutManager?.onRestoreInstanceState(recyclerViewState)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if(!recyclerView.canScrollVertically(1) && !isLoadingNext && newState==RecyclerView.SCROLL_STATE_IDLE) {
                    Log.d("RecyclerView:: ", "reached bottom")
                    isLoadingNext = true
                    fetchResults(term, lat, lng)
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    interface OnLocationFoundListener {
        fun onFound(lat: Double, lng: Double)
    }

    inner class BusinessAdapter(private val data: List<BusinessEntity>) : RecyclerView.Adapter<BusinessAdapter.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(LayoutInflater.from(this@MainActivity).inflate(R.layout.result_recycler_item, parent, false))
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = data[position]
            holder.nameTextView.text = item.name
            holder.ratingView.rating = item.rating!!.toFloat()

            //added to initial design to better differentiate the search results,
            // I.E. when searching "Target", most results had the same Name and Image
            holder.locationTextView.text = item.address

           // holder.reviewTextView.text = item.
            Glide.with(this@MainActivity).load(item.image).centerCrop().into(holder.imageView)
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val imageView: ImageView = itemView.findViewById(R.id.item_imageView)
            val nameTextView: TextView = itemView.findViewById(R.id.item_name_textView)
            val ratingView: RatingBar = itemView.findViewById(R.id.item_rating_view)
            val locationTextView: TextView = itemView.findViewById(R.id.item_location_textView)
            val reviewTextView: TextView = itemView.findViewById(R.id.item_review_textView)
        }
    }

    private fun showError(message: String?) {
        Toast.makeText(this@MainActivity, message, Toast.LENGTH_LONG).show()
    }

    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 0)
            checkPermission()
        }
        else {
            getLocation(object: OnLocationFoundListener{
                override fun onFound(lat: Double, lng: Double) {
                    latitude = lat
                    longitude = lng
                }
            })
        }
    }

    private fun hideKeyboard(editText: EditText) {
        val inputService: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputService.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}