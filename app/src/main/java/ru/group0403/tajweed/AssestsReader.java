package ru.group0403.tajweed.quran;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import android.util.Pair;

 /**
  * Этот класс считывает все файлы оценки, присутствующие в папке оценки, такие как
  * Коранические текстовые файлы и метаданные Корана. Это также заполняет surahIndexer
  * HashMap, который отображает каждое целое число в переменную surah (класса Surah).
 */
public class AssestsReader {

	private HashMap<Integer, Surah> surahIndexer;
	private Context myContext;

	public AssestsReader(Context context) {
		myContext = context;
		surahIndexer = new HashMap<Integer, Surah>();
		readQuranXml();
	}

	/*
	 * Список имен всех сур.
	 * 
	 * @return
	 */
	public ArrayList<Surah> getSurahsList() {
		ArrayList<Surah> surahList = new ArrayList<Surah>();
		for (int surahNumber = 1; surahNumber <= 114; surahNumber++)
			surahList.add(surahIndexer.get(surahNumber));
		return surahList;
	}
    /**
     * Эта функция загружает текст суры, читая CONSTANT> SIMPLEQURNTEXTNAME и
     * возвращает хэш-карту, которая связывает целое число (т.е. число суры) с
     * pair <String, List <String >>, первый элемент в паре - арабское имя суры
     * и второй элемент содержит данные каждого стиха в списке.
     *
     * Чтобы понять эту функцию, сначала прочитайте файл
	 * CONSTANT.SIMPLEQURANTEXTNAME located in the assests folder.
	 * 
	 * @param fileName
	 * @return
	 */
	private HashMap<Integer, Pair<String, List<String>>> loadSurahText(
			String fileName) {
		// /reading surahs verse text

		InputStream is = null;
		try {
			HashMap<Integer, Pair<String, List<String>>> map = new HashMap<Integer, Pair<String, List<String>>>();
			AssetManager assetManager = myContext.getAssets();
			is = assetManager.open(CONSTANT.SIMPLEQURANTEXTNAME);
			DocumentBuilderFactory dbFactoryText = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilderText = dbFactoryText.newDocumentBuilder();
			org.w3c.dom.Document docText = dBuilderText.parse(is);
			docText.getDocumentElement().normalize();
			// System.out.println("Root element :"
			// + docText.getDocumentElement().getNodeName());
			NodeList nListText = docText.getElementsByTagName("sura");
			for (int temp = 0; temp < nListText.getLength(); temp++) {
				Node nSurahNode = nListText.item(temp);
				if (nSurahNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nSurahNode;
					String surahIndex = eElement.getAttribute("index");
					String arabicName = eElement.getAttribute("name");

					NodeList nVerseList = eElement.getElementsByTagName("aya");
					List<String> verseText = new ArrayList<String>();

					for (int j = 0; j < nVerseList.getLength(); j++) {
						Node verseNode = nVerseList.item(j);
						Element eElementVerse = (Element) verseNode;
						String text = eElementVerse.getAttribute("text");
						String verseIndex = eElementVerse.getAttribute("index");

						if (Integer.parseInt(verseIndex) != j + 1) {
							Log.w("erro", "doesnot match");
						}
						verseText.add(text);
					}
					map.put(Integer.parseInt(surahIndex),
							new Pair<String, List<String>>(arabicName,
									verseText));

				}

			}
			is.close();
			return map;
		} catch (IOException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		try {
			is.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	/**
    * Эта функция заполняет переменную surahIndexer. Сначала загружает
    * surahText, вызывая функцию loadSurahText. Затем пересечь
    * surahList.xml, который является метаданными Корана, т.е. он содержит все
    * информация о каждой суре, такая как имя суры, английское имя, английский
    * переведенное имя, нет стихов ...
    *
    * Посмотрите на этот файл, прежде чем приступить к этой функции.
    */
	private void readQuranXml() {
		InputStream is = null;
		try {

			// getting surahText and their arabic names
			HashMap<Integer, Pair<String, List<String>>> surahText = loadSurahText(CONSTANT.SIMPLEQURANTEXTNAME);

			
			AssetManager assetManager = myContext.getAssets();
			
			// /reading surahList.xml and getting surahs name and its details
			is = assetManager.open("surahList.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();
			// System.out.println("Root element :"
			// + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("surah");

			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);

				// System.out.println("\nCurrent Element :" +
				// nNode.getNodeName());
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String surahName = eElement
							.getElementsByTagName("surahName").item(0)
							.getTextContent();
					String surahEnglishName = eElement
							.getElementsByTagName("englishName").item(0)
							.getTextContent();
					String verseCount = eElement
							.getElementsByTagName("verseCount").item(0)
							.getTextContent();
					String type = eElement.getElementsByTagName("type").item(0)
							.getTextContent();
					Pair<String, List<String>> surahTextData = surahText
							.get(temp + 1);

					String arabicName = surahTextData.first;
					Surah tmp = new Surah(surahName, surahEnglishName, type,
							arabicName, Integer.parseInt(verseCount), temp + 1,
							surahTextData.second);
					surahIndexer.put(temp + 1, tmp);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/*
	 * Returns Surah given its id, for example 1 for surah Al-fatiha
	 */
	public Surah getSurah(int surahNumber) {
		if (surahIndexer.containsKey(surahNumber)) {

			return surahIndexer.get(surahNumber);
		}
		return null;
	}

}
