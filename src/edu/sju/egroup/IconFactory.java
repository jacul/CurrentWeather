/**
 * Team E, CurrentWeather
 * Principal author: Xiangdong Zhu
 * UI design and icons: Yuning Zhang
 */
package edu.sju.egroup;

import java.io.InputStream;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

public class IconFactory implements NetworkConstant {
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
			Bitmap newIcon = null;
			InputStream is = IconFactory.class.getResourceAsStream("/res/drawable-ldpi/"
					+ iconpath.substring(iconpath.lastIndexOf('/') + 1));
			if (is != null) {
				newIcon = BitmapFactory.decodeStream(is);
			} else {
				newIcon = fetchIconFromWeb(iconpath);
			}
			if (newIcon == null) {
				return null;
			}
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
			return BitmapFactory.decodeStream(IconFactory.class.getResourceAsStream("/res/drawable-ldpi/na.png"));
		Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
		return bitmap;
	}
}
