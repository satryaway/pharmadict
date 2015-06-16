package com.samstudio.pharmadict.helpers;

import com.samstudio.pharmadict.util.CommonConstants;

public class URLHelper {
	
	public static String buildURLThumb(String obat_pic){
		return CommonConstants.WEB_SERVICE_URL_IMAGE+obat_pic;
	}
}
