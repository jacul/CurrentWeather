package edu.sju.egroup;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;

public class UpdateIconTask extends AsyncTask<String, Integer, Bitmap> implements NetworkConstant {

	private String resourcePath;
	private Context context;
	private int widgetId;

	/**
	 * Create a updateIcontask to get the icon we need.
	 * @param context
	 * @param id
	 */
	public UpdateIconTask(Context context, int id) {
		this.context = context;
		this.widgetId = id;
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (bitmap == null)
			return;

		UpdateService.updateAppWidget(context, widgetId, bitmap);
	}

	@Override
	protected Bitmap doInBackground(String... params) {

		resourcePath = HOSTURL + Uri.encode(params[0].toString(), "/");

		Bitmap bitmap = IconFactory.getIcon(resourcePath);

		return bitmap;
	}

}
