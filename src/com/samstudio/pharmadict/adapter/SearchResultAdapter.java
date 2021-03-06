package com.samstudio.pharmadict.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.samstudio.pharmadict.R;
import com.samstudio.pharmadict.entities.Obat;
import com.samstudio.pharmadict.helpers.Seeder;

public class SearchResultAdapter extends BaseAdapter {
	private Context context;
	private List<Obat> listObat = new ArrayList<Obat>();
	private ImageLoader imageLoader = ImageLoader.getInstance();

	public SearchResultAdapter(Context context, List<Obat> listObat) {
		this.context = context;
		this.listObat = listObat;
		initImageLoader();
	}

	public void updateContent(List<Obat> obat) {
		this.listObat = obat;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return listObat.size();
	}

	@Override
	public Object getItem(int position) {
		return listObat.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.list_item, parent, false);

			holder.thumbnail = (ImageView) convertView
					.findViewById(R.id.thumbnail);
			holder.namaTV = (TextView) convertView.findViewById(R.id.namaTV);
			holder.indikasiTV = (TextView) convertView
					.findViewById(R.id.indikasiTV);
			holder.jenisTV = (TextView) convertView.findViewById(R.id.jenisTV);
			holder.hargaTV = (TextView) convertView.findViewById(R.id.hargaTV);
			holder.kodeV = (View) convertView.findViewById(R.id.kodeV);

			convertView.setTag(holder);
		}

		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String subIndikasi = listObat.get(position).getObat_indikasi();
		
		if (subIndikasi.length() > 50)
			subIndikasi = subIndikasi.substring(0, 50) + "...";

		holder.kodeV.setBackgroundResource(Seeder.kodeObat(Integer.valueOf(listObat.get(position).getObat_kode())));
		holder.namaTV.setText(listObat.get(position).getObat_nama());
		holder.jenisTV.setText(Seeder.setJenisObat(Integer.valueOf(listObat
				.get(position).getObat_jenis())));

		holder.indikasiTV.setText(subIndikasi);
		holder.hargaTV.setText("Rp. " + listObat.get(position).getObat_harga());
		display(holder.thumbnail, listObat.get(position).getObat_pic());

		return convertView;
	}

	class ViewHolder {
		TextView namaTV;
		TextView indikasiTV;
		TextView hargaTV;
		TextView jenisTV;
		ImageView thumbnail;
		View kodeV;
	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		if (!imageLoader.isInited())
			imageLoader.init(config);
		imageLoader.handleSlowNetwork(true);
	}

	public static DisplayImageOptions getUILOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true)
				.showImageOnLoading(R.drawable.default_image)
				.showImageForEmptyUri(R.drawable.default_image)
				.showImageOnFail(R.drawable.default_image)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.cacheInMemory(true).cacheOnDisk(true).postProcessor(null)
				.displayer(new FadeInBitmapDisplayer(300))
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	public void display(ImageView img, String url) {
		imageLoader.displayImage(url, img, getUILOption(),
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {

					}
				});
	}

}
