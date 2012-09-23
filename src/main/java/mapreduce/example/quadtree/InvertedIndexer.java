package mapreduce.example.quadtree;


public class InvertedIndexer 
{
	/**
	 *input : value(one dimension), range(max,min), depth
	 *output : index 
	 *1. value가 주어진 range의 중앙값(mean)보다 작으면 index값에 0, 크면 1을 추가한다. 
	 *2. index에 추가된 값에 따라 range를 변경한다. 0이면 max값에 mean값을 할당하고, 1이면 min값에 mean값을 할당한다.
	   * 3. 1~2를 depth의 숫자만큼 반복한다. 
	 */	
	public static String getIndex(int value, int max, int min, int depth)
	{
		int localMax = max;
		int localMin = min;
	
		String index = new String();	
		
		for(int i=0; i<depth; i++)
		{
			int mean = (localMin + localMax)/2 ;
			
			if (value < mean) {
				index = index + "0";
				localMax = mean;
			}
			else {
				index = index + "1";
				localMin = mean;
			}
		}				
		return index;
	}		
}