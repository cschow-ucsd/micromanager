package ucsd.ieeeqp.fa19.service

import android.content.Context
import android.content.res.Resources
import android.util.Log
import ucsd.ieeeqp.fa19.R
import java.io.IOException
import java.util.*

class LocalhostConfig(
        private val context: Context
) {
    private val TAG = javaClass.simpleName

    operator fun get(name: String): String {
        try {
            val rawResource = context.resources.openRawResource(R.raw.config) // config.properties in raw folder
            val properties = Properties().apply {
                load(rawResource)
            }
            return properties.getProperty(name)
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Unable to find config.properties in raw resource folder.", e)
        } catch (e: IOException) {
            Log.e(TAG, "Failed to open config.properties file in raw resources folder.", e)
        }
        throw RuntimeException("Cannot read $name from config file.")
    }
}