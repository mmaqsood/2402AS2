package assignment2_2402;
public class ArrayStack<T> implements StackADT<T> {

	private boolean isBoundedCapacity = false;
	private int boundedCapacity = StackADT.UNBOUNDED_CAPACITY_INDICATOR; 
	private T[] store;
	
	public ArrayStack(int capacity){
		//used to create a BOUNDED capacity stack
		isBoundedCapacity = true;
	    boundedCapacity = Math.abs(capacity); 
	    //TO DO: ADD MISSING CODE
		store = (T[]) new Object[boundedCapacity];  //ugly
	}
	public ArrayStack(){
	//used to create an UNBOUNDED capacity stack
	//initial backing store size should not be bigger then
	//StackADT.INITIAL_CAPACITY;
	isBoundedCapacity = false;
	
	//TO DO: ADD MISSING CODE
	store = (T[]) new Object[StackADT.INITIAL_CAPACITY]; //ugly
		
	}
	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false; //for now
	}

	@Override
	public boolean isFull() {
		// TODO Auto-generated method stub
		return false; //for now
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;  //for now
	}

	@Override
	public boolean isBoundedCapacity() {
		// TODO Auto-generated method stub
		return isBoundedCapacity;
	}

	@Override
	public int capacity() {
		// TODO Auto-generated method stub
		if(isBoundedCapacity) return boundedCapacity;
		else return StackADT.UNBOUNDED_CAPACITY_INDICATOR;
	}
	
	@Override
	public int backingStoreSize() {
		// TODO Auto-generated method stub
		return 0; //FOR NOW -replace with meaningful code
	}


	@Override
	public T top() throws StackUnderflowException{
		// TODO Auto-generated method stub
		return null;  //for now
	}

	@Override
	public T pop() throws StackUnderflowException {
		// TODO Auto-generated method stub
		return null; //for now
	}

	@Override
	public void push(T item) throws BoundedCapacityOverFlowException {
		// TODO Auto-generated method stub

	}

}
