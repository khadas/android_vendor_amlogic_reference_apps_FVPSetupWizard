package com.droidlogic.setupwizardext;

import android.content.Context;
import android.util.Log;

import com.droidlogic.dtvmgr.settings.DtvKitController;

import org.json.JSONArray;
import org.json.JSONObject;

public class CountrySetHelper {
    private DtvKitController controller;
    private String TAG ="";

    public CountrySetHelper(Context context) {
        controller = DtvKitController.getInstance(context);
    }

    public void unbind() {
        if (controller != null) {
            controller.unBindService();
        }
    }

    private JSONObject setCountry(int countryCode) {
        JSONObject resultObj = null;
        try {
            JSONArray args1 = new JSONArray();
            args1.put(countryCode);
            resultObj = controller.request("Dvb.setCountry", args1);
            if (resultObj != null) {
                Log.d(TAG, "setCountry resultObj:" + resultObj.toString());
            } else {
                Log.d(TAG, "setCountry then get null");
            }
        } catch (Exception e) {
            Log.d(TAG, "setCountry Exception " + e.getMessage() + ", trace=" + e.getStackTrace());
            e.printStackTrace();
        }
        return resultObj;
    }

    private JSONObject getCountrys() {
        JSONObject resultObj = null;
        try {
            JSONArray args1 = new JSONArray();
            resultObj = controller.request("Dvb.getCountrys", args1);
            if (resultObj != null) {
                Log.d(TAG, "getCountrys resultObj:" + resultObj.toString());
            } else {
                Log.d(TAG, "getCountrys then get null");
            }
        } catch (Exception e) {
            Log.d(TAG, "getCountrys Exception " + e.getMessage() + ", trace=" + e.getStackTrace());
            e.printStackTrace();
        }
        return resultObj;
    }

    public JSONObject setCountryCodeByIndex(int index) {
        JSONObject resultObj = null;
        try {
            resultObj = getCountrys();
            JSONArray data = null;
            if (resultObj != null) {
                data = (JSONArray)resultObj.get("data");
                if (data == null || data.length() == 0) {
                    return resultObj;
                }
                resultObj = (JSONObject)(data.get(index));
                if (resultObj != null) {
                    Log.d(TAG, "setCountryCodeByIndex resultObj = " + resultObj.toString());
                    setCountry((int)resultObj.get("country_code"));
                }
            } else {
                Log.d(TAG, "setCountryCodeByIndex then get null");
            }
        } catch (Exception e) {
            Log.d(TAG, "setCountryCodeByIndex Exception " + e.getMessage() + ", trace=" + e.getStackTrace());
            e.printStackTrace();
        }
        return resultObj;
    }

    public int getCurrentCountryCode() {
        int result = -1;//-1 means can't find
        int[] currentInfo = getCurrentLangParameter();
        if (currentInfo != null && currentInfo[0] != -1) {
            result = currentInfo[0];
        }
        return result;
    }

    public int[] getCurrentLangParameter() {
        int[] result = {-1, -1, -1, -1, -1};
        JSONObject resultObj = null;
        try {
            JSONArray args1 = new JSONArray();
            JSONObject data = null;
            resultObj = controller.request("Dvb.getcurrentCountryInfos", args1);
            if (resultObj != null) {
                data = (JSONObject)resultObj.get("data");
                if (data == null || data.length() == 0) {
                    return result;
                }
                resultObj = data;
                if (resultObj != null) {
                    Log.d(TAG, "getCurrentLangParameter resultObj = " + resultObj.toString());
                    result[0] = (int)(resultObj.get("country_code"));
                    result[1] = (int)(resultObj.get("a_pri_lang_id"));
                    result[2] = (int)(resultObj.get("a_sec_lang_id"));
                    result[3] = (int)(resultObj.get("t_pri_lang_id"));
                    result[4] = (int)(resultObj.get("t_sec_lang_id"));
                }
            } else {
                Log.d(TAG, "getCurrentLangParameter then get null");
            }
        } catch (Exception e) {
            Log.d(TAG, "getCurrentLangParameter Exception " + e.getMessage() + ", trace=" + e.getStackTrace());
            e.printStackTrace();
        }
        return result;
    }

    public String getCurrentCountryIso3Name() {
        String result = null;
        int currentCountryCode = getCurrentCountryCode();
        try {
            JSONObject resultObj = getCountries();
            JSONArray data = null;
            if (resultObj != null) {
                data = (JSONArray)resultObj.get("data");
                if (data == null || data.length() == 0) {
                    return result;
                }
                for (int i = 0; i < data.length(); i++) {
                    int countryCode = (int)(((JSONObject)(data.get(i))).get("country_code"));
                    if (countryCode == currentCountryCode) {
                        result = (String)(((JSONObject)(data.get(i))).get("country_name"));;
                        break;
                    }
                }
            } else {
                Log.d(TAG, "getCurrentCountryIso3Name then get null");
            }
        } catch (Exception e) {
            Log.d(TAG, "getCurrentCountryIso3Name Exception " + e.getMessage() + ", trace=" + e.getStackTrace());
            e.printStackTrace();
        }
        return result;
    }

    private JSONObject getCountries() {
        JSONObject resultObj = null;
        try {
            JSONArray args1 = new JSONArray();
            resultObj = controller.request("Dvb.getCountries", args1);
            if (resultObj != null) {
                Log.d(TAG, "getCountries resultObj:" + resultObj.toString());
            } else {
                Log.d(TAG, "getCountries then get null");
            }
        } catch (Exception e) {
            Log.d(TAG, "getCountries Exception " + e.getMessage() + ", trace=" + e.getStackTrace());
            e.printStackTrace();
        }
        return resultObj;
    }
}
