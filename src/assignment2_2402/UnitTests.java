package assignment2_2402;

public class UnitTests {
	public static void execute(){
		/*
		 * TESTING
		 * 
		 * R1.4) Your ArrayStack<T> data structure should be usable as a bounded or unbounded stack. 
		 * 		 Which one it is is determined by the constructor used as can be seen in the ArrayStack<T> skeleton code provided.
		 * 
		 * R1.5) The size() method of the StackADT<T> should return the number of items currently on the stack. (Not the size of the backing store.)
		 * 
		 * R1.6) The capacity() method of the StackADT<T> should return the capacity, in number of possible elements, that can be supported by the stack. 
		 * In the case of a bounded stack it should be the maximum number of elements that the stack can hold. In the case of an unbounded stack it should 
		 * return the special value StackProtocol UNBOUNDED_CAPACITY_INDICATOR.
		 * 
		 * R1.7 The backingStoreSize() method should return the current size of the backing store (in number of possible stack items). This method will be 
		 * used to monitor the performance of your stack.
		 * 
		 * R1.8) If your stack is being used as a bounded stack and an attempt is made to push too many items then your code should throw a BoundedCapacityOverflowException 
		 * (skeleton code is provided for this exception, but you can add to it if you like).
		 * 
		 * R1.9) Calling methods top(), pop() on an empty stack should throw a StackUnderflowException (again, skeleton code has been provided for this.)
		 */
		// For testing
		ArrayStack<String> unboundedS = new ArrayStack<String>();
		ArrayStack<String> boundedS = new ArrayStack<String>(5);
		
		//* R1.9) Calling methods top(), pop() on an empty stack should throw a StackUnderflowException (again, skeleton code has been provided for this.)
		try {
			unboundedS.top();
			unboundedS.pop();
			boundedS.top();
			boundedS.pop();
		} catch (StackUnderflowException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		// Populate with values before testing
		try {
			boundedS.push("My");
			boundedS.push("Bounded Bro");
			
			unboundedS.push("Hey");
			unboundedS.push("Bro");
			unboundedS.push("Sup");
		} catch (BoundedCapacityOverFlowException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// R1.5) The size() method of the StackADT<T> should return the number of items currently on the stack. (Not the size of the backing store.)
		System.out.println(boundedS.size());
		System.out.println(unboundedS.size());

		System.out.println();
		 /* R1.6) The capacity() method of the StackADT<T> should return the capacity, in number of possible elements, that can be supported by the stack. 
		 * In the case of a bounded stack it should be the maximum number of elements that the stack can hold. In the case of an unbounded stack it should 
		 * return the special value StackProtocol UNBOUNDED_CAPACITY_INDICATOR.*/
		System.out.println(boundedS.capacity());
		System.out.println(unboundedS.capacity());
		
		System.out.println();
		/*
		  * R1.7 The backingStoreSize() method should return the current size of the backing store (in number of possible stack items). This method will be 
		 * used to monitor the performance of your stack. 
		 */
		System.out.println(boundedS.backingStoreSize());
		System.out.println(unboundedS.backingStoreSize());
		try {

			unboundedS.push("Hey");
			unboundedS.push("Bro");
			unboundedS.push("Sup");
			unboundedS.push("Hey");
			unboundedS.push("Sup");
			unboundedS.push("Bro");
			unboundedS.push("Sup");
			unboundedS.push("Hey");
			unboundedS.push("Sup");
		} catch (BoundedCapacityOverFlowException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(unboundedS.backingStoreSize());
		
		/*
		 *  * R1.8) If your stack is being used as a bounded stack and an attempt is made to push too many items then your code should throw a BoundedCapacityOverflowException 
		 * (skeleton code is provided for this exception, but you can add to it if you like).
		 */
		try {

			boundedS.push("Hey");
			boundedS.push("Bro");
			boundedS.push("Sup");
			boundedS.push("Hey");
			boundedS.push("Sup");
		} catch (BoundedCapacityOverFlowException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
