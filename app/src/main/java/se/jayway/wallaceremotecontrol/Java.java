package se.jayway.wallaceremotecontrol;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by carlemil on 7/18/17.
 */

public class Java {
    Gson gson = new Gson();

    @SerializedName("theta")
    float theta = 0;
    @SerializedName("dist")
    float dist = 0;
    @SerializedName("Q")
    int Q = 0;

    public List<LidarData> getLidarOneLine() {
        List list = gson.fromJson(oneRotation, new TypeToken<List<LidarData>>() {
        }.getType());
        return list;
    }

    String oneRotation = "{data:[" +
            "{ theta: 0.05, Dist: 01445.00, Q: 47 }," +
            "{ theta: 0.47, Dist: 01452.00, Q: 47 },";
}
