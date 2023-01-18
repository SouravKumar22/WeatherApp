package com.example.weatherapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val city: String = "Sonipat,Snp"
    val Api: String = "be5fdad5a5ce28f5d6f8351336fea8c2"
    var p :ProgressBar?= null
    var rl:RelativeLayout?=null
    var t: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        p= findViewById(R.id.loader)
        rl = findViewById(R.id.mainContainer)
        t = findViewById(R.id.errortext)
        WeatherTask().execute()
    }
    inner class WeatherTask(): AsyncTask<String, Void, String>() {
        override fun onPreExecute() {
            super.onPreExecute()
            p?.visibility = View.VISIBLE
            rl?.visibility = View.VISIBLE
            t?.visibility = View.GONE
        }

        override fun doInBackground(vararg  p0: String?): String? {
            var res: String?
            try {
                res = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$Api")
                        .readText(Charsets.UTF_8)
            }
            catch (e: Exception){
                res = null
                print("Inside catch")
            }
            return res
        }

        override fun onPostExecute(result: String?){
            super.onPostExecute(result)
            try{
                val jsonObj = JSONObject(result)
                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt:Long = jsonObj.getLong("dt")
                val updatedAtText = "Updated at: "+SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH ).format(Date(updatedAt*1000))
                val temp = main.getString("temp")+"°C"
                val tempMin = "Min Temp:"+main.getString("temp_min")+"°C"
                val tempMax = "Max Temp:"+main.getString("temp_max")+"°C"
                val pressure = main.getString("pressure")
                val humidity = main.getString("humidity")
                val sunrise:Long = sys.getLong("sunrise")
                val sunset:Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")
                val address = jsonObj.getString("name")+","+sys.getString("country")

                findViewById<TextView>(R.id.address).text = address
                findViewById<TextView>(R.id.updated_at).text = updatedAtText
                findViewById<TextView>(R.id.status).text = weatherDescription.capitalize()
                findViewById<TextView>(R.id.temp).text = temp
                findViewById<TextView>(R.id.temp_min).text = tempMin
                findViewById<TextView>(R.id.temp_max).text = tempMax
                findViewById<TextView>(R.id.sunrise).text = SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunrise*1000))
                findViewById<TextView>(R.id.sunset).text = SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sunset*1000))
                findViewById<TextView>(R.id.pressure).text = pressure
                findViewById<TextView>(R.id.humidity).text = humidity
                findViewById<TextView>(R.id.wind).text = windSpeed

                p?.visibility  = View.GONE
                rl?.visibility = View.VISIBLE
            }
            catch (e: Exception){
                p?.visibility = View.GONE
                t?.visibility = View.VISIBLE
            }
        }
    }
}