//package finalproject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry; // You may (or may not) need it to implement fastSort

public class Sorting {

	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n^2) as it uses bubble sort, where n is the number 
	 * of pairs in the map. 
	 */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort (HashMap<K, V> results) {
        ArrayList<K> sortedUrls = new ArrayList<K>();
        sortedUrls.addAll(results.keySet());	//Start with unsorted list of urls

        int N = sortedUrls.size();
        for(int i=0; i<N-1; i++){
			for(int j=0; j<N-i-1; j++){
				if(results.get(sortedUrls.get(j)).compareTo(results.get(sortedUrls.get(j+1))) < 0){
					K temp = sortedUrls.get(j);
					sortedUrls.set(j, sortedUrls.get(j+1));
					sortedUrls.set(j+1, temp);					
				}
			}
        }
        return sortedUrls;                    
    }
    
    
	/*
	 * This method takes as input an HashMap with values that are Comparable. 
	 * It returns an ArrayList containing all the keys from the map, ordered 
	 * in descending order based on the values they mapped to. 
	 * 
	 * The time complexity for this method is O(n*log(n)), where n is the number 
	 * of pairs in the map. 
	 */

	private static <K, V extends Comparable<V>> ArrayList<K> mergeSort(ArrayList<K> keys, HashMap<K, V> results) {

		int size = keys.size();

		if(size <= 1)
			return keys;

		int mid = size / 2;

		ArrayList<K> list1 = new ArrayList<K>();
		for(int i = 0; i < mid; i ++)
			list1.add(keys.get(i));

		list1 = mergeSort(list1, results);

		ArrayList<K> list2 = new ArrayList<K>();
		for(int i = mid; i < size; i ++)
			list2.add(keys.get(i));

		list2 = mergeSort(list2, results);

		return merge(list1, list2, results);
	}
	private static <K, V extends Comparable<V>> ArrayList<K> merge(ArrayList<K> list1, ArrayList<K> list2, HashMap<K, V> results) {

		ArrayList<K> new_list = new ArrayList<K>();

		int i = 0;
		int j = 0;

		while (i != list1.size() && j != list2.size()) {

			if (results.get(list1.get(i)).compareTo(results.get(list2.get(j))) > 0) {
				new_list.add(list1.get(i));
				i++;
			} else {
				new_list.add(list2.get(j));
				j++;
			}
		}
		while (i < list1.size()) {
			new_list.add(list1.get(i++));
		}
		
		while (j < list2.size()) {
			new_list.add(list2.get(j++));
		}
		
		return new_list;
	}



    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(HashMap<K, V> results) {

		ArrayList<K> list = new ArrayList<K>();
		
		for(K key: results.keySet())
			list.add(key);
		
		return mergeSort(list, results);
	}
}
