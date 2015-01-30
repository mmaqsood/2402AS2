package assignment2_2402;

public interface StackADT<T>  {
	
	public final static int UNBOUNDED_CAPACITY_INDICATOR = -1;
	public final static int INITIAL_CAPACITY = 8;
	
	public boolean isEmpty(); //O(1)
	public boolean isFull(); //O(1)
	public boolean isBoundedCapacity(); //O(1)
	public int size(); //O(1) Answer number of elements
	public int capacity(); //O(1) Answer number of possible elements
	public int backingStoreSize(); //O(1) Answer the current size, in elements, of the backing store
	public T top() throws StackUnderflowException; //O(1) Answer top item on stack, 
	public T pop() throws StackUnderflowException; //O(1*) amortized Push item on stack
	public void push(T item) throws BoundedCapacityOverFlowException; //O(1*) amortized pop item from stack

}
