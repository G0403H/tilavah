package ru.group0403.tajweed.quran;
/**
 *Этот файл содержит большую часть постоянной переменной, используемой в проекте.
 */
public class CONSTANT {

	public static final String APPLICATIONNAME = "QuranTajweed0403";
	public static final String APPLICATIONROOTDIRECTORYLOCATION = APPLICATIONNAME;
	public static final String AUDIODIRECTORYNAME = "Audio";
	public static final String AUDIODIRECTORYLOCATION = APPLICATIONROOTDIRECTORYLOCATION
			+ "/" + AUDIODIRECTORYNAME;

	// Арабские текстовые файлы
	public static final String SIMPLEQURANTEXTNAME = "quran-simple.xml";



	public static final String BISMILLAH = "بِسْمِ اللَّهِ الرَّحْمَٰنِ الرَّحِيم";
	public static final String RECITER_LIST = "reciterList.txt";
	public static final String DOWNLOAD_SUCCESSFUL = "Download SuccessFul";

	// Цвета фона
	public static final String MUSHAF_BACKGROUND_COLOUR = "#F9FAE1";
	public static final String WHITE_BACKGROUND_COLOUR = "#FFFFFF";
	public static final String BLACK_BACKGROUND_COLOUR = "#000000";

	// всплывающее сообщение при первом нажатии кнопки повторения и перезапуска
	public static final String REPEAT_MESSAGE = "Эта кнопка продолжает повторять текущий стих, пока вы не нажмете его снова";
	public static final String RESTART_MESSAGE = "Эта кнопка перезапускает текущий воспроизводимый стих с выбранного начального стиха.";

	// Цвет фона разделительной линии. Вы можете наблюдать эту линию между
    // surahsList и медиаплеер и настройки активности
	public static final String WHITE_DIVIDER = "#D8D8D8";
	public static final String BLACK_DIVIDER = "#202020";

	// О нас, сообщение
	public static final String ABOUTUSMOD = "ru.group0403.tajweed.tilavah - это бесплатный Коран с открытым исходным кодом."
			+ "Приложение для Android."
			+ "Используется аудио от http://www.everyayah.com, "
			+ "В то время как тексты Корана и файлы перевода Корана взяты из http://www.tanzil.net."
			+ "\n Если это помогло вам запомнить Коран, "
			+ "Пожалуйста, помолиесь Аллаху о прощении всех мусульман"
			+ "Если вы столкнулись с каким-либо предложением, пожалуйста, напишите письмо"
			+ "group0403@gmail.com";

}
