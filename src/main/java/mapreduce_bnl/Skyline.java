package mapreduce_bnl;

import java.util.ArrayList;
import java.util.Iterator;

import mapreduce_bnl.io.TupleWritable;

public class Skyline {


	public static ArrayList<TupleWritable> getSkylineUsingBNL(
			Iterator<TupleWritable> tuples) {

		ArrayList<TupleWritable> window = new ArrayList<TupleWritable>(100);

		while (tuples.hasNext()){
			
			boolean pDominatedByWindow = false;
			TupleWritable p = tuples.next();
			//check tuples in window dominate a point
			for (TupleWritable q : window) {
				if(q.dominate(p))
					pDominatedByWindow = true;

				if(p.dominate(q))
					window.remove(q);

			}

			if (false == pDominatedByWindow)
				window.add(p);
		}

		return window;

	}

}
