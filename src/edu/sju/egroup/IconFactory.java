package edu.sju.egroup;

import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class IconFactory implements NetworkConstant{
	// keep no instance
	private IconFactory() {
	};

	/**
	 * Local cache which holds icon files, avoids extra Http connections.
	 */
	private static HashMap<String, Bitmap> iconCache = new HashMap<String, Bitmap>();

	/**
	 * Get an icon with corresponding path.
	 * 
	 * @param iconpath
	 *            Url of this icon. Note it is not a absolute path. eg.
	 *            /ig/images/weather/sunny.gif
	 * @return The icon file represented by this url.
	 */
	public static Bitmap getIcon(String iconpath) {
		if (iconCache.containsKey(iconpath)) {
			return iconCache.get(iconpath);
		} else {
			Bitmap newIcon = fetchIconFromWeb(iconpath);
			iconCache.put(iconpath, newIcon);
			return newIcon;
		}
	}

	/**
	 * Download the icon file from network.
	 * 
	 * @param iconpath
	 * @return
	 */
	private static Bitmap fetchIconFromWeb(String iconpath) {
		byte[] b = UpdateService.getResource(HOSTURL + Uri.encode(iconpath, "/"));
		if (b == null)
			return null;
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		return bitmap;
	}
}
