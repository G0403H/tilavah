package ru.group0403.tajweed;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**Класс адаптера расширяется с помощью BaseAdapter и реализуется с помощью OnClickListener 
*/
public class CustomAdapter extends BaseAdapter implements OnClickListener {

	/** 
	* Объявите используемые переменные
	*/
	private Activity activity;
	private ArrayList<Surah> data;
	private static LayoutInflater inflater = null;
	public Resources res;
	int i = 0;

	/**
	* Конструктор CustomAdapter
	*/
	public CustomAdapter(Activity a, ArrayList<Surah> d, Resources resLocal) {

		/**
		* Принять пройденные значения
		*/
		activity = a;
		data = d;
		res = resLocal;

		/**
		* Инфлятор макета для вызова внешнего макета XML ()
		*/
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	/**
	* Каков размер пройденного размера Arraylist
	*/
	@Override
	public int getCount() {

		if (data.size() <= 0)
			return 1;
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	* Создайте класс-держатель, содержащий раздутые элементы XML-файла.
	*/
	public static class ViewHolder {

		public TextView leftText;
		public TextView rightText;
		public ImageView downloadButton;
		public ImageView deleteButton;
		public int position;

	}

	/**
	* Зависит от размера данных, вызываемых для каждой строки. Создайте каждую строку ListView. 
	*/
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		ViewHolder holder;

		Display display = activity.getWindowManager().getDefaultDisplay();
		Point size = new Point();
		// display.getSize(size);
		int PaddingWidth = 20, rightMarginWidth = 5, buttonWidth = 48;

		if (display.getWidth() < 480) {
			buttonWidth = (display.getWidth() * 48) / 680;
		}

		int extramargin;

		final DisplayMetrics metrics = activity.getResources()
				.getDisplayMetrics();
		float densityDpi = metrics.density;

		// Я мог бы написать базовую математическую формулу для расчета экстрамаргина.
        // Но в этом случае человек, читающий код, не поймет его лучше.
        // Поэтому я разделил на маленький, HD, полный ... экран.
		
		if (densityDpi <=1) // Для маленького экрана
			extramargin = (int) (densityDpi * 20.0);
		
		else if(densityDpi<=2) // Для экрана HD
			extramargin = (int) (densityDpi * 35.0);
		
		else if(densityDpi<=3) // Для более чем HD экрана
			extramargin = (int) (densityDpi * 55.0);
		
		else if(densityDpi<=4) //Для полного экрана HD
			extramargin=(int) (densityDpi*70.0);
		
		else  // Для ультра HD экрана
			extramargin=(int ) (densityDpi*85.0);

		int availableWidth = display.getWidth() - PaddingWidth
				- rightMarginWidth - 2 * buttonWidth - extramargin;


		if (convertView == null) {

			/**
			* Заполните файл tabitem.xml для каждой строки (определено ниже)
			*/
			vi = inflater.inflate(R.layout.home_page_row, null);

			/**
			* Просмотр объекта Holder для содержания элементов файла tabitem.xml 
			*/

			holder = new ViewHolder();
			holder.leftText = (TextView) vi
					.findViewById(R.id.home_page_left_textview);
			holder.rightText = (TextView) vi
					.findViewById(R.id.home_page_right_textview);

			holder.downloadButton = (ImageView) vi
					.findViewById(R.id.home_page_download_button);

			holder.deleteButton = (ImageView) vi
					.findViewById(R.id.home_page_delete_button);
			holder.deleteButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					ViewHolder myHolder = (ViewHolder) view.getTag();
					MyDialogBuilder.buildDeleteSurah(activity,
							data.get(myHolder.position));

				}
			});
			holder.downloadButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					ViewHolder myHolder = (ViewHolder) view.getTag();
					MyDialogBuilder.buildDownloadDialog(activity,
							data.get(myHolder.position));
				}
			});
			setTheme(holder);
			/**
			* Установите держатель с LayoutInflater 
			*/
			vi.setTag(holder);
		} else
			holder = (ViewHolder) vi.getTag();

		/**
		* Получить каждый объект Model от Arraylist 
		*/
		Surah name = data.get(position);

		/**
		* Задать значения модели в элементах Holder
		*/

		holder.leftText.setText(name.getSurahNumber() + ". " + name.getName());

		// holder.rightText.setTypeface(Typeface.createFromAsset(activity.getAssets(),
		// "TAHOMA.ttf"));
		// holder.rightText.setText(ArabicUtilities.reshape(name.getArabicName()));
		holder.rightText.setText(name.getArabicName());

		;
		holder.position = position;
		holder.rightText.setWidth(availableWidth / 2);
		holder.leftText.setWidth(availableWidth / 2);

		holder.downloadButton.setTag(holder);
		holder.deleteButton.setTag(holder);
		/**
		* Установить элемент Нажмите Listner для LayoutInflater для каждой строки
		*/

		vi.setOnClickListener(new OnItemClickListener(position));
		return vi;
	}

	private void setTheme(ViewHolder holder) {

		int theme = MyPreferenceHandler.getTheme(activity);
		if (theme == MyPreferenceHandler.DARK_THEME) {
			holder.downloadButton.setImageDrawable(res
					.getDrawable(R.drawable.ic_action_download_dark));
			holder.leftText.setTextColor(Color.WHITE);
			holder.rightText.setTextColor(Color.WHITE);
			holder.deleteButton.setImageDrawable(res
					.getDrawable(R.drawable.ic_action_discard));

		} else {
			holder.downloadButton.setImageDrawable(res
					.getDrawable(R.drawable.ic_action_download));
			holder.leftText.setTextColor(Color.BLACK);
			holder.rightText.setTextColor(Color.BLACK);
			holder.deleteButton.setImageDrawable(res
					.getDrawable(R.drawable.ic_action_discard_light));
		}
	}

	/**
	* Вызывается при нажатии элемента в ListView.
	*/
	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}

		@Override
		public void onClick(View arg0) {

			HomePage sct = (HomePage) activity;

			/****
			 * Вызовите метод onItemClick внутри класса CustomListViewAndroidExample
			 * ( См. ниже )
			 ****/

			sct.onItemClick(mPosition);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
