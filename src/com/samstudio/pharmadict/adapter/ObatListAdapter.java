package com.samstudio.pharmadict.adapter;

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

public class ObatListAdapter extends BaseAdapter {
	private Context mContext;
	private List<Obat> obatList;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	
	public ObatListAdapter (Context mContext, List<Obat> obatList){
		this.mContext = mContext;
		this.obatList = obatList;
		initImageLoader();
	}
	
	public void updateContent(List<Obat> obat){
		this.obatList = obat;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return obatList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return obatList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ViewHolder holder;
		
		if(convertView == null){
			convertView = inflater.inflate(R.layout.list_obat_item_layout, parent, false);
			holder = new ViewHolder();
			
			holder.obatName = (TextView) convertView.findViewById(R.id.obat_nama_tv);
			holder.obatPic = (ImageView) convertView.findViewById(R.id.obat_pic_iv);
			convertView.setTag(holder);	
		} else{
			holder = (ViewHolder)convertView.getTag();
		}
		
		holder.obatName.setText(obatList.get(position).getObat_nama());
		display(holder.obatPic, obatList.get(position).getObat_pic());
		
		return convertView;
	}
	
	class ViewHolder {
		TextView obatName;
		ImageView obatPic;
	}
	

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				mContext).threadPriority(Thread.NORM_PRIORITY - 2)
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
