/**
 * 
 */
package com.mycompany.exp9.one;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.mycompany.exp9.common.MyLanguageEnum;

/**
 * @author ilker
 *
 */
public class HelloJava8streams {
	public static final String[] number2str_english = {"0:zero", "1:one", "2:two", "3:three", "4:four", "5:five", "6:six", "7:seven", "8:eight", "9:nine", "10:ten", "11:eleven", "12:twelfe", "13:thirteen", "14:fourteen", "15:fifteen"};
	public static final String[] number2str_spanish = {"0:cero", "1:uno", "2:dos", "3:tres", "4:cuatro", "5:cinco", "6:seis", "7:siete", "8:ocho", "9:nueve", "10:diez", "11:once", "12:doce", "13:trece", "14:catorce", "15:quince"};
	public static final String[] number2str_turkish = {"0:sifir", "1:bir", "2:iki", "3:uc", "4:dort", "5:bes", "6:alti", "7:yedi", "8:sekiz", "9:dokuz", "10:on", "11:onbir", "12:oniki", "13:onuc", "14:ondort", "15:onbes"};
	
	public static final String separator = ":";
	
	/** length of {@link #number2str_english} */
	public static final int MAX_MAP_SIZE = number2str_english.length;
	public static final int DEFAULT_SIZE = 10;
	
	
	public static String[] number2str(MyLanguageEnum myLanguageEnum) {
		String[] number2strArray;
		switch (myLanguageEnum) {
		case Spanish:
			number2strArray = number2str_spanish;
			break;
		case Turkish:
			number2strArray = number2str_turkish;
			break;
		case English:
		default:
			number2strArray = number2str_english;
			break;
		}
		return number2strArray;
	}

	/**
	 * Shows off using Java 8 streams; map, filter, collect
	 * Takes an array of String like "1:one", "2:two", 
	 * breaks it to each String, 
	 * filters on String having 2 parts separated by {@link #separator},
	 * then creates a map made of 1st part of above String and 2nd part of above String and returns that
	 * @param myLanguageEnum
	 * @return Map<Integer,String> of (number,str) pairs
	 */
	public static Map<Integer, String> getMap(MyLanguageEnum myLanguageEnum) {
		String[] number2strArray = number2str(myLanguageEnum);
		Map<Integer, String> number2strMap = null;
		
//		number2strMap = Arrays.stream(number2strArray)
		// NOTE ilker above 1 line is equivalent to below 2 lines
		number2strMap = Arrays.asList(number2strArray)
				.stream()
				.map(number2valStr -> number2valStr.split(separator))
				.filter(number2strSplitArray -> number2strSplitArray.length ==2)
				.collect(Collectors.toMap(number2strSplitArray -> Integer.valueOf(number2strSplitArray[0]), number2strSplitArray -> number2strSplitArray[1]));
		
		return number2strMap;
	}

	/**
	 * @param myLanguageEnum
	 * @param size number of elements to have in returned map. If size < 0, {@link #DEFAULT_SIZE} is used. If size > {@link #MAX_MAP_SIZE}, {@link #MAX_MAP_SIZE} is used.
	 * @return Map<Integer,String> of size number of (number,str) pairs
	 */
	public static Map<Integer, String> getMap2size(MyLanguageEnum myLanguageEnum, int size) {
		Map<Integer, String> number2strSubMap = null;

		size = getAdjustedMapSize(size);
		
		Map<Integer, String> number2strMap = getMap(myLanguageEnum);
		List<Integer> keysToPick_1toSize = list4rangeOfInts(1,size+1);
		number2strSubMap = subMap(number2strMap, keysToPick_1toSize);
		
		return number2strSubMap;
	}
	
	/**
	 * Makes sure size is 0 <= size <= {@link #MAX_MAP_SIZE}.
	 * @param size 
	 * @return If size < 0, {@link #DEFAULT_SIZE}. If size > {@link #MAX_MAP_SIZE}, {@link #MAX_MAP_SIZE}. Otherwise size
	 */
	public static int getAdjustedMapSize(int size) {
		if(size < 0) {
			size = DEFAULT_SIZE;
		} else if(size > MAX_MAP_SIZE) {
			size = MAX_MAP_SIZE;
		}
		return size;
	}
	
	/**
	 * Shows off Java8 streams; IntStream range, boxed
	 * @param startIntIncluded start of int range, included in returned list
	 * @param endIntExcluded end of int range, excluded in returned list
	 * @return List<Integer> for range of ints from startIntIncluded to endIntExcluded
	 */
	public static List<Integer> list4rangeOfInts(int startIntIncluded, int endIntExcluded) {
		return IntStream.range(startIntIncluded, endIntExcluded).boxed().collect(Collectors.toList());
	}

	
	public static Map<Integer, String> subMap(Map<Integer,String> map, List<Integer> keysToPick) {
		Map<Integer, String> subMap = null;
		
		subMap = keysToPick.stream()
				.filter(map::containsKey)
				.collect(Collectors.toMap(Function.identity(), map::get));
		
		// NOTE ilker above and below are basically same. Below one specifies the inputs
		
		subMap = keysToPick.stream()
				.map(key -> key)
				.filter(key -> map.containsKey(key))
				.collect(Collectors.toMap(key -> key, key -> map.get(key)));
		
		return subMap;
	}
	
	public static void test_getMap() {
		Map<Integer, String> number2strMap_english = getMap(MyLanguageEnum.English);
		System.out.println("number2strMap_english:" + number2strMap_english);
		
		Map<Integer, String> number2strMap_spanish = getMap(MyLanguageEnum.Spanish);
		System.out.println("number2strMap_spanish:" + number2strMap_spanish);
		
		Map<Integer, String> number2strMap_turkish = getMap(MyLanguageEnum.Turkish);
		System.out.println("number2strMap_turkish:" + number2strMap_turkish);
		
		Map<Integer, String> number2strMap_english_default = getMap(MyLanguageEnum.British);
		System.out.println("number2strMap_english_default:" + number2strMap_english_default);	
	}

	public static void test_getMap2size() {
		Map<Integer, String> number2strMap_english_5 = getMap2size(MyLanguageEnum.English, 5);
		System.out.println("number2strMap_english_5:" + number2strMap_english_5);
		
		Map<Integer, String> number2strMap_english_10 = getMap2size(MyLanguageEnum.English, 10);
		System.out.println("number2strMap_english_10:" + number2strMap_english_10);
	}
	
	public static void test_subMap() {
		Map<Integer, String> number2strMap = getMap(MyLanguageEnum.English);

		List<Integer> rangeList_1to11 = list4rangeOfInts(1,11);
		Map<Integer, String> number2strMap_1to11 = subMap(number2strMap, rangeList_1to11);
		System.out.println("number2strMap_1to11:" + number2strMap_1to11);
		
		List<Integer> rangeList_1to6 = list4rangeOfInts(1,6);
		Map<Integer, String> number2strMap_1to6 = subMap(number2strMap, rangeList_1to6);
		System.out.println("number2strMap_1to6:" + number2strMap_1to6);
	}
	
	public static void test_list4rangeOfInts() {
		List<Integer> rangeList_1to11 = list4rangeOfInts(1,11);
		System.out.println("rangeList_1to11:" + rangeList_1to11);
		
		List<Integer> rangeList_6to11 = list4rangeOfInts(6,11);
		System.out.println("rangeList_6to11:" + rangeList_6to11);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		test_getMap();
//		test_list4rangeOfInts();
//		test_subMap();
		test_getMap2size();
	}
	
}
