package app.model.data;

/**
 * 
 * REFERENCE: design pattern source code from
 * http://www.journaldev.com/1739/observer-design-pattern-in-java-example-tutorial
 *
 */
public interface ISubject {
	 
    //methods to register and unregister observers
    public void register(IObserver obj);
    public void unregister(IObserver obj);
     
    //method to notify observers of change
    public void notifyObservers();
}
