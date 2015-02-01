package assignment2_2402;
public class ArrayStack<T> implements StackADT<T> {

	private boolean isBoundedCapacity = false;
	private int boundedCapacity = StackADT.UNBOUNDED_CAPACITY_INDICATOR; 
	private T[] store;
	private int topElementIndex = -1; 
	private int itemsOnStack = 0;
	
	// Constructor for a Bounded Stack
	public ArrayStack(int capacity){
		isBoundedCapacity = true;
		
		//Parse and set the capacity.
	    boundedCapacity = Math.abs(capacity); 
		store = (T[]) new Object[boundedCapacity];
	}
	
	// Constructor for an Unbounded Stack
	public ArrayStack(){
		isBoundedCapacity = false;
		
		//Size for an unbounded stack is the initial capacity.
		store = (T[]) new Object[StackADT.INITIAL_CAPACITY]; 
	}
	
	@Override
	/*
	 * Note: Assignment does not mention this. I am presuming
	 * this indicates if the there are any items in the Stack.
	 */
	public boolean isEmpty() {
		return topElementIndex < 0;
	}

	@Override
	/*
	 * Note: Assignment does not mention this. I am presuming
	 * this indicates if anymore items can be added.
	 */
	public boolean isFull() {
		return topElementIndex + 1 >= store.length;
	}

	@Override
	/*
	 * Returns the current number of items on the stack.
	 */
	public int size() {
		return itemsOnStack;
	}

	@Override
	public boolean isBoundedCapacity() {
		return isBoundedCapacity;
	}

	@Override
	/*
	 * Returns how many items the stack can hold.
	 */
	public int capacity() {
		if(isBoundedCapacity) return boundedCapacity;
		else return StackADT.UNBOUNDED_CAPACITY_INDICATOR;
	}
	
	@Override
	public int backingStoreSize() {
		return store.length;
	}

	@Override
	/*
	 * Returns the top element of the Stack.
	 */
	public T top() throws StackUnderflowException{
		// Make sure the stack isn't empty.
		if (isEmpty()){
			throw new StackUnderflowException();
		}
		
		return store[topElementIndex];
	}

	@Override
	/*
	 * Removes the top element in the stack and returns it.
	 * 
	 * Throws an exception if the stack is empty.
	 */
	public T pop() throws StackUnderflowException {
		// Make sure the stack isn't empty.
		if (isEmpty()){
			throw new StackUnderflowException();
		}
		
		// Top element changes as the top one's popped
		T poppedElement = store[topElementIndex];
		
		// Top element is now one element behind.
		topElementIndex--;

		// Decrement as we've removed something
		itemsOnStack--;
				
		return poppedElement;
		
	}

	@Override
	/*
	 * Adds an item to the top of the Stack.
	 * 
	 * Throws an exception if there's no room in the case of a bounded Stack.
	 */
	public void push(T item) throws BoundedCapacityOverFlowException {
		// Check for overflow and size
		if (isFull() && isBoundedCapacity == false ){
			// Double the size so we can add more
			doubleStoreSize();
		}
		else if(isFull() && isBoundedCapacity){
			throw new BoundedCapacityOverFlowException();
		}
		
		// Increment index for next entry
		topElementIndex++;
		
		// Set the element on the stack
		store[topElementIndex] = item;
		
		// Increment as we've added something
		itemsOnStack++;
	}
	
	/*
	 * Helper function that doubles the size of the array.
	 */
	private void doubleStoreSize(){
		// Double size of array
		int doubledSize = store.length * 2;
		
		// Create the new store
		T[] newStore = (T[]) new Object[doubledSize]; 
		
		// Copy over values
		for (int i=0; i<store.length; i++){
			newStore[i] = store[i];
		}
		
		// Set the store to the new store
		store = newStore;
	}

}
