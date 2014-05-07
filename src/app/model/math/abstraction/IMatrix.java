package app.model.math.abstraction;

public interface IMatrix<T> {
	
	public T getItem(int row, int column);
	
	public void addRow(IMatrix<T> matrixB);
	public void insertRow(int rowIndex);
	public void deleteRow(int rowIndex);
	
	public void addColumn(IMatrix<T> matrixB);
	public void insertColumn(int rowIndex);
	public void deleteColumn(int rowIndex);
	
	public IMatrix<T> add(IMatrix<T> matrixB);
	public IMatrix<T> substract(IMatrix<T> matrixB);
	
	//public IMatrix inverse(IMatrix matrixB);
	//public IMatrix determinant(IMatrix matrixB);
}
