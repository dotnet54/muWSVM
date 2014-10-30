package app.model.data;
/**
 * 
 * REFERENCE: design pattern source code from
 * http://www.journaldev.com/1739/observer-design-pattern-in-java-example-tutorial
 *
 */
public interface IObserver {
    
    //method to update the observer, used by subject
    public void update();
     
}
