int f(int[] arr) {
	int max = arr[0], min = arr[0];
	for(int i : arr) {
		if (i > max) {
			max = i;
		}
	}

	for(int i : arr) {
		if (i < min) {
			min = i;
		}
	}
    return max - min;
}