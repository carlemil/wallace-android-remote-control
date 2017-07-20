package se.jayway.wallaceremotecontrol

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

/**
 * Created by carlemil on 7/18/17.
 */

class LidarData {
    @SerializedName("theta")
    var theta = 0f
    @SerializedName("Dist")
    var distance = 0f
    @SerializedName("Q")
    var quality = 0
}
