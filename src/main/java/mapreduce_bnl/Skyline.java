package mapreduce_bnl;

import java.util.ArrayList;
import java.util.Iterator;

import mapreduce_bnl.io.TupleWritable;

public class Skyline {


	public static ArrayList<TupleWritable> getSkylineUsingBNL(
			Iterator<TupleWritable> tuples) {

		ArrayList<TupleWritable> window = new ArrayList<TupleWritable>();

		while (tuples.hasNext()){
			
			boolean pointDominatedByWindow = false;
			TupleWritable p = tuples.next();
			
			ArrayList<TupleWritable> dominatedTupels = new ArrayList<TupleWritable>();
			
			//check tuples in window dominate a point
			for (TupleWritable q : window) {
				if(q.dominate(p))
					pointDominatedByWindow = true;

				if(p.dominate(q))
					dominatedTupels.add(q);
			}
			
			//remove dominated tuple
			for (TupleWritable dominatedTuple : dominatedTupels) {
				window.remove(dominatedTuple);
			}

			if (false == pointDominatedByWindow)
				window.add(new TupleWritable(p.getX(), p.getY()));
		}
		return window;
	}
}
