package cz.craftmania.logger.utils

import org.bukkit.Location
import org.json.simple.JSONObject

class LogUtils {

    companion object {

        fun prepareJsonLocation(location: Location): String {
            val locationObject = JSONObject()
            locationObject["world"] = location.world?.name
            locationObject["x"] = location.x
            locationObject["y"] = location.y
            locationObject["z"] = location.z
            locationObject["yaw"] = location.yaw
            locationObject["pitch"] = location.pitch
            return locationObject.toString()
        }
    }
}