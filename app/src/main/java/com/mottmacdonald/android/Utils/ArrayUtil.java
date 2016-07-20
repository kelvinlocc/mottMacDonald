package com.mottmacdonald.android.Utils;

import java.util.List;

public class ArrayUtil {

	
	public static float getMax(float[] arr) {
		float max = arr[0];
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > max)
				max = arr[x];
		}
		return max;
	}
	
	public static float getMax_2(float[] arr) {
		int max = 0;
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] > arr[max])
				max = x;
		}
		return arr[max];
	}
	
	public static float getMax_2(List<Float> arr) {
		int max = 0;
		for (int x = 1; x < arr.size(); x++) {
			if (arr.get(x) > arr.get(max))
				max = x;
		}
		return arr.get(max);
	}
	
	public static int getMin(int[] arr) {
		int min = 0;
		for (int x = 1; x < arr.length; x++) {
			if (arr[x] < arr[min])
				min = x;
		}
		return arr[min];
	}
	
	public static float getMin(List<Float> arr) {
		int min = 0;
		for (int x = 1; x < arr.size(); x++) {
			if (arr.get(x) < arr.get(min))
				min = x;
		}
		return arr.get(min);
	}
}
